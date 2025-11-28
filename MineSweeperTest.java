package org.cis1200.minesweeper;

import org.junit.jupiter.api.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MineSweeperTest {
    MineSweeper t;

    @BeforeEach
    public void setUp() {
        t = new MineSweeper(0);
        t.setBoard(new Cell[9][9]);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                t.getBoard()[i][j] = new Cell(false, false, 0, false);
            }
        }
        t.getBoard()[0][1].setMineCount(1);
        t.getBoard()[1][0].setMineCount(1);
        t.getBoard()[1][1].setMineCount(1);
        t.toggleIsFirstMove();
    }

    @Test
    public void revealCellWorks() {
        t.getBoard()[0][0].setMineCount(0);
        t.getBoard()[0][1].setMineCount(1);
        t.getBoard()[1][0].setMineCount(1);
        t.getBoard()[1][1].setMineCount(1);

        //Initially, none of the cells should be revealed
        assertFalse(t.getBoard()[0][0].isRevealed());
        assertFalse(t.getBoard()[0][1].isRevealed());
        assertFalse(t.getBoard()[1][0].isRevealed());
        assertFalse(t.getBoard()[1][1].isRevealed());

        t.playTurn(3, 3);
        t.printBoard();

        //Only the cells not in the upper left corner should be revealed
        assertFalse(t.getBoard()[0][0].isRevealed());
        assertTrue(t.getBoard()[0][1].isRevealed());
        assertTrue(t.getBoard()[1][0].isRevealed());
        assertTrue(t.getBoard()[1][1].isRevealed());

        t.printBoard();
    }


    @Test
    public void toggleFlagWorks() {
        t.getBoard()[0][0].setMine();
        Cell c = t.getCell(0, 0);
        System.out.println("The cell is a mine: " + c.isMine());


        t.playTurn(5, 5);
        t.printBoard();

        assertFalse(c.isFlagged());
        c.toggleFlagged();
        assertTrue(c.isFlagged());
        c.toggleFlagged();
        assertFalse(c.isFlagged());
    }

    @Test
    public void playTurnWorks() {
        t.getBoard()[0][0].setMine();
        Cell c = t.getCell(0, 0);
        System.out.println("The cell is a mine: " + c.isMine());


        t.playTurn(5, 5);
        t.printBoard();

        assertTrue(t.getBoard()[4][4].isRevealed());
    }

    @Test
    public void checkWinConditionLoss() {
        t.getBoard()[0][0].setMine();
        Cell c = t.getCell(0, 0);
        System.out.println("The cell is a mine: " + c.isMine());

        t.playTurn(1, 1);
        assertTrue(t.getGameOver());
    }

    @Test
    public void checkWinConditionsWin() {
        t.getBoard()[0][0].setMine();
        Cell c = t.getCell(0, 0);
        System.out.println("The cell is a mine: " + c.isMine());

        t.flagCell(1, 1);
        assertTrue(!t.getGameOver());
    }

    @Test
    public void countMinesBorder() {
        t.getBoard()[8][8].setMineCount(3);
        t.getBoard()[6][6].setMineCount(1);
        t.getBoard()[6][7].setMineCount(2);
        t.getBoard()[6][8].setMineCount(2);
        t.getBoard()[7][6].setMineCount(2);
        t.getBoard()[8][6].setMineCount(2);

        t.playTurn(5, 5);
        t.printBoard();

        assertEquals(t.getBoard()[8][8].getMineCount(), 3);
    }

    @Test
    public void countMinesNonBorder() {
        t.getBoard()[8][8].setMineCount(3);
        t.getBoard()[6][6].setMineCount(1);
        t.getBoard()[6][7].setMineCount(2);
        t.getBoard()[6][8].setMineCount(2);
        t.getBoard()[7][6].setMineCount(2);
        t.getBoard()[8][6].setMineCount(2);

        t.playTurn(5, 5);
        t.printBoard();

        assertEquals(t.getBoard()[6][6].getMineCount(), 1);
    }

    @Test
    public void flagRevealedCell() {
        t.playTurn(5, 5);
        t.flagCell(1, 1);
        t.getCell(0, 0).setMine();
        t.printBoard();

        assertFalse(t.getBoard()[5][5].isFlagged());
    }

    @Test
    public void loadSavedGame() {
        t.loadGame("files/loadTest");
        assertTrue(t.getCell(0, 0).isMine());
        assertTrue(t.getCell(0, 0).isFlagged());
        assertEquals(t.getCell(1, 0).getMineCount(), 1);
        assertEquals(t.getCell(0, 1).getMineCount(), 1);
        assertEquals(t.getCell(1, 1).getMineCount(), 1);
    }

    @Test
    public void saveAGame() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/loadTest"))) {
            String difficultyLine = reader.readLine();
            assertEquals("difficulty=0", difficultyLine);

            String gameOverLine = reader.readLine();
            assertEquals("gameOver=false", gameOverLine);

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("cell0,0")) {
                    assertEquals("cell0,0=true,true,0,false", line);
                } else if (line.startsWith("cell0,1")) {
                    assertEquals("cell0,1=false,false,1,true", line);
                } else if (line.startsWith("cell1,0")) {
                    assertEquals("cell1,0=false,false,1,true", line);
                } else if (line.startsWith("cell1,1")) {
                    assertEquals("cell1,1=false,false,1,true", line);
                }
            }
        } catch (IOException e) {
            System.out.println("Exception thrown");
        }
    }
}


