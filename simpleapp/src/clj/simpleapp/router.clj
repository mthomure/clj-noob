(ns simpleapp.router
  (:require [compojure.core :refer (routes GET POST)]
            [taoensso.sente :as sente]
            [taoensso.sente.server-adapters.http-kit :refer [http-kit-adapter]]))

;; singleton sente socket. avoid using this directly. instead use (socket).
(def SOCKET (sente/make-channel-socket! http-kit-adapter))

(defn socket [] SOCKET)

(defn wrap-router
  "add routes needed for sente comms"
  [app  ;; compojure application to wrap
   comms-path  ;; url path for router, such as "/chsk"
   socket]
  (let [{:keys [ajax-post-fn ajax-get-or-ws-handshake-fn]} socket]
    (routes
     (GET comms-path req ajax-get-or-ws-handshake-fn)
     (POST comms-path req ajax-post-fn)
     app)))

;;;; Lifecycle management for singleton sente router using core.async

(defonce router_ (atom nil))

(defn stop! [] (when-let [stop-f @router_] (stop-f)))

(defn start! [handler socket]
  ;; for now, use singleton socket
  (let [{:keys [ch-recv]} socket]
    (stop!)
    ;; run core.async event loop
    (reset! router_ (sente/start-chsk-router! ch-recv handler))))
