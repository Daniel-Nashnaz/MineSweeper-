package Save;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public final class BoardHistory {

    private static Connection con = null;
    private static PreparedStatement ps = null;
    private static ResultSet rs = null;
    private int userID;
    private int BoardID;
    private int SizeRow;
    private int SizeCol;
    private int SumFlag;
    private int[][] arrayHistory;
    private RefundGame refundGame;
    private int sumMine;
    private ArrayList<HistoryTable> tables;
    private String modeGame;
    private final String username = "daniel";
    private final String password = "7624";
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=MSG";
    private final int userId;

    /*public BoardHistory(int userID) throws SQLException {
        this.userID = userID;
        findHistory();
        if (isHaveHistory()) {
            new HomeGame().dispose();
            //call class that return array ready for game 
            this.refundGame = new RefundGame(this.SizeRow, this.SizeCol, this.BoardID, this.userID);
            this.arrayHistory = refundGame.getInitializedArray();
            this.SumFlag = refundGame.getSumFlag();
            this.sumMine = refundGame.getSumMines();
            //GraphicsBoardHistory graphicsBoardHistory = new GraphicsBoardHistory(arrayHistory, this.SumFlag, this.sumMine, this.userID, this.BoardID);
            Graphic graphicsBoardHistory = new Graphic(arrayHistory, this.SumFlag, this.sumMine, this.userID, this.BoardID);
        } else {
            JOptionPane.showMessageDialog(null, "No History", null, JOptionPane.ERROR_MESSAGE);
            //new HomeGame().setVisible(true);
        }

    }
    
    public boolean isHaveHistory() {
        return this.BoardID != 0;
    }

     */
    public BoardHistory(int userId) {
        this.userId = userId;
    }

    /**
     * find history of userID and returns the last game (to do list of games
     * that you can choose)
     *
     * @throws SQLException
     */
    public void findHistory() throws SQLException {
        con = DriverManager.getConnection(url, username, password);
        String query = "select *from BoardProperties\n"
                + "where UserID = ? and [Status] is NULL";
        ps = con.prepareStatement(query);
        ps.setInt(1, this.userID);
        rs = ps.executeQuery();
        while (rs.next()) {
            this.BoardID = rs.getInt("BoardID");
            this.SizeRow = rs.getInt("SizeRow");
            this.SizeCol = rs.getInt("SizeCol");

        }

    }

    public void print() {

        for (int i = 0; i < this.SizeRow; i++) {
            for (int j = 0; j < this.SizeCol; j++) {
                System.out.print("|" + arrayHistory[i][j] + "|");
            }
            System.out.println();
        }
    }

    public ArrayList<HistoryTable> insertToArrList() throws SQLException {
        tables = new ArrayList<>();
        con = DriverManager.getConnection(url, username, password);
        String query = "SELECT U.ID,BP.BoardID,U.Username,BP.SizeRow,BP.[Status],BP.[Date]\n"
                + "FROM BoardProperties BP INNER JOIN\n"
                + "Users U ON BP.UserID = U.ID\n"
                + "WHERE BP.UserID =?";
        ps = con.prepareStatement(query);
        ps.setInt(1, this.userId);
        rs = ps.executeQuery();
        while (rs.next()) {
            this.modeGame = "Loss";
            if (rs.getInt("Status") == 1) {
                this.modeGame = "Win";
            }
            if (rs.getString("Status") == null) {
                this.modeGame = "InTheMiddle";
            }
            tables.add(new HistoryTable(rs.getInt("ID"), rs.getInt("BoardID"), rs.getString("Username"),
                    retLevels(rs.getInt("SizeRow")), this.modeGame, rs.getString("Date")));
        }//while

        //sort arrayList 
        //Collections.sort(tables ,HistoryTable.dateComparator);
        
        if (tables.isEmpty()) {
            throw new NullPointerException("No game history");
        }
        return tables;
    }

    private String retLevels(int number) {
        if (number == 5) {
            return "Easy";
        }
        if (number == 10) {
            return "Mormal";
        }
        return "Hard";
    }

    @Override
    public String toString() {
        return "BoardHistory{" + "userID=" + userID + ", BoardID=" + BoardID + ", SizeRow=" + SizeRow + ", SizeCol=" + SizeCol + '}';
    }

}
