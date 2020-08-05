(ns kc2d.hooks
  (:require [cljs.cli]
            [cljs.repl.browser :as browser]
            [kaocha.testable :as testable]))

(defn find-free-port []
  (let [socket (java.net.ServerSocket. 0)]
    (.close socket)
    (.getLocalPort socket)))

(defn compile-and-load [suite]
  (let [testable testable/*current-testable*]
    (future
      (binding [testable/*current-testable* testable]
        (cljs.cli/main browser/repl-env
                       "--compile-opts" (pr-str '{:preloads []
                                                  :npm-deps true
                                                  :infer-externs true
                                                  :install-deps true})
                       "--port" (str (find-free-port))
                       "--main" "kaocha.cljs2.autorequire"))))
  suite)



;; (def f (future
;;          (try
;;            (cljs.cli/main browser/repl-env
;;                           "--compile-opts"
;;                           (pr-str '{:preloads [kaocha.cljs2.autorequire]})
;;                           "--port" (str (find-free-port))
;;                           "--repl")
;;            (catch Exception e
;;              (println e)
;;              e))))

;; f
