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
import entity.PypAdmAgend;
import entity.PypAdmProgramas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class PypAdmProgramasJpaController implements Serializable {

    public PypAdmProgramasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PypAdmProgramas pypAdmProgramas) {
        if (pypAdmProgramas.getPypAdmAgendList() == null) {
            pypAdmProgramas.setPypAdmAgendList(new ArrayList<PypAdmAgend>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<PypAdmAgend> attachedPypAdmAgendList = new ArrayList<PypAdmAgend>();
            for (PypAdmAgend pypAdmAgendListPypAdmAgendToAttach : pypAdmProgramas.getPypAdmAgendList()) {
                pypAdmAgendListPypAdmAgendToAttach = em.getReference(pypAdmAgendListPypAdmAgendToAttach.getClass(), pypAdmAgendListPypAdmAgendToAttach.getId());
                attachedPypAdmAgendList.add(pypAdmAgendListPypAdmAgendToAttach);
            }
            pypAdmProgramas.setPypAdmAgendList(attachedPypAdmAgendList);
            em.persist(pypAdmProgramas);
            for (PypAdmAgend pypAdmAgendListPypAdmAgend : pypAdmProgramas.getPypAdmAgendList()) {
                PypAdmProgramas oldIdProgramaOfPypAdmAgendListPypAdmAgend = pypAdmAgendListPypAdmAgend.getIdPrograma();
                pypAdmAgendListPypAdmAgend.setIdPrograma(pypAdmProgramas);
                pypAdmAgendListPypAdmAgend = em.merge(pypAdmAgendListPypAdmAgend);
                if (oldIdProgramaOfPypAdmAgendListPypAdmAgend != null) {
                    oldIdProgramaOfPypAdmAgendListPypAdmAgend.getPypAdmAgendList().remove(pypAdmAgendListPypAdmAgend);
                    oldIdProgramaOfPypAdmAgendListPypAdmAgend = em.merge(oldIdProgramaOfPypAdmAgendListPypAdmAgend);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PypAdmProgramas pypAdmProgramas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PypAdmProgramas persistentPypAdmProgramas = em.find(PypAdmProgramas.class, pypAdmProgramas.getId());
            List<PypAdmAgend> pypAdmAgendListOld = persistentPypAdmProgramas.getPypAdmAgendList();
            List<PypAdmAgend> pypAdmAgendListNew = pypAdmProgramas.getPypAdmAgendList();
            List<String> illegalOrphanMessages = null;
            for (PypAdmAgend pypAdmAgendListOldPypAdmAgend : pypAdmAgendListOld) {
                if (!pypAdmAgendListNew.contains(pypAdmAgendListOldPypAdmAgend)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PypAdmAgend " + pypAdmAgendListOldPypAdmAgend + " since its idPrograma field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<PypAdmAgend> attachedPypAdmAgendListNew = new ArrayList<PypAdmAgend>();
            for (PypAdmAgend pypAdmAgendListNewPypAdmAgendToAttach : pypAdmAgendListNew) {
                pypAdmAgendListNewPypAdmAgendToAttach = em.getReference(pypAdmAgendListNewPypAdmAgendToAttach.getClass(), pypAdmAgendListNewPypAdmAgendToAttach.getId());
                attachedPypAdmAgendListNew.add(pypAdmAgendListNewPypAdmAgendToAttach);
            }
            pypAdmAgendListNew = attachedPypAdmAgendListNew;
            pypAdmProgramas.setPypAdmAgendList(pypAdmAgendListNew);
            pypAdmProgramas = em.merge(pypAdmProgramas);
            for (PypAdmAgend pypAdmAgendListNewPypAdmAgend : pypAdmAgendListNew) {
                if (!pypAdmAgendListOld.contains(pypAdmAgendListNewPypAdmAgend)) {
                    PypAdmProgramas oldIdProgramaOfPypAdmAgendListNewPypAdmAgend = pypAdmAgendListNewPypAdmAgend.getIdPrograma();
                    pypAdmAgendListNewPypAdmAgend.setIdPrograma(pypAdmProgramas);
                    pypAdmAgendListNewPypAdmAgend = em.merge(pypAdmAgendListNewPypAdmAgend);
                    if (oldIdProgramaOfPypAdmAgendListNewPypAdmAgend != null && !oldIdProgramaOfPypAdmAgendListNewPypAdmAgend.equals(pypAdmProgramas)) {
                        oldIdProgramaOfPypAdmAgendListNewPypAdmAgend.getPypAdmAgendList().remove(pypAdmAgendListNewPypAdmAgend);
                        oldIdProgramaOfPypAdmAgendListNewPypAdmAgend = em.merge(oldIdProgramaOfPypAdmAgendListNewPypAdmAgend);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pypAdmProgramas.getId();
                if (findPypAdmProgramas(id) == null) {
                    throw new NonexistentEntityException("The pypAdmProgramas with id " + id + " no longer exists.");
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
            PypAdmProgramas pypAdmProgramas;
            try {
                pypAdmProgramas = em.getReference(PypAdmProgramas.class, id);
                pypAdmProgramas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pypAdmProgramas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PypAdmAgend> pypAdmAgendListOrphanCheck = pypAdmProgramas.getPypAdmAgendList();
            for (PypAdmAgend pypAdmAgendListOrphanCheckPypAdmAgend : pypAdmAgendListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PypAdmProgramas (" + pypAdmProgramas + ") cannot be destroyed since the PypAdmAgend " + pypAdmAgendListOrphanCheckPypAdmAgend + " in its pypAdmAgendList field has a non-nullable idPrograma field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pypAdmProgramas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PypAdmProgramas> findPypAdmProgramasEntities() {
        return findPypAdmProgramasEntities(true, -1, -1);
    }

    public List<PypAdmProgramas> findPypAdmProgramasEntities(int maxResults, int firstResult) {
        return findPypAdmProgramasEntities(false, maxResults, firstResult);
    }

    private List<PypAdmProgramas> findPypAdmProgramasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PypAdmProgramas.class));
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

    public PypAdmProgramas findPypAdmProgramas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PypAdmProgramas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPypAdmProgramasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PypAdmProgramas> rt = cq.from(PypAdmProgramas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
