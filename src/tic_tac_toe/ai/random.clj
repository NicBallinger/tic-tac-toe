(ns tic-tac-toe.ai.random
  (:require [tic-tac-toe.core :as ttt]))

(defn state-change-random [piece wait key ref old new]
  (if (= piece (ttt/current-piece new))
    (let [ids (ttt/empty-squares)
          offset (rand-int (count ids))
          box (nth (list* ids) offset)]
      (when box
          (Thread/sleep wait)
          (ttt/make-move piece box)))))
