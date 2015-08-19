(ns home.comps.widget
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defn un-bouton [data]
  (println "Buttoning ...")
  (html
    [:hr]
    (html/submit-button (:text data))))

(defn hello [data]
  (println "Rendering ...")
  (om/component
    (html [:div "Hello world!"
           [:ul (for [n (range 1 10)]
                  [:li {:key n} n])]
           (un-bouton {:text "React!!"})])))

;(defn hello [data]
;  (println "Rendering ...")
;  (html [:div "Hello world!"
;         [:ul (for [n (range 1 10)]
;                [:li {:key n} n])]
;         (un-bouton {:text "React!!"})]))


