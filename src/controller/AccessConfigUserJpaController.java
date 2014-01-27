/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import entity.AccessConfigUser;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.AccessPerfiles;
import entity.AccessEspeciales;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class AccessConfigUserJpaController implements Serializable {

    public AccessConfigUserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccessConfigUser accessConfigUser) {
        if (accessConfigUser.getAccessEspecialesList() == null) {
            accessConfigUser.setAccessEspecialesList(new ArrayList<AccessEspeciales>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessPerfiles idPerfiles = accessConfigUser.getIdPerfiles();
            if (idPerfiles != null) {
                idPerfiles = em.getReference(idPerfiles.getClass(), idPerfiles.getId());
                accessConfigUser.setIdPerfiles(idPerfiles);
            }
            List<AccessEspeciales> attachedAccessEspecialesList = new ArrayList<AccessEspeciales>();
            for (AccessEspeciales accessEspecialesListAccessEspecialesToAttach : accessConfigUser.getAccessEspecialesList()) {
                accessEspecialesListAccessEspecialesToAttach = em.getReference(accessEspecialesListAccessEspecialesToAttach.getClass(), accessEspecialesListAccessEspecialesToAttach.getId());
                attachedAccessEspecialesList.add(accessEspecialesListAccessEspecialesToAttach);
            }
            accessConfigUser.setAccessEspecialesList(attachedAccessEspecialesList);
            em.persist(accessConfigUser);
            if (idPerfiles != null) {
                idPerfiles.getAccessConfigUserList().add(accessConfigUser);
                idPerfiles = em.merge(idPerfiles);
            }
            for (AccessEspeciales accessEspecialesListAccessEspeciales : accessConfigUser.getAccessEspecialesList()) {
                AccessConfigUser oldIdConfigUsuarioOfAccessEspecialesListAccessEspeciales = accessEspecialesListAccessEspeciales.getIdConfigUsuario();
                accessEspecialesListAccessEspeciales.setIdConfigUsuario(accessConfigUser);
                accessEspecialesListAccessEspeciales = em.merge(accessEspecialesListAccessEspeciales);
                if (oldIdConfigUsuarioOfAccessEspecialesListAccessEspeciales != null) {
                    oldIdConfigUsuarioOfAccessEspecialesListAccessEspeciales.getAccessEspecialesList().remove(accessEspecialesListAccessEspeciales);
                    oldIdConfigUsuarioOfAccessEspecialesListAccessEspeciales = em.merge(oldIdConfigUsuarioOfAccessEspecialesListAccessEspeciales);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccessConfigUser accessConfigUser) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessConfigUser persistentAccessConfigUser = em.find(AccessConfigUser.class, accessConfigUser.getId());
            AccessPerfiles idPerfilesOld = persistentAccessConfigUser.getIdPerfiles();
            AccessPerfiles idPerfilesNew = accessConfigUser.getIdPerfiles();
            List<AccessEspeciales> accessEspecialesListOld = persistentAccessConfigUser.getAccessEspecialesList();
            List<AccessEspeciales> accessEspecialesListNew = accessConfigUser.getAccessEspecialesList();
            List<String> illegalOrphanMessages = null;
            for (AccessEspeciales accessEspecialesListOldAccessEspeciales : accessEspecialesListOld) {
                if (!accessEspecialesListNew.contains(accessEspecialesListOldAccessEspeciales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccessEspeciales " + accessEspecialesListOldAccessEspeciales + " since its idConfigUsuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idPerfilesNew != null) {
                idPerfilesNew = em.getReference(idPerfilesNew.getClass(), idPerfilesNew.getId());
                accessConfigUser.setIdPerfiles(idPerfilesNew);
            }
            List<AccessEspeciales> attachedAccessEspecialesListNew = new ArrayList<AccessEspeciales>();
            for (AccessEspeciales accessEspecialesListNewAccessEspecialesToAttach : accessEspecialesListNew) {
                accessEspecialesListNewAccessEspecialesToAttach = em.getReference(accessEspecialesListNewAccessEspecialesToAttach.getClass(), accessEspecialesListNewAccessEspecialesToAttach.getId());
                attachedAccessEspecialesListNew.add(accessEspecialesListNewAccessEspecialesToAttach);
            }
            accessEspecialesListNew = attachedAccessEspecialesListNew;
            accessConfigUser.setAccessEspecialesList(accessEspecialesListNew);
            accessConfigUser = em.merge(accessConfigUser);
            if (idPerfilesOld != null && !idPerfilesOld.equals(idPerfilesNew)) {
                idPerfilesOld.getAccessConfigUserList().remove(accessConfigUser);
                idPerfilesOld = em.merge(idPerfilesOld);
            }
            if (idPerfilesNew != null && !idPerfilesNew.equals(idPerfilesOld)) {
                idPerfilesNew.getAccessConfigUserList().add(accessConfigUser);
                idPerfilesNew = em.merge(idPerfilesNew);
            }
            for (AccessEspeciales accessEspecialesListNewAccessEspeciales : accessEspecialesListNew) {
                if (!accessEspecialesListOld.contains(accessEspecialesListNewAccessEspeciales)) {
                    AccessConfigUser oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales = accessEspecialesListNewAccessEspeciales.getIdConfigUsuario();
                    accessEspecialesListNewAccessEspeciales.setIdConfigUsuario(accessConfigUser);
                    accessEspecialesListNewAccessEspeciales = em.merge(accessEspecialesListNewAccessEspeciales);
                    if (oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales != null && !oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales.equals(accessConfigUser)) {
                        oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales.getAccessEspecialesList().remove(accessEspecialesListNewAccessEspeciales);
                        oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales = em.merge(oldIdConfigUsuarioOfAccessEspecialesListNewAccessEspeciales);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accessConfigUser.getId();
                if (findAccessConfigUser(id) == null) {
                    throw new NonexistentEntityException("The accessConfigUser with id " + id + " no longer exists.");
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
            AccessConfigUser accessConfigUser;
            try {
                accessConfigUser = em.getReference(AccessConfigUser.class, id);
                accessConfigUser.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accessConfigUser with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccessEspeciales> accessEspecialesListOrphanCheck = accessConfigUser.getAccessEspecialesList();
            for (AccessEspeciales accessEspecialesListOrphanCheckAccessEspeciales : accessEspecialesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AccessConfigUser (" + accessConfigUser + ") cannot be destroyed since the AccessEspeciales " + accessEspecialesListOrphanCheckAccessEspeciales + " in its accessEspecialesList field has a non-nullable idConfigUsuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            AccessPerfiles idPerfiles = accessConfigUser.getIdPerfiles();
            if (idPerfiles != null) {
                idPerfiles.getAccessConfigUserList().remove(accessConfigUser);
                idPerfiles = em.merge(idPerfiles);
            }
            em.remove(accessConfigUser);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccessConfigUser> findAccessConfigUserEntities() {
        return findAccessConfigUserEntities(true, -1, -1);
    }

    public List<AccessConfigUser> findAccessConfigUserEntities(int maxResults, int firstResult) {
        return findAccessConfigUserEntities(false, maxResults, firstResult);
    }

    private List<AccessConfigUser> findAccessConfigUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccessConfigUser.class));
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

    public AccessConfigUser findAccessConfigUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccessConfigUser.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccessConfigUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessConfigUser> rt = cq.from(AccessConfigUser.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
