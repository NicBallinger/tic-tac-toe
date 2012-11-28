(ns tic-tac-toe.ui.swing
  (:require [tic-tac-toe.core :as ttt])
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
   :listen [:action (partial handle offset)]))

(config! board
         :items (map make-square ttt/all-ids))

(defn update-squares [game-board]
  (println "update squares")
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
  (println " ==> " (:move-allowed new) (:msg new))
  (swap! piece-ref (fn [f] piece))
  (update-squares (:board new))
  
  (when (and (not= (:msg old) (:msg new)) (:msg new))
    (alert (:msg new))
    (ttt/restart-game)))


(defn state-change-random [piece key ref old new]
  (if (= piece (ttt/current-piece new))
    (let [ids (ttt/empty-squares)
          offset (rand-int (count ids))
          box (nth (list* ids) offset)]
      (when box
        (println "random" offset box piece)
        (ttt/make-move piece box)))))

(defn c-v-c []
  (ttt/add-game-watch :me (partial state-change-single "observer"))
  (ttt/add-game-watch :me2 (partial state-change-random ttt/X))
  (ttt/add-game-watch :me3 (partial state-change-random ttt/Y)))

(defn h-v-c []
  (ttt/add-game-watch :me (partial state-change-single ttt/X))
  (ttt/add-game-watch :me2 (partial state-change-random ttt/Y)))


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

