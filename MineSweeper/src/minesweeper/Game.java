package minesweeper;

import java.security.SecureRandom;
import java.util.Random;

public class Game {

    //variables
    private int rowCount;
    private int colCount;
    private int minesCount;
    private int[][] Board;
    private int flag;
    private int sumFlag;
    private boolean chick;
    private Random r = new SecureRandom();

    //Getter&Setter
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public void setMinesCount(int minesCount) {
        this.minesCount = minesCount;
    }

    public int[][] getBoard() {
        return this.Board;
    }

    public void setBoard(int[][] Board) {
        this.Board = Board;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSumFlag() {
        return sumFlag;
    }

    public void setSumFlag(int sumFlag) {
        this.sumFlag = sumFlag;
    }

    public int getLocationBy(int i, int j) {
        return Board[i][j];
    }

    /**
     * Constructor and Random
     *
     * @param rowCount -number of Rows
     * @param colCount -number of Columns
     * @param minesCount -number of Mines
     */
    public Game(int rowCount, int colCount, int minesCount) {

        this.minesCount = minesCount;
        this.colCount = colCount;
        this.rowCount = rowCount;
        Board = new int[rowCount][colCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < colCount; j++) {
                Board[i][j] = -1;

            }
        }
        for (int i = 0; i < minesCount; i++) {

            int x = r.nextInt(this.rowCount);//(int) (Math.random() * this.rowCount);
            int y = r.nextInt(this.colCount);
            if (x >= this.rowCount || y > this.colCount) {
                System.err.println("error");
            }

            if (Board[x][y] == 10) {
                i = i - 1;
                continue;
            }
            Board[x][y] = 10;
        }
    }

    public Game() {

    }

    /**
     * Search for mines around the location
     *
     * @param i
     * @param j
     * @return
     */
    public int BoomAround(int i, int j) {

        int count = 0;
        for (int r = i - 1; r <= i + 1; r++) {
            for (int c = j - 1; c <= j + 1; c++) {

                if (Limits(r, c) == 1) {

                    if (Boom(Board[r][c]) == true) {
                        count++;
                    }

                }

            }
        }

        return count;

    }

    /**
     * Checking domains
     *
     * @param i
     * @param j
     * @return
     */
    public int Limits(int i, int j) {

        if (i < 0 || i > Board.length - 1) {
            return 0;
        }
        if (j < 0 || j > Board[i].length - 1) {
            return 0;
        }

        return 1;

    }

    public boolean isBoom(int i, int j) {
        //Are you on a minefield?
        if (Board[i][j] == 10 || Board[i][j] == -3) {
            return true;
        }
        return false;

    }

    public boolean Boom(int Location) {
        //Are you on a minefield?
        if (Location == 10 || Location == -3) {
            return true;
        }
        return false;

    }

    // Opens cells if there are no mines around
    public void OpenCell(int i, int j) {
        int count = 0;
        if (Board[i][j] != -1) {
            return;
        }
        count = BoomAround(i, j);
        if (count != 0) {
            Board[i][j] = count;
            return;
        }
        Board[i][j] = 0;
        for (int r = i - 1; r <= i + 1; r++) {
            for (int c = j - 1; c <= j + 1; c++) {

                if (Limits(r, c) == 0) {
                    continue;
                }
                OpenCell(r, c);//Reads again
            }

        }

    }

    public boolean Win() {
        last();
        return chick || flag == minesCount;
    }

    public void last() {
        chick = true;
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColCount(); j++) {
                if (Board[i][j] == -1) {
                    chick = false;
                }

            }
        }
    }

    public boolean flag(int row, int col) {
        this.sumFlag++;
        if (Boom(Board[row][col]) == true) {
            //if real mines, count
            Board[row][col] = -3;
            flag++;
            return true;
        } else {
            //if not mines in location
            Board[row][col] = -2;
            return false;
        }

    }

    public void removeFlag(int i, int j) {
        if (Board[i][j] == -2) {
            this.sumFlag--;
            //fake mine os ckeck mineAround
            //if ((Board[i][j] = BoomAround(i, j)) == 0) {
                Board[i][j] = -1;
            System.out.println(i+" " +j+" "+Board[i][j]);
           // }
        }
        if (Board[i][j] == -3) {
            this.sumFlag--;
            //real mine os put 10
            Board[i][j] = 10;
            flag--;

        }

    }

}
