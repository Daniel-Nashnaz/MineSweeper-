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
public class UsersJpaController implements Serializable {

    public UsersJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Users users) {
        if (users.getBoardValueCollection() == null) {
            users.setBoardValueCollection(new ArrayList<BoardValue>());
        }
        if (users.getBoardPropertiesCollection() == null) {
            users.setBoardPropertiesCollection(new ArrayList<BoardProperties>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<BoardValue> attachedBoardValueCollection = new ArrayList<BoardValue>();
            for (BoardValue boardValueCollectionBoardValueToAttach : users.getBoardValueCollection()) {
                boardValueCollectionBoardValueToAttach = em.getReference(boardValueCollectionBoardValueToAttach.getClass(), boardValueCollectionBoardValueToAttach.getId());
                attachedBoardValueCollection.add(boardValueCollectionBoardValueToAttach);
            }
            users.setBoardValueCollection(attachedBoardValueCollection);
            Collection<BoardProperties> attachedBoardPropertiesCollection = new ArrayList<BoardProperties>();
            for (BoardProperties boardPropertiesCollectionBoardPropertiesToAttach : users.getBoardPropertiesCollection()) {
                boardPropertiesCollectionBoardPropertiesToAttach = em.getReference(boardPropertiesCollectionBoardPropertiesToAttach.getClass(), boardPropertiesCollectionBoardPropertiesToAttach.getBoardID());
                attachedBoardPropertiesCollection.add(boardPropertiesCollectionBoardPropertiesToAttach);
            }
            users.setBoardPropertiesCollection(attachedBoardPropertiesCollection);
            em.persist(users);
            for (BoardValue boardValueCollectionBoardValue : users.getBoardValueCollection()) {
                Users oldUserIDOfBoardValueCollectionBoardValue = boardValueCollectionBoardValue.getUserID();
                boardValueCollectionBoardValue.setUserID(users);
                boardValueCollectionBoardValue = em.merge(boardValueCollectionBoardValue);
                if (oldUserIDOfBoardValueCollectionBoardValue != null) {
                    oldUserIDOfBoardValueCollectionBoardValue.getBoardValueCollection().remove(boardValueCollectionBoardValue);
                    oldUserIDOfBoardValueCollectionBoardValue = em.merge(oldUserIDOfBoardValueCollectionBoardValue);
                }
            }
            for (BoardProperties boardPropertiesCollectionBoardProperties : users.getBoardPropertiesCollection()) {
                Users oldUserIDOfBoardPropertiesCollectionBoardProperties = boardPropertiesCollectionBoardProperties.getUserID();
                boardPropertiesCollectionBoardProperties.setUserID(users);
                boardPropertiesCollectionBoardProperties = em.merge(boardPropertiesCollectionBoardProperties);
                if (oldUserIDOfBoardPropertiesCollectionBoardProperties != null) {
                    oldUserIDOfBoardPropertiesCollectionBoardProperties.getBoardPropertiesCollection().remove(boardPropertiesCollectionBoardProperties);
                    oldUserIDOfBoardPropertiesCollectionBoardProperties = em.merge(oldUserIDOfBoardPropertiesCollectionBoardProperties);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Users users) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Users persistentUsers = em.find(Users.class, users.getId());
            Collection<BoardValue> boardValueCollectionOld = persistentUsers.getBoardValueCollection();
            Collection<BoardValue> boardValueCollectionNew = users.getBoardValueCollection();
            Collection<BoardProperties> boardPropertiesCollectionOld = persistentUsers.getBoardPropertiesCollection();
            Collection<BoardProperties> boardPropertiesCollectionNew = users.getBoardPropertiesCollection();
            List<String> illegalOrphanMessages = null;
            for (BoardValue boardValueCollectionOldBoardValue : boardValueCollectionOld) {
                if (!boardValueCollectionNew.contains(boardValueCollectionOldBoardValue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoardValue " + boardValueCollectionOldBoardValue + " since its userID field is not nullable.");
                }
            }
            for (BoardProperties boardPropertiesCollectionOldBoardProperties : boardPropertiesCollectionOld) {
                if (!boardPropertiesCollectionNew.contains(boardPropertiesCollectionOldBoardProperties)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BoardProperties " + boardPropertiesCollectionOldBoardProperties + " since its userID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<BoardValue> attachedBoardValueCollectionNew = new ArrayList<BoardValue>();
            for (BoardValue boardValueCollectionNewBoardValueToAttach : boardValueCollectionNew) {
                boardValueCollectionNewBoardValueToAttach = em.getReference(boardValueCollectionNewBoardValueToAttach.getClass(), boardValueCollectionNewBoardValueToAttach.getId());
                attachedBoardValueCollectionNew.add(boardValueCollectionNewBoardValueToAttach);
            }
            boardValueCollectionNew = attachedBoardValueCollectionNew;
            users.setBoardValueCollection(boardValueCollectionNew);
            Collection<BoardProperties> attachedBoardPropertiesCollectionNew = new ArrayList<BoardProperties>();
            for (BoardProperties boardPropertiesCollectionNewBoardPropertiesToAttach : boardPropertiesCollectionNew) {
                boardPropertiesCollectionNewBoardPropertiesToAttach = em.getReference(boardPropertiesCollectionNewBoardPropertiesToAttach.getClass(), boardPropertiesCollectionNewBoardPropertiesToAttach.getBoardID());
                attachedBoardPropertiesCollectionNew.add(boardPropertiesCollectionNewBoardPropertiesToAttach);
            }
            boardPropertiesCollectionNew = attachedBoardPropertiesCollectionNew;
            users.setBoardPropertiesCollection(boardPropertiesCollectionNew);
            users = em.merge(users);
            for (BoardValue boardValueCollectionNewBoardValue : boardValueCollectionNew) {
                if (!boardValueCollectionOld.contains(boardValueCollectionNewBoardValue)) {
                    Users oldUserIDOfBoardValueCollectionNewBoardValue = boardValueCollectionNewBoardValue.getUserID();
                    boardValueCollectionNewBoardValue.setUserID(users);
                    boardValueCollectionNewBoardValue = em.merge(boardValueCollectionNewBoardValue);
                    if (oldUserIDOfBoardValueCollectionNewBoardValue != null && !oldUserIDOfBoardValueCollectionNewBoardValue.equals(users)) {
                        oldUserIDOfBoardValueCollectionNewBoardValue.getBoardValueCollection().remove(boardValueCollectionNewBoardValue);
                        oldUserIDOfBoardValueCollectionNewBoardValue = em.merge(oldUserIDOfBoardValueCollectionNewBoardValue);
                    }
                }
            }
            for (BoardProperties boardPropertiesCollectionNewBoardProperties : boardPropertiesCollectionNew) {
                if (!boardPropertiesCollectionOld.contains(boardPropertiesCollectionNewBoardProperties)) {
                    Users oldUserIDOfBoardPropertiesCollectionNewBoardProperties = boardPropertiesCollectionNewBoardProperties.getUserID();
                    boardPropertiesCollectionNewBoardProperties.setUserID(users);
                    boardPropertiesCollectionNewBoardProperties = em.merge(boardPropertiesCollectionNewBoardProperties);
                    if (oldUserIDOfBoardPropertiesCollectionNewBoardProperties != null && !oldUserIDOfBoardPropertiesCollectionNewBoardProperties.equals(users)) {
                        oldUserIDOfBoardPropertiesCollectionNewBoardProperties.getBoardPropertiesCollection().remove(boardPropertiesCollectionNewBoardProperties);
                        oldUserIDOfBoardPropertiesCollectionNewBoardProperties = em.merge(oldUserIDOfBoardPropertiesCollectionNewBoardProperties);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = users.getId();
                if (findUsers(id) == null) {
                    throw new NonexistentEntityException("The users with id " + id + " no longer exists.");
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
            Users users;
            try {
                users = em.getReference(Users.class, id);
                users.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The users with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<BoardValue> boardValueCollectionOrphanCheck = users.getBoardValueCollection();
            for (BoardValue boardValueCollectionOrphanCheckBoardValue : boardValueCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the BoardValue " + boardValueCollectionOrphanCheckBoardValue + " in its boardValueCollection field has a non-nullable userID field.");
            }
            Collection<BoardProperties> boardPropertiesCollectionOrphanCheck = users.getBoardPropertiesCollection();
            for (BoardProperties boardPropertiesCollectionOrphanCheckBoardProperties : boardPropertiesCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Users (" + users + ") cannot be destroyed since the BoardProperties " + boardPropertiesCollectionOrphanCheckBoardProperties + " in its boardPropertiesCollection field has a non-nullable userID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(users);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Users> findUsersEntities() {
        return findUsersEntities(true, -1, -1);
    }

    public List<Users> findUsersEntities(int maxResults, int firstResult) {
        return findUsersEntities(false, maxResults, firstResult);
    }

    private List<Users> findUsersEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Users.class));
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

    public Users findUsers(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Users.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsersCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Users> rt = cq.from(Users.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
