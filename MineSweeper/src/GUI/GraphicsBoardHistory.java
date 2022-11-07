package GUI;

import Save.BoardInsertValues;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import minesweeper.Game;

public class GraphicsBoardHistory extends JFrame implements MouseListener {

    JPanel p = new JPanel();
    private final int Row;
    private final int Column;
    private final int[][] arrayShow;
    private final int locationID;
    private final int boardID;
    private boolean status;
    private final ImageIcon icon = new ImageIcon("D:\\Users\\Daniel\\Desktop\\Studies\\flag.png");
    private final ImageIcon icon2 = new ImageIcon("D:\\Users\\Daniel\\Desktop\\Studies\\mines.jpg");
    private final Game gameLogic;
    private final BoardInsertValues biv;
    private final JButton button[][];
    

    public GraphicsBoardHistory(int[][] InitializedArray, int flag, int mines, int locationID, int boardID) {
        super("MineSweeper");
        this.locationID = locationID;
        this.boardID = boardID;
        this.gameLogic = new Game();
        this.arrayShow = InitializedArray;
        this.gameLogic.setBoard(InitializedArray);
        this.gameLogic.setMinesCount(mines);
        this.gameLogic.setFlag(flag);
        this.Row = this.gameLogic.getBoard().length;
        this.Column = this.gameLogic.getBoard()[0].length;
        this.gameLogic.setRowCount(Row);
        this.gameLogic.setColCount(Column);
        button = new JButton[this.Row][this.Column];
        biv = new BoardInsertValues(this.locationID, this.boardID);
        setSize(800, 800);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        p.setLayout(new GridLayout(this.Row, this.Column));
        for (int i = 0; i < button.length; i++) {
            for (int j = 0; j < button[i].length; j++) {
                button[i][j] = new JButton();
                button[i][j].setFocusable(false);
                button[i][j].addMouseListener(this);
                p.add(button[i][j]);

            }
        }
        IntroducingVelues(true);
        add(p);
        setVisible(true);
    }

    
    
    private void IntroducingVelues(boolean ststus) {
        for (int i = 0; i < arrayShow.length; i++) {
            for (int j = 0; j < arrayShow[i].length; j++) {
                if (!ststus) {
                    //print all mines !
                    if (arrayShow[i][j] == 10 || arrayShow[i][j] == -3) {
                        this.button[i][j].setIcon(icon2);
                    }
                } else {
                    if (arrayShow[i][j] == 10 || arrayShow[i][j] == -1) {
                        continue;

                    }
                    if (arrayShow[i][j] == -2 || arrayShow[i][j] == -3) {
                        this.button[i][j].setIcon(icon);
                        continue;
                    }
                    this.button[i][j].setFont(new Font("segoe script", Font.BOLD, 15));
                    this.button[i][j].setText(arrayShow[i][j] + "");
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < button.length; i++) {
            for (int j = 0; j < button[i].length; j++) {
                if (e.getSource() == button[i][j]) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        if (gameLogic.isBoom(i, j)) {
                            biv.winOrlost(this.status);
                            biv.deleteSaveIfWinORLast();
                            IntroducingVelues(false);
                            int result = JOptionPane.showConfirmDialog(null, "Press OK to new game",
                                    "End of the game", JOptionPane.OK_CANCEL_OPTION);
                            if (result == JOptionPane.OK_OPTION) {
                                this.dispose();
                                //return to the choose levels(work on returning name)
                                HomeGame homeGame = new HomeGame("", this.locationID);
                            } else if (result == JOptionPane.CANCEL_OPTION
                                    || result == JOptionPane.CLOSED_OPTION) {
                                new HomeGame().dispose();
                                this.dispose();
                            }

                        }
                        biv.insertMineOrClick(i, j, 2);
                        button[i][j].setIcon(null);
                        gameLogic.removeFlag(i, j);
                        gameLogic.OpenCell(i, j);
                        IntroducingVelues(true);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        if (this.button[i][j].getIcon() == icon) {
                            button[i][j].setIcon(null);
                            gameLogic.removeFlag(i, j);
                            biv.deleteFlag(i, j);
                            continue;
                        }
                        if (gameLogic.flag(i, j)) {
                            //if there is a mine in the flag
                            biv.insertMineOrClick(i, j, -3);
                        } else {
                            //if there is on mine in the flag
                            biv.insertMineOrClick(i, j, -2);

                        }

                        if (button[i][j].getText().isEmpty()) {
                            gameLogic.flag(i, j);
                            button[i][j].setIcon(icon);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (gameLogic.Win()) {
            this.status = true;
            biv.winOrlost(this.status);
            biv.deleteSaveIfWinORLast();
            int result = JOptionPane.showConfirmDialog(null, "you Win \n Press OK to new game",
                    "End of the game", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                this.dispose();
                //return to the choose levels(work on returning name)
                HomeGame homeGame = new HomeGame("", this.locationID);

            } else if (result == JOptionPane.CANCEL_OPTION
                    || result == JOptionPane.CLOSED_OPTION) {
                this.dispose();
            }

        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
