(ns broquaint.handler
  (:use compojure.core
        [ring.util.response :only (file-response content-type)]
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route   :as route]
            [ring.middleware.json :as middleware]
            [broquaint.github-repos :as ghr])
  (:gen-class))

(defroutes app-routes
  ;; Landing (and currently only) page.
  (route/files "/" {:root "resources/public"})

  ;; github repo json
  (GET "/repos.json" []
       (middleware/wrap-json-response ghr/handler))

  ;; Nice h5bp 404 page.
  (route/not-found 
   (-> (file-response "resources/404.html")
       (content-type "text/html"))))

(def app
  (handler/site app-routes))

(defn -main [& args]
  (run-jetty app {:port 3080 :join? false}))
