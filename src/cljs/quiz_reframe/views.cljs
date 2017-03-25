(ns quiz-reframe.views
    (:require [re-frame.core :as rf]))


(defn log [s]
  (.log js/console (str s)))

(defn main-panel-old []
  (let [name (rf/subscribe [:name])]
    (fn []
      [:div "Hello from " @name])))


(defn collect-data [type unit dp]
  {:type type :unit unit :id (nth dp 0) :val (nth dp 1)})

(defn parse-measurements [type unit marr]
   (map #(collect-data type unit %) marr))


(defn collect-data-v2 [type dp]
  {:type type :id (nth dp 0) :val (nth dp 1)})

(defn parse-measurements-v2 [type marr]
   (map #(collect-data-v2 type %) marr))

(defn check-dp [dp]
  (let [first-node (nth (into [] dp) 0)
        second-node (nth (into [] dp) 1)
        first-val (nth first-node 1)
        second-val (nth second-node 1)]
        (case first-val
          "Location" (parse-measurements-v2 first-val second-val)
          "Serial" (parse-measurements-v2 first-val second-val)
          "Temperature" (parse-measurements first-val second-val (nth (nth (into [] dp) 2) 1))
          "Pressure" (parse-measurements first-val second-val (nth (nth (into [] dp) 2) 1))
          "Batt. Voltage" (parse-measurements first-val second-val (nth (nth (into [] dp) 2) 1))
          "PM1" (parse-measurements first-val second-val (nth  (nth (into [] dp) 2) 1))
          )))


(defn main-panel []
  (let [data-orig  (rf/subscribe [:data-response])]
  (fn []
    (let [data (take 15 (reverse @data-orig))
          
          t-data (sort-by :id (flatten (map check-dp data)))
          ids (map #(:id %) t-data)
          temperature-data (filter #(= "Temperature" (:type %)) t-data)
          location-data (filter #(= "Location" (:type %)) t-data)
          serial-data (filter #(= "Serial" (:type %)) t-data)
          voltage-data (filter #(= "Batt. Voltage" (:type %)) t-data)
          pm-data (filter #(= "PM1" (:type %)) t-data)
          pressure-data (filter #(= "Pressure" (:type %)) t-data)]

    [:div 
      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "center" :margin-left "20px"}}
       [:h2 "Sensor data"]
      ]
      [:div {:style {:margin-left "100px"}}
  
   (doall      
        (for [idx (range (count ids))]
              ^{:key (str (rand 10000000))}
            [:div 
              [:div [:h5 (str "Temperature for node in" (nth ids idx) " in " (:unit (first temperature-data)))]]
              [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                (let [node-data (into [] (map #(:val %)  (filter #(= (:id %) (nth ids idx)) temperature-data)))]
                 (for [idy (range (count node-data))]
                ^{:key (str (rand 10000000))}
                      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                         [:div [:h5 (nth node-data idy)]]
                      ]))]]))

   (doall      
        (for [idx (range (count ids))]
              ^{:key (str (rand 10000000))}
            [:div 
              [:div [:h5 (str "Voltage for node " (nth ids idx) " in " (:unit (first voltage-data)))]]
              [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                (let [node-data (into [] (map #(:val %)  (filter #(= (:id %) (nth ids idx)) voltage-data)))]
                 (for [idy (range (count node-data))]
                ^{:key (str (rand 10000000))}
                      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                         [:div [:h5 (nth node-data idy)]]
                      ]))]]))

   (doall      
        (for [idx (range (count ids))]
              ^{:key (str (rand 10000000))}
            [:div 
              [:div [:h5 (str "Pressure for node " (nth ids idx) " in " (:unit (first pressure-data)))]]
              [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                (let [node-data (into [] (map #(:val %)  (filter #(= (:id %) (nth ids idx)) pressure-data)))]
                 (for [idy (range (count node-data))]
                ^{:key (str (rand 10000000))}
                      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                         [:div [:h5 (nth node-data idy)]]
                      ]))]]))

    (doall      
        (for [idx (range (count ids))]
              ^{:key (str (rand 10000000))}
            [:div 
              [:div [:h5 (str "PM1 for node " (nth ids idx) " in " (:unit (first pm-data)))]]
              [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                (let [node-data (into [] (map #(:val %)  (filter #(= (:id %) (nth ids idx)) pm-data)))]
                 (for [idy (range (count node-data))]
                ^{:key (str (rand 10000000))}
                      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                         [:div [:h5 (nth node-data idy)]]
                      ]))]]))

    (doall      
        (for [idx (range (count ids))]
              ^{:key (str (rand 10000000))}
            [:div 
              [:div [:h5 (str "Serial data for node " (nth ids idx))]]
              [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                (let [node-data (into [] (map #(:val %)  (filter #(= (:id %) (nth ids idx)) serial-data)))]
                 (for [idy (range (count node-data))]
                ^{:key (str (rand 10000000))}
                      [:div {:style {:display "flex" :flex-flow "row wrap" :justify-content "flex-start" :margin-left "20px"}}
                         [:div [:h5 (nth node-data idy)]]
                      ]))]]))]]))))
