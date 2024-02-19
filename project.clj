(defproject broquaint "0.2.0"
  :description "The broquaint.com website"
  :url "http://broquaint.com/"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.11.0"]
                 [ring/ring-jetty-adapter "1.11.0"]
                 [compojure "1.7.1"]
                 ;; broquaint.github-repos deps vv
                 [ring/ring-json "0.5.1"]
                 [irresponsible/tentacles "0.6.9" :exclusions [cheshire]]
                 [org.clojure/java.classpath "1.0.0"]
                 [org.clojure/core.cache "1.0.225"]]
  :plugins [[lein-ring "0.12.6"]]
  :ring {:handler broquaint.handler/app :port 3080}
  :profiles
  {:dev {:dependencies [[ring/ring-mock "0.4.0"]]}}
  :main broquaint.handler)
