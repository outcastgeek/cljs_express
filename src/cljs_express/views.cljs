(ns cljs_express.views
  (:require [om.dom :as dom :include-macros true]
            [om.core :as om :include-macros true]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defn- default-template [data]
  (om/component
    (html [:html {:lang "en"}
           [:header
            [:meta {:charset "utf-8"}]
            [:meta {:http-equiv "content-type" :content "text/html; charset=UTF-8"}]
            [:title (str "CLJS Express | " (:title data))]
            (html/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
            (html/include-css "/public/css/sample.css")
            ]
           [:body
            [:div {:class "container"}
             [:div {:class "jumbotron"}
              [:h1 "CLJS Express"]]
             [:div {:class "row"}
              [:div {:id "app"
                     :class "col-lg-12"
                     :dangerouslySetInnerHTML {:__html (:content data)}}]]]
            [:script {:type "text/javascript" :src "https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}]
            [:script {:type "text/javascript" :src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]
            [:script {:type "text/javascript" :src (:script data)}]
            ]
           ])))

(defn- render-to-str [widget data]
  (dom/render-to-str
    (om/build widget data)))

(defn render
  [tmpl data]
  (str
    "<!DOCTYPE html>"
    (render-to-str tmpl data)
    ))



