(ns life
  (:require [life.life :as life]
            [life.grid-view :as grid-view]))

(declare current-board)
(declare current-view)


(defn update []
  (swap! current-board life/step))

(defn run []
  (grid-view/display @current-view @current-board)
  (swap! current-board life/step)
  (js/setTimeout run 500))


(defn ^:export init []
  (let [canvas (.getElementById js/document "canvas")]
    (def current-view (atom (grid-view/new-view canvas [0 0] 10)))
    (def current-board (atom life/cube))
    (run)))

(defn ^:export zoom [n]
  (swap! current-view (partial grid-view/zoom n)))


(defn ^:export pan [x y]
  (swap! current-view (partial grid-view/pan x y)))

(defn ^:export pan-right [n]
  (swap! current-view (partial grid-view/pan-right n)))
