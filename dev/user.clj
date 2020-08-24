(ns user
  (:require [sc.api]))

(defmacro jit [sym]
  `(requiring-resolve '~sym))

(defn browse []
  ((jit clojure.java.browse/browse-url) "http://localhost:8000"))

(defn use-sc []
  (use 'sc.api))
