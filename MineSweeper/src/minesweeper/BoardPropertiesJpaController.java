/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import minesweeper.exceptions.IllegalOrphanException;
import minesweeper.exceptions.NonexistentEntityException;

/**
 *
 * @author Daniel
 */
public class BoardPropertiesJpaController implements Serializable {

    public BoardPropertiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BoardProperties boardProperties) {
        if (boardProperties.getBoardValueCollection() == null) {
            boardProperties.setBoardValueCollection(new ArrayList<BoardValue>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users userID = boardProperties.getUserID();
            if (userID != null) {
                userID = em.getReference(userID.getClass(), userID.getId());
                boardProperties.setUserID(userID);
            }
            Collection<BoardValue> attachedBoardValueCollection = new ArrayList<BoardValue>();
            for (BoardValue boardValueCollectionBoardValueToAttach : boardProperties.getBoardValueCollection()) {
                boardValueCollectionBoardValueToAttach = em.getReference(boardValueCollectionBoardValueToAttach.getClass(), boardValueCollectionBoardValueToAttach.getId());
                attachedBoardValueCollection.add(boardValueCollectionBoardValueToAttach);
            }
            boardProperties.setBoardValueCollection(attachedBoardValueCollection);
            em.persist(boardProperties);
            if (userID != null) {
                userID.getBoardPropertiesCollection().add(boardProperties);
                userID = em.merge(userID);
            }
            for (BoardValue boardValueCollectionBoardValue : boardProperties.getBoardValueCollection()) {
                BoardProperties oldBoardIDOfBoardValueCollectionBoardValue = boardValueCollectionBoardValue.getBoardID();
                boardValueCollectionBoardValue.setBoardID(boardProperties);
                boardValueCollectionBoardValue = em.merge(boardValueCollectionBoardValue);
                if (oldBoardIDOfBoardValueCollectionBoardValue != null) {
                    oldBoardIDOfBoardValueCollectionBoardValue.getBoardValueCollection().remove(boardValueCollectionBoardValue);
                    oldBoardIDOfBoardValueCollectionBoardValue = em.merge(oldBoardIDOfBoardValueCollectionBoardValue);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BoardProperties boardProperties) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardProperties persistentBoardProperties = em.find(BoardProperties.class, boardProperties.getBoardID());
            Users userIDOld = persistentBoardProperties.getUserID();
            Users userIDNew = boardProperties.getUserID();
            Collection<BoardValue> boardValueCollectionOld = persistentBoardProperties.getBoardValueCollection();
            Collection<BoardValue> boardValueCollectionNew = boardProperties.getBoardValueCollection();
            List<String> illegalOrphanMessages = null;
            for (BoardValue boardValueCollectionOldBoardValue : boardValueCollectionOld) {
                if (!boardValueCollectionNew.contains(boardValueCollectionOldBoardValue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoardValue " + boardValueCollectionOldBoardValue + " since its boardID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (userIDNew != null) {
                userIDNew = em.getReference(userIDNew.getClass(), userIDNew.getId());
                boardProperties.setUserID(userIDNew);
            }
            Collection<BoardValue> attachedBoardValueCollectionNew = new ArrayList<BoardValue>();
            for (BoardValue boardValueCollectionNewBoardValueToAttach : boardValueCollectionNew) {
                boardValueCollectionNewBoardValueToAttach = em.getReference(boardValueCollectionNewBoardValueToAttach.getClass(), boardValueCollectionNewBoardValueToAttach.getId());
                attachedBoardValueCollectionNew.add(boardValueCollectionNewBoardValueToAttach);
            }
            boardValueCollectionNew = attachedBoardValueCollectionNew;
            boardProperties.setBoardValueCollection(boardValueCollectionNew);
            boardProperties = em.merge(boardProperties);
            if (userIDOld != null && !userIDOld.equals(userIDNew)) {
                userIDOld.getBoardPropertiesCollection().remove(boardProperties);
                userIDOld = em.merge(userIDOld);
            }
            if (userIDNew != null && !userIDNew.equals(userIDOld)) {
                userIDNew.getBoardPropertiesCollection().add(boardProperties);
                userIDNew = em.merge(userIDNew);
            }
            for (BoardValue boardValueCollectionNewBoardValue : boardValueCollectionNew) {
                if (!boardValueCollectionOld.contains(boardValueCollectionNewBoardValue)) {
                    BoardProperties oldBoardIDOfBoardValueCollectionNewBoardValue = boardValueCollectionNewBoardValue.getBoardID();
                    boardValueCollectionNewBoardValue.setBoardID(boardProperties);
                    boardValueCollectionNewBoardValue = em.merge(boardValueCollectionNewBoardValue);
                    if (oldBoardIDOfBoardValueCollectionNewBoardValue != null && !oldBoardIDOfBoardValueCollectionNewBoardValue.equals(boardProperties)) {
                        oldBoardIDOfBoardValueCollectionNewBoardValue.getBoardValueCollection().remove(boardValueCollectionNewBoardValue);
                        oldBoardIDOfBoardValueCollectionNewBoardValue = em.merge(oldBoardIDOfBoardValueCollectionNewBoardValue);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = boardProperties.getBoardID();
                if (findBoardProperties(id) == null) {
                    throw new NonexistentEntityException("The boardProperties with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BoardProperties boardProperties;
            try {
                boardProperties = em.getReference(BoardProperties.class, id);
                boardProperties.getBoardID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The boardProperties with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<BoardValue> boardValueCollectionOrphanCheck = boardProperties.getBoardValueCollection();
            for (BoardValue boardValueCollectionOrphanCheckBoardValue : boardValueCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BoardProperties (" + boardProperties + ") cannot be destroyed since the BoardValue " + boardValueCollectionOrphanCheckBoardValue + " in its boardValueCollection field has a non-nullable boardID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Users userID = boardProperties.getUserID();
            if (userID != null) {
                userID.getBoardPropertiesCollection().remove(boardProperties);
                userID = em.merge(userID);
            }
            em.remove(boardProperties);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BoardProperties> findBoardPropertiesEntities() {
        return findBoardPropertiesEntities(true, -1, -1);
    }

    public List<BoardProperties> findBoardPropertiesEntities(int maxResults, int firstResult) {
        return findBoardPropertiesEntities(false, maxResults, firstResult);
    }

    private List<BoardProperties> findBoardPropertiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BoardProperties.class));
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

    public BoardProperties findBoardProperties(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BoardProperties.class, id);
        } finally {
            em.close();
        }
    }

    public int getBoardPropertiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BoardProperties> rt = cq.from(BoardProperties.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
