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

(def blinker #{[5 5] [6 5] [7 5]})
(def beacon #{[1 1] [1 2] [2 1] [3 4] [4 3] [4 4]})
(def glider #{[51 51] [52 51] [53 51] [51 52] [52 53]})
(def cube #{[0 0] [0 1] [1 0] [1 1]})

