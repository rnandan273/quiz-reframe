(ns quiz-reframe.events
    (:require [re-frame.core :as re-frame]
              [quiz-reframe.db :as db]
              [clojure.walk :as walk]
              [kvlt.core :as kvlt])
   (:require-macros
   [cljs.core.async.macros :refer [go alt!]]))


(defn log [s]
  (.log js/console (str s)))

(re-frame/reg-event-db
 :initialize-db
 (fn  [_ _]
   db/default-db))

(comment

(re-frame/reg-event-db
 :publish-dp
 (fn [db [_ dp]]
 (log (str "Datapoint - " dp)))
 )


(defn collect-data [unit dp]
  {:type unit :id (nth dp 0) :val (nth dp 1)})

(defn parse-measurements [unit marr]
   (map #(collect-data unit %) marr))

(defn check-dp [dp]
  (let [first-node (nth (into [] dp) 0)
        second-node (nth (into [] dp) 1)
        first-val (nth first-node 1)
        second-val (nth second-node 1)]

        (comment(case first-val
          "Location" {:type first-val :measurements second-val}
          "Serial" {:type first-val :measurements  second-val}
          "Temperature" {:type first-val :unit  second-val :measurements (nth (nth (into [] dp) 2) 1)}
          "Pressure" {:type first-val :unit  second-val :measurements (nth (nth (into [] dp) 2) 1)}
          "Batt. Voltage" {:type first-val :unit  second-val :measurements (nth (nth (into [] dp) 2) 1)}
          "PM1" {:type first-val :unit  second-val :measurements (nth  (nth (into [] dp) 2) 1)}
          ))
        (case first-val
          "Location" (parse-measurements first-val second-val)
          "Serial" (parse-measurements first-val second-val)
          "Temperature" (parse-measurements first-val (nth (nth (into [] dp) 2) 1))
          "Pressure" (parse-measurements first-val (nth (nth (into [] dp) 2) 1))
          "Batt. Voltage" (parse-measurements first-val (nth (nth (into [] dp) 2) 1))
          "PM1" (parse-measurements first-val (nth  (nth (into [] dp) 2) 1))
          )
    )

  )

(defn pub-dp [dp]
  (re-frame/dispatch [:publish-dp dp]))


(re-frame/reg-event-db
 :clean-dp
 (fn [db [_ raw-data]]
  (log raw-data)
 (loop [data (flatten (map check-dp raw-data))]
        (pub-dp (first data))
        (recur (rest data)))
 ))
)


(re-frame/reg-event-db
 :dataresp
 (fn [db [_ current-data-response]]
    ;(assoc db :data-response current-data-response)))            
   (update-in db [:data-response] concat  current-data-response)))




(defn listen-events []
  (let [events (kvlt.core/event-source! "https://jsdemo.envdev.io/sse")]
    (go (loop []
          (when-let [event (<! events)]
            (let [sensordata  (.parse js/JSON (:data event))]
              (re-frame/dispatch [:dataresp (walk/keywordize-keys (js->clj sensordata))]))
            (recur))))))

(listen-events)
