(defproject cljs_express "0.1.0-SNAPSHOT"
  :description "Isomorphic Clojurescript Application"
  :url "http://outcastgeek.com"
  :license {:name "Eclipse"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.145"]
                 [org.clojure/core.typed "0.3.11"]
                 [org.clojure/core.match "0.3.0-alpha4"]
                 [com.cognitect/transit-cljs "0.8.220"]
                 [org.omcljs/om "1.0.0-alpha14"]
                 [sablono "0.3.6" :exclusions [cljsjs/react]]
                 [cljs-http "0.1.37"]]

  :plugins [[lein-cljsbuild "1.0.6"]]

  :jvm-opts ^:replace ["-Xmx256M" "-Xms256m" "-server"]

  :source-paths ["src"]

  :cljsbuild {
    ;:repl-listen-port 9000
    :builds {
             :cljs_express
             {
              :source-paths ["src-common" "src"]
              ;:notify-command ["node" "run.js"] ;; << ADD THIS
              ;:notify-command ["nodemon" "run.js"] ;; << ADD THIS
              :compiler {
                         :main cljs_express.core
                         :output-to "app.js"
                         ;:output-to "cljs_express.js"
                         ;:output-to "out/cljs_express.js"
                         ;:output-dir "out"
                         :preamble ["include.js"]
                         ; :verbose true
                         :cache-analysis true
                         :static-fns true
                         :target :nodejs
                         :optimizations :simple
                         ;:optimizations :none
                         ;:source-map true
                         :pretty-print true}
              }
             :base
             {
              :source-paths ["src-common" "src-client"]
              :compiler {
                         :main hello.core
                         :output-to "static/js/base.js"
                         :cache-analysis true
                         :static-fns true
                         :optimizations :advanced
                         :pretty-print true
                         }
              }
             :home
             {
              :source-paths ["src-common" "src-client"]
              :compiler {
                         :main base.core
                         :output-to "static/js/home.js"
                         :cache-analysis true
                         :static-fns true
                         :optimizations :advanced
                         :pretty-print true
                         }
              }
             }})
