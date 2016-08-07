(ns nerses.core
  (:require [instaparse.core :as insta]
            [clojure.string :as str]
            
            [nerses.compiler :as compiler])
  (:refer-clojure :exclude [compile]))

(def parser
  (insta/parser
    "program = func_def+
     func_def = name arglist ws* <'='> ws* expr ws*
     expr = if_expr / func_call / number
     arglist = (ws name)*
     if_expr = <'if'> ws expr ws <'then'> ws expr ws <'else'> ws expr
     func_call = name (ws expr) | name
     name = #'[A-z]+'
     <ws> = <#'\\s+'>
     number = #'[0-9]+'"))

(defn parse
  [s]
  "Calls the `parser` grammer defined above on the argument." 
  (insta/parse parser s))

(defn parse-file
  [f]
  (parse (str/trim (slurp f))))

(defn compile
  [file]
  (let [ast (parse (str/trim (slurp file)))]
    (compiler/compile ast)))

(parse (str/trim (slurp "resources/sample.ns")))
(compile "resources/sample.ns")
