(ns cljs_express.endpoints
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [ui.templates :as tmpl]
            [home.comps.widget :as widget]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(nodejs/enable-util-print!)

(defn render-widget
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                 tmpl/default-template
                 {:title "SS Reacting"
                  :content (tmpl/render-to-str
                             widget/hello {})
                  :script "/public/js/base.js"}))
      ))

(defn say-hello!
  [req res]
  (-> res
    (ex/status 200)
    (ex/send (tmpl/render
               tmpl/default-template
               {:title "Bonjour"
                :content (tmpl/render-to-str
                           widget/raw-str-widget
                           {:text "Hello world!!!!"})
                }))
      ))

(defn check-github-users
  [req res]
  (go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials false}
                                   :query-params {:since 135}))
            status (:status response)
            body (:body response)
            names (map :login body)]
        (prn status)
        (prn names)
        (-> res
            (ex/status 200)
            (ex/send (tmpl/render
                       tmpl/default-template
                       {:title "Weather"
                        :content (tmpl/render-to-str
                                   widget/raw-str-widget
                                   {:text (clojure.string/join "," names)})
                        }))
            ))
      ))

(defn app-start
  [req res]
  (-> res
    (ex/status 200)
    (ex/send "Started")))

(defn check-health
  [req res]
  (-> res
    (ex/status 200)
    (ex/send "Healthy!")))

(defn app-stop
  [req res]
  (-> res
    (ex/status 200)
    (ex/send "Stopping...")))

