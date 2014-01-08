(ns broquaint.github-repos
  (:use [ring.util.response :only (response)]
        [tentacles.core     :only (api-meta)])
  (:require [tentacles.repos :as repos]
            [clojure.core.cache :as cache]
            [clojure.string     :as string]))

;; 1000ms = 1s
;; 1000 * 60 * 60 * 24 = 86400000
(def one-day 86400000)

(defn ttl-cache [seed]
  (cache/ttl-cache-factory seed :ttl one-day))

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
   :language  (string/lower-case (:language repo))
   :pushed_at (string/replace (:pushed_at repo) #"T.*" "")
   })

(defn handler [_] ;; request
  (let [recent-repos (take 4 (reverse (sort-by :pushed_at (get-repos))))
        for-json (map normalize-fields recent-repos)]
    (response {:repos for-json})))
