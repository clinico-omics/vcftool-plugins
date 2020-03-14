(ns extract-format
  (:require [vcftool.vcflib.reader :refer [variant-seq vcf-reader]]
            [vcftool.vcflib.writer :refer [variant-dump]]
            [clojure.string :as str]
            [vcftool.io :as io]))

(def metadata
  {:command     "extract-format"
   :description "Extract fields from the format column of a vcf file."
   :opts        [{:option "input" :short "i" :as "Input file/directory" :type :string :default :present}
                 {:option "output" :short "o" :as "Output file/directory" :type :string :default :present}
                 {:option "ftype" :short "f" :as "File type supported by parser command." :type #{"txt" "csv" "tsv"} :default nil}
                 {:option "field" :short "e" :as "Field name, e.g. AAL,CO" :type :string :default "AAL"}]})

(defn update-map [m f]
  (reduce-kv
   (fn [m k v]
     (assoc m k (f v))) {} m))

(defn select-cols
  [record cols]
  (update-in
   record [:gtype]
   (fn [nested]
     (update-map nested #(select-keys % cols)))))

(defn update-format
  [rdr cols]
  (pmap #(assoc % :format cols) rdr))

(defn update-dtype
  [rdr cols]
  (pmap #(select-cols % cols) rdr))

(defn parse
  [{:keys [input output ftype field]}]
  (with-open [vcf (vcf-reader input)]
    (let [fields (str/split field #",")]
      (doall
       (-> (variant-seq vcf)
           (update-dtype fields)
           (update-format fields)
           (variant-dump (:metadata-headers (:headers vcf)))
           ((io/find-out-func ftype) output))))))