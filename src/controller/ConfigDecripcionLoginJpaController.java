/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ConfigLogin;
import entity.CmProfesionales;
import entity.ConfigDecripcionLogin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class ConfigDecripcionLoginJpaController implements Serializable {

    public ConfigDecripcionLoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConfigDecripcionLogin configDecripcionLogin) {
        if (configDecripcionLogin.getCmProfesionalesList() == null) {
            configDecripcionLogin.setCmProfesionalesList(new ArrayList<CmProfesionales>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConfigLogin idLogin = configDecripcionLogin.getIdLogin();
            if (idLogin != null) {
                idLogin = em.getReference(idLogin.getClass(), idLogin.getId());
                configDecripcionLogin.setIdLogin(idLogin);
            }
            List<CmProfesionales> attachedCmProfesionalesList = new ArrayList<CmProfesionales>();
            for (CmProfesionales cmProfesionalesListCmProfesionalesToAttach : configDecripcionLogin.getCmProfesionalesList()) {
                cmProfesionalesListCmProfesionalesToAttach = em.getReference(cmProfesionalesListCmProfesionalesToAttach.getClass(), cmProfesionalesListCmProfesionalesToAttach.getId());
                attachedCmProfesionalesList.add(cmProfesionalesListCmProfesionalesToAttach);
            }
            configDecripcionLogin.setCmProfesionalesList(attachedCmProfesionalesList);
            em.persist(configDecripcionLogin);
            if (idLogin != null) {
                idLogin.getConfigDecripcionLoginList().add(configDecripcionLogin);
                idLogin = em.merge(idLogin);
            }
            for (CmProfesionales cmProfesionalesListCmProfesionales : configDecripcionLogin.getCmProfesionalesList()) {
                ConfigDecripcionLogin oldIdDescripcionLoginOfCmProfesionalesListCmProfesionales = cmProfesionalesListCmProfesionales.getIdDescripcionLogin();
                cmProfesionalesListCmProfesionales.setIdDescripcionLogin(configDecripcionLogin);
                cmProfesionalesListCmProfesionales = em.merge(cmProfesionalesListCmProfesionales);
                if (oldIdDescripcionLoginOfCmProfesionalesListCmProfesionales != null) {
                    oldIdDescripcionLoginOfCmProfesionalesListCmProfesionales.getCmProfesionalesList().remove(cmProfesionalesListCmProfesionales);
                    oldIdDescripcionLoginOfCmProfesionalesListCmProfesionales = em.merge(oldIdDescripcionLoginOfCmProfesionalesListCmProfesionales);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConfigDecripcionLogin configDecripcionLogin) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConfigDecripcionLogin persistentConfigDecripcionLogin = em.find(ConfigDecripcionLogin.class, configDecripcionLogin.getId());
            ConfigLogin idLoginOld = persistentConfigDecripcionLogin.getIdLogin();
            ConfigLogin idLoginNew = configDecripcionLogin.getIdLogin();
            List<CmProfesionales> cmProfesionalesListOld = persistentConfigDecripcionLogin.getCmProfesionalesList();
            List<CmProfesionales> cmProfesionalesListNew = configDecripcionLogin.getCmProfesionalesList();
            List<String> illegalOrphanMessages = null;
            for (CmProfesionales cmProfesionalesListOldCmProfesionales : cmProfesionalesListOld) {
                if (!cmProfesionalesListNew.contains(cmProfesionalesListOldCmProfesionales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CmProfesionales " + cmProfesionalesListOldCmProfesionales + " since its idDescripcionLogin field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idLoginNew != null) {
                idLoginNew = em.getReference(idLoginNew.getClass(), idLoginNew.getId());
                configDecripcionLogin.setIdLogin(idLoginNew);
            }
            List<CmProfesionales> attachedCmProfesionalesListNew = new ArrayList<CmProfesionales>();
            for (CmProfesionales cmProfesionalesListNewCmProfesionalesToAttach : cmProfesionalesListNew) {
                cmProfesionalesListNewCmProfesionalesToAttach = em.getReference(cmProfesionalesListNewCmProfesionalesToAttach.getClass(), cmProfesionalesListNewCmProfesionalesToAttach.getId());
                attachedCmProfesionalesListNew.add(cmProfesionalesListNewCmProfesionalesToAttach);
            }
            cmProfesionalesListNew = attachedCmProfesionalesListNew;
            configDecripcionLogin.setCmProfesionalesList(cmProfesionalesListNew);
            configDecripcionLogin = em.merge(configDecripcionLogin);
            if (idLoginOld != null && !idLoginOld.equals(idLoginNew)) {
                idLoginOld.getConfigDecripcionLoginList().remove(configDecripcionLogin);
                idLoginOld = em.merge(idLoginOld);
            }
            if (idLoginNew != null && !idLoginNew.equals(idLoginOld)) {
                idLoginNew.getConfigDecripcionLoginList().add(configDecripcionLogin);
                idLoginNew = em.merge(idLoginNew);
            }
            for (CmProfesionales cmProfesionalesListNewCmProfesionales : cmProfesionalesListNew) {
                if (!cmProfesionalesListOld.contains(cmProfesionalesListNewCmProfesionales)) {
                    ConfigDecripcionLogin oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales = cmProfesionalesListNewCmProfesionales.getIdDescripcionLogin();
                    cmProfesionalesListNewCmProfesionales.setIdDescripcionLogin(configDecripcionLogin);
                    cmProfesionalesListNewCmProfesionales = em.merge(cmProfesionalesListNewCmProfesionales);
                    if (oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales != null && !oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales.equals(configDecripcionLogin)) {
                        oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales.getCmProfesionalesList().remove(cmProfesionalesListNewCmProfesionales);
                        oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales = em.merge(oldIdDescripcionLoginOfCmProfesionalesListNewCmProfesionales);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configDecripcionLogin.getId();
                if (findConfigDecripcionLogin(id) == null) {
                    throw new NonexistentEntityException("The configDecripcionLogin with id " + id + " no longer exists.");
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
            ConfigDecripcionLogin configDecripcionLogin;
            try {
                configDecripcionLogin = em.getReference(ConfigDecripcionLogin.class, id);
                configDecripcionLogin.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configDecripcionLogin with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CmProfesionales> cmProfesionalesListOrphanCheck = configDecripcionLogin.getCmProfesionalesList();
            for (CmProfesionales cmProfesionalesListOrphanCheckCmProfesionales : cmProfesionalesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ConfigDecripcionLogin (" + configDecripcionLogin + ") cannot be destroyed since the CmProfesionales " + cmProfesionalesListOrphanCheckCmProfesionales + " in its cmProfesionalesList field has a non-nullable idDescripcionLogin field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ConfigLogin idLogin = configDecripcionLogin.getIdLogin();
            if (idLogin != null) {
                idLogin.getConfigDecripcionLoginList().remove(configDecripcionLogin);
                idLogin = em.merge(idLogin);
            }
            em.remove(configDecripcionLogin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConfigDecripcionLogin> findConfigDecripcionLoginEntities() {
        return findConfigDecripcionLoginEntities(true, -1, -1);
    }

    public List<ConfigDecripcionLogin> findConfigDecripcionLoginEntities(int maxResults, int firstResult) {
        return findConfigDecripcionLoginEntities(false, maxResults, firstResult);
    }

    private List<ConfigDecripcionLogin> findConfigDecripcionLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConfigDecripcionLogin.class));
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

    public ConfigDecripcionLogin findConfigDecripcionLogin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConfigDecripcionLogin.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigDecripcionLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConfigDecripcionLogin> rt = cq.from(ConfigDecripcionLogin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
