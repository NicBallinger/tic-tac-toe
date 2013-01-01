(ns tic-tac-toe-cljs.core
  (:require [tic-tac-toe.core :as ttt]
            [tic-tac-toe.ai.random :as ai-random]))

(def colors { "?" "grey" "X" "yellow" "Y" "white"})

(def last-id (atom -1))

(defn set-html! [dom content]
  (set! (. dom -innerHTML) content))

(defn update-squares [game-board]
  (let [boxes ttt/all-ids]
    (doseq [box boxes]
      (let [button (.getElementById js/document (str box))
            piece (get game-board box)]
        (if piece
          (do
            (set-html! button piece)
            (set! (. button -style.background ) (colors piece)))
          (do
            (set-html! button "?")
            (set! (. button -style.background ) (colors "?"))
            ))))))

(defn ^:export pressed
  ([row col] (pressed [row col]))
  ([box]
     (ttt/make-move ttt/X box)))

(defn state-change [piece key ref old new]
  (when (< @last-id (:id new))
    (swap! last-id (fn [f] (:id new)))
    (update-squares (:board new)))
  
  (when (ttt/game-over? new)
    (js/alert (:msg new))
    (ttt/restart-game)))

(defn alerter [key ref old new]
  (js/alert (str "alerter" (:board new))))

(defn ^:export init []
  (ttt/add-game-watch :p1 (partial state-change ttt/X))
  (ttt/add-game-watch :p2 (partial ai-random/state-change-random ttt/Y (fn [f] (f))))
  (ttt/start-game)
  ;;(add-watch ttt/state :key alerter)
)