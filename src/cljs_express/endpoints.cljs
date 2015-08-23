(ns cljs_express.endpoints
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [ui.templates :as tmpl]
            [home.comps.widget :as widget]))

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

