(ns tic-tac-toe.core
  (:use [clojure set]))

(def EMPTY "?")

(def X "X")
(def Y "Y")

(def all-ids (for [y (range 3) x (range 3)] [x y]))

(def initial-state {
                  :next-piece (cycle [X Y])
                  :board {}
                  })

(def state (atom {}))

(defn add-game-watch [key fn]
  (add-watch state key fn))

(defn occupied-squares []
  (-> @state :board keys))

(defn empty-squares []
  (difference (set all-ids) (set (occupied-squares))))

(defn start-game []
  (swap! state (fn [f] initial-state)))

(defn restart-game []
  (swap! state assoc :board {})

(def winning-combos
  [
   [[0 0] [1 1] [2 2]]
   [[2 0] [1 1] [0 2]]
   [[0 0] [0 1] [0 2]]
   [[1 0] [1 1] [1 2]]
   [[2 0] [2 1] [2 2]]

   [[0 0] [1 0] [2 0]]
   [[0 1] [1 1] [2 1]]
   [[0 2] [1 2] [2 2]]
   ])

(defn is-win? [boxes]
  (let [board (:board @state)
        pieces (map #(get board %) boxes)
        first-piece (some identity pieces)
        eq (apply = pieces)]
    (println "is-win?" boxes pieces first-piece eq)
    (if (and first-piece eq) first-piece)))

(defn cat? []
  (println "cat?" (count (:board @state)))
  (= 9 (count (:board @state))))

(defn end-game [msg]
  (println msg)
  (restart-game)
  
  msg)

(defn game-over []
  (let [piece (filter identity (map is-win? winning-combos))]
    (cond
     (seq piece) (end-game (str "GAME OVER: " (first piece)))
     (cat?) (end-game "GAME OVER: CAT"))))

(defn valid-move? [box]
  (not (get-in @state [:board box])))

(defn place-piece [st box piece]
  (->
   (assoc-in st [:board box]  piece)
   (update-in [:next-piece] rest)))

(defn current-piece
  ([] (current-piece @state))
  ([st]  (first (get-in st [:next-piece]))))

(defn expected-piece? [piece]
  (= piece (current-piece)))

(defn make-move [piece box]
  (when (and (expected-piece? piece) (valid-move? box))
    (swap! state place-piece box piece)
    (println "OS" (empty-squares))
    (game-over)))





