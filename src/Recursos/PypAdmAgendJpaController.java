/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Recursos;

import Recursos.exceptions.IllegalOrphanException;
import Recursos.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.PypAdmProgramas;
import entity.InfoPaciente;
import entity.PypAdmAgend;
import entity.PypAdmAsistCon;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Administrador
 */
public class PypAdmAgendJpaController implements Serializable {

    public PypAdmAgendJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PypAdmAgend pypAdmAgend) {
        if (pypAdmAgend.getPypAdmAsistConList() == null) {
            pypAdmAgend.setPypAdmAsistConList(new ArrayList<PypAdmAsistCon>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PypAdmProgramas idPrograma = pypAdmAgend.getIdPrograma();
            if (idPrograma != null) {
                idPrograma = em.getReference(idPrograma.getClass(), idPrograma.getId());
                pypAdmAgend.setIdPrograma(idPrograma);
            }
            InfoPaciente idPaciente = pypAdmAgend.getIdPaciente();
            if (idPaciente != null) {
                idPaciente = em.getReference(idPaciente.getClass(), idPaciente.getId());
                pypAdmAgend.setIdPaciente(idPaciente);
            }
            List<PypAdmAsistCon> attachedPypAdmAsistConList = new ArrayList<PypAdmAsistCon>();
            for (PypAdmAsistCon pypAdmAsistConListPypAdmAsistConToAttach : pypAdmAgend.getPypAdmAsistConList()) {
                pypAdmAsistConListPypAdmAsistConToAttach = em.getReference(pypAdmAsistConListPypAdmAsistConToAttach.getClass(), pypAdmAsistConListPypAdmAsistConToAttach.getId());
                attachedPypAdmAsistConList.add(pypAdmAsistConListPypAdmAsistConToAttach);
            }
            pypAdmAgend.setPypAdmAsistConList(attachedPypAdmAsistConList);
            em.persist(pypAdmAgend);
            if (idPrograma != null) {
                idPrograma.getPypAdmAgendList().add(pypAdmAgend);
                idPrograma = em.merge(idPrograma);
            }
            if (idPaciente != null) {
                idPaciente.getPypAdmAgendList().add(pypAdmAgend);
                idPaciente = em.merge(idPaciente);
            }
            for (PypAdmAsistCon pypAdmAsistConListPypAdmAsistCon : pypAdmAgend.getPypAdmAsistConList()) {
                PypAdmAgend oldIdAgendOfPypAdmAsistConListPypAdmAsistCon = pypAdmAsistConListPypAdmAsistCon.getIdAgend();
                pypAdmAsistConListPypAdmAsistCon.setIdAgend(pypAdmAgend);
                pypAdmAsistConListPypAdmAsistCon = em.merge(pypAdmAsistConListPypAdmAsistCon);
                if (oldIdAgendOfPypAdmAsistConListPypAdmAsistCon != null) {
                    oldIdAgendOfPypAdmAsistConListPypAdmAsistCon.getPypAdmAsistConList().remove(pypAdmAsistConListPypAdmAsistCon);
                    oldIdAgendOfPypAdmAsistConListPypAdmAsistCon = em.merge(oldIdAgendOfPypAdmAsistConListPypAdmAsistCon);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PypAdmAgend pypAdmAgend) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PypAdmAgend persistentPypAdmAgend = em.find(PypAdmAgend.class, pypAdmAgend.getId());
            PypAdmProgramas idProgramaOld = persistentPypAdmAgend.getIdPrograma();
            PypAdmProgramas idProgramaNew = pypAdmAgend.getIdPrograma();
            InfoPaciente idPacienteOld = persistentPypAdmAgend.getIdPaciente();
            InfoPaciente idPacienteNew = pypAdmAgend.getIdPaciente();
            List<PypAdmAsistCon> pypAdmAsistConListOld = persistentPypAdmAgend.getPypAdmAsistConList();
            List<PypAdmAsistCon> pypAdmAsistConListNew = pypAdmAgend.getPypAdmAsistConList();
            List<String> illegalOrphanMessages = null;
            for (PypAdmAsistCon pypAdmAsistConListOldPypAdmAsistCon : pypAdmAsistConListOld) {
                if (!pypAdmAsistConListNew.contains(pypAdmAsistConListOldPypAdmAsistCon)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PypAdmAsistCon " + pypAdmAsistConListOldPypAdmAsistCon + " since its idAgend field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idProgramaNew != null) {
                idProgramaNew = em.getReference(idProgramaNew.getClass(), idProgramaNew.getId());
                pypAdmAgend.setIdPrograma(idProgramaNew);
            }
            if (idPacienteNew != null) {
                idPacienteNew = em.getReference(idPacienteNew.getClass(), idPacienteNew.getId());
                pypAdmAgend.setIdPaciente(idPacienteNew);
            }
            List<PypAdmAsistCon> attachedPypAdmAsistConListNew = new ArrayList<PypAdmAsistCon>();
            for (PypAdmAsistCon pypAdmAsistConListNewPypAdmAsistConToAttach : pypAdmAsistConListNew) {
                pypAdmAsistConListNewPypAdmAsistConToAttach = em.getReference(pypAdmAsistConListNewPypAdmAsistConToAttach.getClass(), pypAdmAsistConListNewPypAdmAsistConToAttach.getId());
                attachedPypAdmAsistConListNew.add(pypAdmAsistConListNewPypAdmAsistConToAttach);
            }
            pypAdmAsistConListNew = attachedPypAdmAsistConListNew;
            pypAdmAgend.setPypAdmAsistConList(pypAdmAsistConListNew);
            pypAdmAgend = em.merge(pypAdmAgend);
            if (idProgramaOld != null && !idProgramaOld.equals(idProgramaNew)) {
                idProgramaOld.getPypAdmAgendList().remove(pypAdmAgend);
                idProgramaOld = em.merge(idProgramaOld);
            }
            if (idProgramaNew != null && !idProgramaNew.equals(idProgramaOld)) {
                idProgramaNew.getPypAdmAgendList().add(pypAdmAgend);
                idProgramaNew = em.merge(idProgramaNew);
            }
            if (idPacienteOld != null && !idPacienteOld.equals(idPacienteNew)) {
                idPacienteOld.getPypAdmAgendList().remove(pypAdmAgend);
                idPacienteOld = em.merge(idPacienteOld);
            }
            if (idPacienteNew != null && !idPacienteNew.equals(idPacienteOld)) {
                idPacienteNew.getPypAdmAgendList().add(pypAdmAgend);
                idPacienteNew = em.merge(idPacienteNew);
            }
            for (PypAdmAsistCon pypAdmAsistConListNewPypAdmAsistCon : pypAdmAsistConListNew) {
                if (!pypAdmAsistConListOld.contains(pypAdmAsistConListNewPypAdmAsistCon)) {
                    PypAdmAgend oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon = pypAdmAsistConListNewPypAdmAsistCon.getIdAgend();
                    pypAdmAsistConListNewPypAdmAsistCon.setIdAgend(pypAdmAgend);
                    pypAdmAsistConListNewPypAdmAsistCon = em.merge(pypAdmAsistConListNewPypAdmAsistCon);
                    if (oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon != null && !oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon.equals(pypAdmAgend)) {
                        oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon.getPypAdmAsistConList().remove(pypAdmAsistConListNewPypAdmAsistCon);
                        oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon = em.merge(oldIdAgendOfPypAdmAsistConListNewPypAdmAsistCon);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pypAdmAgend.getId();
                if (findPypAdmAgend(id) == null) {
                    throw new NonexistentEntityException("The pypAdmAgend with id " + id + " no longer exists.");
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
            PypAdmAgend pypAdmAgend;
            try {
                pypAdmAgend = em.getReference(PypAdmAgend.class, id);
                pypAdmAgend.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pypAdmAgend with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<PypAdmAsistCon> pypAdmAsistConListOrphanCheck = pypAdmAgend.getPypAdmAsistConList();
            for (PypAdmAsistCon pypAdmAsistConListOrphanCheckPypAdmAsistCon : pypAdmAsistConListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This PypAdmAgend (" + pypAdmAgend + ") cannot be destroyed since the PypAdmAsistCon " + pypAdmAsistConListOrphanCheckPypAdmAsistCon + " in its pypAdmAsistConList field has a non-nullable idAgend field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            PypAdmProgramas idPrograma = pypAdmAgend.getIdPrograma();
            if (idPrograma != null) {
                idPrograma.getPypAdmAgendList().remove(pypAdmAgend);
                idPrograma = em.merge(idPrograma);
            }
            InfoPaciente idPaciente = pypAdmAgend.getIdPaciente();
            if (idPaciente != null) {
                idPaciente.getPypAdmAgendList().remove(pypAdmAgend);
                idPaciente = em.merge(idPaciente);
            }
            em.remove(pypAdmAgend);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PypAdmAgend> findPypAdmAgendEntities() {
        return findPypAdmAgendEntities(true, -1, -1);
    }

    public List<PypAdmAgend> findPypAdmAgendEntities(int maxResults, int firstResult) {
        return findPypAdmAgendEntities(false, maxResults, firstResult);
    }

    private List<PypAdmAgend> findPypAdmAgendEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PypAdmAgend.class));
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

    public PypAdmAgend findPypAdmAgend(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PypAdmAgend.class, id);
        } finally {
            em.close();
        }
    }

    public int getPypAdmAgendCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PypAdmAgend> rt = cq.from(PypAdmAgend.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
