(ns nerses.compiler
  (:require [clojure.string :as str])
  (:refer-clojure :exclude [compile]))

(def arg-sep ", ")

(defn emit
  [& strs]
  (apply str strs))

(defn tpl-varname
  [k]
  (str "~"
       (cond
         (keyword? k) (name k)
         :default (str k))
       "~"))

(defn emit-tpl
  [tpl m]
  (emit (reduce-kv (fn [s k v] (str/replace s (tpl-varname k) v))
                   tpl
                   m)))

(def fn-tpl
  "function ~n~(~a~) { ~b~; }")

(defn emit-fn
  [func-name arglist body]
  (emit-tpl fn-tpl {:n func-name :a arglist :b body}))

(def if-then-tpl
  "if (~c~) { ~t~; }else{ ~e~; }")

(defn emit-if-then
  [condition then else]
  (emit-tpl if-then-tpl {:c condition :t then :e else}))

(defmulti compile
  (fn [ast] (first ast)))

(defmethod compile :expr
  [expr]
  (emit (compile (second expr))))

(defmethod compile :func_def
  [[_ func-name arglist body]]
  (let [c-func-name (compile func-name)
        c-arglist (compile arglist)
        c-body (compile body)]
    (emit-fn c-func-name c-arglist c-body)))

(defmethod compile :arglist
  [[_ & args]]
  (emit (str/join arg-sep (map compile args))))

(defmethod compile :if_expr
  [[_ condition then else]]
  (let [c-condition (compile condition)
        c-then (compile then)
        c-else (compile else)]
    (emit-if-then c-condition c-then c-else)))

(defmethod compile :func_call
  [[_ func-name arg]]
  (emit (compile func-name) "(" (compile arg) ")"))

(defmethod compile :name
  [[_ nm]]
  (emit nm))

(defmethod compile :number
  [[_ num]]
  (emit num))

(defmethod compile nil
  [_]
  (emit ""))
