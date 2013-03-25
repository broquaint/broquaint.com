(ns broquaint.handler
  (:use compojure.core [ring.util.response :only (file-response)]
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route   :as route])
  (:gen-class))

(defroutes app-routes
  (route/files "/" {:root "resources/public"})
  (route/not-found (file-response "resources/404.html")))

(def app
  (handler/site app-routes))

(defn -main [& args]
  (run-jetty app {:port 3080 :join? false}))
