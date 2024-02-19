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
  (GET "/" []
        (-> (file-response "blog/_site/index.html")
            (content-type "text/html")))

  ;; The ol' blog
  (route/files "/blog" {:root "blog/_site/blog"})
  ;; Assets etc
  (route/files "/assets" {:root "blog/assets"})
  (route/files "/css" {:root "resources/public/css"})
  (route/files "/js" {:root "resources/public/js"})
  (route/files "/fonts" {:root "resources/public/fonts"})

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
