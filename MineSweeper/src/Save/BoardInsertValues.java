package Save;

import static Save.BoardProperties1.begin;
import static Save.BoardProperties1.end;
import static Save.BoardProperties1.entityManager;
import static Save.BoardProperties1.factory;
import static Save.BoardProperties1.procedureQuery;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.ParameterMode;
import javax.swing.JOptionPane;
import minesweeper.BoardProperties;
import minesweeper.BoardPropertiesJpaController;
import minesweeper.exceptions.NonexistentEntityException;

public class BoardInsertValues {

    private int[][] save;
    private int valueID;
    private BoardProperties1 bp;
    private final int locationUserID;
    private final int BoardID;

    public BoardInsertValues(int locationUserID, int BoardID) {
        this.locationUserID = locationUserID;
        this.BoardID = BoardID;
    }

    public void insertMineOrClick(int i, int j, int valueMinesOrClick) {

        // save[i][j] = value;
        // 1 = mines 
        // 2 = click
        // 0 = have flag
        begin();
        procedureQuery = entityManager.createStoredProcedureQuery("InsertValues");
        procedureQuery.registerStoredProcedureParameter("BoardID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("UserID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("Rows", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("Columns", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("ValueMine", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.OUT);
        procedureQuery.setParameter("BoardID", this.BoardID);
        procedureQuery.setParameter("UserID", this.locationUserID);
        procedureQuery.setParameter("Rows", i);
        procedureQuery.setParameter("Columns", j);
        procedureQuery.setParameter("ValueMine", valueMinesOrClick);
        procedureQuery.execute();
        //return ID from table 
        this.valueID = (Integer) procedureQuery.getOutputParameterValue("ID");
        end();
    }

    public void print() {
        for (int i = 0; i < save.length; i++) {
            for (int j = 0; j < save[i].length; j++) {
                System.out.print(save[i][j] + " ");
            }
            System.out.println();
        }

    }

    public void deleteFlag(int i, int j) {
        begin();
        procedureQuery = entityManager.createStoredProcedureQuery("DeleteFlag");
        procedureQuery.registerStoredProcedureParameter("BoardID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("indexI", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("indexJ", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("ret", Integer.class, ParameterMode.OUT);
        procedureQuery.setParameter("BoardID", this.BoardID);
        procedureQuery.setParameter("indexI", i);
        procedureQuery.setParameter("indexJ", j);
        procedureQuery.execute();
        Integer ret = (Integer) procedureQuery.getOutputParameterValue("ret");
        //System.out.println(ret);
        end();
    }

    public void deleteSaveIfWinORLast() {
        begin();
        procedureQuery = entityManager.createStoredProcedureQuery("DeleteSavesIfWinORLast");
        procedureQuery.registerStoredProcedureParameter("BoardID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("UserID", Integer.class, ParameterMode.IN);
        procedureQuery.setParameter("BoardID", this.BoardID);
        procedureQuery.setParameter("UserID", this.locationUserID);
        procedureQuery.execute();
        end();
    }

    public void deleteProperties() {
        begin();
        procedureQuery = entityManager.createStoredProcedureQuery("DeleteProperties");
        procedureQuery.registerStoredProcedureParameter("UserID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("BoardID", Integer.class, ParameterMode.IN);
        procedureQuery.setParameter("UserID", this.locationUserID);
        procedureQuery.setParameter("BoardID", this.BoardID);
        procedureQuery.execute();
        end();
    }

    public void winOrlost(boolean status) {
        try {
            begin();
            BoardProperties finish = new BoardProperties();
            BoardPropertiesJpaController bpjc = new BoardPropertiesJpaController(factory);
            finish = bpjc.findBoardProperties(this.BoardID);
            finish.setStatus(status);
            bpjc.edit(finish);
            end();
        } catch (NonexistentEntityException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(BoardInsertValues.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
