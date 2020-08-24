(ns build-api
  (:require [cljs.build.api :as cljs]
            [clojure.java.io :as io]
            [kaocha.repl :as k]
            [kaocha.testable]))

(defn build [source]
  (time
   (binding [kaocha.testable/*current-testable* (first (:kaocha/tests (k/config)))]
     (cljs/build source
                 '{:npm-deps true
                   :infer-externs true
                   :install-deps true
                   :target :nodejs
                   :optimizations :advanced
                   :output-to "out/test.js"
                   :source-map "out/test.js.map"
                   :output-dir "out"}))))

(build '[(require '[lambdaisland.funnel-client :as fc])
         (fc/connect {})])



(build '[(require '[lambdaisland.funnel-client.websocket :as websocket])
         (prn (js/require "ws"))
         (websocket/prn-websocket-class)
         #_(prn (goog.net.WebSocket. true))
         #_(prn (websocket/ensure-websocket #(goog.net.WebSocket. true)))])

(build '#{kaocha.cljs2.autorequire
          lambdaisland.chui.remote})
