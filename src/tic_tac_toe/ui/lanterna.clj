(ns tic-tac-toe.ui.lanterna
  (:require
   [lanterna.terminal :as ui]
   [tic-tac-toe.core :as ttt]
   [tic-tac-toe.ai.random :as ai-random])) 

(def term (ui/get-terminal :text))

(defn to-number
  ([row col]   (+ (* row 3) col 1))
  ([[row col]] (+ (* row 3) col 1)))

(def move-map (reduce #(conj %1 { (str (to-number %2)) %2 }) {} ttt/all-ids))

(def boardh "---+---+---")

(defn piece-at [board row col]
  (let [piece (get board [row col])]
    (if piece piece (to-number row col))))

(defn board1 [board row]
  (str " " (piece-at board row 0) " | "
       (piece-at board row 1) " | "
       (piece-at board row 2) " "))

(defn put-at [col msg]
  (ui/move-cursor term 0 col)
  (ui/put-string term msg))

(defn print-board [piece board]
  (doall (map put-at (range) [
                       (board1 board 0)
                       boardh
                       (board1 board 1)
                       boardh
                       (board1 board 2)
                       (str "You are " piece)
       ])))

           
(defn update-squares [piece board]
  (print-board piece board))

(defn my-turn? [st piece]
  (= piece (ttt/current-piece st)))

(defn end-game [st]
  (put-at 15 (:msg st))
  (ttt/restart-game))

(defn take-turn [piece]
  (put-at 11 "Enter your move:")
  (let [move (str (ui/get-key-blocking term))]
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
  (ui/start term)
  (future (ttt/start-game))
  nil)

