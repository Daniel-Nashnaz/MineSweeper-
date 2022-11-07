package GUI;

import Save.BoardInsertValues;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import minesweeper.Game;

public final class GraphicsBoard extends JFrame implements ActionListener, MouseListener {

    JPanel p = new JPanel();
    private int Row;
    private int Column;
    private int mines;
    private final int locationID;
    private final int boardID;
    private boolean status;
    private final ImageIcon icon = new ImageIcon("D:\\Users\\Daniel\\Desktop\\Studies\\flag.png");
    private final ImageIcon icon2 = new ImageIcon("D:\\Users\\Daniel\\Desktop\\Studies\\mines.jpg");
    private final Game gameLogic;
    private final BoardInsertValues biv;
    private final JButton button[][];
    private final JMenuItem saveMenuItem;
    private final JMenuItem quitMenuItem;
    private final JMenu m;
    private final JMenuBar bar;

    public GraphicsBoard(int Row, int Column, int mines, int locationID, int boardID) throws Exception {
        super("MineSweeper");
        this.Row = Row;
        this.Column = Column;
        this.mines = mines;
        this.locationID = locationID;
        this.boardID = boardID;
        button = new JButton[this.Row][this.Column];
        gameLogic = new Game(this.Row, this.Column, this.mines);
        biv = new BoardInsertValues(this.locationID, this.boardID);
        isertMines();
        this.setLayout(null);
        setSize(800, 800);
        bar = new JMenuBar();
        m = new JMenu("Quit Game");
        quitMenuItem = new JMenuItem("exit without saveing");
        quitMenuItem.setForeground(Color.BLUE);
        saveMenuItem = new JMenuItem("exit and save");
        saveMenuItem.setForeground(Color.red);
        quitMenuItem.addActionListener(this);
        saveMenuItem.addActionListener(this);
        m.add(quitMenuItem);
        m.add(saveMenuItem);
        bar.add(m);
        this.setJMenuBar(bar);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JLabel label = new JLabel("Mine Sweeper", JLabel.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.red);
        label.setFont(new Font("Dialog", Font.BOLD, 100));
        p.setLayout(null);
        label.setBounds(0, 0, 800, 100);
        this.add(label);
        p.setBounds(0, 100, 785, 639);
        p.setLayout(new GridLayout(this.Row, this.Column));
        for (int i = 0; i < button.length; i++) {
            for (int j = 0; j < button[i].length; j++) {
                button[i][j] = new JButton();
                button[i][j].addMouseListener(this);
                p.add(button[i][j]);

            }
        }
        add(p);

        setVisible(true);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitMenuItem) {
            int result = JOptionPane.showConfirmDialog(null, "You sure", "MineSweeper",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                biv.winOrlost(false);
                biv.deleteSaveIfWinORLast();
                this.dispose();
            }
        }
        if (e.getSource() == saveMenuItem) {
            this.dispose();
        }

    }

    //isert mines to array and update in table
    public void isertMines() {
        int[][] cast = gameLogic.getBoard();
        for (int i = 0; i < cast.length; i++) {
            for (int j = 0; j < cast[i].length; j++) {
                if (cast[i][j] == 10) {
                    biv.insertMineOrClick(i, j, 10);
                }
            }
        }
    }

    private void IntroducingVelues(boolean ststus) {
        int[][] cast = gameLogic.getBoard();
        for (int i = 0; i < cast.length; i++) {
            for (int j = 0; j < cast[i].length; j++) {
                if (cast[i][j] == 10 || cast[i][j] == -1 || cast[i][j] == -2 || cast[i][j] == -3) {
                    //if win or last
                    if (!ststus) {
                        //print all mines !
                        if (cast[i][j] == 10 || cast[i][j] == -3) {
                            this.button[i][j].setIcon(icon2);
                            continue;
                        } else {
                            continue;
                        }
                    } else {
                        continue;
                    }

                }

                this.button[i][j].setFont(new Font("segoe script", Font.BOLD, sizeFont()));
                this.button[i][j].setText(cast[i][j] + "");
                button[i][j].setEnabled(false);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (int i = 0; i < button.length; i++) {
            for (int j = 0; j < button[i].length; j++) {
                if (e.getSource() == button[i][j]) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        //if checking whether a button is enabled or not...
                        if (button[i][j].isEnabled()) {
                            if (gameLogic.isBoom(i, j)) {
                                biv.winOrlost(this.status);
                                biv.deleteSaveIfWinORLast();
                                IntroducingVelues(false);
                                int resulta = JOptionPane.showConfirmDialog(null, "Game over!", "last", JOptionPane.PLAIN_MESSAGE);
                                if (resulta == JOptionPane.OK_OPTION) {
                                    int result = JOptionPane.showConfirmDialog(null, "Press OK to new game",
                                            "End of the game", JOptionPane.OK_CANCEL_OPTION);
                                    if (result == JOptionPane.OK_OPTION) {
                                        //return to the choose levels (work on returning name)
                                        new HomeGame("", this.locationID).setVisible(true);
                                    } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                                        this.dispose();
                                    }
                                }
                                this.dispose();
                                break;
                            }
                            biv.insertMineOrClick(i, j, 2);
                            button[i][j].setIcon(null);
                            gameLogic.removeFlag(i, j);
                            gameLogic.OpenCell(i, j);
                            IntroducingVelues(true);
                        }
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
                            button[i][j].setIcon(icon);
                        }
                    }

                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameLogic.Win()) {
            this.dispose();
            this.status = true;
            biv.winOrlost(this.status);
            biv.deleteSaveIfWinORLast();
            int resulta = JOptionPane.showConfirmDialog(null, "You Winner", "Win", JOptionPane.PLAIN_MESSAGE);
            if (resulta == JOptionPane.OK_OPTION) {
                int result = JOptionPane.showConfirmDialog(null, "Press OK to new game",
                        "End of the game", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    //return to the choose levels(work on returning name)
                    new HomeGame("", this.locationID).setVisible(true);
                    //HomeGame homeGame = new HomeGame("", this.locationID);
                } else if (result == JOptionPane.CANCEL_OPTION || result == JOptionPane.CLOSED_OPTION) {
                    this.dispose();
                }
            }
            this.dispose();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}

/*@Override
   public void paint(Graphics g){
        
        g.fillRect(100, 100, 400, 400);
        for(int i = 100; i <= 400; i+=100){
            for(int j = 100; j <= 400; j+=100){
                g.clearRect(i, j, 50, 50);
            }
        }
        
        for(int i = 150; i <= 450; i+=100){
            for(int j = 150; j <= 450; j+=100){
                g.clearRect(i, j, 50, 50);
            }
        }
    }*/
//    public static void main(String[] args) {
//
//        try {
////            JFrame frame = new JFrame();
////            frame.setSize(600, 600);
////            frame.getContentPane().add(new GraphicsBoard());
////            frame.setLocationRelativeTo(null);
////            frame.setBackground(Color.LIGHT_GRAY);
////            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
////            frame.setVisible(true);
//            GraphicsBoard p = new GraphicsBoard(15, 15, 10);
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        }
//
//    }
