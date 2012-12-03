(ns tic-tac-toe.ui.tty
  (:require [tic-tac-toe.core :as ttt]
            [tic-tac-toe.ai.random :as ai-random]))

(defn do-after "Wait for 'delay' before executing the function 'f'"
  [delay f]
;;  (println "do-after Delaying " delay)
  (let [executor (java.util.concurrent.Executors/newSingleThreadScheduledExecutor)]
  ;;  (println "do-after Delaying  " delay  executor)
    (.schedule executor f delay java.util.concurrent.TimeUnit/MILLISECONDS)))


(defn to-number
  ([row col]   (+ (* row 3) col 1))
  ([[row col]] (+ (* row 3) col 1)))

(def move-map (reduce #(conj %1 { (str (to-number %2)) %2 }) {} ttt/all-ids))

(def boardv "   |   |   ")

(def boardh "---+---+---")

(defn piece-at [board row col]
  (let [piece (get board [row col])]
    (if piece piece (to-number row col))))

(defn board1 [board row]
  (str " " (piece-at board row 0) " | "
       (piece-at board row 1) " | "
       (piece-at board row 2) " "))

(defn print-board [piece board current]

  (println (board1 board 0))
  (println boardh)
  (println (board1 board 1))
  (println boardh)
  (println (board1 board 2))
  (println "You are " piece)
  (println "Turn:" current))

           
(defn update-squares [piece board current]
  (print-board piece board current))

(defn my-turn? [st piece]
  (= piece (ttt/current-piece st)))

(defn end-game [st]
  (println (:msg st))
  (ttt/restart-game))

(defn take-turn [piece]
  (print "Enter your move:")
  (let [move (read-line)]
    (println "You entered " move)
    (ttt/make-move piece (move-map move))))

(defn state-change-single [piece key ref old new]
  (update-squares piece (:board new) (ttt/current-piece new))
  (cond
   (ttt/game-over? new) (end-game new)
   (my-turn? new piece) (take-turn piece)))


(defn computer-handler [piece]
  (partial ai-random/state-change-random
           piece
           (partial do-after 1000)))

(defn c-v-c []
  (ttt/add-game-watch :me (partial state-change-single "an observer"))
  (ttt/add-game-watch :x (computer-handler ttt/X))
  (ttt/add-game-watch :y (computer-handler ttt/Y))
  )

(defn h-v-c []
  (ttt/add-game-watch :x (partial state-change-single ttt/X))
  (ttt/add-game-watch :y (computer-handler ttt/Y))
  )


(defn -main [& args]
  (cond
   (= (first args) "cc") (c-v-c)
   :else (h-v-c))

  (future (ttt/start-game)))

