(ns cljs_express.endpoints
  (:require-macros [cljs.core.async.macros :refer [alt! go]]
                   [com.rpl.specter.macros :refer [paramsfn]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :as async :refer [<! put! chan close! timeout]]
            [com.rpl.specter :as s]
            [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [parse.candy :as cdy]
            [ui.templates :as tmpl]
            [home.comps.widget :as widget]
            [data.db :as db]))

(nodejs/enable-util-print!)

(defn handle-response [response grab-data-fn]
  (let [status (:status response)]
    (prn "Status: " status)
    (condp = status
      200 (grab-data-fn response)
      401 ["Ouch!!!!"]
      403 ["Remote Service Denied Access"]
      404 ["Could not Find Anything"]
      500 ["Something Broke"])))

(defn render-widget
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                [:default
                 {:title "SS Reacting"
                  :content (widget/hello {:upper-bound 8})
                  :script "/js/base.js"
                  }]))
      ))

(defn say-hello!
  [req res]
  (println "Was DB Round Trip Successful? " (db/do-round-trip))
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                 [:raw
                  {:title "Bonjour!!"
                   :content (widget/raw-str-widget {:text "Hello world!!!"})
                   }]))
      ))

(defn check-github-users
  [req res]
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Fetch, Parse, Render, or Timeout  ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (go
    (alt!
      (http/get "https://api.github.com/users" {:with-credentials false}
                :query-params {:since 135})
      ([response]
       (let [names (handle-response
                    response
                    (fn [resp]
                      (map :login (:body resp))))]
         (prn "Names: " names)
         (-> res
             (ex/status 200)
             (ex/send (tmpl/render
                        [:default
                         {:title "Github Users"
                          :content (widget/raw-str-widget {:text (clojure.string/join "," names)})
                          }]))
             )))
      (timeout 1000)
      (-> res
          (ex/status 200)
          (ex/send (tmpl/render
                     [:default
                      {:title "Github Users"
                       :content "Could not Fetch the Github Users!"
                       }])))
      )))

(defn check-weather
  [req res]
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Fetch, Parse, Render, or Timeout  ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (go (let [params (aget req "params")
            city-query (aget params "city")]
        (println (str "City Query: " city-query))
        (alt!
          (http/get (str "http://api.openweathermap.org/data/2.5/weather?q=" city-query "&appid=2de143494c0b295cca9337e1e96b00e0"))
          ([raw-resp]
           (let [grab-data-fn (fn [resp]
                                     (let [city (-> resp :body :name)
                                           country (-> resp :body :sys :country)
                                           description (-> resp :body :weather first :description)
                                           temperature (-> resp :body :main :temp)]
                                       [city country description temperature]))
                 weather-info (-> raw-resp (js->clj) (handle-response grab-data-fn))]
             (prn weather-info)
             (-> res
                 (ex/status 200)
                 (ex/send (tmpl/render
                            [:default
                             {:title "Weather"
                              :content (widget/raw-str-widget {:text (clojure.string/join "," weather-info)})
                              }])))
             ))
          (timeout 1000)
          (-> res
              (ex/status 200)
              (ex/send (tmpl/render
                         [:default
                          {:title "Weather"
                           :content "Could not Fetch the Weather Info."
                           }])))
          ))
      ))

;; Application LifeCycle
(defn app-start
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                 [:none "Started"]))
      ))

(defn check-health
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                 [:none "Healthy!"]))
      ))

(defn app-stop
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (tmpl/render
                 [:none "Stopping..."]))
      ))
