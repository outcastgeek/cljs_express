(ns cljs_express.endpoints
  (:require-macros [cljs.core.async.macros :refer [go alt!]])
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [ui.templates :as tmpl]
            [home.comps.widget :as widget]
            [cljs-http.client :as http]
            [cljs.core.async :as async :refer [<! >! chan close! timeout]]))

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
  (let [users-chan (chan)]
    (go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials false}
                                     :query-params {:since 135}))
              status (:status response)
              body (:body response)
              names (map :login body)]
          (prn status)
          (prn names)
          ;(println "Going to Sleep for a bit!")
          ;(<! (timeout 2000))
          (println "Channeling names...")
          (>! users-chan names)))
    (go (let [flush (fn [raw-str]
                      (-> res
                          (ex/status 200)
                          (ex/send (tmpl/render
                                     tmpl/default-template
                                     {:title "Weather"
                                      :content (tmpl/render-to-str
                                                 widget/raw-str-widget
                                                 {:text raw-str})
                                      }))
                          ))]
          (alt!
            users-chan ([names] (flush (clojure.string/join "," names)))
            (timeout 1000) (flush "Could not Fetch the Github Users!"))
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

