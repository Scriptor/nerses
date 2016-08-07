(ns nerses.core
  (:require [instaparse.core :as insta]
            [clojure.string :as str]
            
            [nerses.compiler :as compiler])
  (:refer-clojure :exclude [compile]))

(def parser
  (insta/parser
    "expr = func_def / func_call / number
     func_def = name arglist ws* <'='> ws* expr
     arglist = (ws name)*
     func_call = name (ws expr) / name
     name = #'[A-z]+'
     <ws> = <#'\\s+'>
     number = #'[0-9]+'"))

(defn parse
  [s]
  "Calls the `parser` grammer defined above on the argument." 
  (insta/parse parser s))

(defn compile
  [file]
  (let [ast (parse (str/trim (slurp file)))]
    (compiler/compile ast)))

(parse (str/trim (slurp "resources/sample.ns")))
(compile "resources/sample.ns")
