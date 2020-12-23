(ns {{namespace}}.server
    (:require [clojure.string :as str]
              [ring.adapter.jetty :as jetty]))

(defn handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello World"})

(defn env-port []
  (some-> (System/getenv "PORT") Integer/parseInt))

(defn env-db-config []
  (let [uri                 (java.net.URI. (System/getenv "DATABASE_URL"))
        [username password] (str/split (.getUserInfo uri) #":")]
    {:backend  :pg
     :host     (.getHost uri)
     :username username
     :password password
     :path     (.getPath uri)
     :port     (.getPort uri)}))

(defn -main []
  (let [port (or (env-port) 3000)]
    (jetty/run-jetty handler {:port port})))
