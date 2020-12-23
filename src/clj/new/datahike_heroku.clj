(ns clj.new.datahike-heroku
  (:require [clj.new.templates :refer [renderer project-data ->files]]))

(defn datahike-heroku
  "FIXME: write documentation"
  [name]
  (let [render (renderer "datahike-heroku")
        data   (project-data name)]
    (println "Generating fresh 'clj new' datahike-heroku project.")
    (->files data
             ["deps.edn" (render "deps.edn" data)]
             ["project.clj" (render "project.clj" data)]
             ["bin/build" (render "build" data) :executable true]
             ["Procfile" (render "Procfile" data)]
             ["src/{{nested-dirs}}/server.clj" (render "server.clj" data)])))

(comment
  (with-bindings {#'clj.new.templates/*force?* true}
    (datahike-heroku "test1app")))
