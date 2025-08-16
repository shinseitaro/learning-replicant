(ns example.core
  (:require [replicant.dom :as r]
            [example.ui :as ui]))

(defn render-ui  [state]
  (r/render js/document.body
            (ui/render-ui state)))

(defn init [store]
  (add-watch
   store
   ::render
   (fn [_ _ _ new-state]
     (render-ui new-state)))

  (r/set-dispatch!
   (fn [_ event-data]
     (doseq [[action & args] event-data]
       (case action
         ::ui/inc-number (swap! store update :number inc)))))

  (swap! store assoc :hoge :moge))

