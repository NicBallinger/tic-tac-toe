(ns tic-tac-toe.core
  (:use [clojure set]))

(def EMPTY "?")

(def X "X")
(def Y "Y")

(def all-ids (for [y (range 3) x (range 3)] [x y]))

(def initial-state {
                    :next-piece (cycle [X Y])
                    :board {}
                    :move-allowed true
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
  (swap! state merge {:board {} :move-allowed true :msg nil}))

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

(defn is-win? [st boxes]
  (let [board (:board st)
        pieces (map #(get board %) boxes)
        first-piece (some identity pieces)
        eq (apply = pieces)]
    (if (and first-piece eq) first-piece)))

(defn cat? [st]
  (= 9 (count (:board st))))

(defn winning-player [st]
  (let [combos (map (partial is-win? st) winning-combos)]
    (first (filter identity combos))))
  
(defn end-game [st msg]
  (-> st
      (assoc :move-allowed false)
      (assoc :msg msg)))

(defn check-game-state [st]
  (let [piece (winning-player st)] 
    (cond
     piece (end-game st (str "GAME OVER: " piece))
     (cat? st) (end-game st "GAME OVER: CAT")
     :else st)))

(defn game-over? [st]
  (not (:move-allowed st)))

(defn valid-move? [st box]
  (not (get-in st [:board box])))

(defn place-piece [st box piece]
  (->
   (assoc-in st [:board box] piece)
   (update-in [:next-piece] rest)
   (check-game-state)))

(defn current-piece
  ([] (current-piece @state))
  ([st]  (first (get-in st [:next-piece]))))

(defn expected-piece? [st piece]
  (= piece (current-piece st)))

(defn move-allowed? [st]
  (:move-allowed st))

(defn make-move [piece box]
  (let [st @state]
    (when (and (move-allowed? st) (expected-piece? st piece) (valid-move? st box))
      (swap! state place-piece box piece))))







