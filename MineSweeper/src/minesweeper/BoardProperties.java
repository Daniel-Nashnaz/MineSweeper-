/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "BoardProperties")
@NamedQueries({
    @NamedQuery(name = "BoardProperties.findAll", query = "SELECT b FROM BoardProperties b"),
    @NamedQuery(name = "BoardProperties.findByBoardID", query = "SELECT b FROM BoardProperties b WHERE b.boardID = :boardID"),
    @NamedQuery(name = "BoardProperties.findBySizeRow", query = "SELECT b FROM BoardProperties b WHERE b.sizeRow = :sizeRow"),
    @NamedQuery(name = "BoardProperties.findBySizeCol", query = "SELECT b FROM BoardProperties b WHERE b.sizeCol = :sizeCol"),
    @NamedQuery(name = "BoardProperties.findByStatus", query = "SELECT b FROM BoardProperties b WHERE b.status = :status"),
    @NamedQuery(name = "BoardProperties.findByDate", query = "SELECT b FROM BoardProperties b WHERE b.Date = :Date")})

public class BoardProperties implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "BoardID")
    private Integer boardID;
    @Basic(optional = false)
    @Column(name = "SizeRow")
    private int sizeRow;
    @Basic(optional = false)
    @Column(name = "SizeCol")
    private int sizeCol;
    @Column(name = "Status")
    private Boolean status;
    @Column(name = "Date")
    private String Date;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "boardID")
    private Collection<BoardValue> boardValueCollection;
    @JoinColumn(name = "UserID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users userID;

    public BoardProperties() {
    }

    public BoardProperties(Integer boardID) {
        this.boardID = boardID;
    }

    public BoardProperties(Integer boardID, int sizeRow, int sizeCol,String date) {
        this.boardID = boardID;
        this.sizeRow = sizeRow;
        this.sizeCol = sizeCol;
        this.Date = date;
    }

    public Integer getBoardID() {
        return boardID;
    }

    public void setBoardID(Integer boardID) {
        this.boardID = boardID;
    }

    public int getSizeRow() {
        return sizeRow;
    }

    public void setSizeRow(int sizeRow) {
        this.sizeRow = sizeRow;
    }

    public int getSizeCol() {
        return sizeCol;
    }

    public void setSizeCol(int sizeCol) {
        this.sizeCol = sizeCol;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
    
    public Collection<BoardValue> getBoardValueCollection() {
        return boardValueCollection;
    }

    public void setBoardValueCollection(Collection<BoardValue> boardValueCollection) {
        this.boardValueCollection = boardValueCollection;
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
        hash += (boardID != null ? boardID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BoardProperties)) {
            return false;
        }
        BoardProperties other = (BoardProperties) object;
        if ((this.boardID == null && other.boardID != null) || (this.boardID != null && !this.boardID.equals(other.boardID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "minesweeper.BoardProperties[ boardID=" + boardID + " ]";
    }
    
}
