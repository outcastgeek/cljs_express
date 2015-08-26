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

(defn- handle-response [response grab-data-fn]
  (let [status (:status response)]
    (prn "Status: " status)
    (condp = status
      200 (grab-data-fn response)
      403 ["Remote Service Denied Access"]
      404 ["Could not Find Anything"]
      500 ["Something Broke"])))

(defn check-github-users
  [req res]
  (let [users-chan (chan)]
    (go (let [response (<! (http/get "https://api.github.com/users" {:with-credentials false}
                                     :query-params {:since 135}))
              names (handle-response
                      response
                      (fn [resp]
                        (map :login (:body resp))))]
          (prn "Names: " names)
          ;(println "Going to Sleep for a bit!")
          ;(<! (timeout 2000))
          (println "Channeling names...")
          (>! users-chan names)))
    (go (let [flush (fn [raw-str]
                      (-> res
                          (ex/status 200)
                          (ex/send (tmpl/render
                                     tmpl/default-template
                                     {:title "Github Users"
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

(defn check-weather
  [req res]
  (let [weather-chan (chan)]
    (go (let [params req/params
              city-query (aget params "city")
              raw-resp (<! (http/get (str "http://api.openweathermap.org/data/2.5/weather?q=" city-query)))
              response (js->clj raw-resp)
              weather-info (handle-response
                             response
                             (fn [resp]
                               (let [city (-> resp :body :name)
                                     country (-> resp :body :sys :country)
                                     description (-> resp :body :weather first :description)
                                     temperature (-> resp :body :main :temp)]
                                 [city country description temperature])
                               ))]
          (println (str "City Query: " city-query))
          (println response)
          (prn weather-info)
          ;(println "Going to Sleep for a bit!")
          ;(<! (timeout 2000))
          (println "Channeling Weather Info...")
          (>! weather-chan weather-info)))
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
            weather-chan ([names] (flush (clojure.string/join "," names)))
            (timeout 1000) (flush (str "Could not Fetch the Weather Infor for: " city-query)))
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

