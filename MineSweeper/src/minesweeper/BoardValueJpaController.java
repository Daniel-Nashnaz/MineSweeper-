/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import minesweeper.exceptions.NonexistentEntityException;

/**
 *
 * @author Daniel
 */
public class BoardValueJpaController implements Serializable {

    public BoardValueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BoardValue boardValue) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardProperties boardID = boardValue.getBoardID();
            if (boardID != null) {
                boardID = em.getReference(boardID.getClass(), boardID.getBoardID());
                boardValue.setBoardID(boardID);
            }
            Users userID = boardValue.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getId());
                boardValue.setUserID(userID);
            }
            em.persist(boardValue);
            if (boardID != null) {
                boardID.getBoardValueCollection().add(boardValue);
                boardID = em.merge(boardID);
            }
            if (userID != null) {
                userID.getBoardValueCollection().add(boardValue);
                userID = em.merge(userID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BoardValue boardValue) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardValue persistentBoardValue = em.find(BoardValue.class, boardValue.getId());
            BoardProperties boardIDOld = persistentBoardValue.getBoardID();
            BoardProperties boardIDNew = boardValue.getBoardID();
            Users userIDOld = persistentBoardValue.getUserID();
            Users userIDNew = boardValue.getUserID();
            if (boardIDNew != null) {
                boardIDNew = em.getReference(boardIDNew.getClass(), boardIDNew.getBoardID());
                boardValue.setBoardID(boardIDNew);
            }
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getId());
                boardValue.setUserID(userIDNew);
            }
            boardValue = em.merge(boardValue);
            if (boardIDOld != null && !boardIDOld.equals(boardIDNew)) {
                boardIDOld.getBoardValueCollection().remove(boardValue);
                boardIDOld = em.merge(boardIDOld);
            }
            if (boardIDNew != null && !boardIDNew.equals(boardIDOld)) {
                boardIDNew.getBoardValueCollection().add(boardValue);
                boardIDNew = em.merge(boardIDNew);
            }
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getBoardValueCollection().remove(boardValue);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getBoardValueCollection().add(boardValue);
                userIDNew = em.merge(userIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boardValue.getId();
                if (findBoardValue(id) == null) {
                    throw new NonexistentEntityException("The boardValue with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardValue boardValue;
            try {
                boardValue = em.getReference(BoardValue.class, id);
                boardValue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boardValue with id " + id + " no longer exists.", enfe);
            }
            BoardProperties boardID = boardValue.getBoardID();
            if (boardID != null) {
                boardID.getBoardValueCollection().remove(boardValue);
                boardID = em.merge(boardID);
            }
            Users userID = boardValue.getUserID();
            if (userID != null) {
                userID.getBoardValueCollection().remove(boardValue);
                userID = em.merge(userID);
            }
            em.remove(boardValue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BoardValue> findBoardValueEntities() {
        return findBoardValueEntities(true, -1, -1);
    }

    public List<BoardValue> findBoardValueEntities(int maxResults, int firstResult) {
        return findBoardValueEntities(false, maxResults, firstResult);
    }

    private List<BoardValue> findBoardValueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BoardValue.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public BoardValue findBoardValue(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BoardValue.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoardValueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BoardValue> rt = cq.from(BoardValue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
