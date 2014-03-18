/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import entity.StaticCie10;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Alvaro Monsalve
 */
public class StaticCie10JpaController implements Serializable {

    public StaticCie10JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StaticCie10 staticCie10) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(staticCie10);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StaticCie10 staticCie10) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            staticCie10 = em.merge(staticCie10);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = staticCie10.getId();
                if (findStaticCie10(id) == null) {
                    throw new NonexistentEntityException("The staticCie10 with id " + id + " no longer exists.");
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
            StaticCie10 staticCie10;
            try {
                staticCie10 = em.getReference(StaticCie10.class, id);
                staticCie10.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The staticCie10 with id " + id + " no longer exists.", enfe);
            }
            em.remove(staticCie10);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StaticCie10> findStaticCie10Entities() {
        return findStaticCie10Entities(true, -1, -1);
    }

    public List<StaticCie10> findStaticCie10Entities(int maxResults, int firstResult) {
        return findStaticCie10Entities(false, maxResults, firstResult);
    }

    private List<StaticCie10> findStaticCie10Entities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StaticCie10.class));
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

    public StaticCie10 findStaticCie10(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StaticCie10.class, id);
        } finally {
            em.close();
        }
    }

    public int getStaticCie10Count() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StaticCie10> rt = cq.from(StaticCie10.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
