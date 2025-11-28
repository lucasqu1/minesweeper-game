package org.cis1200.minesweeper;

import java.util.Random;
import java.io.*;

public class MineSweeper {
    private static Cell[][] board;
    private boolean gameOver;
    private Random r;
    int difficulty; //0 if easy, 1 if medium, 2 if hard
    private boolean firstMove;

    public MineSweeper(int difficulty) {
        this.difficulty = difficulty;
        this.firstMove = true;
        reset();
    }
    public boolean getGameOver() {
        return gameOver;
    }
    public Cell getCell(int x, int y) {
        return board[y][x];
    }

    public Cell[][] getBoard() {
        return board;
    }

    public void setBoard(Cell[][] r) {
        this.board = r;
    }

    public void toggleIsFirstMove() {
        this.firstMove = !firstMove;
    }


    /*
    * The assumption is that I am only being passed in valid coordinates.
    * I will attempt to preserve this invariant.
    */

    public int getSurroundingMines(int xCoord, int yCoord) {
        int mineCount = 0;

        int x1 = xCoord - 1;
        int y1 = yCoord - 1;
        int x2 = xCoord + 1;
        int y2 = yCoord + 1;

        if (x1 < 0) {
            x1 = 0;
        }
        if (x1 >= board[0].length) {
            x1 = board[0].length - 1;
        }
        if (y1 < 0) {
            y1 = 0;
        }
        if (y1 >= board.length) {
            y1 = board.length - 1;
        }

        if (x2 < 0) {
            x2 = 0;
        }
        if (x2 >= board[0].length) {
            x2 = board[0].length - 1;
        }
        if (y2 < 0) {
            y2 = 0;
        }
        if (y2 >= board.length) {
            y2 = board.length - 1;
        }

        for (int i = y1; i <= y2; i++) {
            for (int j = x1; j <= x2; j++) {
                if (board[i][j].isMine()) {
                    mineCount++;

                }
            }
        }

        if (board[yCoord][xCoord].isMine()) {
            mineCount = mineCount - 1;
        }
        return mineCount;

        /*
        Debugging purposes
        System.out.println("Top left X: "+x1);
        System.out.println("Top left Y: "+y1);
        System.out.println("bottom left X: "+x2);
        System.out.println("bottom left Y: "+y2);
         */
    }

    public void reset() {
        this.gameOver = false;
        this.firstMove = true;
        if (difficulty == 0) {
            initializeBoard(9, 9);
        } else if (difficulty == 1) {
            initializeBoard(16, 16);
        } else {
            initializeBoard(30, 16);
        }
    }

