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
import entity.CmProfesionales;
import entity.PypAdmAsistCon;
import entity.PypAdmControlProfesionales;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class PypAdmControlProfesionalesJpaController implements Serializable {

    public PypAdmControlProfesionalesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PypAdmControlProfesionales pypAdmControlProfesionales) {
        if (pypAdmControlProfesionales.getPypAdmAsistConList() == null) {
            pypAdmControlProfesionales.setPypAdmAsistConList(new ArrayList<PypAdmAsistCon>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CmProfesionales idProfesional = pypAdmControlProfesionales.getIdProfesional();
            if (idProfesional != null) {
                idProfesional = em.getReference(idProfesional.getClass(), idProfesional.getId());
                pypAdmControlProfesionales.setIdProfesional(idProfesional);
            }
            List<PypAdmAsistCon> attachedPypAdmAsistConList = new ArrayList<PypAdmAsistCon>();
            for (PypAdmAsistCon pypAdmAsistConListPypAdmAsistConToAttach : pypAdmControlProfesionales.getPypAdmAsistConList()) {
                pypAdmAsistConListPypAdmAsistConToAttach = em.getReference(pypAdmAsistConListPypAdmAsistConToAttach.getClass(), pypAdmAsistConListPypAdmAsistConToAttach.getId());
                attachedPypAdmAsistConList.add(pypAdmAsistConListPypAdmAsistConToAttach);
            }
            pypAdmControlProfesionales.setPypAdmAsistConList(attachedPypAdmAsistConList);
            em.persist(pypAdmControlProfesionales);
            if (idProfesional != null) {
                idProfesional.getPypAdmControlProfesionalesList().add(pypAdmControlProfesionales);
                idProfesional = em.merge(idProfesional);
            }
            for (PypAdmAsistCon pypAdmAsistConListPypAdmAsistCon : pypAdmControlProfesionales.getPypAdmAsistConList()) {
                PypAdmControlProfesionales oldIdControlProOfPypAdmAsistConListPypAdmAsistCon = pypAdmAsistConListPypAdmAsistCon.getIdControlPro();
                pypAdmAsistConListPypAdmAsistCon.setIdControlPro(pypAdmControlProfesionales);
                pypAdmAsistConListPypAdmAsistCon = em.merge(pypAdmAsistConListPypAdmAsistCon);
                if (oldIdControlProOfPypAdmAsistConListPypAdmAsistCon != null) {
                    oldIdControlProOfPypAdmAsistConListPypAdmAsistCon.getPypAdmAsistConList().remove(pypAdmAsistConListPypAdmAsistCon);
                    oldIdControlProOfPypAdmAsistConListPypAdmAsistCon = em.merge(oldIdControlProOfPypAdmAsistConListPypAdmAsistCon);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PypAdmControlProfesionales pypAdmControlProfesionales) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PypAdmControlProfesionales persistentPypAdmControlProfesionales = em.find(PypAdmControlProfesionales.class, pypAdmControlProfesionales.getId());
            CmProfesionales idProfesionalOld = persistentPypAdmControlProfesionales.getIdProfesional();
            CmProfesionales idProfesionalNew = pypAdmControlProfesionales.getIdProfesional();
            List<PypAdmAsistCon> pypAdmAsistConListOld = persistentPypAdmControlProfesionales.getPypAdmAsistConList();
            List<PypAdmAsistCon> pypAdmAsistConListNew = pypAdmControlProfesionales.getPypAdmAsistConList();
            List<String> illegalOrphanMessages = null;
            for (PypAdmAsistCon pypAdmAsistConListOldPypAdmAsistCon : pypAdmAsistConListOld) {
                if (!pypAdmAsistConListNew.contains(pypAdmAsistConListOldPypAdmAsistCon)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PypAdmAsistCon " + pypAdmAsistConListOldPypAdmAsistCon + " since its idControlPro field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProfesionalNew != null) {
                idProfesionalNew = em.getReference(idProfesionalNew.getClass(), idProfesionalNew.getId());
                pypAdmControlProfesionales.setIdProfesional(idProfesionalNew);
            }
            List<PypAdmAsistCon> attachedPypAdmAsistConListNew = new ArrayList<PypAdmAsistCon>();
            for (PypAdmAsistCon pypAdmAsistConListNewPypAdmAsistConToAttach : pypAdmAsistConListNew) {
                pypAdmAsistConListNewPypAdmAsistConToAttach = em.getReference(pypAdmAsistConListNewPypAdmAsistConToAttach.getClass(), pypAdmAsistConListNewPypAdmAsistConToAttach.getId());
                attachedPypAdmAsistConListNew.add(pypAdmAsistConListNewPypAdmAsistConToAttach);
            }
            pypAdmAsistConListNew = attachedPypAdmAsistConListNew;
            pypAdmControlProfesionales.setPypAdmAsistConList(pypAdmAsistConListNew);
            pypAdmControlProfesionales = em.merge(pypAdmControlProfesionales);
            if (idProfesionalOld != null && !idProfesionalOld.equals(idProfesionalNew)) {
                idProfesionalOld.getPypAdmControlProfesionalesList().remove(pypAdmControlProfesionales);
                idProfesionalOld = em.merge(idProfesionalOld);
            }
            if (idProfesionalNew != null && !idProfesionalNew.equals(idProfesionalOld)) {
                idProfesionalNew.getPypAdmControlProfesionalesList().add(pypAdmControlProfesionales);
                idProfesionalNew = em.merge(idProfesionalNew);
            }
            for (PypAdmAsistCon pypAdmAsistConListNewPypAdmAsistCon : pypAdmAsistConListNew) {
                if (!pypAdmAsistConListOld.contains(pypAdmAsistConListNewPypAdmAsistCon)) {
                    PypAdmControlProfesionales oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon = pypAdmAsistConListNewPypAdmAsistCon.getIdControlPro();
                    pypAdmAsistConListNewPypAdmAsistCon.setIdControlPro(pypAdmControlProfesionales);
                    pypAdmAsistConListNewPypAdmAsistCon = em.merge(pypAdmAsistConListNewPypAdmAsistCon);
                    if (oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon != null && !oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon.equals(pypAdmControlProfesionales)) {
                        oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon.getPypAdmAsistConList().remove(pypAdmAsistConListNewPypAdmAsistCon);
                        oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon = em.merge(oldIdControlProOfPypAdmAsistConListNewPypAdmAsistCon);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pypAdmControlProfesionales.getId();
                if (findPypAdmControlProfesionales(id) == null) {
                    throw new NonexistentEntityException("The pypAdmControlProfesionales with id " + id + " no longer exists.");
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
            PypAdmControlProfesionales pypAdmControlProfesionales;
            try {
                pypAdmControlProfesionales = em.getReference(PypAdmControlProfesionales.class, id);
                pypAdmControlProfesionales.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pypAdmControlProfesionales with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PypAdmAsistCon> pypAdmAsistConListOrphanCheck = pypAdmControlProfesionales.getPypAdmAsistConList();
            for (PypAdmAsistCon pypAdmAsistConListOrphanCheckPypAdmAsistCon : pypAdmAsistConListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PypAdmControlProfesionales (" + pypAdmControlProfesionales + ") cannot be destroyed since the PypAdmAsistCon " + pypAdmAsistConListOrphanCheckPypAdmAsistCon + " in its pypAdmAsistConList field has a non-nullable idControlPro field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CmProfesionales idProfesional = pypAdmControlProfesionales.getIdProfesional();
            if (idProfesional != null) {
                idProfesional.getPypAdmControlProfesionalesList().remove(pypAdmControlProfesionales);
                idProfesional = em.merge(idProfesional);
            }
            em.remove(pypAdmControlProfesionales);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PypAdmControlProfesionales> findPypAdmControlProfesionalesEntities() {
        return findPypAdmControlProfesionalesEntities(true, -1, -1);
    }

    public List<PypAdmControlProfesionales> findPypAdmControlProfesionalesEntities(int maxResults, int firstResult) {
        return findPypAdmControlProfesionalesEntities(false, maxResults, firstResult);
    }

    private List<PypAdmControlProfesionales> findPypAdmControlProfesionalesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PypAdmControlProfesionales.class));
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

    public PypAdmControlProfesionales findPypAdmControlProfesionales(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PypAdmControlProfesionales.class, id);
        } finally {
            em.close();
        }
    }

    public int getPypAdmControlProfesionalesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PypAdmControlProfesionales> rt = cq.from(PypAdmControlProfesionales.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
