(ns home.comps.widget
  (:require [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om-tools.core :refer-macros [defcomponent]]))

(enable-console-print!)

(defcomponent raw-str-widget [data owner]
  (display-name [_] "RawString")
  (render [_]
          (println "Raw Stringing...")
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

(defcomponent hello [data owner]
  (display-name [_] "Hello")
  (render [_]
    (println "Rendering ...")
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


