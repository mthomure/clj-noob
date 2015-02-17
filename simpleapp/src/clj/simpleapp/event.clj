(ns simpleapp.event
  (:require [taoensso.timbre :refer [info]]))

(defmulti handle-event :id)  ;; dispatch on event-id

;; entry-point for event handling
(defn handler [{:keys [event] :as msg}]
  (info "Event:" event)
  (handle-event msg))

;; fallback event handler
(defmethod handle-event :default [{:keys [event] :as msg}]
  (info "Unhandled event:" event))

;; Add your (defmethod event-msg-handler <event-id> [msg] <body>)s here...
