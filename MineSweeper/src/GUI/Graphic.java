package GUI;

import Save.BoardInsertValues;
import static GUI.GameAccess.Username;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import minesweeper.Game;

public final class Graphic implements ActionListener, MouseListener {

    //Class properties 
    private JPanel panel;
    private JFrame frame;
    private JButton jbNewGame;
    private JButton jbExit;
    private JLabel labelTitle;
    private int Row;
    private int Column;
    private int mines;
    private int locationID;
    private int boardID;
    private int[][] arrayShow;
    private boolean status;
    //private final ImageIcon icon = new ImageIcon(getClass().getResource("/GUI/newpackage/images/flag.png"));
    //private final ImageIcon icon2 = new ImageIcon(getClass().getResource("/GUI/newpackage/images/mines.jpg"));
    private Game gameLogic;
    private GameImage gameImage;
    private BoardInsertValues biv;
    private JButton jbBoard[][];
    private JMenuItem saveMenuItem;
    private JMenuItem quitMenuItem;
    private JMenu m;
    private JMenuBar bar;
    private Clip clip;

    //New game
    public Graphic(int Row, int Column, int mines, int locationID, int boardID) {
        this.Row = Row;
        this.Column = Column;
        this.mines = mines;
        this.locationID = locationID;
        this.boardID = boardID;
        gameImage = new GameImage(this.Row); //or column
        jbBoard = new JButton[this.Row][this.Column];
        gameLogic = new Game(this.Row, this.Column, this.mines);
        biv = new BoardInsertValues(this.locationID, this.boardID);
        this.arrayShow = gameLogic.getBoard();
        isertMines();
        board();
    }

    //Recycling game (History)
    public Graphic(int[][] InitializedArray, int flag, int sumFlag, int mines, int locationID, int boardID) {
        this.locationID = locationID;
        this.boardID = boardID;
        this.gameLogic = new Game();
        this.arrayShow = InitializedArray;
        this.gameLogic.setBoard(InitializedArray);
        this.gameLogic.setMinesCount(mines);
        this.gameLogic.setFlag(flag);
        this.gameLogic.setSumFlag(sumFlag + flag);
        this.Row = this.gameLogic.getBoard().length;
        this.Column = this.gameLogic.getBoard()[0].length;
        this.mines = mines;
        gameImage = new GameImage(this.Row); //or column
        this.gameLogic.setRowCount(Row);
        this.gameLogic.setColCount(Column);
        jbBoard = new JButton[this.Row][this.Column];
        biv = new BoardInsertValues(this.locationID, this.boardID);
        board();

    }

    //create board 
    public void board() {
        frame = new JFrame("MineSweeper");
        panel = new JPanel();
        frame.setLayout(null);
        panel.setLayout(null);
        frame.setSize(800, 800);
        panel.setBounds(0, 10, 785, 720);
        //menu bar to game
        bar = new JMenuBar();
        m = new JMenu("Quit Game");
        quitMenuItem = new JMenuItem("exit without saveing");
        quitMenuItem.setForeground(Color.BLUE);
        saveMenuItem = new JMenuItem("exit and save");
        saveMenuItem.setForeground(Color.red);
        quitMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        //adds
        m.add(quitMenuItem);
        m.add(saveMenuItem);
        bar.add(m);
        frame.setJMenuBar(bar);
        //display
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        /*JLabel label = new JLabel("Mine Sweeper", JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.red);
        label.setFont(new Font("Dialog", Font.BOLD, 100));
        panel.setLayout(null);
        label.setBounds(0, 0, 800, 100);
        frame.add(label);
        panel.setBounds(0, 100, 785, 639);*/

        //buttons to click
        panel.setLayout(new GridLayout(this.Row, this.Column));
        for (int i = 0; i < jbBoard.length; i++) {
            for (int j = 0; j < jbBoard[i].length; j++) {
                jbBoard[i][j] = new JButton();
                jbBoard[i][j].setFocusable(false);
                jbBoard[i][j].addMouseListener(this);
                panel.add(jbBoard[i][j]);
            }
        }
        /*ImageIcon icon1 = new ImageIcon(getClass().getResource("/GUI/newpackage/images/6.png"));
        Image i = icon1.getImage();
        Image image = i.getScaledInstance(33, 30, Image.SCALE_SMOOTH);
        icon1 = new ImageIcon(image);
        jbBoard[0][0].setIcon(icon1);
        jbBoard[2][3].setIcon(icon1);
        //starting from 0
        
        
        GameImage gi = new GameImage("Easy");
        int i = 9;
        jbBoard[2][3].setIcon(gi.setImage(i));*/
        //insert to board
        insertValues(true);
        //adding buttons
        frame.add(panel);
        frame.setVisible(true);
    }

    public void insertValues(boolean GameMode) {
        for (int i = 0; i < this.Row; i++) {
            for (int j = 0; j < this.Column; j++) {
                if (this.jbBoard[i][j].getIcon() != null) {
                    //have image
                    continue;
                }
                if (!GameMode) {
                    //print all mines !
                    if (arrayShow[i][j] == 10 || arrayShow[i][j] == -3) {
                        this.jbBoard[i][j].setIcon(gameImage.setImage(arrayShow[i][j]));
                    }
                    continue;
                } else if (arrayShow[i][j] == 10 || arrayShow[i][j] == -1) {
                    continue;
                }
                this.jbBoard[i][j].setIcon(gameImage.setImage(arrayShow[i][j]));
            }
        }
    }

    public boolean isStatus() {
        return status;
    }

    public int sizeFont() {
        if (this.Row < 10) {
            return 20;
        }
        if (this.Row < 25) {
            return 15;
        }
        return 10;
    }

