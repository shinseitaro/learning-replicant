(ns example.ui)

(defn render-ui [state]
  [:div.m-8
   [:h1.text-lg "カウンター"]
   [:div "Number is " (:number state)]
   [:button.btn
    {:on {:click [[::inc-number]]}}
    "カウント！"]])