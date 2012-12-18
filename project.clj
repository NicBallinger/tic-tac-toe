(defproject tic-tac-toe "1.0.0-SNAPSHOT"
  :description "Tic Tac Toe"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [seesaw "1.4.2"]
                 [clojure-lanterna "0.9.2"]]
  :plugins [[lein-cljsbuild "0.2.9"] [lein-stencil "0.1.0"] ]
  :hooks [leiningen.cljsbuild]
  :source-path "src"
  :url "http://m0smith.freeshell.org/tictactoe.html"
  :prep-tasks ["javac" "compile" "stencil"]
  :stencil {:resource-paths ["src-stencil"] ;; required or does nothing
            :target-path "target/html"      ;; optional default to the global one
            :extra-values { :year ~(.get (java.util.GregorianCalendar.)
                                         (java.util.Calendar/YEAR)) }  ;; optional - default to nil
           }
  ;; :filespecs {:type :fn
  ;;             :fn (fn [p]
  ;;                   {:type :bytes
  ;;                    :path +"inside-jar-path"
  ;;                    :bytes (format ...)})}
  :cljsbuild {
              :builds [{:source-path "src-cljs"
                        :compiler {:output-to "target/html/tictactoe.js"
                                   :pretty-print true}}]
              :crossovers [tic-tac-toe.core tic-tac-toe.ai.random]
  }
  :main tic-tac-toe.ui.tty)
