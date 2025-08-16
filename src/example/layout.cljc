(ns example.layout)

(defn tab-bar [current-view views]
  [:div.tabs.tabs-box {:role "tablist"}
   (for [{:keys [id text] } views]
     (let [current? (= id current-view)]
       [:a.tab (cond->  {:role "tab"} 
                 current? (assoc :class "tab-active")
                 (not current?) (assoc-in [:on :click ] [[::set-current-view id]]))
        text]))]) 

(comment
  (def views [{:id   :counter
               :text "カウンター"}
              {:id   :temperature
               :text "気温"}])
  (tab-bar :counter views)
  :rcf)