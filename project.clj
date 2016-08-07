(defproject nerses "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [instaparse "1.4.2"]]
  :profiles {:repl {:plugins [[cider/cider-nrepl "0.13.0"]]}}
  :main ^{:skip-aot true} nerses.core
  )
