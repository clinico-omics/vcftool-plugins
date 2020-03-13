(ns sample-id
  (:require [vcftool.vcflib.reader :refer [sample-ids vcf-reader]]))

(defn parse
  [input]
  (with-open [reader (vcf-reader input)]
    (sample-ids reader)))