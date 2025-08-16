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

(defn perform-actions [state event-data] ;; state に引数名を変更 
  (mapcat
   (fn [[action & args]]
     (case action
       ::ui/inc-number
       [[:effect/assoc-in [:number] (inc (:number state))]]
       ::layout/set-current-view
       (let [[id] args]
         [[:effect/assoc-in [:current-view] id]])))
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

