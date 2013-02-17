(ns life.grid-view)

(def CELL_PADDING 1)

(defn new-view [canvas origin size]
  {:canvas canvas :origin origin :size size})

(defn origin-x [{:keys [origin]}] (origin 0))
(defn origin-y [{:keys [origin]}] (origin 1))


(defn ctx [{:keys [canvas]}]
  (.getContext canvas "2d"))

(defn width [{:keys [canvas]}]
  (.-width canvas))

(defn height [{:keys [canvas]}]
  (.-height canvas))

(defn cell-size [{:keys [canvas size]}]
  (quot (.-width canvas) size))

(defn size-x [{:keys [size]}] size)
(defn size-y [view]
  (/ (height view) (cell-size view)))

(defn cell-visible? [view [x y]]
  (let [
    origin-x (origin-x view)
    origin-y (origin-y view)]
    (and (<= origin-x x (+ origin-x (size-x view)))
         (<= origin-y y (+ origin-y (size-y view))))))

(defn clear [view]
  (.clearRect (ctx view) 0 0 (width view) (height view)))

(defn normalize [view [x y]]
  [(- x (origin-x view)) (- y (origin-y view))])

(defn denormalize [view [x y]]
  [(+ x (origin-x view)) (+ y (origin-y view))])

(defn cell-at [view [screen-x screen-y]]
  (denormalize view [(quot screen-x (cell-size view)) (quot screen-y (cell-size view))]))

(defn draw-cell [view [x y]]
  (let [size (cell-size view)
        ctx (ctx view)]
    (set! (.-lineWidth ctx) .5)
    (set! (.-fillStyle ctx) "rgba(115, 117, 222, .5)")
    (.fillRect ctx (inc (* x size)) (inc (* y size)) (- size CELL_PADDING) (- size CELL_PADDING))
    (set! (.-strokeStyle ctx) "rgba(75, 77, 224, 1)")
    (.strokeRect ctx (inc (* x size)) (inc (* y size)) (- size CELL_PADDING) (- size CELL_PADDING))))

(defn draw-grid [view]
  (let [
    ctx (ctx view)
    csize (cell-size view)]
    (set! (.-strokeStyle ctx) "rgba(50, 50, 50, 1)")
    (set! (.-lineWidth ctx) .1)
    (doseq [x (map (partial * csize) (range (size-x view)))]
      (.beginPath ctx)
      (.moveTo ctx x 0)
      (.lineTo ctx x (height view))
      (.stroke ctx))
    (doseq [y (map (partial * csize) (range (size-y view)))]
      (.beginPath ctx)
      (.moveTo ctx 0 y)
      (.lineTo ctx (width view) y)
      (.stroke ctx))))

(defn display [view board]
  (clear view)
  (draw-grid view)
  (doseq [cell (filter (partial cell-visible? view) board)]
    (draw-cell view (normalize view cell))))

(defn zoom [n {:keys [canvas origin size]}]
  (new-view 
    canvas 
    (vec (map (partial + n) origin))
    (- size (* 2 n))))


(defn pan [x y {:keys [canvas origin size]}]
  (new-view
    canvas
    [(+ (origin 0) x) (+ (origin 1) y)]
    size))

