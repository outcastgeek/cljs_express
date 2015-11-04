(ns express.sugar
  "Ligweight interface to requirejs."
  (:refer-clojure :exclude [set get])
  (:require [cljs.nodejs :as nodejs]))

  ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Constants
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def ^{:doc "Global express import."
       :private true}
  express (nodejs/require "express"))

(def ^{:doc "Global logger import."
       :private true}
  logger (nodejs/require "morgan"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Constructors
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn app
  "Create a express app instance."
  []
  (express))

(defn router
  "Create a express router instance."
  [& [opts]]
  (.Router express (clj->js opts)))

(defn routes
  "Create a router instance and attach
  handlers to it."
  [& args]
  (let [app (router)]
    (reduce (fn [app [verb path handler]]
              (condp = verb
                :all (. app (all path handler))
                :get (. app (get path handler))
                :post (. app (post path handler))
                :put (. app (put path handler))
                :delete (. app (delete path handler))
                app))
            app
            args)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Request & Response Helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn send
  [res data]
  (.send res data))

(defn status
  [res code]
  (.status res code))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Helpers
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn use
  "Attach middlware to express app."
  ([app handler]
   (.use app handler))
  ([app path handler]
   (.use app path handler)))

(defn listen
  "Shortcut for start http server."
  ([app port]
   (.listen app port "0.0.0.0"))
  ([app port callback]
   (.listen app port "0.0.0.0" callback)))

(defn set
  [app key value]
  (.set app key value)
  app)

(defn get
  [app key]
  (.get app key))

(defn enable
  [app key]
  (.enable app key)
  app)

(defn disable
  [app key]
  (.disable app key)
  app)

(defn enabled?
  [app key]
  (.enabled app key))

(defn disabled?
  [app key]
  (.disabled app key))

(defn static
  ([app path]
   (use app ((aget express "static") path)))
  ([app path mount]
   (use app mount ((aget express "static") path))))
