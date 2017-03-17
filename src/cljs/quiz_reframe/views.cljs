(ns quiz-reframe.views
    (:require [re-frame.core :as rf]))

(defn main-panel-old []
  (let [name (rf/subscribe [:name])]
    (fn []
      [:div "Hello from " @name])))

(defn main-panel []
  (let [data-orig  (rf/subscribe [:data-response])]
  (fn []
    (let [data (take 10 (reverse @data-orig))]
    [:div 
      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "center" :margin-left "20px"}}
       [:h2 "Sensor data "]
      ]
      (doall      
        (for [idx (range (count data))]
              ^{:key (str "at3 -" (rand 10000000))}
           [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "center" :margin-left "20px"}}
               [:div {:style {:flex 1}} [:h5 (str (:name (nth data idx)))]]
               [:div {:style {:flex 1}} [:h5 (str (:unit (nth data idx)))]]
               [:div {:style {:flex 3}} [:h5 (str (:measurements (nth data idx)))]]
           ]))]))))
