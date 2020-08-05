(ns foo.bar-test
  (:require [foo.bar :as bar]
            [clojure.test :refer [deftest testing is are use-fixtures run-tests join-fixtures]]))

(deftest my-first-test
  (is (= 1 2)))
