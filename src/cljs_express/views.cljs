(ns cljs_express.views
  (:require [om.dom :as dom :include-macros true]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defn- do-template [content]
  (om/component
    (html [:html
           [:header
            [:meta {:charset "utf-8"}]
            [:meta {:http-equiv "content-type" :content "text/html; charset=UTF-8"}]
            [:title "CLJS Express"]
            (html/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
            ]
           [:body
            [:div {:id "container"
                   :dangerouslySetInnerHTML {:__html content}}]
            [:script {:type "text/javascript" :src "https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}]
            [:script {:type "text/javascript" :src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]
            [:script {:type "text/javascript" :src "/public/js/base.js"}]
            ]
           ])))

(defn- render-to-str [widget data]
  (dom/render-to-str
    (om/build widget data)))

(defn render
  [widget data]
  (str
    "<!DOCTYPE html>"
    (render-to-str
      do-template (render-to-str widget/hello {}))))

;(defn render
;  [widget data]
;  (str
;    "<!DOCTYPE html>"
;    (render-to-str
;      do-template "")))

;(defn render
;  [widget data]
;  (str
;    "<!DOCTYPE html>"
;    (render-to-str
;      do-template (render-to-str widget data))))


