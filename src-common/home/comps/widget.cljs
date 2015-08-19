(ns home.comps.widget
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defn raw-str-widget [data]
  (om/component
    (html
      [:div (:text data)])))

(defn un-bouton [data]
  (println "Buttoning ...")
  (html
    [:hr]
    [:input {
             :type "submit"
             :class "btn btn-default"
             :value (:text data)}]
    ))

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


