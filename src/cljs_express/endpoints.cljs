(ns cljs_express.endpoints
  (:require [cljs.nodejs :as nodejs]
            [util.os :as os]
            [express.sugar :as ex]
            [cljs_express.views :as views]
            [home.comps.widget :as widget]))

(nodejs/enable-util-print!)

(defn render-widget
  [req res]
  (-> res
      (ex/status 200)
      (ex/send (views/render widget/hello {}))))

(defn say-hello!
  [req res]
  (-> res
    (ex/status 200)
    (ex/send "Hello world!!")))

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

