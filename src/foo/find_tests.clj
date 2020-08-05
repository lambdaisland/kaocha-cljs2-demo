(ns foo.find-tests
  (:require [clojure.java.io :as io]
            [kaocha.load :as load]))

(defmacro require-all [paths patterns]
  `(~'ns*
    (:require
     ~@(map #(list 'quote %) (load/find-test-nss paths patterns load/cljs)))))
