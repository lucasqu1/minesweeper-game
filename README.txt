=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1: 2D Arrays:
            I used 2D arrays to model the state of the board in Minesweeper. It was a 2D array of Cell
        objects. I created a custom cell object because I felt like it would simplify obtaining values from
        each cell. The cell object contains functions to check for whether a cell is revealed, is a
        mine, or is flagged, or a combination of all of these. It also contains functions for setting the
        number of mines surrounding a cell, setting a cell as flagged, or setting a cell as a mine itself.
        I used iteration to initialize the cell objects, set the surrounding mine numbers, and to set if the
        cell was a mine.

            It is simply 1 2D array that models all states of every cell. This simplifies things allowing
        iteration to be done once, modifying cells as needed. I used a 2D array because the game itself is
        inherently 2-dimensional. There isn't an intuitive way to model the game state with a 1D array. I also
        use accessor methods to hide all game components, ensuring that outside methods cannot directly call the
        game components.

  2: Recursion:
            I used recursion to implement the "reveal" algorithm for minesweeper. I used recursion rather than
        iteration because it is a naturally intuitive approach to finding all neighboring revealed cells. When
        a cell with no surrounding mines is clicked, in my algorithm, if the 8 neighboring cells also have no
        mines surrounding them, they get revealed. The search continues until cells with surrounding mines are
        hit. In my mind, iteration feels less intuitive, and I wasn't sure how to implement my reveal algorithm
        with it.

            My base cases are at the beginning of the algorithm. If they are met, then the call stops.

        - If the cell is already revealed
        - If the cell has a mine count greater than zero
        - If the bounds of the cell are outside the board

            While my code is a bit inefficient, stack overflow doesn't occur because the board size isn't too
        large. This is why I felt comfortable implementing the check for each surrounding cell with 8 if-statements.


  3: File I/O:

            I chose to implement file I/O to save and load game states. I felt that this was the most appropriate use
         of this concept because a user may want to save the game and play it later. A file is the easiest way to store
         game states, and the program should be able to read the game state to a file. If the user wants to play it later,
         they should be able to enter the file name and continue where they were playing from. Code written in the IDE can't
         really communicate with the outside world unless you save to files, or do something of that nature.

            I use the BufferedWriter class along with the FileWriter class to create a file and write the state of every cell
         line by line. If the user were to open up this file, they could most likely understand what it is saying, and this
         made it simple for me to load the save file later. When I loaded the save file, I just read the state of the game
         line by line, created the respective difficulty board, and then loaded every cell one by one. For the loading
         of the game, I just used a BufferedReader object and FileReader class. More details in my class description.

  4: J-Unit Testable Component:
         I implemented the following unit tests. Because of the way my game was modeled, no GUI components were tested.
            - Check for win
            - Check for loss
            - Flag a revealed cell (should do nothing)
            - Toggling a flag works
            - Revealing a cell works
            - Counting mines on the border works
            - Counting mines in the middle of the field works
            - Playing a turn works
            - Loading a game works
            - Saving a game works
         I did this by hard coding a specific game in and ensuring functionality was proper.
         These are appropriate concepts to test because they only rely on the state of the game itself.
         I test the most important parts of the game, ensuring that basic functionality works, and no
         large bug persist in my code.


