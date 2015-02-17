(defproject simpleapp "0.1.0-SNAPSHOT"
  :description "minimum example of webapp with bidirectional comms"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ;;;; server
                 [com.taoensso/sente "1.4.0-alpha2"]  ;; bidirectional comms
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]  ;; queue-based concurrency
                 [http-kit "2.1.19"]  ;; async web server
                 [compojure "1.3.1"]  ;; routing
                 [ring/ring "1.3.2"]  ;; web request interface
                 [ring/ring-defaults "0.1.4"]  ;; (cont)
                 [com.taoensso/timbre "3.3.1"]  ;; logging
                 [hiccup "1.0.5"]  ;; html generation

                 ;;;; client
                 [org.clojure/clojurescript "0.0-2411"]  ;; clojure on javascript

                 ]
  :plugins [[lein-cljsbuild "1.0.4"]]
  :main simpleapp.main
  :cljsbuild {:builds [{:id :main
                        :source-paths ["src" "target/classes"]
                        :compiler {:output-to "resources/public/main.js"
                                   :optimizations :whitespace #_:advanced
                                   :pretty-print true}}]}
  :source-paths ["src/clj"])
