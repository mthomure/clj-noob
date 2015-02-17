(ns simpleapp.client
  (:require [taoensso.sente :as sente]
            [cljs.core.async :as async]
            [taoensso.encore :as enc :refer [logf]]))

(enable-console-print!)

;;;; routing

;; singleton sente socket. avoid using this directly. instead use (socket).
(def SOCKET (sente/make-channel-socket! "/event" {:type :auto}))

(defn socket [] SOCKET)

(def router_ (atom nil))

(defn stop-router! [] (when-let [stop-f @router_] (stop-f)))

(defn start-router! [handler socket]
  ;; for now, use singleton socket
  (let [{:keys [ch-recv]} socket]
    (stop-router!)
    ;; run core.async event loop
    (reset! router_ (sente/start-chsk-router! ch-recv handler))))

;;;; event handling

(defmulti handle-event :id)  ;; dispatch on event-id

;; entry-point for event handling
(defn event-handler [{:keys [event] :as msg}]
  (logf "Event: %s" event)
  (handle-event msg))

;; fallback event handler
(defmethod handle-event :default [{:keys [event] :as msg}]
  (logf "Unhandled event: %" event))

;;;; on-load

(start-router! event-handler (socket))
