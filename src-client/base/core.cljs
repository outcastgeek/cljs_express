(ns base.core
  (:require [om.core :as om :include-macros true]
            [home.comps.widget :as widget]))

(enable-console-print!)

(om/root
  widget/hello {}
  {:target (.getElementById js/document "app")})
