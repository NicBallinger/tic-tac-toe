(ns tic-tac-toe.ui.swing
  (:require [tic-tac-toe.core :as ttt])
  (:use [seesaw core graphics]))

(def piece-ref (atom ttt/X))

(def board (grid-panel :columns 3))

(defn handle [box e]
  (let [piece @piece-ref
        msg (ttt/make-move piece box)]
    (when piece (config! e :text piece))
    (when msg
      (doseq [b (config board :items)]
        (config! b :text ""))
      (alert msg))))

(defn make-square [offset]
  (button
   :id offset
   :text ""
   :listen [:action (partial handle offset)]))

(config! board
         :items (map make-square ttt/all-ids))

(defn state-change-both [key ref old new]
  (swap! piece-ref (fn [f] (ttt/current-piece new))))

(defn state-change-single [piece key ref old new]
  (swap! piece-ref (fn [f] piece)))

(defn state-change-random [piece key ref old new]
  (let [ids (ttt/empty-squares)
        offset (rand-int (count ids))
        box (nth (list* ids) offset)]
    (ttt/make-move piece box)))

(defn -main [& args]
  (invoke-later
   (ttt/add-game-watch :me (partial state-change-single ttt/X))
   (ttt/add-game-watch :me2 (partial state-change-random ttt/Y))
   (-> (frame :title "Tic Tac Toe",
              :size [500 :by 500]
              :content board
              :on-close :exit)
       pack!
       show!)
     (ttt/start-game)))

