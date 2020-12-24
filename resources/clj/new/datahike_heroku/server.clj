(ns {{namespace}}.server
    (:require [clojure.string :as str]
              [datahike-postgres.core]
              [datahike.api :as d]
              [ring.adapter.jetty :as jetty]
              [ring.middleware.params :as params]))

(def app-state (atom {:conn nil
                      :server nil}))

(defn handler
  "A ring handler returning all todo items in the database
  If a `todo` query param is present, transacts it to the database"
  [request]
  (when-let [todo (get-in request [:params "todo"])]
    (d/transact (:conn @app-state) [{:todo/name todo}]))

  (let [todos (d/q '[:find [?name ...]
                     :where [_ :todo/name ?name]]
                   (d/db (:conn @app-state)))]
    {:status  200
     :headers {"Content-Type" "text/html"}
     :body    (str "Todo items: <br/>" (str/join "<br/>" todos))}))

(def app
  "The application's main ring handler which is passed to the web server"
  (params/wrap-params handler))

(def schema
  "The database schema which is transacted when the app starts"
  [{:db/ident :todo/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}])

(defn env-port
  "Parses the `PORT` env var provided by Heroku on which port
  to start the http server"
  []
  (some-> (System/getenv "PORT") Integer/parseInt))

(defn env-db-config
  "Constructs a datahike configuration map from the the heroku
  provided `DATABASE_URL` or returns nil if that env var is not present"
  []
  (when-let [db-url (System/getenv "DATABASE_URL")]
    (let [uri                 (java.net.URI. db-url)
          [username password] (str/split (.getUserInfo uri) #":")]
      {:store
       {:backend  :pg
        :host     (.getHost uri)
        :username username
        :password password
        :path     (.getPath uri)
        :port     (.getPort uri)}})))

(defn start-db!
  "Creates a datahike connection and transacts the schema
  If no DATABASE_URL is present, uses an in-memory store"
  []
  (let [db-config (or (env-db-config)
                      {:store {:backend :mem :id "server"}})]
    (when-not (d/database-exists? db-config)
      (d/create-database db-config))

    (let [conn (d/connect db-config)]
      (d/transact conn schema)
      (swap! app-state assoc :conn conn))))

(defn start-http!
  "Starts an http webserver on `PORT`, or if not present on 3000"
  []
  (let [port   (or (env-port) 3000)
        server (jetty/run-jetty #'app {:port port :join? false})]
    (swap! app-state assoc :server server)))

(defn -main []
  (start-db!)
  (start-http!))

(comment
  (swap! app-state update :server #(.stop %)))
