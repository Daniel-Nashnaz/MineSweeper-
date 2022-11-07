package Save;

import java.text.SimpleDateFormat;
import java.util.Comparator;

public class HistoryTable {

    private Integer id;
    private Integer boardID;
    private String userName;
    private String level;
    private String mode;
    private String date;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");

    public HistoryTable(Integer id, Integer boardID, String userName, String level, String mode, String date) {
        this.id = id;
        this.boardID = boardID;
        this.userName = userName;
        this.level = level;
        this.mode = mode;
        this.date = date;
    }

    public HistoryTable() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBoardID() {
        return boardID;
    }

    public void setBoardID(Integer boardID) {
        this.boardID = boardID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /*Comparator for sorting the list by Student mode*/
    public static Comparator<HistoryTable> modeComparator = new Comparator<HistoryTable>() {

        @Override
        public int compare(HistoryTable s1, HistoryTable s2) {
            String StudentName1 = s1.getMode().toUpperCase();
            String StudentName2 = s2.getMode().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
    /*Comparator for sorting the list by Student level*/
    public static Comparator<HistoryTable> levelComparator = new Comparator<HistoryTable>() {

        @Override
        public int compare(HistoryTable s1, HistoryTable s2) {
            String StudentName1 = s1.getLevel();
            String StudentName2 = s2.getLevel();

            //ascending order
            return StudentName1.compareTo(StudentName2);
            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };
       
    /*Comparator for sorting the list by date*/
    public static Comparator<HistoryTable> dateComparator = (HistoryTable s1, HistoryTable s2)
            -> s1.getDate().compareTo(s2.getDate());
    //For ascending order*/ 
    //return rollno1-rollno2;
    //For descending order*/ 
    //rollno2-rollno1;

    // for those implements Comparable<HistoryTable> 
    /*@Override
    //sorted by alphabetical 
    public int compareTo(HistoryTable s1) {
        return this.getMode().compareTo(s1.getMode());
    }*/
 /*@Override
    //sorted by date.
    public int compareTo(HistoryTable o) {
        return getDate().compareTo(o.getDate());
    }*/

 /*@Override
    //sort by number of ID
    public int compareTo(Object arg0) {
        int compareage=((HistoryTable)arg0).getId();
        //For Ascending order
        return this.id-compareage;
    }*/
    @Override
    public String toString() {
        return "HistoryTable{" + "id = " + id + ", boardID = " + boardID
                + ", userName = " + userName + ", level = " + level + ", mode = " + mode + ", date = " + date + '}';
    }

}
