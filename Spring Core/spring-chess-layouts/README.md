# Spring Chess Layouts

Chess is a famous table game. This exercise is about representing chess board states as Spring configurations.

## Details
Consider each chess piece as a particular object implementing [ChessPiece](com.epam.rd.autotasks.chesspuzles.ChessPiece) interface.
Then a chess board state may be represented via Spring Application Context containing beans of chess pieces.

In tests Spring Java and XML configurations forms such Application Contexts.
Then all the chess pieces beans goes to create an instance of [ChessBoard](com.epam.rd.autotasks.chesspuzles.ChessBoard), which must present chess board state as a String.

Required XMl Configurations (put them into src/main/resources):
- default.xml
- puzzle03.xml
- puzzle04.xml
- puzzle05.xml

Required Java Configurations (put them into com.epam.rd.autotasks.chesspuzles.layout package)
- Default
- DefaultBlack
- DefaultWhite
- Puzzle01
- Puzzle02

### State String Symbols:

|Symbols|Meaning|
|---|---| 
| . | empty cell|
| K | black king|
| Q | black queen|
| R | black rook, castle|
| B | black bishop|
| N | black knight|
| P | black pawn|
| k | white king|
| q | white queen|
| r | white rook, castle|
| b | white bishop|
| n | white knight|
| p | white pawn|

Example:
```
RNBQKBNR
PPPPPPPP
........
........
....p...
........
pppp.ppp
rnbqkbnr
```

### Cell address
To address a cell, a regular chess scheme is used, as in an example below:
```
8│RNBQKBNR
7│PPPPPPPP
6│........
5│........
4│....p...
3│........
2│pppp.ppp
1│rnbqkbnr
 └────────
  ABCDEFGH      
```