    /*
     * Called by the constructor.
     *
     *
     */
    private void initializeBoard(int row, int col) {
        int boardWidth;
        int boardHeight;
        int mineNum;

        if (difficulty == 0) {
            boardWidth = 9;
            boardHeight = 9;
            mineNum = 10;
        } else if (difficulty == 1) {
            boardWidth = 16;
            boardHeight = 16;
            mineNum = 40;
        } else {
            boardWidth = 30;
            boardHeight = 16;
            mineNum = 99;
        }

        board = new Cell[boardHeight][boardWidth];

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                board[i][j] = new Cell(false, false, 0, false);
            }
        }

        r = new Random(System.currentTimeMillis());

        int iter = 0;
        while (iter < mineNum) {
            int randX = r.nextInt(boardWidth);
            int randY = r.nextInt(boardHeight);


            if (Math.abs(randX - col) <= 1 && Math.abs(randY - row) <= 1) {
                //immediately skips to next iteration
                continue;
            }

            if (!board[randY][randX].isMine()) {
                board[randY][randX].setMine();
                iter++;
            }
        }

        for (int i = 0; i < boardHeight; i++) {
            for (int j = 0; j < boardWidth; j++) {
                if (!board[i][j].isMine()) {
                    board[i][j].setMineCount(getSurroundingMines(j, i));
                }
            }
        }
    }

    //returns whether turn was successful or not
    // true if went through
    // false if game over, or if player clicks on flag
    public boolean playTurn(int x, int y) {


        x = x - 1;
        y = y - 1;

        if (gameOver) {
            return false;
        }
        System.out.println("Turn played at x:" + x + " y: " + y);

        if (firstMove) {
            initializeBoard(y, x);
            firstMove = false;
        }

        if (board[y][x].isFlagged() || gameOver) {
            return false;
        }

        if (board[y][x].isMine()) {
            gameOver = true;
            return false;
        }

        reveal(y, x);

        if (checkWin()) {
            gameOver = true;
            System.out.println("Game Won!");
            return true;
        }

        return true;
    }

    public boolean flagCell(int x, int y) {
        if (gameOver) {
            return false;
        }
        x = x - 1;
        y = y - 1;

        if (board[y][x].isRevealed()) {
            return false;
        } else {
            if (board[y][x].isFlagged()) {
                System.out.println("Cell UnFlagged at x:" + x + " y: " + y);
            }
            if (!board[y][x].isFlagged()) {
                System.out.println("Cell Flagged at x:" + x + " y: " + y);
            }
            board[y][x].toggleFlagged();
        }

        if (checkWin()) {
            gameOver = true;
            System.out.println("Game Won!");
            return true;
        }

        return true;
    }

    public void reveal(int row, int col) {
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) {
            return;
        }
        if (board[row][col].isRevealed() ||
                board[row][col].isFlagged() || board[row][col].isMine()) {
            return;
        }
        if (board[row][col].getMineCount() > 0) {
            board[row][col].revealCell();
            return;
        }

        board[row][col].revealCell();

        int d = 1;

        /*
         * I will label the cells like this:
         * 1 2 3
         * 4 c 5
         * 6 7 8
         */

        // CELL 1
        if (row - d >= 0 && col - d >= 0) {
            if (board[row - d][col - d].getMineCount() >= 0) {
                reveal(row - d, col - d);
            }
        }

        // CELL 2
        if (row - d >= 0) {
            if (board[row - d][col].getMineCount() >= 0) {
                reveal(row - d, col);
            }
        }

        // CELL 3
        if (row - d >= 0 && col + d < board[0].length) {
            if (board[row - d][col + d].getMineCount() >= 0) {
                reveal(row - d, col + d);
            }
        }

        // CELL 4
        if (col - d >= 0) {
            if (board[row][col - d].getMineCount() >= 0) {
                reveal(row, col - d);
            }
        }

        // CELL 5
        if (col + d < board[0].length) {
            if (board[row][col + d].getMineCount() >= 0) {
                reveal(row, col + d);
            }
        }

        // CELL 6
        if (row + d < board.length && col - d >= 0) {
            if (board[row + d][col - d].getMineCount() >= 0) {
                reveal(row + d, col - d);
            }
        }

        // CELL 7
        if (row + d < board.length) {
            if (board[row + d][col].getMineCount() >= 0) {
                reveal(row + d, col);
            }
        }

        // CELL 8
        if (row + d < board.length && col + d < board[0].length) {
            if (board[row + d][col + d].getMineCount() >= 0) {
                reveal(row + d, col + d);
            }
        }
    }

    public boolean checkWin() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Cell cell = board[i][j];
                if (!cell.isMine() && !cell.isRevealed()) {
                    return false;
                }
                if (cell.isMine() && !cell.isFlagged()) {
                    return false;
                }
                if (!cell.isMine() && cell.isFlagged()) {
                    return false;
                }
            }
        }
        return true;
    }
    /*
     * FILE IO
     */

    public void saveGame(String fileName) {
        try (BufferedWriter r = new BufferedWriter(new FileWriter(fileName))) {
            r.write("difficulty=" + difficulty);
            r.newLine();
            r.write("gameOver=" + gameOver);
            r.newLine();
            r.write("firstMove=" + firstMove);
            r.newLine();

            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    Cell cell = board[i][j];
                    r.write("cell" + i + "," + j + "=" +
                            cell.isFlagged() + "," +
                            cell.isMine() + "," +
                            cell.getMineCount() + "," +
                            cell.isRevealed());
                    r.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving game");
        }
    }

    public void loadGame(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String difficultyLine = reader.readLine();
            difficulty = Integer.parseInt(difficultyLine.split("=")[1]);

            String gameOverLine = reader.readLine();
            gameOver = Boolean.parseBoolean(gameOverLine.split("=")[1]);

            String firstMoveLine = reader.readLine();
            firstMove = Boolean.parseBoolean(firstMoveLine.split("=")[1]);

            if (difficulty == 0) {
                board = new Cell[9][9];
            } else if (difficulty == 1) {
                board = new Cell[16][16];
            } else {
                board = new Cell[16][30];
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("cell")) {
                    String[] parts = line.split("=");
                    String[] coordinates = parts[0].substring(4).split(",");
                    int i = Integer.parseInt(coordinates[0]);
                    int j = Integer.parseInt(coordinates[1]);

                    String[] cellState = parts[1].split(",");
                    boolean isFlagged = Boolean.parseBoolean(cellState[0]);
                    boolean isMine = Boolean.parseBoolean(cellState[1]);
                    int mineCount = Integer.parseInt(cellState[2]);
                    boolean isRevealed = Boolean.parseBoolean(cellState[3]);

                    board[i][j] = new Cell(isFlagged, isMine, mineCount, isRevealed);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading game");
        }
    }



    /*
     * FOR TESTING PURPOSES
     */
    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].isRevealed() && board[i][j].getMineCount() == 0) {
                    System.out.print("R ");
                } else if (board[i][j].isRevealed() && board[i][j].getMineCount() > 0) {
                    System.out.print(board[i][j].getMineCount() + " ");
                } else if (board[i][j].isFlagged()) {
                    System.out.print("F ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }


    public static void main(String[] args) {
        //Play through of game goes here

        MineSweeper g = new MineSweeper(0);
        g.playTurn(6,6);
        g.flagCell(2,7);
        g.flagCell(3,7);
        g.playTurn(1,7);
        g.flagCell(4,3);
        g.playTurn(5,3);
        g.playTurn(6,3);
        g.playTurn(4,2);
        g.playTurn(5,2);
        g.playTurn(6,2);
        g.flagCell(2,2);
        g.playTurn(1,1);
        g.playTurn(1,2);
        g.playTurn(2,1);
        g.flagCell(7,4);
        g.playTurn(8,4);
        g.playTurn(8,8);
        g.playTurn(8,7);
        g.flagCell(8,9);
        g.flagCell(8,6);
        g.playTurn(9,6);
        g.playTurn(9,7);
        g.playTurn(9,8);
        g.playTurn(9,9);
        g.playTurn(8,5);
        g.flagCell(9,5);
        g.flagCell(9,4);
        g.playTurn(9,3);
        g.playTurn(9,1);
        g.flagCell(9,2);
        System.out.println("baba booey");
        g.printBoard();


    }

}
