(ns com.wsscode.log.protocols)

(defprotocol Logger
  (-log-event [this data]))
