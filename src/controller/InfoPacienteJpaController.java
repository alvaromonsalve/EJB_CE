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
import entity.InfoEntidades;
import entity.InfoPaciente;
import entity.PypAdmAgend;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class InfoPacienteJpaController implements Serializable {

    public InfoPacienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InfoPaciente infoPaciente) {
        if (infoPaciente.getPypAdmAgendList() == null) {
            infoPaciente.setPypAdmAgendList(new ArrayList<PypAdmAgend>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InfoEntidades contratante = infoPaciente.getContratante();
            if (contratante != null) {
                contratante = em.getReference(contratante.getClass(), contratante.getId());
                infoPaciente.setContratante(contratante);
            }
            List<PypAdmAgend> attachedPypAdmAgendList = new ArrayList<PypAdmAgend>();
            for (PypAdmAgend pypAdmAgendListPypAdmAgendToAttach : infoPaciente.getPypAdmAgendList()) {
                pypAdmAgendListPypAdmAgendToAttach = em.getReference(pypAdmAgendListPypAdmAgendToAttach.getClass(), pypAdmAgendListPypAdmAgendToAttach.getId());
                attachedPypAdmAgendList.add(pypAdmAgendListPypAdmAgendToAttach);
            }
            infoPaciente.setPypAdmAgendList(attachedPypAdmAgendList);
            em.persist(infoPaciente);
            if (contratante != null) {
                contratante.getInfoPacienteList().add(infoPaciente);
                contratante = em.merge(contratante);
            }
            for (PypAdmAgend pypAdmAgendListPypAdmAgend : infoPaciente.getPypAdmAgendList()) {
                InfoPaciente oldIdPacienteOfPypAdmAgendListPypAdmAgend = pypAdmAgendListPypAdmAgend.getIdPaciente();
                pypAdmAgendListPypAdmAgend.setIdPaciente(infoPaciente);
                pypAdmAgendListPypAdmAgend = em.merge(pypAdmAgendListPypAdmAgend);
                if (oldIdPacienteOfPypAdmAgendListPypAdmAgend != null) {
                    oldIdPacienteOfPypAdmAgendListPypAdmAgend.getPypAdmAgendList().remove(pypAdmAgendListPypAdmAgend);
                    oldIdPacienteOfPypAdmAgendListPypAdmAgend = em.merge(oldIdPacienteOfPypAdmAgendListPypAdmAgend);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InfoPaciente infoPaciente) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InfoPaciente persistentInfoPaciente = em.find(InfoPaciente.class, infoPaciente.getId());
            InfoEntidades contratanteOld = persistentInfoPaciente.getContratante();
            InfoEntidades contratanteNew = infoPaciente.getContratante();
            List<PypAdmAgend> pypAdmAgendListOld = persistentInfoPaciente.getPypAdmAgendList();
            List<PypAdmAgend> pypAdmAgendListNew = infoPaciente.getPypAdmAgendList();
            List<String> illegalOrphanMessages = null;
            for (PypAdmAgend pypAdmAgendListOldPypAdmAgend : pypAdmAgendListOld) {
                if (!pypAdmAgendListNew.contains(pypAdmAgendListOldPypAdmAgend)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PypAdmAgend " + pypAdmAgendListOldPypAdmAgend + " since its idPaciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (contratanteNew != null) {
                contratanteNew = em.getReference(contratanteNew.getClass(), contratanteNew.getId());
                infoPaciente.setContratante(contratanteNew);
            }
            List<PypAdmAgend> attachedPypAdmAgendListNew = new ArrayList<PypAdmAgend>();
            for (PypAdmAgend pypAdmAgendListNewPypAdmAgendToAttach : pypAdmAgendListNew) {
                pypAdmAgendListNewPypAdmAgendToAttach = em.getReference(pypAdmAgendListNewPypAdmAgendToAttach.getClass(), pypAdmAgendListNewPypAdmAgendToAttach.getId());
                attachedPypAdmAgendListNew.add(pypAdmAgendListNewPypAdmAgendToAttach);
            }
            pypAdmAgendListNew = attachedPypAdmAgendListNew;
            infoPaciente.setPypAdmAgendList(pypAdmAgendListNew);
            infoPaciente = em.merge(infoPaciente);
            if (contratanteOld != null && !contratanteOld.equals(contratanteNew)) {
                contratanteOld.getInfoPacienteList().remove(infoPaciente);
                contratanteOld = em.merge(contratanteOld);
            }
            if (contratanteNew != null && !contratanteNew.equals(contratanteOld)) {
                contratanteNew.getInfoPacienteList().add(infoPaciente);
                contratanteNew = em.merge(contratanteNew);
            }
            for (PypAdmAgend pypAdmAgendListNewPypAdmAgend : pypAdmAgendListNew) {
                if (!pypAdmAgendListOld.contains(pypAdmAgendListNewPypAdmAgend)) {
                    InfoPaciente oldIdPacienteOfPypAdmAgendListNewPypAdmAgend = pypAdmAgendListNewPypAdmAgend.getIdPaciente();
                    pypAdmAgendListNewPypAdmAgend.setIdPaciente(infoPaciente);
                    pypAdmAgendListNewPypAdmAgend = em.merge(pypAdmAgendListNewPypAdmAgend);
                    if (oldIdPacienteOfPypAdmAgendListNewPypAdmAgend != null && !oldIdPacienteOfPypAdmAgendListNewPypAdmAgend.equals(infoPaciente)) {
                        oldIdPacienteOfPypAdmAgendListNewPypAdmAgend.getPypAdmAgendList().remove(pypAdmAgendListNewPypAdmAgend);
                        oldIdPacienteOfPypAdmAgendListNewPypAdmAgend = em.merge(oldIdPacienteOfPypAdmAgendListNewPypAdmAgend);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = infoPaciente.getId();
                if (findInfoPaciente(id) == null) {
                    throw new NonexistentEntityException("The infoPaciente with id " + id + " no longer exists.");
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
            InfoPaciente infoPaciente;
            try {
                infoPaciente = em.getReference(InfoPaciente.class, id);
                infoPaciente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infoPaciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PypAdmAgend> pypAdmAgendListOrphanCheck = infoPaciente.getPypAdmAgendList();
            for (PypAdmAgend pypAdmAgendListOrphanCheckPypAdmAgend : pypAdmAgendListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This InfoPaciente (" + infoPaciente + ") cannot be destroyed since the PypAdmAgend " + pypAdmAgendListOrphanCheckPypAdmAgend + " in its pypAdmAgendList field has a non-nullable idPaciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            InfoEntidades contratante = infoPaciente.getContratante();
            if (contratante != null) {
                contratante.getInfoPacienteList().remove(infoPaciente);
                contratante = em.merge(contratante);
            }
            em.remove(infoPaciente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InfoPaciente> findInfoPacienteEntities() {
        return findInfoPacienteEntities(true, -1, -1);
    }

    public List<InfoPaciente> findInfoPacienteEntities(int maxResults, int firstResult) {
        return findInfoPacienteEntities(false, maxResults, firstResult);
    }

    private List<InfoPaciente> findInfoPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InfoPaciente.class));
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

    public InfoPaciente findInfoPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InfoPaciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getInfoPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InfoPaciente> rt = cq.from(InfoPaciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
