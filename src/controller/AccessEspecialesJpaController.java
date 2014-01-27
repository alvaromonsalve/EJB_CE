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
import entity.AccessConfigUser;
import entity.AccessEspeciales;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class AccessEspecialesJpaController implements Serializable {

    public AccessEspecialesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccessEspeciales accessEspeciales) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessConfigUser idConfigUsuario = accessEspeciales.getIdConfigUsuario();
            if (idConfigUsuario != null) {
                idConfigUsuario = em.getReference(idConfigUsuario.getClass(), idConfigUsuario.getId());
                accessEspeciales.setIdConfigUsuario(idConfigUsuario);
            }
            em.persist(accessEspeciales);
            if (idConfigUsuario != null) {
                idConfigUsuario.getAccessEspecialesList().add(accessEspeciales);
                idConfigUsuario = em.merge(idConfigUsuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccessEspeciales accessEspeciales) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessEspeciales persistentAccessEspeciales = em.find(AccessEspeciales.class, accessEspeciales.getId());
            AccessConfigUser idConfigUsuarioOld = persistentAccessEspeciales.getIdConfigUsuario();
            AccessConfigUser idConfigUsuarioNew = accessEspeciales.getIdConfigUsuario();
            if (idConfigUsuarioNew != null) {
                idConfigUsuarioNew = em.getReference(idConfigUsuarioNew.getClass(), idConfigUsuarioNew.getId());
                accessEspeciales.setIdConfigUsuario(idConfigUsuarioNew);
            }
            accessEspeciales = em.merge(accessEspeciales);
            if (idConfigUsuarioOld != null && !idConfigUsuarioOld.equals(idConfigUsuarioNew)) {
                idConfigUsuarioOld.getAccessEspecialesList().remove(accessEspeciales);
                idConfigUsuarioOld = em.merge(idConfigUsuarioOld);
            }
            if (idConfigUsuarioNew != null && !idConfigUsuarioNew.equals(idConfigUsuarioOld)) {
                idConfigUsuarioNew.getAccessEspecialesList().add(accessEspeciales);
                idConfigUsuarioNew = em.merge(idConfigUsuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accessEspeciales.getId();
                if (findAccessEspeciales(id) == null) {
                    throw new NonexistentEntityException("The accessEspeciales with id " + id + " no longer exists.");
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
            AccessEspeciales accessEspeciales;
            try {
                accessEspeciales = em.getReference(AccessEspeciales.class, id);
                accessEspeciales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accessEspeciales with id " + id + " no longer exists.", enfe);
            }
            AccessConfigUser idConfigUsuario = accessEspeciales.getIdConfigUsuario();
            if (idConfigUsuario != null) {
                idConfigUsuario.getAccessEspecialesList().remove(accessEspeciales);
                idConfigUsuario = em.merge(idConfigUsuario);
            }
            em.remove(accessEspeciales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccessEspeciales> findAccessEspecialesEntities() {
        return findAccessEspecialesEntities(true, -1, -1);
    }

    public List<AccessEspeciales> findAccessEspecialesEntities(int maxResults, int firstResult) {
        return findAccessEspecialesEntities(false, maxResults, firstResult);
    }

    private List<AccessEspeciales> findAccessEspecialesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccessEspeciales.class));
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

    public AccessEspeciales findAccessEspeciales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccessEspeciales.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccessEspecialesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessEspeciales> rt = cq.from(AccessEspeciales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