===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

    The Cell class is the state of every square in my minesweeper game. It stores 4 essential states,
  which are isFlagged, isMine, mineCount, and isRevealed. This allows the game to detect whether a cell
  is a mine, if its flagged, and whether its revealed. If it's revealed, it will execute the recursive
  reveal function I wrote. If it's flagged, the turn will not carry out. If it's a mine, then the game
  will end. The Cell class I made is essential to simplifying my code and making it easier to read.

    The MineSweeper class is the game itself. I code all of the functions needed to play the game, and
  I attempted to implement the model view controller design pattern, allowing me to first develop the game
  functionality, then add the GUI elements later on. This also made it very easy to execute JUnit tests on my
  game. I could simply create a custom game and ensure all my functions work properly with the code.

    The MineSweeper class also includes functions for loading and saving the game state to a file. When the user
  is prompted to enter a filename they want to save to by the save button, it calls the saveGame method with the file name.
  The function utilizes a BufferedWriter that writes to a file with the name "fileName". All it does is save the game
  difficulty, whether the game has ended, and whether a move has been made after that.
  For each cell, it writes the x and y coordinates and then places an equals sign.
  Afterwards, it writes the 4 states of every cell one by one, separated by commas. This can get tedious, but syntactically,
  it is really easy to read, and I found it easy to understand and work with.

    The loadGame function just reads the saved file. I used an equals sign to separate the 3 large states, and then just
  parsed whatever was after the equals sign. For the cells themselves, I parse the x and y coordintes, the 4 states,
  and then assign those states to the Cell in the game array (already initialized depending on what difficulty was parsed).

    I made 3 classes: MineSweeperEasy, MineSweeperMedium, and MineSweeperHard. These were to execute when a user
  input was received in the RunMineSweeper class. They are implementations of JPanel, each with their own custom
  dimension depending on the difficulty of the game.

    Upon the click of the respective difficulty button, the JPanel is created and displayed. It also stores a MineSweeper object
  with its respective difficulty. I used the listener in TicTacToe to divide the mouse inputs depending on what square is
  clicked on the board. Each of these classes is responsible for dividing up the space into cells, and then painting the
  state of the cells on the board as well. I designed the board for the easy one first, and then modified the Medium and
  Hard boards to have their respective dimensions.

    If the cell was revealed, I drew a light grey square on the board where it was clicked. If the user chose to flag,
  I would draw the flag sprite onto that square. If the user failed, it would highlight all squares that were mines and
  whether the flags were correct or not. (red if not identified, green if identified)

    Finally, I have the RunMineSweeper class, which creates the menu that I designed. It is a JPanel placed in a JFrame.
  The JPanel is a 3 x 1 grid layout which stores 3 things: the image of the game, the difficulty buttons, and then the
  load button. The difficulty buttons create instances of their respective MineSweeper___ board, starting the game. It
  also creates listeners for the Save / Reset buttons that are placed on screen when the game is started. Then, I have
  the "startGameFromSave" function that searches for the respective game file, and then calls MineSweeper's loadGame
  function.


- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

    Absolutely. At times, I found it confusing when I had three boards that were essentially doing the exact same thing
  and this made it a bit confusing when I had to integrate them into my menu. I would have found it easier to create one
  class that had the functionality of creating objects when the button was clicked. At the time, this seemed like the most
  intuitive solution to me.

    I also found the File I/O to be difficult at times. After reading through the Libraries for BufferedReader/Writer and
  FileReader/Writer, I found the exact functions that I was looking for which made it simple to parse strings and
  extract the necessary data to work with.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

    I would attempt to refactor my minesweeper to work on any input the user wanted. In original minesweeper, the
  user usually has the 3 difficulty options, but I'd like the user to be able to input a grid of any size, and my
  code to automatically generate a game of that size.

    There are also most definitely better ways to implement recursion in my algorithm. There are most likely more
  efficient algorithms that don't require using 8 if statements per recursive call. The stack most likely looks horrific
  when calls are made like this, but because the grid isn't that large, it does the job.

    Lastly, I would try and experiment to see if there are more efficient ways of storing the state of my minesweeper board.
  While my method works, it could be better to not have so much data per line, and potentially use fewer variables to model states.
  Maybe I could use letters or numbers representing all possible combinations of cells and this could work out better.


========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  - Minesweeper flag: https://www.iconfinder.com/icons/3024770/flag_flags_marker_nation_icon
  - https://www.youtube.com/watch?v=1OyYyVlziRM (ImageIcon class for putting the title image of the game)

