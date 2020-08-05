(ns foo.find-tests
  (:require [clojure.test :as t])
  (:require-macros [foo.find-tests :refer [require-all]]))

(require-all ["test"] [#".*"])

(defn -main []
  (t/run-all-tests))
