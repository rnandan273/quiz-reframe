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


(re-frame/reg-event-db
 :dataresp
 (fn [db [_ current-data-response]]
   ;(assoc db :data-response current-data-response)
   (update-in db [:data-response] concat  current-data-response)))


(defn listen-events []
  (let [events (kvlt.core/event-source! "https://jsdemo.envdev.io/sse")]
    (go (loop []
          (when-let [event (<! events)]
            (let [sensordata  (.parse js/JSON (:data event))]
              (re-frame/dispatch [:dataresp (walk/keywordize-keys (js->clj sensordata))]))
            (recur))))))

(listen-events)
