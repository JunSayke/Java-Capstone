# Java-Capstone
- Antonio M. Ubaldo
- Jian Lev C. Olamit
- Ranz Matheu M. Lumayno
- Shayne Marie B. Angus
  
# Minesweeper Solver
How to use? Tested on mnsw.pro and minesweeper.online

Run BoardGUI and setup the following inputs indicated:
1. Set up a minesweeper app or website. Run BoardGUI to open the minesweeper solver app
2. Split your screen to have one side the minesweeper app and the other the minesweeper solver app
3. Set your Rows, Columns, and Total Mines
4. Click Select Region and draw the minesweeper board as accurately as possible
5. Click Scan Board to check if the board is accurately drawn
6. Try opening up tiles on the minesweeper app before scanning the board again
7. Click Scan Board and enjoy!

Buttons:
- Toggle Auto-Click: Solves the current scan for you once toggled
- Scan Board: Recaptures the minesweeper board to display the current probabilities of the board
- Solve for me: Solves everything for you, unless it's a 50/50
- Select Region: Draws a rectangle on mouse input used to select the minesweeper board for scanning

Legend:
- Green: Guaranteed safe tiles
- Red: Guaranteed mine tiles
- Gray: Displays the chances of a mine tile depending on how dark the shade is

Structural Design Pattern Used:
*  Facade - Minesweeper AI
*  Bridge - Tile Analyzer to Board Analyzer

Minesweeper Solver Algorithm Used:
*  Advanced - https://github.com/Zomis/Minesweeper-Analyze

# UML Class Diagram
![MinesweeperSolverUML](https://github.com/JunSayke/Java-Capstone/assets/142708326/c98e6bc7-a9ea-4ce1-8359-31ae648807c5)

Shoutout to Sir Serato
