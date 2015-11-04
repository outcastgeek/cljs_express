(ns home.comps.widget
  (:require [om.core :as om :include-macros true]
            [om.next :as omnext :refer-macros [defui]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defui raw-str-widget-ui
  Object
  (render [this]
          (let [{:keys [text]} (omnext/props this)]
            (println "Raw Stringing...")
            (html[:div text]))
          ))

(def raw-str-widget
  (omnext/factory raw-str-widget-ui))

(defui un-bouton-ui
  Object
  (render [this]
          (let [{:keys [text]} (omnext/props this)]
            (println "Buttoning ...")
            (html
             [:hr]
             [:input {
                      :type "submit"
                      :class "btn btn-default"
                      :value text}]
             ))))

(def un-bouton
  (omnext/factory un-bouton-ui))

(defui hello-ui
  Object
  (render [this]
          (let [{:keys [upper-bound]} (omnext/props this)]
            (println "Rendering ...")
            (html [:div "Hello world!"
                   [:ul (for [n (range 1 upper-bound)]
                          [:li {:key n} n])]
                   #(un-bouton {:text "React!!"})])) 
          ))

(def hello
  (omnext/factory hello-ui))
