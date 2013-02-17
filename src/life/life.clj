(ns life.life
  (:use [clojure.set :only [difference union select]]))

(def new-board {})

(defn neighbors [board [x y]]
  (let [offsets [[0 1] [0 -1] [1 0] [1 1] [1 -1] [-1 0] [-1 1] [-1 -1]]]
    (map (fn [[xoff yoff]] [(+ x xoff) (+ y yoff)]) offsets)))

(defn live-neighbors [board pt]
  (set (keep board (neighbors board pt))))

(defn dead-neighbors [board pt]
  (difference (set (neighbors board pt)) board))

(defn dead-by-alive [board]
  (reduce (fn [counts dead-cell] (assoc counts dead-cell (inc (get counts dead-cell 0)))) {}
    (mapcat (partial dead-neighbors board) board)))

(defn step [board] 
  (let [
    stay-alive? (fn [pt] (<= 2 (count (live-neighbors board pt)) 3))
    dead-cells (dead-by-alive board)
    come-alive? (fn [pt] (= 3 (dead-cells pt)))]
    (union (select stay-alive? board) (select come-alive? (set (keys dead-cells))))))

(defn toggle-cell [pt board]
  (.log js/console (pr-str pt))
  ((if (board pt) disj conj) board pt))

(def blinker #{[5 5] [6 5] [7 5]})
(def beacon #{[1 1] [1 2] [2 1] [3 4] [4 3] [4 4]})
(def glider #{[51 51] [52 51] [53 51] [51 52] [52 53]})
(def cube #{[0 0] [0 1] [1 0] [1 1]})
(def shooter #{[2 7] [3 8] [37 5] [2 8] [3 7] [36 5] [12 8] [15 11] [12 9] [14 11] [13 10] [36 6] [37 6] [15 5] [12 7] [13 6] [14 5] [26 9] [17 6] [22 6] [23 7] [22 7] [23 6] [18 9] [23 5] [16 8] [22 5] [17 10] [18 7] [19 8] [18 8] [26 3] [26 4] [24 4] [26 8] [24 8]})

