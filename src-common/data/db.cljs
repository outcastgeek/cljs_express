(ns data.db
  (:require [datascript.transit :as dt]
            [datascript.core :as d]))

(defn- common-db []
  (-> (d/empty-db {:email {:db/unique :db.unique/identity}})
      (d/db-with [{:name "Lambert" :email "lambert@lambert.com"}
                  {:name "Digbeu" :email "digbeu@digbeupoule.com"}])
      ))

(defn do-round-trip []
  (=
   (common-db)
   (dt/read-transit-str (dt/write-transit-str (common-db)))
   ))

