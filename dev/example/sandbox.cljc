(ns example.sandbox
  (:require [nexus.registry :as nxr]
            [replicant.dom :as r]
            [dataspex.core :as dataspex]))


(defn save [_ store path value]
  (swap! store assoc-in path value))

(defn increment [state path]
  [[:effects/save path (+ (:step state) (get-in state path))]])

(defn render [state]
  [:div
   [:p "Number: " (:number state)]
   [:div
    [:label "Step size: "]
    [:input
     {:value (:step state)
      :on    {:input [[:effects/save [:step] [:fmt/number [:event.target/value]]]]}}]]
   [:button.btn
    {:on {:click [[:actions/inc [:number]]]}}
    "Count!"]])

;; アプリケーション状態
(def store (atom {}))

;; ユーザー入力を処理：エフェクト、アクション、プレースホルダを登録
;; グローバル登録が嫌な場合、次節でnexus.coreの使用法（暗黙の状態なし）を説明
(nxr/register-effect! :effects/save save)
(nxr/register-action! :actions/inc increment)

(nxr/register-placeholder! :event.target/value
                           (fn [{:replicant/keys [dom-event]}]
                             (some-> dom-event .-target .-value)))

(nxr/register-placeholder! :fmt/number
                           (fn [_ value]
                             (or (some-> value parse-long) 0)))

(nxr/register-system->state! deref)

;; レンダーループを設定
(r/set-dispatch! #(nxr/dispatch store %1 %2))
(add-watch store ::render #(r/render js/document.body (render %4)))

;; 初期レンダーを開始
(reset! store {:number 0
               :step   1})   