(ns example.core
  (:require [replicant.dom :as r]
            [example.counter :as counter]
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
       :counter (counter/render-ui state)
       [:div.m-8
        [:h1.text-lg "気温の画面"]])]))

(defn perform-actions [state event-data]
  (mapcat
   (fn [action]
     (or (counter/perform-action state action)
         (layout/perform-action state action)))
   event-data))

(defn process-effect [store [effect & args]]
  (case effect
    :effect/assoc-in (apply swap! store assoc-in args)));; args は可変長なので apply を使う

(defn init [store]
  (add-watch
   store
   ::render
   (fn [_ _ _ new-state]
     (r/render js/document.body (render-ui new-state))))

  (r/set-dispatch!
   (fn [_ event-data]
     (->> (perform-actions @store event-data)
          (run! #(process-effect store %)))))

  (swap! store assoc :hoge :moge))

