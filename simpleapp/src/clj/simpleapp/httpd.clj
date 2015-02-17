(ns simpleapp.httpd
  (:require [org.httpkit.server :as http-kit]
            [taoensso.timbre :refer [info]]))

;;;; Lifecycle management for singleton web server

(defonce web-server_ (atom nil))  ;; {:server _ :port _ :stop-fn (fn [])}

(defn stop! [] (when-let [m @web-server_] ((:stop-fn m))))

(defn start! [app & [port]]
  (stop!)
  (info "Starting http-kit...")
  (let [port (or port 0)  ;; 0 => auto
        stop-fn (http-kit/run-server app {:port port :join? false})
        port (:local-port (meta stop-fn))
        stop-fn (fn [] (stop-fn :timeout 100))]
    (info "Web server is running at:" (format "http://localhost:%s/" port))
    (reset! web-server_ {:stop-fn stop-fn :port port})))
