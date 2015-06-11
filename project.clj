(defproject tic-tac-toe "1.0.1"
  :description "The classic tic-tac-toe game written in Clojure. This project isn't so much about tic-tac-toe but more about Clojure technology and how to write a code base that will run in multiple user interfaces. The tic-tac-toe games presents a small but interesting problem domain."
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [seesaw "1.4.4"]
                 [clojure-lanterna "0.9.4"]]
  :plugins [[lein-cljsbuild "1.0.2"] [lein-resource "14.10.2"] ]
  :hooks [leiningen.cljsbuild leiningen.resource]
  :source-path "src"
  :url "http://m0smith.freeshell.org/tictactoe.html"
  :prep-tasks ["javac" "compile" "resource"]
  :resource {:resource-paths ["src-stencil"] ;; required or does nothing
             :target-path "target/html"      ;; optional default to the global one
             :excludes [#".*~"]
             :extra-values { :year ~(.get (java.util.GregorianCalendar.)
                                          (java.util.Calendar/YEAR)) }  ;; optional - default to nil
           }
  ;; :filespecs {:type :fn
  ;;             :fn (fn [p]
  ;;                   {:type :bytes
  ;;                    :path +"inside-jar-path"
  ;;                    :bytes (format ...)})}
  :cljsbuild {
              :builds [{:source-paths ["src-cljs"],
                        :compiler
                        {:pretty-print true,
                         :output-to "target/html/tictactoe.js",
                         :optimizations :advanced}}],
              :crossovers [tic-tac-toe.core tic-tac-toe.ai.random]}

  
  :main tic-tac-toe.ui.tty
  :pom-addition [:developers [:developer
                              [:name "Matthew O. Smith"]
                              [:url "http://m0smith.com"]
                              [:email "matt@m0smith.com"]
                              [:timezone "-7"]]])
