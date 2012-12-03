(ns tic-tac-toe.ai.random
  (:require [tic-tac-toe.core :as ttt]))

(defn state-change-random [piece wrapper key ref old new]
  ;;(println "state-change-random start " piece (ttt/current-piece new))
  (if (= piece (ttt/current-piece new))
    (let [ids (ttt/empty-squares)
          offset (rand-int (count ids))
          box (nth (list* ids) offset)]
      (when box
        (wrapper #(ttt/make-move piece box))))))
