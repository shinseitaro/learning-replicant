(ns example.counter)

(defn perform-action [state [action & _]]
  (when (= action ::inc-number)
    [[:effect/assoc-in [:number] (inc (:number state))]]))

(defn render-ui [state]
  [:div.m-8
   [:h1.text-lg "カウンター"]
   [:div "Number is " (:number state)]
   [:button.btn
    {:on {:click [[::inc-number]]}}
    "カウント！"]])