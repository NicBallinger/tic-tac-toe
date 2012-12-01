(ns tic-tac-toe.core
  (:use [clojure.set :only [difference]]))

;; Define the constants

(def EMPTY  "The default for empty squares"
  "?")
(def X  "The X piece"
  "X")
(def Y  "The O piece (not sure how it ended up as Y, but there you have it"
  "Y")

(def initial-state "The start state for a game"
  {
   :current-piece X
   :board {}
   :move-allowed true
   :id 0
   })

(def all-ids "All the squares in order from top left to bottom right"
  (for [y (range 3) x (range 3)] [x y]))

(def winning-combos "All the different ways to win the game"
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


;; Define the states

(def state "Holds the current game state as a map with the following keys:
     :current-piece - whose turn it is
     :board - a map of cood [row col] to the piece (X or Y)
     :move-allowed - will be set to false when the game is over
     :id - a sequence id for each different state, always increasing
     :msg - Usually not defined but defined at the end of the game to declare the winner"
  (atom {}))

(def next-piece "Whose turn is it now and who is next"
  (atom (cycle [X Y])))

;; Functions that act on the game state

(defn add-game-watch "Add a callback that will be called on any change to the 'state'"
  [key fn] (add-watch state key fn))

(defn start-game "Update the 'state' with the initial state to start a game"
  [] (swap! state (fn [f] initial-state)))

(defn restart-game "Update the 'state' to restart a game.  Does not reset the :id"
  []  (swap! state merge {:board {} :move-allowed true :msg nil}))

(defn update-next-piece []
  (first (swap! next-piece next)))


;; Functions to examine the :board

(defn occupied-squares []
  (-> @state :board keys))

(defn empty-squares []
  (difference (set all-ids) (set (occupied-squares))))


(defn is-win? "Returns nil or the winning piece"
  [st coods]
  (let [board (:board st)
        pieces (map board coods)]
    (reduce #(if (= %1 %2) %1 nil) pieces)))

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

(defn valid-move? [st box]
  (not (get-in st [:board box])))


(defn newer-state? [old new]
  (< (:id old) (:id new)))

(defn place-piece [st box piece]
  (->
   (assoc-in st [:board box] piece)
   (assoc-in [:current-piece] (first @next-piece))
   (update-in [:id] inc)
   (check-game-state)))

(defn current-piece
  ([] (current-piece @state))
  ([st]  (get st :current-piece)))

(defn expected-piece? [st piece]
  (= piece (current-piece st)))

(defn move-allowed? [st]
  (:move-allowed st))


(defn game-over? [st]
  (not (:move-allowed st)))

(defn make-move [piece box]
  (let [st @state]
    (when (and (move-allowed? st) (expected-piece? st piece) (valid-move? st box))
      (update-next-piece)
      (swap! state place-piece box piece))))







