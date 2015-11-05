(ns utils.async
  #?(:clj (:require [clojure.core.async :as async :refer [<! go]])
     :cljs (:require-macros [cljs.core.async.macros :refer [<! go]]
                            [utils.async :refer [<? go!]])))

(defn throw-err [x]
  (if #?(:clj (instance? Exception x)
         :cljs (instance? js/Error x))
    (throw x)
    x))

(defmacro <? [expr]
  `(throw-err (<! ~expr)))

(defmacro go! [& body]
  `(go
     (try
       ~@body
       (catch :default error#
         error#))))






