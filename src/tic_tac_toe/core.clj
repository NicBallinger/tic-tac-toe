(ns tic-tac-toe.core)

(def EMPTY "?")

(def X "X")
(def Y "Y")

(def initial-state {
                  :next-piece (cycle [X Y])
                  :board {}
                  })

(def state (atom initial-state))

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

(defn cat []
  (= 9 (count (:board @state))))

(defn end-game [msg]
  
  (swap! state (fn [f] initial-state))
  msg)

(defn game-over []
  (let [piece (filter identity (map is-win? winning-combos))]
    (cond
     (seq piece) (end-game (str "GAME OVER: " (first piece)))
     (cat) (end-game "GAME OVER: CAT"))))

(defn valid-move [box]
  (not (get-in @state [:board box])))

(defn place-piece [st box piece]
  (println "keys" (keys st))
  (->
   (assoc-in st [:board box]  piece)
   (update-in [:next-piece] rest)))

(defn make-move [box]
  (when (valid-move box)
    (let [piece (first (get-in @state [:next-piece]))]
      (swap! state place-piece box piece)
      (println "make-move" box (:board @state))
      [piece (game-over)])))

