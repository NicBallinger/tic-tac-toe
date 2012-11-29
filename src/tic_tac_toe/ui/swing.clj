(ns tic-tac-toe.ui.swing
  (:require [tic-tac-toe.core :as ttt]
            [tic-tac-toe.ai.random :as ai-random])
  (:use [seesaw core graphics]))

(def piece-ref (atom ttt/X))

(def board (grid-panel :columns 3))

(defn handle [box e]
  (let [piece @piece-ref
        msg (ttt/make-move piece box)]))

(defn make-square [offset]
  (button
   :user-data offset
   :text ""
   :size [50 :by 50]
   :listen [:action (partial handle offset)]))

(config! board
         :items (map make-square ttt/all-ids))

(defn update-squares [game-board]
  (let [squares (config board :items)]
    (doseq [sq squares]
      (let [piece (get game-board (config sq :user-data))]
        (if piece
          (config! sq :text piece)
          (config! sq :text ""))))))
            
(defn state-change-both [key ref old new]
  (swap! piece-ref (fn [f] (ttt/current-piece new)))
  (update-squares (:board new)))

(defn state-change-single [piece key ref old new]
  (swap! piece-ref (fn [f] piece))
  (update-squares (:board new))
  
  (when (and (not= (:msg old) (:msg new)) (:msg new))
    (alert (:msg new))
    (ttt/restart-game)))

(defn state-change-observer [key ref old new]
  (update-squares (:board new))
  
  (when (and (not= (:msg old) (:msg new)) (:msg new))
    (alert (:msg new))
    (ttt/restart-game)))

(defn c-v-c []
  (ttt/add-game-watch :me state-change-observer)
  (ttt/add-game-watch :me2 #(invoke-later (ai-random/state-change-random ttt/X 500 %1 %2 %3 %4)))
  (ttt/add-game-watch :me3 #(invoke-later (ai-random/state-change-random ttt/Y 500 %1 %2 %3 %4))))

(defn h-v-c []
  (ttt/add-game-watch :me (partial state-change-single ttt/X))
  (ttt/add-game-watch :me2 (partial ai-random/state-change-random ttt/Y 0)))


(defn -main [& args]
  (let [t (first args)]
    (invoke-later
     (cond
      (= "cc" t) (c-v-c)
      :else (h-v-c))
     (-> (frame :title "Tic Tac Toe",
                :size [500 :by 500]
                :content board
                :on-close :exit)
         pack!
         show!)
     (ttt/start-game))))

