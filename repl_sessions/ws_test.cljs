(ns ws-test
  (:require [lambdaisland.chui.websocket :as ws]
            [lambdaisland.funnel-client :as fc]))

(def client (fc/connect {:uri "wss://localhost:44221"
                         :on-open prn
                         :on-message prn}))
