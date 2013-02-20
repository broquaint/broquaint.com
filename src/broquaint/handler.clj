(ns broquaint.handler
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route   :as route]
            )
  (:gen-class))

(defroutes app-routes
  (route/files "/")
  (route/not-found "Err dunno!\n"));(resource-file-str "../404.html")

(def app
  (->
   (handler/site app-routes)
   ))

(defn -main [& args]
  (run-jetty app {:port 3080 :join? false}))
