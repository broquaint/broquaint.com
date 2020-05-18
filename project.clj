(defproject broquaint "0.1.4"
  :description "The broquaint.com website"
  :url "http://broquaint.com/"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring/ring-jetty-adapter "1.1.8"]
                 [compojure "1.1.5"]
                 ;; broquaint.github-repos deps vv
                 [ring/ring-json "0.1.2"]
                 [clj-http "0.7.8"] ;; Needed otherwise tentacles blows up
                 [irresponsible/tentacles "0.6.6"]
                 [org.clojure/core.cache "0.6.3"]]
  :plugins [[lein-ring "0.8.2"]]
  :ring {:handler broquaint.handler/app :port 3080}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}}
  :main broquaint.handler)
