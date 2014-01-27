/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import controller.exceptions.NonexistentEntityException;
import entity.InfoEntidades;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entity.InfoPaciente;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class InfoEntidadesJpaController implements Serializable {

    public InfoEntidadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(InfoEntidades infoEntidades) {
        if (infoEntidades.getInfoPacienteList() == null) {
            infoEntidades.setInfoPacienteList(new ArrayList<InfoPaciente>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<InfoPaciente> attachedInfoPacienteList = new ArrayList<InfoPaciente>();
            for (InfoPaciente infoPacienteListInfoPacienteToAttach : infoEntidades.getInfoPacienteList()) {
                infoPacienteListInfoPacienteToAttach = em.getReference(infoPacienteListInfoPacienteToAttach.getClass(), infoPacienteListInfoPacienteToAttach.getId());
                attachedInfoPacienteList.add(infoPacienteListInfoPacienteToAttach);
            }
            infoEntidades.setInfoPacienteList(attachedInfoPacienteList);
            em.persist(infoEntidades);
            for (InfoPaciente infoPacienteListInfoPaciente : infoEntidades.getInfoPacienteList()) {
                InfoEntidades oldContratanteOfInfoPacienteListInfoPaciente = infoPacienteListInfoPaciente.getContratante();
                infoPacienteListInfoPaciente.setContratante(infoEntidades);
                infoPacienteListInfoPaciente = em.merge(infoPacienteListInfoPaciente);
                if (oldContratanteOfInfoPacienteListInfoPaciente != null) {
                    oldContratanteOfInfoPacienteListInfoPaciente.getInfoPacienteList().remove(infoPacienteListInfoPaciente);
                    oldContratanteOfInfoPacienteListInfoPaciente = em.merge(oldContratanteOfInfoPacienteListInfoPaciente);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(InfoEntidades infoEntidades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            InfoEntidades persistentInfoEntidades = em.find(InfoEntidades.class, infoEntidades.getId());
            List<InfoPaciente> infoPacienteListOld = persistentInfoEntidades.getInfoPacienteList();
            List<InfoPaciente> infoPacienteListNew = infoEntidades.getInfoPacienteList();
            List<InfoPaciente> attachedInfoPacienteListNew = new ArrayList<InfoPaciente>();
            for (InfoPaciente infoPacienteListNewInfoPacienteToAttach : infoPacienteListNew) {
                infoPacienteListNewInfoPacienteToAttach = em.getReference(infoPacienteListNewInfoPacienteToAttach.getClass(), infoPacienteListNewInfoPacienteToAttach.getId());
                attachedInfoPacienteListNew.add(infoPacienteListNewInfoPacienteToAttach);
            }
            infoPacienteListNew = attachedInfoPacienteListNew;
            infoEntidades.setInfoPacienteList(infoPacienteListNew);
            infoEntidades = em.merge(infoEntidades);
            for (InfoPaciente infoPacienteListOldInfoPaciente : infoPacienteListOld) {
                if (!infoPacienteListNew.contains(infoPacienteListOldInfoPaciente)) {
                    infoPacienteListOldInfoPaciente.setContratante(null);
                    infoPacienteListOldInfoPaciente = em.merge(infoPacienteListOldInfoPaciente);
                }
            }
            for (InfoPaciente infoPacienteListNewInfoPaciente : infoPacienteListNew) {
                if (!infoPacienteListOld.contains(infoPacienteListNewInfoPaciente)) {
                    InfoEntidades oldContratanteOfInfoPacienteListNewInfoPaciente = infoPacienteListNewInfoPaciente.getContratante();
                    infoPacienteListNewInfoPaciente.setContratante(infoEntidades);
                    infoPacienteListNewInfoPaciente = em.merge(infoPacienteListNewInfoPaciente);
                    if (oldContratanteOfInfoPacienteListNewInfoPaciente != null && !oldContratanteOfInfoPacienteListNewInfoPaciente.equals(infoEntidades)) {
                        oldContratanteOfInfoPacienteListNewInfoPaciente.getInfoPacienteList().remove(infoPacienteListNewInfoPaciente);
                        oldContratanteOfInfoPacienteListNewInfoPaciente = em.merge(oldContratanteOfInfoPacienteListNewInfoPaciente);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = infoEntidades.getId();
                if (findInfoEntidades(id) == null) {
                    throw new NonexistentEntityException("The infoEntidades with id " + id + " no longer exists.");
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
            InfoEntidades infoEntidades;
            try {
                infoEntidades = em.getReference(InfoEntidades.class, id);
                infoEntidades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The infoEntidades with id " + id + " no longer exists.", enfe);
            }
            List<InfoPaciente> infoPacienteList = infoEntidades.getInfoPacienteList();
            for (InfoPaciente infoPacienteListInfoPaciente : infoPacienteList) {
                infoPacienteListInfoPaciente.setContratante(null);
                infoPacienteListInfoPaciente = em.merge(infoPacienteListInfoPaciente);
            }
            em.remove(infoEntidades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<InfoEntidades> findInfoEntidadesEntities() {
        return findInfoEntidadesEntities(true, -1, -1);
    }

    public List<InfoEntidades> findInfoEntidadesEntities(int maxResults, int firstResult) {
        return findInfoEntidadesEntities(false, maxResults, firstResult);
    }

    private List<InfoEntidades> findInfoEntidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(InfoEntidades.class));
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

    public InfoEntidades findInfoEntidades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(InfoEntidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getInfoEntidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<InfoEntidades> rt = cq.from(InfoEntidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
