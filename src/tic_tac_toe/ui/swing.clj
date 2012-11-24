(ns tic-tac-toe.ui.swing
  (:use [seesaw core graphics]
        [tic-tac-toe core]))

(def board (grid-panel :columns 3))

(defn handle [box e]
  (let [[piece msg] (make-move box)]
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
   :items [
           (make-square [0 0])
           (make-square [1 0])
           (make-square [2 0])

           (make-square [0 1])
           (make-square [1 1])
           (make-square [2 1])

           (make-square [0 2])
           (make-square [1 2])
           (make-square [2 2])

           ])


(defn -main [& args]
  (invoke-later
   (-> (frame :title "Tic Tac Toe",
              :size [500 :by 500]
              :content board
              :on-close :exit)
       pack!
       show!)))
