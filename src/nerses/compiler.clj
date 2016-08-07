(ns nerses.compiler
  (:require [clojure.string :as str])
  (:refer-clojure :exclude [compile]))

(def arg-sep ", ")

(defn emit
  [& strs]
  (apply str strs))

(defn emit-fn
  [func-name arglist body]
  (emit "function " func-name "(" arglist ")"
        "{" body "}"))

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

(defmethod compile :func_call
  [[_ func-name arg]]
  (emit (compile func-name) "(" (compile arg) ")"))

(defmethod compile :name
  [[_ nm]]
  (emit nm))

(defmethod compile nil
  [_]
  (emit ""))
