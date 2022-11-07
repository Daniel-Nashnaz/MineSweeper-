package Save;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import minesweeper.Game;

public final class RefundGame {

    private final int[][] arrayHistory;
    private int[][] initializedArray;
    private final int sizeRow;
    private final int sizeCol;
    private int sumFlagReally;
    private int sumFlagFake;
    private int sumMines;
    private final int BoardID;
    private final int UserID;
    private final Game game;
    private static Connection con = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;

    public RefundGame(int sizeRow, int sizeCol, int BoardID, int UserID) throws SQLException {
        this.sizeRow = sizeRow;
        this.sizeCol = sizeCol;
        this.BoardID = BoardID;
        this.UserID = UserID;
        this.arrayHistory = new int[this.sizeRow][this.sizeCol];
        for (int i = 0; i < this.sizeRow; i++) {
            for (int j = 0; j < this.sizeCol; j++) {
                //-1 because of my logic
                this.arrayHistory[i][j] = -1;
            }
        }
        this.game = new Game();
        this.game.setBoard(arrayHistory);
        insertToArray();
    }

    public void insertToArray() throws SQLException {
        String username = "daniel";
        String password = "7624";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=MSG";
        con = DriverManager.getConnection(url, username, password);
        String query = "select * from BoardValue where BoardID = ? and UserID = ?";
        ps = con.prepareStatement(query);
        ps.setInt(1, this.BoardID);
        ps.setInt(2, this.UserID);
        rs = ps.executeQuery();
        while (rs.next()) {
            //insert values into the array
            this.arrayHistory[rs.getInt("Rows")][rs.getInt("Columns")] = rs.getInt("ValueMine");
        }
        this.returnArrayReadyForGame();
    }

    /**
     * Initializes an array for the game with the clicks pressed.
     */
    public void returnArrayReadyForGame() {
        for (int i = 0; i < this.sizeRow; i++) {
            for (int j = 0; j < this.sizeCol; j++) {
                //if it's click
                if (arrayHistory[i][j] == 2) {
                    arrayHistory[i][j] = -1;
                    this.game.OpenCell(i, j);
                    continue;
                }
                //if it's a real flag 
                if (arrayHistory[i][j] == -3) {
                    this.sumMines++;
                    this.sumFlagReally++;
                    continue;
                }
                //if it's a mines
                if (arrayHistory[i][j] == 10) {
                    this.sumMines++;
                    continue;
                }
                //if it's a fake flag 
                if (arrayHistory[i][j] == -2) {
                    this.sumFlagFake++;
                    continue;
                }

            }
        }
        this.initializedArray = this.game.getBoard();
        System.out.println(this.sumFlagFake);
    }
    //return arrayBoard saved the previous time

    public int[][] getInitializedArray() {
        return initializedArray;
    }

    public int getSumFlagFake() {
        return sumFlagFake;
    }

    //count flags to send to the class game
    public int getSumFlagReally() {
        return sumFlagReally;
    }

    public void setSumFlagReally(int sumFlagReally) {
        this.sumFlagReally = sumFlagReally;
    }

    //count mines to send to the class game
    public int getSumMines() {
        return sumMines;
    }

    /*public static void main(String[] args)  {
        try {
            RefundGame g = new RefundGame(5, 5, 16, 1);
            int[][] arr1 = g.getInitializedArray();
            for (int i = 0; i < arr1.length; i++) {
                for (int j = 0; j < arr1[i].length; j++) {
                    System.out.print("|" + arr1[i][j] + "|");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }*/
}
