(ns cljs_express.core
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [cljs_express.endpoints :as ep]))

(nodejs/enable-util-print!)

(def routes
  (ex/routes
    [:get "/" ep/say-hello!]
    [:get "/react" ep/render-widget]
    [:get "/github-users" ep/check-github-users]
    [:get "/_ah/start" ep/app-start]
    [:get "/_ah/health" ep/check-health]
    [:get "/_ah/stop" ep/app-stop]
    [:all "/foo" ep/say-hello!]))

(defn -main []
  (-> (ex/app)
      (ex/static "static" "/public")
      (ex/use (.requestLogger ex/logfmt))                      ; Logger
      (ex/use "/" routes)
      (ex/listen (if-let [PORT (os/env "PORT")] PORT 3333))))

(set! *main-cli-fn* -main)
