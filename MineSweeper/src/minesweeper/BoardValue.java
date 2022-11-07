package minesweeper;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "BoardValue")
@NamedQueries({
    @NamedQuery(name = "BoardValue.findAll", query = "SELECT b FROM BoardValue b"),
    @NamedQuery(name = "BoardValue.findById", query = "SELECT b FROM BoardValue b WHERE b.id = :id"),
    @NamedQuery(name = "BoardValue.findByRows", query = "SELECT b FROM BoardValue b WHERE b.rows = :rows"),
    @NamedQuery(name = "BoardValue.findByColumns", query = "SELECT b FROM BoardValue b WHERE b.columns = :columns"),
    //@NamedQuery(name = "BoardValue.findByValueFlag", query = "SELECT b FROM BoardValue b WHERE b.valueFlag = :valueFlag"),
    @NamedQuery(name = "BoardValue.findByValueMine", query = "SELECT b FROM BoardValue b WHERE b.valueMine = :valueMine")})
public class BoardValue implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Rows")
    private int rows;
    @Basic(optional = false)
    @Column(name = "Columns")
    private int columns;
    @Basic(optional = false)
    @Column(name = "ValueMine")
    private int valueMine;
    @JoinColumn(name = "BoardID", referencedColumnName = "BoardID")
    @ManyToOne(optional = false)
    private BoardProperties boardID;
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users userID;

    public BoardValue() {
    }

    public BoardValue(Integer id) {
        this.id = id;
    }

    public BoardValue(Integer id, int rows, int columns,  int valueMine) {
        this.id = id;
        this.rows = rows;
        this.columns = columns;
        this.valueMine = valueMine;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getValueMine() {
        return valueMine;
    }

    public void setValueMine(int valueMine) {
        this.valueMine = valueMine;
    }

    public BoardProperties getBoardID() {
        return boardID;
    }

    public void setBoardID(BoardProperties boardID) {
        this.boardID = boardID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoardValue)) {
            return false;
        }
        BoardValue other = (BoardValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }




    
}
