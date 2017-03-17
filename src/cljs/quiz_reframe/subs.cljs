(ns quiz-reframe.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))


(defn log [s]
  (.log js/console (str s)))

(re-frame/reg-sub
 :name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
  :data-response
  (fn [db]
    (log (str "SUBS" (:data-response db)))
    (:data-response db)))