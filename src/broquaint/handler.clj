(ns broquaint.handler
  (:use compojure.core
        ring.adapter.jetty
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.handler :as handler]
            [compojure.route   :as route]
            [fs.core           :as fs])
  (:gen-class))

(defn- get-file-name [name]
  (str fs/*cwd* "/resources/public/" name))

(defn- resource-file-str [name]
  (slurp (get-file-name name)))

(defroutes app-routes
  (GET "/" [] (resource-file-str "index.html"))
  (route/resources "/")
  (route/not-found (resource-file-str "../404.html") ))

(def app
  (handler/site app-routes))

(defn -main [& args]
  (run-jetty app {:port 3080 :join? false}))
