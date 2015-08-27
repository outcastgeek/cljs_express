(defproject cljs_express "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.48"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [com.cognitect/transit-cljs "0.8.220"]
                 [org.omcljs/om "0.9.0" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.13.3-0"]
                 [sablono "0.3.5" :exclusions [cljsjs/react]]
                 [prismatic/om-tools "0.3.12"]
                 [cljs-http "0.1.37"]]

  :plugins [[lein-cljsbuild "1.0.6"]]

  :jvm-opts ^:replace ["-Xmx128M" "-Xms128m" "-server"]

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
