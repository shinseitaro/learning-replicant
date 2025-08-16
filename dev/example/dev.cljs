(ns example.dev
  (:require [example.core :as core]))

;; 状態を持つための保管場所としての store 
(defonce store (atom {:number 0}))

(defn main []
  (core/init store)
  (js/console.log "Loaded"))

(defn ^:dev/after-load reload []
  (core/init store)
  (js/console.log "Reloaded!"))
