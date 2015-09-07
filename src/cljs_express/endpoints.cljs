(ns cljs_express.endpoints
  (:require-macros [cljs.core.async.macros :refer [alt! go]])
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [ui.templates :as tmpl]
            [home.comps.widget :as widget]
            [cljs-http.client :as http]
            [cljs.core.async :as async :refer [<! put! chan close! timeout]]))

(nodejs/enable-util-print!)

(defn handle-response [response grab-data-fn]
  (let [status (:status response)]
    (prn "Status: " status)
    (condp = status
      200 (grab-data-fn response)
      403 ["Remote Service Denied Access"]
      404 ["Could not Find Anything"]
      500 ["Something Broke"])))

(defn send-response [response]
  (let [{res :response
         template :template
         data :data} response]
    (println "Responding with: " data)
    (-> res
        (ex/status 200)
        (ex/send (tmpl/render
                   [template data])))
    ))

(defn render-widget
  [req res]
  (send-response
   {:response res
    :template :default
    :data {:title "SS Reacting"
           :content (tmpl/render-to-str
                     widget/hello {})
           :script "/public/js/base.js"
           }
    }))

(defn say-hello!
  [req res]
  (send-response
   {:response res
    :template :raw
    :data {:title "Bonjour!!"
           :content (tmpl/render-to-str
                     widget/raw-str-widget
                     {:text "Hello world!!!"})
           }
    }))

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
         (send-response
          {:response res
           :template :default
           :data {:title "Github Users"
                  :content (tmpl/render-to-str
                            widget/raw-str-widget
                            {:text (clojure.string/join "," names)})
                  }}))
       )
      (timeout 1000)
      (send-response
       {:response res
        :template :default
        :data {:title "Github Users"
               :content "Could not Fetch the Github Users!"
               }}))))

(defn check-weather
  [req res]
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;; Fetch, Parse, Render, or Timeout  ;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
  (go (let [params (aget req "params")
            city-query (aget params "city")]
        (println (str "City Query: " city-query))
        (alt!
          (http/get (str "http://api.openweathermap.org/data/2.5/weather?q=" city-query))
          ([raw-resp]
           (let [weather-info (-> raw-resp
                                  (js->clj)
                                  (handle-response
                                   (fn [resp]
                                     (let [city (-> resp :body :name)
                                           country (-> resp :body :sys :country)
                                           description (-> resp :body :weather first :description)
                                           temperature (-> resp :body :main :temp)]
                                       [city country description temperature])
                                     )))]
             (prn weather-info)
             (send-response
              {:response res
               :template :default
               :data {:title "Weather"
                      :content (tmpl/render-to-str
                                widget/raw-str-widget
                                {:text (clojure.string/join "," weather-info)})
                      }})))
          (timeout 1000)
          (send-response
           {:response res
            :template :default
            :data {:title "Weather"
                   :content "Could not Fetch the Weather Info."
                   }})
          ))
      ))

(defn app-start
  [req res]
  (send-response
   {:response res
    :data "Started"}))

(defn check-health
  [req res]
  (send-response
   {:response res
    :data "Healthy!"}))

(defn app-stop
  [req res]
  (send-response
   {:response res
    :data "Stopping..."}))

