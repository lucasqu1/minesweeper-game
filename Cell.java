package org.cis1200.minesweeper;

public class Cell {
    private boolean isFlagged;
    private boolean isMine;
    private int mineCount; //only return if isMine is false
    private boolean isRevealed; //should always be false upon creation

    //Creation of a cell using pre-determined parameters
    public Cell(boolean isFlagged, boolean isMine, int mineCount, boolean isRevealed) {
        this.isFlagged = isFlagged;
        this.isMine = isMine;
        this.mineCount = mineCount;
        this.isRevealed = isRevealed;
    }

    /*
        Static method to find the number of mines surrounding a cell.

        Not intended for cells containing a mine. Only use on non-mine cells

        When the main method finishes creating the game board, iterate through all
        non-mines and calculate their surrounding counts.
    */

    public void toggleFlagged() {
        this.isFlagged = !this.isFlagged;
    }

    public int getMineCount() {
        return this.mineCount;
    }

    public void setMineCount(int x) {
        this.mineCount = x;
    }

    public void revealCell() {
        this.isRevealed = true;
    }

    public void setMine() {
        this.isMine = true;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public boolean isMine() {
        return this.isMine;
    }

    public boolean isRevealed() {
        return this.isRevealed;
    }
}
