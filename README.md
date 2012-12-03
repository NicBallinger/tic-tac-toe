# tic-tic-toe

The classic tic-tac-toe game written in Clojure.  This project isn't so much about tic-tac-toe but more about Clojure
technology and how to write a code base that will run in multiple user interfaces.  The tic-tac-toe games presents a 
small but interesting problem domain.

There are 4 different UIs: swing using seesaw (I just got that pun as I typed that), tty, laterna and ClojureScript.

## Usage

* swing
    * Human Vs Computer: lein run -m tic-tac-toe.ui.swing 
    * Computer Vs Computer: lein run -m tic-tac-toe.ui.swing cc
* tty
    * Human Vs Computer: lein run -m tic-tac-toe.ui.tty 
    * Computer Vs Computer: lein run -m tic-tac-toe.ui.tty cc
* lanterna
    * Human Vs Computer: lein run -m tic-tac-toe.ui.lanterna
* ClojureScript
    * Load tictactoe.html in your favorite browser that supports JavaScript (obviously)

## License

Copyright (C) 2012 Matthew O. Smith

Distributed under the Eclipse Public License, the same as Clojure.
