/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import entity.CmProfesionales;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.ConfigDecripcionLogin;
import entity.PypAdmControlProfesionales;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class CmProfesionalesJpaController implements Serializable {

    public CmProfesionalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CmProfesionales cmProfesionales) {
        if (cmProfesionales.getPypAdmControlProfesionalesList() == null) {
            cmProfesionales.setPypAdmControlProfesionalesList(new ArrayList<PypAdmControlProfesionales>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ConfigDecripcionLogin idDescripcionLogin = cmProfesionales.getIdDescripcionLogin();
            if (idDescripcionLogin != null) {
                idDescripcionLogin = em.getReference(idDescripcionLogin.getClass(), idDescripcionLogin.getId());
                cmProfesionales.setIdDescripcionLogin(idDescripcionLogin);
            }
            List<PypAdmControlProfesionales> attachedPypAdmControlProfesionalesList = new ArrayList<PypAdmControlProfesionales>();
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListPypAdmControlProfesionalesToAttach : cmProfesionales.getPypAdmControlProfesionalesList()) {
                pypAdmControlProfesionalesListPypAdmControlProfesionalesToAttach = em.getReference(pypAdmControlProfesionalesListPypAdmControlProfesionalesToAttach.getClass(), pypAdmControlProfesionalesListPypAdmControlProfesionalesToAttach.getId());
                attachedPypAdmControlProfesionalesList.add(pypAdmControlProfesionalesListPypAdmControlProfesionalesToAttach);
            }
            cmProfesionales.setPypAdmControlProfesionalesList(attachedPypAdmControlProfesionalesList);
            em.persist(cmProfesionales);
            if (idDescripcionLogin != null) {
                idDescripcionLogin.getCmProfesionalesList().add(cmProfesionales);
                idDescripcionLogin = em.merge(idDescripcionLogin);
            }
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListPypAdmControlProfesionales : cmProfesionales.getPypAdmControlProfesionalesList()) {
                CmProfesionales oldIdProfesionalOfPypAdmControlProfesionalesListPypAdmControlProfesionales = pypAdmControlProfesionalesListPypAdmControlProfesionales.getIdProfesional();
                pypAdmControlProfesionalesListPypAdmControlProfesionales.setIdProfesional(cmProfesionales);
                pypAdmControlProfesionalesListPypAdmControlProfesionales = em.merge(pypAdmControlProfesionalesListPypAdmControlProfesionales);
                if (oldIdProfesionalOfPypAdmControlProfesionalesListPypAdmControlProfesionales != null) {
                    oldIdProfesionalOfPypAdmControlProfesionalesListPypAdmControlProfesionales.getPypAdmControlProfesionalesList().remove(pypAdmControlProfesionalesListPypAdmControlProfesionales);
                    oldIdProfesionalOfPypAdmControlProfesionalesListPypAdmControlProfesionales = em.merge(oldIdProfesionalOfPypAdmControlProfesionalesListPypAdmControlProfesionales);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CmProfesionales cmProfesionales) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CmProfesionales persistentCmProfesionales = em.find(CmProfesionales.class, cmProfesionales.getId());
            ConfigDecripcionLogin idDescripcionLoginOld = persistentCmProfesionales.getIdDescripcionLogin();
            ConfigDecripcionLogin idDescripcionLoginNew = cmProfesionales.getIdDescripcionLogin();
            List<PypAdmControlProfesionales> pypAdmControlProfesionalesListOld = persistentCmProfesionales.getPypAdmControlProfesionalesList();
            List<PypAdmControlProfesionales> pypAdmControlProfesionalesListNew = cmProfesionales.getPypAdmControlProfesionalesList();
            List<String> illegalOrphanMessages = null;
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListOldPypAdmControlProfesionales : pypAdmControlProfesionalesListOld) {
                if (!pypAdmControlProfesionalesListNew.contains(pypAdmControlProfesionalesListOldPypAdmControlProfesionales)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PypAdmControlProfesionales " + pypAdmControlProfesionalesListOldPypAdmControlProfesionales + " since its idProfesional field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idDescripcionLoginNew != null) {
                idDescripcionLoginNew = em.getReference(idDescripcionLoginNew.getClass(), idDescripcionLoginNew.getId());
                cmProfesionales.setIdDescripcionLogin(idDescripcionLoginNew);
            }
            List<PypAdmControlProfesionales> attachedPypAdmControlProfesionalesListNew = new ArrayList<PypAdmControlProfesionales>();
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListNewPypAdmControlProfesionalesToAttach : pypAdmControlProfesionalesListNew) {
                pypAdmControlProfesionalesListNewPypAdmControlProfesionalesToAttach = em.getReference(pypAdmControlProfesionalesListNewPypAdmControlProfesionalesToAttach.getClass(), pypAdmControlProfesionalesListNewPypAdmControlProfesionalesToAttach.getId());
                attachedPypAdmControlProfesionalesListNew.add(pypAdmControlProfesionalesListNewPypAdmControlProfesionalesToAttach);
            }
            pypAdmControlProfesionalesListNew = attachedPypAdmControlProfesionalesListNew;
            cmProfesionales.setPypAdmControlProfesionalesList(pypAdmControlProfesionalesListNew);
            cmProfesionales = em.merge(cmProfesionales);
            if (idDescripcionLoginOld != null && !idDescripcionLoginOld.equals(idDescripcionLoginNew)) {
                idDescripcionLoginOld.getCmProfesionalesList().remove(cmProfesionales);
                idDescripcionLoginOld = em.merge(idDescripcionLoginOld);
            }
            if (idDescripcionLoginNew != null && !idDescripcionLoginNew.equals(idDescripcionLoginOld)) {
                idDescripcionLoginNew.getCmProfesionalesList().add(cmProfesionales);
                idDescripcionLoginNew = em.merge(idDescripcionLoginNew);
            }
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListNewPypAdmControlProfesionales : pypAdmControlProfesionalesListNew) {
                if (!pypAdmControlProfesionalesListOld.contains(pypAdmControlProfesionalesListNewPypAdmControlProfesionales)) {
                    CmProfesionales oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales = pypAdmControlProfesionalesListNewPypAdmControlProfesionales.getIdProfesional();
                    pypAdmControlProfesionalesListNewPypAdmControlProfesionales.setIdProfesional(cmProfesionales);
                    pypAdmControlProfesionalesListNewPypAdmControlProfesionales = em.merge(pypAdmControlProfesionalesListNewPypAdmControlProfesionales);
                    if (oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales != null && !oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales.equals(cmProfesionales)) {
                        oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales.getPypAdmControlProfesionalesList().remove(pypAdmControlProfesionalesListNewPypAdmControlProfesionales);
                        oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales = em.merge(oldIdProfesionalOfPypAdmControlProfesionalesListNewPypAdmControlProfesionales);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cmProfesionales.getId();
                if (findCmProfesionales(id) == null) {
                    throw new NonexistentEntityException("The cmProfesionales with id " + id + " no longer exists.");
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
            CmProfesionales cmProfesionales;
            try {
                cmProfesionales = em.getReference(CmProfesionales.class, id);
                cmProfesionales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cmProfesionales with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PypAdmControlProfesionales> pypAdmControlProfesionalesListOrphanCheck = cmProfesionales.getPypAdmControlProfesionalesList();
            for (PypAdmControlProfesionales pypAdmControlProfesionalesListOrphanCheckPypAdmControlProfesionales : pypAdmControlProfesionalesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This CmProfesionales (" + cmProfesionales + ") cannot be destroyed since the PypAdmControlProfesionales " + pypAdmControlProfesionalesListOrphanCheckPypAdmControlProfesionales + " in its pypAdmControlProfesionalesList field has a non-nullable idProfesional field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            ConfigDecripcionLogin idDescripcionLogin = cmProfesionales.getIdDescripcionLogin();
            if (idDescripcionLogin != null) {
                idDescripcionLogin.getCmProfesionalesList().remove(cmProfesionales);
                idDescripcionLogin = em.merge(idDescripcionLogin);
            }
            em.remove(cmProfesionales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CmProfesionales> findCmProfesionalesEntities() {
        return findCmProfesionalesEntities(true, -1, -1);
    }

    public List<CmProfesionales> findCmProfesionalesEntities(int maxResults, int firstResult) {
        return findCmProfesionalesEntities(false, maxResults, firstResult);
    }

    private List<CmProfesionales> findCmProfesionalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CmProfesionales.class));
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

    public CmProfesionales findCmProfesionales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CmProfesionales.class, id);
        } finally {
            em.close();
        }
    }

    public int getCmProfesionalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CmProfesionales> rt = cq.from(CmProfesionales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
