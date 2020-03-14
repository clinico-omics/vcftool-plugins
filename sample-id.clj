(ns sample-id
  (:require [vcftool.vcflib.reader :refer [sample-ids vcf-reader]]
            [vcftool.io :as io]))

(def metadata
  {:command     "sample-id"
   :description "Extract sample-id from vcf file."
   :opts        [{:option "input" :short "i" :as "Input file/directory" :type :string :default :present}
                 {:option "output" :short "o" :as "Output file/directory" :type :string}
                 {:option "ftype" :short "f" :as "File type supported by parser command." :type #{"txt" "csv" "tsv"} :default nil}]})

(defn parse
  [{:keys [input output ftype]}]
  (with-open [reader (vcf-reader input)]
    (-> (sample-ids reader)
        ((io/find-out-func ftype) output))))
