(ns kaocha-repl
  (:require [kaocha.repl :as k]
            [kaocha.testable :as testable]
            [cljs.cli]
            [cljs.repl.browser]
            [kc2d.hooks]
            [cljs.env :as env]
            [cljs.js-deps :as deps]))

(user/use-sc)

(def x
  (binding [testable/*current-testable* (first (:kaocha/tests (k/config)))]
    #_(let [port (kc2d.hooks/find-free-port)]
        (prn port))
    (cljs.cli/main cljs.repl.browser/repl-env
                   "--compile-opts" (pr-str '{:preloads [kaocha.cljs2.autorequire]
                                              :npm-deps {}
                                              :infer-externs true
                                              :install-deps true})
                   "--output-to" "out/main.js"
                   "--compile" "lambdaisland.chui.remote"
                   #_#_"--serve" (str "0.0.0.0:" port))))

(filter (comp '#{cljs.user.autorequireE706945} :ns) (sc.api/letsc [4 -1]
                                                      sources))


(map :ns
     (env/ensure
      (binding [testable/*current-testable* (first (:kaocha/tests (k/config)))]
        (cljs.closure/add-preloads () (letsc [8 -5] opts)))))

(filter #(re-find #"autoreq" (str (first %))) @cljs.compiler/sources)

(k/test-plan)

(filter #(re-find #"autoreq" (str %))
        (:cljs.closure/compiled-cljs @(last @cljs.closure/build-ops)))

(let [[source opts compiler-env] @cljs.closure/build-ops
      one-file? (and (:main opts)
                     (#{:advanced :simple :whitespace} (:optimizations opts)))
      source (if one-file?
               (let [main (:main opts)
                     uri  (:uri (cljs.closure/cljs-source-for-namespace main))]
                 (assert uri (str "No file for namespace " main " exists"))
                 uri)
               source)
      compile-opts (if one-file?
                     (assoc opts :output-file (:output-to opts))
                     opts)
      js-sources (env/with-compiler-env (dissoc @compiler-env :js-module-index)
                   (-> (if source
                         (cljs.closure/-find-sources source opts)
                         (cljs.closure/-find-sources (reduce into #{} (map (comp :entries val) (:modules opts))) opts))
                       (cljs.closure/add-dependency-sources compile-opts)
                       ))
      opts       (cljs.closure/handle-js-modules opts js-sources compiler-env)
      js-sources (-> js-sources
                     deps/dependency-order
                     (cljs.closure/compile-sources (:compiler-stats opts) compile-opts)
                     ;; (#(map add-core-macros-if-cljs-js %))
                     ;; (add-js-sources opts)
                     ;; (cond->
                     ;;     (and (= :nodejs (:target opts))
                     ;;          (:nodejs-rt opts))
                     ;;   (concat
                     ;;    [(-compile (io/resource "cljs/nodejs.cljs")
                     ;;               (assoc opts :output-file "nodejs.js"))]))
                     ;; deps/dependency-order
                     ;; (add-preloads opts)
                     ;; remove-goog-base
                     ;; add-goog-base
                     ;; (cond->
                     ;;     (and (= :nodejs (:target opts))
                     ;;          (:nodejs-rt opts))
                     ;;   (concat
                     ;;    [(-compile (io/resource "cljs/nodejscli.cljs")
                     ;;               (assoc opts :output-file "nodejscli.js"))]))
                     ;; (->> (map #(source-on-disk opts %)) doall)
                     ;; (compile-loader opts)
                     )
      ]
  js-sources
  (:compiler-stats opts))

(cljs.closure/compute-upstream-npm-deps)
(= (cljs.closure/get-upstream-deps*)
   (cljs.closure/get-upstream-deps))

(filter #(re-find #"autoreq" (pr-str %)) @cljs.closure/js-sources-3)
