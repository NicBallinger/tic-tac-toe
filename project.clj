(defproject tic-tac-toe "1.0.0-SNAPSHOT"
  :description "Tic Tac Toe"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [seesaw "1.4.2"]
                 [clojure-lanterna "0.9.2"]]
  :plugins [[lein-cljsbuild "0.2.9"]]
  :hooks [leiningen.cljsbuild]
  :source-path "src"
  :cljsbuild {
    :builds [{:source-path "src-cljs" :compiler {:output-to "tictactoe.js" }}]
    :crossovers [tic-tac-toe.core tic-tac-toe.ai.random]
  }
  :main tic-tac-toe.ui.tty)