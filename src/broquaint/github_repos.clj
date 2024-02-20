(ns broquaint.github-repos
  (:use [ring.util.response :only (response)]
        [tentacles.core     :only (api-meta)])
  (:require [tentacles.repos :as repos]
            [clojure.core.cache :as cache]
            [clojure.string     :as string]))

;; 1000ms = 1s
;; 1000ms * 60s * 10m = 600000ms
(def ten-minutes 600000)

(defn ttl-cache [seed]
  (cache/ttl-cache-factory seed :ttl ten-minutes))

;; For some reason I wasn't consistently getting a body back with 304
;; responses, possibly due to github but more likely something in the
;; clojure stack silently eating it. So just cache for a day. Could've
;; used a cron job perhaps but here we are.
(def cached-repos
  (atom (ttl-cache {})))

(defn get-repos []
  (or
   (:repos @cached-repos)
   (let [repos (repos/user-repos "broquaint")]
     (do
       (reset! cached-repos (ttl-cache {:repos repos}))
       repos))))

;; Make it friendly to render.
(defn normalize-fields [repo]
  {
   :html_url    (:html_url repo)
   :description (:description repo)
   :full_name (string/replace (:full_name repo) #"broquaint/" "")
   :language  (string/lower-case (or (:language repo) "unknown"))
   :pushed_at (string/replace (:pushed_at repo) #"T.*" "")
   })

(defn get-renderable-repos []
  (let [recent-repos (take 4 (reverse (sort-by :pushed_at (get-repos))))]
    (map normalize-fields recent-repos)))

(defn handler [_] ;; request
  (response {:repos (get-renderable-repos)}))
