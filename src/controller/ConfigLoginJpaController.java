/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ConfigDecripcionLogin;
import entity.ConfigLogin;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class ConfigLoginJpaController implements Serializable {

    public ConfigLoginJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ConfigLogin configLogin) {
        if (configLogin.getConfigDecripcionLoginList() == null) {
            configLogin.setConfigDecripcionLoginList(new ArrayList<ConfigDecripcionLogin>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ConfigDecripcionLogin> attachedConfigDecripcionLoginList = new ArrayList<ConfigDecripcionLogin>();
            for (ConfigDecripcionLogin configDecripcionLoginListConfigDecripcionLoginToAttach : configLogin.getConfigDecripcionLoginList()) {
                configDecripcionLoginListConfigDecripcionLoginToAttach = em.getReference(configDecripcionLoginListConfigDecripcionLoginToAttach.getClass(), configDecripcionLoginListConfigDecripcionLoginToAttach.getId());
                attachedConfigDecripcionLoginList.add(configDecripcionLoginListConfigDecripcionLoginToAttach);
            }
            configLogin.setConfigDecripcionLoginList(attachedConfigDecripcionLoginList);
            em.persist(configLogin);
            for (ConfigDecripcionLogin configDecripcionLoginListConfigDecripcionLogin : configLogin.getConfigDecripcionLoginList()) {
                ConfigLogin oldIdLoginOfConfigDecripcionLoginListConfigDecripcionLogin = configDecripcionLoginListConfigDecripcionLogin.getIdLogin();
                configDecripcionLoginListConfigDecripcionLogin.setIdLogin(configLogin);
                configDecripcionLoginListConfigDecripcionLogin = em.merge(configDecripcionLoginListConfigDecripcionLogin);
                if (oldIdLoginOfConfigDecripcionLoginListConfigDecripcionLogin != null) {
                    oldIdLoginOfConfigDecripcionLoginListConfigDecripcionLogin.getConfigDecripcionLoginList().remove(configDecripcionLoginListConfigDecripcionLogin);
                    oldIdLoginOfConfigDecripcionLoginListConfigDecripcionLogin = em.merge(oldIdLoginOfConfigDecripcionLoginListConfigDecripcionLogin);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ConfigLogin configLogin) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConfigLogin persistentConfigLogin = em.find(ConfigLogin.class, configLogin.getId());
            List<ConfigDecripcionLogin> configDecripcionLoginListOld = persistentConfigLogin.getConfigDecripcionLoginList();
            List<ConfigDecripcionLogin> configDecripcionLoginListNew = configLogin.getConfigDecripcionLoginList();
            List<ConfigDecripcionLogin> attachedConfigDecripcionLoginListNew = new ArrayList<ConfigDecripcionLogin>();
            for (ConfigDecripcionLogin configDecripcionLoginListNewConfigDecripcionLoginToAttach : configDecripcionLoginListNew) {
                configDecripcionLoginListNewConfigDecripcionLoginToAttach = em.getReference(configDecripcionLoginListNewConfigDecripcionLoginToAttach.getClass(), configDecripcionLoginListNewConfigDecripcionLoginToAttach.getId());
                attachedConfigDecripcionLoginListNew.add(configDecripcionLoginListNewConfigDecripcionLoginToAttach);
            }
            configDecripcionLoginListNew = attachedConfigDecripcionLoginListNew;
            configLogin.setConfigDecripcionLoginList(configDecripcionLoginListNew);
            configLogin = em.merge(configLogin);
            for (ConfigDecripcionLogin configDecripcionLoginListOldConfigDecripcionLogin : configDecripcionLoginListOld) {
                if (!configDecripcionLoginListNew.contains(configDecripcionLoginListOldConfigDecripcionLogin)) {
                    configDecripcionLoginListOldConfigDecripcionLogin.setIdLogin(null);
                    configDecripcionLoginListOldConfigDecripcionLogin = em.merge(configDecripcionLoginListOldConfigDecripcionLogin);
                }
            }
            for (ConfigDecripcionLogin configDecripcionLoginListNewConfigDecripcionLogin : configDecripcionLoginListNew) {
                if (!configDecripcionLoginListOld.contains(configDecripcionLoginListNewConfigDecripcionLogin)) {
                    ConfigLogin oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin = configDecripcionLoginListNewConfigDecripcionLogin.getIdLogin();
                    configDecripcionLoginListNewConfigDecripcionLogin.setIdLogin(configLogin);
                    configDecripcionLoginListNewConfigDecripcionLogin = em.merge(configDecripcionLoginListNewConfigDecripcionLogin);
                    if (oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin != null && !oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin.equals(configLogin)) {
                        oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin.getConfigDecripcionLoginList().remove(configDecripcionLoginListNewConfigDecripcionLogin);
                        oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin = em.merge(oldIdLoginOfConfigDecripcionLoginListNewConfigDecripcionLogin);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = configLogin.getId();
                if (findConfigLogin(id) == null) {
                    throw new NonexistentEntityException("The configLogin with id " + id + " no longer exists.");
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
            ConfigLogin configLogin;
            try {
                configLogin = em.getReference(ConfigLogin.class, id);
                configLogin.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The configLogin with id " + id + " no longer exists.", enfe);
            }
            List<ConfigDecripcionLogin> configDecripcionLoginList = configLogin.getConfigDecripcionLoginList();
            for (ConfigDecripcionLogin configDecripcionLoginListConfigDecripcionLogin : configDecripcionLoginList) {
                configDecripcionLoginListConfigDecripcionLogin.setIdLogin(null);
                configDecripcionLoginListConfigDecripcionLogin = em.merge(configDecripcionLoginListConfigDecripcionLogin);
            }
            em.remove(configLogin);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ConfigLogin> findConfigLoginEntities() {
        return findConfigLoginEntities(true, -1, -1);
    }

    public List<ConfigLogin> findConfigLoginEntities(int maxResults, int firstResult) {
        return findConfigLoginEntities(false, maxResults, firstResult);
    }

    private List<ConfigLogin> findConfigLoginEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ConfigLogin.class));
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

    public ConfigLogin findConfigLogin(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ConfigLogin.class, id);
        } finally {
            em.close();
        }
    }

    public int getConfigLoginCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ConfigLogin> rt = cq.from(ConfigLogin.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
