package org.cis1200.minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MineSweeperMediumBoard extends JPanel {

    private MineSweeper game;

    public final static int BOARD_WIDTH = 800;

    public final static int BOARD_HEIGHT = 800;

    public MineSweeperMediumBoard() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        setFocusable(true);

        game = new MineSweeper(1);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                if (SwingUtilities.isLeftMouseButton(e)) {
                    game.playTurn((p.x / 50) + 1, (p.y / 50) + 1);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    game.flagCell((p.x / 50) + 1, (p.y / 50) + 1);
                }

                repaint();
            }
        });
    }

    public void reset() {
        game.reset();
        repaint();

        requestFocusInWindow();
    }

    public MineSweeper getGame() {
        return game;
    }

    @Override
    public void paintComponent(Graphics g) {
        BufferedImage img = null;
        try {
            if (img == null) {
                img = ImageIO.read(new File("files/flag.png"));
            }
        } catch (IOException e) {
            System.out.println("Error loading flag");
        }

        super.paintComponent(g);

        int unitWidth = BOARD_WIDTH / 16;
        int unitHeight = BOARD_HEIGHT / 16;


        for (int i = 0; i < 9; i++) {
            g.drawLine(i * unitWidth, 0, i * unitWidth, 800);
        }

        for (int j = 0; j < 16; j++) {
            g.drawLine(0, j * unitHeight, BOARD_WIDTH, j * unitHeight);
        }

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                Cell state = game.getCell(j, i);
                int x = j * unitWidth;
                int y = i * unitHeight;

                if (state.isRevealed()) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x, y, unitWidth, unitHeight);

                    if (state.getMineCount() > 0) {
                        g.setColor(Color.BLACK);
                        g.drawString(String.valueOf(
                                state.getMineCount()), x + unitWidth / 2, y + unitHeight / 2);
                    }
                } else {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect(x, y, unitWidth, unitHeight);

                    if (state.isFlagged()) {
                        g.drawImage(img, x + 10, y + 10, unitWidth - 20, unitHeight - 20, null);
                    }
                }

                g.setColor(Color.BLACK);
                g.drawRect(x, y, unitWidth, unitHeight);
            }
        }

        if (game.getGameOver()) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    Cell state = game.getCell(j, i);
                    int x = j * unitWidth;
                    int y = i * unitHeight;

                    if (state.isMine() && !state.isFlagged()) {
                        g.setColor(Color.RED);
                        g.fillRect(x, y, unitWidth, unitHeight);
                    } else if (state.isMine() && state.isFlagged()) {
                        g.setColor(Color.GREEN);
                        g.fillRect(x, y, unitWidth, unitHeight);
                    }

                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, unitWidth, unitHeight);
                }
            }
        }

    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}

