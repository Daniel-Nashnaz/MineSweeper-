package Save;

import GUI.Graphic;
import javax.persistence.*;

public class BoardProperties1 {

    static EntityManagerFactory factory;
    static EntityManager entityManager;
    static StoredProcedureQuery procedureQuery;

    private final int locationUserID;
    private final int row;
    private final int column;
    private final int mines;
    private Integer BoardID;

    public BoardProperties1(int location, int row, int column, int mines) {
        this.locationUserID = location;
        this.row = row;
        this.column = column;
        this.mines = mines;

    }

    public void insertProperties() throws Exception {
        begin();
        procedureQuery = entityManager.createStoredProcedureQuery("InsertProperties");
        procedureQuery.registerStoredProcedureParameter("UserID", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("SizeRow", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("SizeCol", Integer.class, ParameterMode.IN);
        procedureQuery.registerStoredProcedureParameter("ID", Integer.class, ParameterMode.OUT);
        procedureQuery.setParameter("UserID", this.locationUserID);
        procedureQuery.setParameter("SizeRow", this.row);
        procedureQuery.setParameter("SizeCol", this.column);
        procedureQuery.execute();
        this.BoardID = (Integer) procedureQuery.getOutputParameterValue("ID");
        end();
        //start graphic

        //GraphicsBoard start = new GraphicsBoard(this.row, this.column, this.mines, this.locationUserID, this.BoardID);
        Graphic start = new Graphic(this.row, this.column, this.mines, this.locationUserID, this.BoardID);
    }



    public static void begin() {
        factory = Persistence.createEntityManagerFactory("MineSweeperPU");
        entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
    }

    public static void end() {
        entityManager.getTransaction().commit();
        entityManager.close();
        factory.close();

    }

    @Override
    public String toString() {
        return "BoardProperties1{" + "locationUserID=" + locationUserID + ", row=" + row + ", column=" + column + ", BoardID=" + BoardID + ", mines=" + mines + '}';
    }

}
