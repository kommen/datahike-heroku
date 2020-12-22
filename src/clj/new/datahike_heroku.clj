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
             ["src/{{nested-dirs}}/foo.clj" (render "foo.clj" data)])))
