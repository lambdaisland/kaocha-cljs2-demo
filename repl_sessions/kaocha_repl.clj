(ns kaocha-repl
  (:require [kaocha.repl :as k]
            [kaocha.testable :as testable]
            [cljs.cli]
            [cljs.repl.browser]
            [kc2d.hooks]))

(def x
  (binding [testable/*current-testable* (first (:kaocha/tests (k/config)))]
    (let [port (kc2d.hooks/find-free-port)]
      (prn port)
      (cljs.cli/main cljs.repl.browser/repl-env
                     "--compile-opts" (pr-str '{:preloads [kaocha.cljs2.autorequire]
                                                :npm-deps {}
                                                :infer-externs true
                                                :install-deps true})
                     "--output-to" "out/main.js"
                     "--compile" "lambdaisland.chui.remote"
                     "--serve" (str "0.0.0.0:" port)))))


(k/test-plan)

(cljs.closure/compute-upstream-npm-deps)
(= (cljs.closure/get-upstream-deps*)
   (cljs.closure/get-upstream-deps))
