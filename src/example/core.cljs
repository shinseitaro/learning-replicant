(ns example.core
  (:require [replicant.dom :as r]
            [example.ui :as ui]
            [example.layout :as layout]))

(def views [{:id   :counter
             :text "カウンター"}
            {:id   :temperature
             :text "気温"}])

(defn get-current-view [state]
  (or (:current-view state)
      (-> views first :id)))

(defn render-ui  [state]
  (let [current-view (get-current-view state)]
    [:div
     (layout/tab-bar
      current-view
      views)
     (case current-view
       :counter (ui/render-ui state)
       [:div.m-8
        [:h1.text-lg "気温の画面"]])]))

(defn init [store]
  (add-watch
   store
   ::render
   (fn [_ _ _ new-state]
     (r/render js/document.body (render-ui new-state))))

  (r/set-dispatch!
   (fn [_ event-data]
     (doseq [[action & args] event-data]
       (case action
         ::ui/inc-number (swap! store update :number inc)
         ::layout/set-current-view
         (let [[id] args]
           (swap! store assoc :current-view id))))))

  (swap! store assoc :hoge :moge))

