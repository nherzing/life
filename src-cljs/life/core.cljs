(ns life
  (:require [life.life :as life]
            [life.grid-view :as grid-view]))

(declare current-board)
(declare current-view)
(declare running)


(defn update []
  (swap! current-board life/step))


(defn resize [canvas]
  (set! (.-width canvas) (+ -50 (.-width js/document)))
  (set! (.-height canvas) (+ -50 (.-height js/document)))
  (grid-view/display @current-view @current-board))

(defn onclick [canvas evt]
  (let [pt [(- (.-clientX evt) (.-offsetLeft canvas))
            (- (.-clientY evt) (.-offsetTop canvas))]]
    (swap! current-board (partial life/toggle-cell (grid-view/cell-at @current-view pt)))
    (grid-view/display @current-view @current-board)))

(defn run []
  (grid-view/display @current-view @current-board)
  (swap! current-board life/step)
  (if @running (js/setTimeout run 500)))

(defn ^:export init []
  (let [canvas (.getElementById js/document "canvas")]
    (set! (.-width canvas) (+ -50 (.-width js/document)))
    (set! (.-height canvas) (+ -50 (.-height js/document)))
    (set! (.-onmouseup canvas) (partial onclick canvas))
    (set! (.-onresize js/window) (fn [] (resize canvas)))
    (def current-view (atom (grid-view/new-view canvas [0 0] 10)))
    (def current-board (atom life/cube))
    (def running (atom true))
    (run)))

(defn ^:export zoom [n]
  (swap! current-view (partial grid-view/zoom n))
  (grid-view/display @current-view @current-board))

(defn ^:export pan [x y]
  (swap! current-view (partial grid-view/pan x y))
  (grid-view/display @current-view @current-board))

(defn ^:export pause [] (reset! running false))
(defn ^:export play [] (reset! running true) (run))

(defn ^:export reset [] (reset! current-board life/new-board)
  (grid-view/display @current-view @current-board))

