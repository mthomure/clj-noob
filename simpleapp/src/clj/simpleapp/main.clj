(ns simpleapp.main
  (:require [simpleapp.router :as router]
            [simpleapp.httpd :as httpd]
            [simpleapp.event :as event]
            [compojure.core :as comp :refer (defroutes GET)]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [clojure.pprint :refer [pprint]]
            [hiccup.core :as hiccup]))

(defn landing-page [req]
  (hiccup/html
   [:h1 "simpleapp landing page"]
   ;; Include our cljs target
   [:script {:src "main.js"}]))

(defroutes http-routes
  (GET "/" req landing-page)
  (route/resources "/")  ;; static files including public/main.js
  (route/not-found "<h1>Page not found</h1>"))

;;;; Routing handlers

(defn wrap-logging
  "write http requests to stdout"
  [handler]
  (fn [req]
    (println "WRAP-LOGGING:")
    (pprint req)
    (handler req)))

(defn app [socket]
  (-> http-routes
      ;; handle websocket handeshake
      (router/wrap-router "/event" socket)
      wrap-logging
      ;; add session handling
      (wrap-defaults site-defaults)))

(defn start! [& [port]]
  (let [socket (router/socket)]
    ;; start http server
    (httpd/start! (app socket) port)
    ;; start event handler
    (router/start! event/handler socket)))

(defn stop! []
  (httpd/stop!)
  (router/stop!))

(defn -main [& [port]]
  (let [port (when port (Integer. port))]
    (start! port)))
