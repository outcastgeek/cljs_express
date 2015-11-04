(ns ui.templates
  (:require [cljs.core.match :refer-macros [match]]
            [om.dom :as dom :include-macros true]
            [om.core :as om :include-macros true]
            [om.next :as omnext :refer-macros [defui]]
            [sablono.core :as html :refer-macros [html]]))

(enable-console-print!)

(defui default-template-ui
  Object
  (render [this]
          (let [{:keys [content script title]} (omnext/props this)]
            (html [:html {:lang "en"}
          [:header
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "content-type" :content "text/html; charset=UTF-8"}]
           [:title (str "CLJS Express | " title)]
           (html/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
           (html/include-css "/public/css/sample.css")
           ]
          [:body
           [:div {:class "container"}
            [:div {:class "jumbotron"}
             [:h1 "CLJS Express"]]
            [:div {:class "row"}
             [:div {:id "app"
                    :class "col-lg-12"} content]]]
           [:script {:type "text/javascript" :src "https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}]
           [:script {:type "text/javascript" :src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]
           [:script {:type "text/javascript" :src script}]
           ]
          ]))))

(def default-template
  (omnext/factory default-template-ui))

(defui raw-template-ui
  Object
  (render [this]
   (let [{:keys [content script title]} (omnext/props this)]
     (html [:html {:lang "en"}
          [:header
           [:meta {:charset "utf-8"}]
           [:meta {:http-equiv "content-type" :content "text/html; charset=UTF-8"}]
           [:title (str "RAW CLJS Express | " title)]
           (html/include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css")
           (html/include-css "/public/css/sample.css")
           ]
          [:body
           [:div {:class "container"}
            [:div {:class "jumbotron"}
             [:h1 "RAW CLJS Express"]]
            [:div {:class "row"}
             [:div {:id "app"
                    :class "col-lg-12"} content]]]
           [:script {:type "text/javascript" :src "https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"}]
           [:script {:type "text/javascript" :src "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"}]
           [:script {:type "text/javascript" :src script}]
           ]
          ]))))

(def raw-template
  (omnext/factory raw-template-ui))

(defn- render-to-str [widget]
  (js/React.renderToString widget))

(defn render
  [resp]
  (match resp
         [:default data]
         (str
           "<!DOCTYPE html>"
           (render-to-str (default-template data))
           )
         [:raw data]
         (str
           "<!DOCTYPE html>"
           (render-to-str (raw-template data))
           )
         [_ data] data))
