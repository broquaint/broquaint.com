(ns broquaint.index-response
  (:use [ring.util.response :only (response file-response content-type)])
  (:require [broquaint.github-repos :as ghr]
            [hickory.core :as hick]
            [hickory.select :as hs]
            [hickory.zip :refer [hickory-zip]]
            [clojure.zip :as zip]
            [hickory.render :refer [hickory-to-html]])
  (:gen-class))

(defn helem [tag attrs kids]
  {:type :element :tag tag :attrs attrs :content [kids]})
(defn htag [tag kids]
  {:type :element :tag tag :content kids})

(defn repo-list-entry [{:keys [html_url title full_name language pushed_at description]}]
  (htag :li
        [(helem :a {:href html_url :title title} full_name)
         " ⋅ "
         (helem :span {:class "lang" :title "Main language"} language)
         " ⋅ "
         (helem :span {:class "ymd" :title "Pushed at "} pushed_at)
         (htag :p [description])]))

(defn insert-repos-into-index [path]
  (let [repo-elems (map repo-list-entry (ghr/get-renderable-repos))
        repo-list (htag :ul repo-elems)
        index-hick (-> (slurp path) hick/parse hick/as-hickory)
        repo-sel (first (hs/select-locs
                         (hs/child (hs/id "projects") (hs/tag "ul")) index-hick))
        index-hzip (hickory-zip index-hick)]
     (hickory-to-html (zip/root (zip/replace repo-sel repo-list)))))

(defn index-html []
  (let [html-path "blog/_site/index.html"
        html-response
        (if (empty? @ghr/cached-repos)
          ;; Have JS load the repos async
          (file-response html-path)
          (response (insert-repos-into-index html-path)))]
    html-response))

(defn index-html-response []
  (-> (index-html)
      (content-type "text/html")))
