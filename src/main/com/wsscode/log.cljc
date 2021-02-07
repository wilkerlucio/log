(ns com.wsscode.log
  (:require [com.wsscode.log.protocols :as logp]
            [com.fulcrologic.guardrails.core :refer [<- => >def >defn >fdef ? |]]
            [clojure.string :as str])
  #?(:clj (:import (java.util Date))))

(>def ::level #{::level-debug
                ::level-info
                ::level-warn
                ::level-error})

(def log-levels
  {::level-debug 1
   ::level-info  2
   ::level-warn  3
   ::level-error 4})

(defrecord PrintLogger [min-level]
  logp/Logger
  (-log-event [this {::keys [timestamp level event] :as data}]
    (print (str
             timestamp " "
             (if level (str (str/upper-case (subs (name level) 6)) " "))
             event
             " - "
             (pr-str (dissoc data ::level ::event ::timestamp))
             "\n"))))

(def ^:dynamic *active-logger* (->PrintLogger ::level-debug))

(defn now []
  #?(:clj  (Date.)
     :cljs (js/Date.)))

(defn log-event [logger event]
  (logp/-log-event logger event))

(defn make-event [event-level event-name data]
  (merge
    {::event     event-name
     ::level     event-level
     ::timestamp (now)}
    data))

(defmacro debug
  [event-name event-data]
  `(log-event *active-logger*
     (make-event ::level-debug ~event-name ~event-data)))

(defmacro info
  [event-name event-data]
  `(log-event *active-logger*
     (make-event ::level-info ~event-name ~event-data)))

(defmacro warn
  [event-name event-data]
  `(log-event *active-logger*
     (make-event ::level-warn ~event-name ~event-data)))

(defmacro error
  [event-name event-data]
  `(log-event *active-logger*
     (make-event ::level-error ~event-name ~event-data)))

(defmacro with-logger [logger & body]
  `(binding [*active-logger* ~logger]
     ~@body))