    public void isertMines() {
        for (int i = 0; i < arrayShow.length; i++) {
            for (int j = 0; j < arrayShow[i].length; j++) {
                if (this.arrayShow[i][j] == 10) {
                    biv.insertMineOrClick(i, j, 10);
                }
            }
        }
    }

    private void glassPane() {
        frame.setGlassPane(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        Container glasspane = (Container) frame.getGlassPane();
        glasspane.setLayout(null);
        //close frame because only listening to glasspane!
        glasspane.addMouseListener(new MouseAdapter() {
        });

        labelTitle = new JLabel("Game Over");
        if (isStatus()) {
            //if he win the game
            labelTitle.setText("You Win");
        }
        labelTitle.setForeground(Color.darkGray);
        labelTitle.setFont(new Font("David", Font.BOLD, 80));
        labelTitle.setBounds(150, 280, 500, 100);
        labelTitle.setHorizontalAlignment(JLabel.CENTER);
        //button to new game
        jbNewGame = new JButton("NEW GAME");
        jbNewGame.setBackground(Color.ORANGE);
        jbNewGame.setForeground(Color.RED);
        jbNewGame.setBounds(250, 450, 100, 40);
        jbNewGame.addActionListener(this);
        //button to exit
        jbExit = new JButton("EXIT");
        jbExit.setBackground(Color.BLACK);
        jbExit.setForeground(Color.LIGHT_GRAY);
        jbExit.setBounds(450, 450, 80, 40);
        jbExit.addActionListener(this);
        //adds
        glasspane.add(jbNewGame);
        glasspane.add(jbExit);
        glasspane.add(labelTitle);
        //show screen
        glasspane.setVisible(true);
    }

    public void gameSound(String mode) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        File file = null;
        if (mode.equalsIgnoreCase("CLICK")) {
            file = new File("D:\\JavaProjects\\TEST\\Sound\\click.wav");
        } else if (mode.equalsIgnoreCase("LOSS")) {
            clip.stop();
            file = new File("D:\\JavaProjects\\TEST\\Sound\\loss.wav");
        } else if (mode.equalsIgnoreCase("FLAG")) {
            clip.stop();
            file = new File("D:\\JavaProjects\\TEST\\Sound\\flag.wav");
        } else if (mode.equalsIgnoreCase("WIN")) {
            clip.stop();
            file = new File("D:\\JavaProjects\\TEST\\Sound\\win.wav");
        }
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
        clip.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitMenuItem) {
            int result = JOptionPane.showConfirmDialog(null, "You sure", "MineSweeper",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                biv.winOrlost(false);
                biv.deleteSaveIfWinORLast();
                frame.dispose();
                return;
            }
        }
        if (e.getSource() == saveMenuItem || e.getSource() == String.valueOf(EXIT_ON_CLOSE)) {
            frame.dispose();
            System.exit(0);
            return;
        }
        if (e.getSource() == jbExit) {
            HomeGame homeGame = new HomeGame(Username, this.locationID);
            homeGame.dispose();
            frame.dispose();
            return;
        }
        if (e.getSource() == jbNewGame) {
            frame.dispose();
            //return to the choose levels(work on returning name)
            HomeGame homeGame = new HomeGame(Username, this.locationID);
            homeGame.setVisible(true);
            homeGame.pack();
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            for (int i = 0; i < jbBoard.length; i++) {
                for (int j = 0; j < jbBoard[i].length; j++) {
                    if (e.getSource() == jbBoard[i][j]) {
                        //to lock button
                        if (this.arrayShow[i][j] >= 0 && this.arrayShow[i][j] <= 8) {
                            continue;
                        }
                        gameSound("CLICK");
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            //if checking whether a button is enabled or not...
                            if (jbBoard[i][j].isEnabled()) {
                                //if it's over
                                if (gameLogic.isBoom(i, j)) {
                                    biv.winOrlost(false);
                                    biv.deleteSaveIfWinORLast();
                                    insertValues(false);
                                    gameSound("LOSS");
                                    glassPane();
                                    return;
                                }
                                biv.insertMineOrClick(i, j, 2);
                                jbBoard[i][j].setIcon(null);
                                gameLogic.removeFlag(i, j);
                                gameLogic.OpenCell(i, j);
                                insertValues(true);
                            }
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            //check if have image in button
                            if (this.jbBoard[i][j].getIcon() != null) {
                                jbBoard[i][j].setIcon(null);
                                gameLogic.removeFlag(i, j);
                                biv.deleteFlag(i, j);
                                this.arrayShow[i][j] = gameLogic.getLocationBy(i, j);
                                /*for (int id = 0; id < this.Row; id++) {
                                    for (int jd = 0; jd < this.Column; jd++) {
                                        System.out.print(this.arrayShow[id][jd] + " ");
                                    }
                                    System.out.println("");
                                }*/
                                continue;
                            }
                            //  if (this.arrayShow[i][j] == -2 || this.arrayShow[i][j] == -3) {
                            //lock click
                            //     continue;
                            // }
                            //if there are more flags than mines! 
                            if (this.gameLogic.getSumFlag() < this.mines) {
                                gameSound("FLAG");
                                if (gameLogic.flag(i, j)) {
                                    //if there is a mine in the flag
                                    biv.insertMineOrClick(i, j, -3);
                                } else {
                                    //if there is on mine in the flag
                                    biv.insertMineOrClick(i, j, -2);
                                }
                                this.jbBoard[i][j].setIcon(gameImage.setImage(9));
                                ifWin();
                                return;
                            }
                        }

                    }
                }
            }
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        }
        ifWin();
    }

    public void ifWin() {
        if (gameLogic.Win()) {
            try {
                gameSound("WIN");
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            }
            this.status = true;
            biv.winOrlost(true);
            biv.deleteSaveIfWinORLast();
            glassPane();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
}
