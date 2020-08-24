(ns kc2d.hooks
  (:require [cljs.cli]
            [cljs.repl.browser :as browser]
            [kaocha.testable :as testable]
            [io.pedestal.log :as log]))

(defn find-free-port []
  (let [socket (java.net.ServerSocket. 0)]
    (.close socket)
    (.getLocalPort socket)))

(defn compile-and-load [suite config]
  (log/debug :compile-and-load/begin {:testable-id (::testable/id suite)})
  (future
    (try
      (binding [testable/*current-testable* suite]
        (log/debug :compile-and-load/cljs.main-starting true)
        (cljs.cli/main browser/repl-env
                       "--compile-opts" (pr-str '{:preloads [lambdaisland.chui.remote]
                                                  :npm-deps true
                                                  :infer-externs true
                                                  :install-deps true})
                       "--port" (str (find-free-port))
                       "--main" "kaocha.cljs2.autorequire"))
      (log/debug :compile-and-load/cljs.main-finished true)
      (catch Throwable e
        (log/error :compile-and-load/failed {:testable-id (::testable/id suite)} :exception e))
      (finally
        (log/debug :compile-and-load/cljs.main-finally true))
      ))
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
