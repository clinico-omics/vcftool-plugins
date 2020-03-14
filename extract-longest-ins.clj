(ns extract-longest-ins
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]))

(def metadata
  {:command     "extract-longest-ins"
   :description "Extract the longest sequence of INS from multiple samples."
   :opts        [{:option "input" :short "i" :as "Input file/directory" :type :string :default :present}
                 {:option "output" :short "o" :as "Output file/directory" :type :string :default :present}
                 {:option "drop-nums" :short "d" :as "Begin with which column." :type :int :default 4}]})

(defn find-max-len-item
  [drop-nums lines]
  (pmap #(concat (take drop-nums %) [(apply max-key count (drop drop-nums %))]) lines))

(defn parse
  [{:keys [input output drop-nums]}]
  (with-open [reader (io/reader input)
              writer (io/writer output)]
    (->> (csv/read-csv reader :separator \tab)
         (drop 1)
         (find-max-len-item drop-nums)
         (csv/write-csv writer))))