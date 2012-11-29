(ns tic-tac-toe.ui.tty
  (:require [tic-tac-toe.core :as ttt]
            [tic-tac-toe.ai.random :as ai-random]))

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

(defn print-board [piece board]
  (println move-map)
  (println (board1 board 0))
  (println boardh)
  (println (board1 board 1))
  (println boardh)
  (println (board1 board 2))
  (println "You are " piece))

           
(defn update-squares [piece board]
  (print-board piece board))

(defn my-turn? [st piece]
  (= piece (ttt/current-piece st)))

(defn end-game [st]
  (println (:msg st))
  (ttt/restart-game))

(defn take-turn [piece]
  (println "Enter your move:")
  (let [move (read-line)]
    (println "You entered " move)
    (ttt/make-move piece (move-map move))))

(defn state-change-single [piece key ref old new]
  (update-squares piece (:board new))
  (cond
   (ttt/game-over? new) (end-game new)
   (my-turn? new piece) (take-turn piece)))


(defn h-v-c []
  (ttt/add-game-watch :me (partial state-change-single ttt/X))
  (ttt/add-game-watch :me2 (partial ai-random/state-change-random ttt/Y 0)))


(defn -main [& args]
  (h-v-c)
  
  (future (ttt/start-game)))

