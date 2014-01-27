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
import entity.AccessPerfiles;
import entity.AccessRoles;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class AccessRolesJpaController implements Serializable {

    public AccessRolesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccessRoles accessRoles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessPerfiles idPerfil = accessRoles.getIdPerfil();
            if (idPerfil != null) {
                idPerfil = em.getReference(idPerfil.getClass(), idPerfil.getId());
                accessRoles.setIdPerfil(idPerfil);
            }
            em.persist(accessRoles);
            if (idPerfil != null) {
                idPerfil.getAccessRolesList().add(accessRoles);
                idPerfil = em.merge(idPerfil);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccessRoles accessRoles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessRoles persistentAccessRoles = em.find(AccessRoles.class, accessRoles.getId());
            AccessPerfiles idPerfilOld = persistentAccessRoles.getIdPerfil();
            AccessPerfiles idPerfilNew = accessRoles.getIdPerfil();
            if (idPerfilNew != null) {
                idPerfilNew = em.getReference(idPerfilNew.getClass(), idPerfilNew.getId());
                accessRoles.setIdPerfil(idPerfilNew);
            }
            accessRoles = em.merge(accessRoles);
            if (idPerfilOld != null && !idPerfilOld.equals(idPerfilNew)) {
                idPerfilOld.getAccessRolesList().remove(accessRoles);
                idPerfilOld = em.merge(idPerfilOld);
            }
            if (idPerfilNew != null && !idPerfilNew.equals(idPerfilOld)) {
                idPerfilNew.getAccessRolesList().add(accessRoles);
                idPerfilNew = em.merge(idPerfilNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accessRoles.getId();
                if (findAccessRoles(id) == null) {
                    throw new NonexistentEntityException("The accessRoles with id " + id + " no longer exists.");
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
            AccessRoles accessRoles;
            try {
                accessRoles = em.getReference(AccessRoles.class, id);
                accessRoles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accessRoles with id " + id + " no longer exists.", enfe);
            }
            AccessPerfiles idPerfil = accessRoles.getIdPerfil();
            if (idPerfil != null) {
                idPerfil.getAccessRolesList().remove(accessRoles);
                idPerfil = em.merge(idPerfil);
            }
            em.remove(accessRoles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccessRoles> findAccessRolesEntities() {
        return findAccessRolesEntities(true, -1, -1);
    }

    public List<AccessRoles> findAccessRolesEntities(int maxResults, int firstResult) {
        return findAccessRolesEntities(false, maxResults, firstResult);
    }

    private List<AccessRoles> findAccessRolesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccessRoles.class));
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

    public AccessRoles findAccessRoles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccessRoles.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccessRolesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessRoles> rt = cq.from(AccessRoles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
