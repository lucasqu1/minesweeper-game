package org.cis1200.minesweeper;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class RunMineSweeper implements Runnable {
    public void run() {
        JFrame menu = new JFrame("Minesweeper");

        JPanel panel = new JPanel();
        menu.add(panel);

        JButton loadButton = new JButton("Load from Save");

        loadButton.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(menu,
                    "Enter the name of your save file:");

            if (fileName != null && !fileName.isEmpty()) {

                String filePath = "files/" + fileName;

                File saveFile = new File(filePath);
                if (!saveFile.exists()) {
                    JOptionPane.showMessageDialog(menu, "File does not exist!");
                    return;
                }

                try {
                    MineSweeper tempGame = new MineSweeper(0);
                    tempGame.loadGame(filePath);

                    int loadedDifficulty = tempGame.difficulty;

                    menu.dispose();
                    startGameFromSave(loadedDifficulty, filePath);

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(menu, "Error loading game!");
                }
            } else {
                JOptionPane.showMessageDialog(menu, "Load Cancelled");
            }
        });

        JPanel difficultyButtons = new JPanel();
        JButton easy = new JButton("Easy");
        JButton medium = new JButton("Medium");
        JButton hard = new JButton("Hard");

        difficultyButtons.add(easy);
        difficultyButtons.add(medium);
        difficultyButtons.add(hard);

        panel.setLayout(new GridLayout(3, 1));

        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("files/minesweeperTitle.png");
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(
                100, 100, Image.SCALE_DEFAULT));
        imageLabel.setIcon(imageIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);


        panel.add(imageLabel);
        panel.add(difficultyButtons);
        panel.add(loadButton);

        menu.pack();
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setVisible(true);

        easy.addActionListener(e -> startGame(0, menu));
        medium.addActionListener(e -> startGame(1, menu));
        hard.addActionListener(e -> startGame(2, menu));

    }

    private void startGame(int difficulty, JFrame hey) {
        hey.dispose();
        String diffLabel;

        if (difficulty == 0) {
            diffLabel = "Easy";
        } else if (difficulty == 1) {
            diffLabel = "Medium";
        } else {
            diffLabel = "Hard";
        }
        JFrame gameFrame = new JFrame("Minesweeper - " + diffLabel);

        JPanel x = new JPanel();
        x.setLayout(new BorderLayout());

        MineSweeperEasyBoard board1 = null;
        MineSweeperMediumBoard board2 = null;
        MineSweeperHardBoard board3 = null;

        JButton reset = new JButton("Reset");

        JPanel toolbar = new JPanel();
        JButton saveGame = new JButton("Save and Quit");

        toolbar.add(saveGame);

        x.add(reset, BorderLayout.PAGE_END);
        x.add(toolbar, BorderLayout.PAGE_START);

        int w;
        int h;
        if (difficulty == 0) {
            w = 450;
            h = 546;
            board1 = new MineSweeperEasyBoard();
            x.add(board1, BorderLayout.CENTER);
            gameFrame.setSize(w, h);
            MineSweeperEasyBoard finalBoard = board1;
            reset.addActionListener(e -> {
                finalBoard.getGame().reset();
                finalBoard.repaint();
            });
        } else if (difficulty == 1) {
            w = 800;
            h = 896;
            board2 = new MineSweeperMediumBoard();
            x.add(board2, BorderLayout.CENTER);
            gameFrame.setSize(w, h);
            MineSweeperMediumBoard finalBoard1 = board2;
            reset.addActionListener(e -> {
                finalBoard1.getGame().reset();
                finalBoard1.repaint();
            });

        } else {
            w = 1050;
            h = 656;
            board3 = new MineSweeperHardBoard();
            x.add(board3, BorderLayout.CENTER);
            gameFrame.setSize(w, h);
            MineSweeperHardBoard finalBoard2 = board3;
            reset.addActionListener(e -> {
                finalBoard2.getGame().reset();
                finalBoard2.repaint();
            });
        }

        MineSweeperEasyBoard finalBoard3 = board1;
        MineSweeperMediumBoard finalBoard4 = board2;
        MineSweeperHardBoard finalBoard5 = board3;

        saveGame.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(gameFrame,
                    "Enter the name of your save file:");

            if (fileName != null && !fileName.isEmpty()) {
                try {
                    String filePath = "files/" + fileName;
                    if (difficulty == 0) {
                        finalBoard3.getGame().saveGame(filePath);
                    } else if (difficulty == 1) {
                        finalBoard4.getGame().saveGame(filePath);
                    } else {
                        finalBoard5.getGame().saveGame(filePath);
                    }
                    JOptionPane.showMessageDialog(gameFrame,
                            "Game saved successfully to " + fileName);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(gameFrame, "Error saving game");
                }
                gameFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(gameFrame, "Save Cancelled");
            }
        });

        gameFrame.add(x);

        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setVisible(true);
    }

    private void startGameFromSave(int difficulty, String filePath) {
        String diffLabel;

        if (difficulty == 0) {
            diffLabel = "Easy";
        } else if (difficulty == 1) {
            diffLabel = "Medium";
        } else {
            diffLabel = "Hard";
        }

        JFrame gameFrame = new JFrame("Minesweeper - " + diffLabel);

        JPanel x = new JPanel();
        x.setLayout(new BorderLayout());

        JButton reset = new JButton("Reset");
        JPanel toolbar = new JPanel();
        JButton saveGame = new JButton("Save and Quit");

        toolbar.add(saveGame);
        x.add(reset, BorderLayout.PAGE_END);
        x.add(toolbar, BorderLayout.PAGE_START);

        int w, h;

        try {
            if (difficulty == 0) {
                w = 450;
                h = 546;
                MineSweeperEasyBoard board = new MineSweeperEasyBoard();
                board.getGame().loadGame(filePath);
                x.add(board, BorderLayout.CENTER);
                reset.addActionListener(e -> {
                    board.getGame().reset();
                    board.repaint();
                });
            } else if (difficulty == 1) {
                w = 800;
                h = 896;
                MineSweeperMediumBoard board = new MineSweeperMediumBoard();
                board.getGame().loadGame(filePath);
                x.add(board, BorderLayout.CENTER);
                reset.addActionListener(e -> {
                    board.getGame().reset();
                    board.repaint();
                });
            } else {
                w = 1050;
                h = 656;
                MineSweeperHardBoard board = new MineSweeperHardBoard();
                board.getGame().loadGame(filePath);
                x.add(board, BorderLayout.CENTER);
                reset.addActionListener(e -> {
                    board.getGame().reset();
                    board.repaint();
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(gameFrame,
                    "Error loading game!");
            return;
        }

        saveGame.addActionListener(e -> {
            String fileName = JOptionPane.showInputDialog(gameFrame,
                    "Enter the name of your save file:");

            if (fileName != null && !fileName.isEmpty()) {
                try {
                    String saveFilePath = "files/" + fileName;
                    if (difficulty == 0) {
                        ((MineSweeperEasyBoard) x.getComponent(2)).getGame().saveGame(saveFilePath);
                    } else if (difficulty == 1) {
                        ((MineSweeperMediumBoard)
                                x.getComponent(2)).getGame().saveGame(saveFilePath);
                    } else {
                        ((MineSweeperHardBoard) x.getComponent(2)).getGame().saveGame(saveFilePath);
                    }
                    JOptionPane.showMessageDialog(gameFrame,
                            "Game saved successfully to " + saveFilePath);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(gameFrame, "Error saving game");
                }
            } else {
                JOptionPane.showMessageDialog(gameFrame, "Save Cancelled");
            }
        });

        gameFrame.add(x);
        gameFrame.setSize(w, h);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setVisible(true);
    }
}
