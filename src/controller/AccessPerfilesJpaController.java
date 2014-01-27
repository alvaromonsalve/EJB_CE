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
import entity.AccessConfigUser;
import entity.AccessPerfiles;
import java.util.ArrayList;
import java.util.List;
import entity.AccessRoles;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Alvaro Monsalve
 */
public class AccessPerfilesJpaController implements Serializable {

    public AccessPerfilesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AccessPerfiles accessPerfiles) {
        if (accessPerfiles.getAccessConfigUserList() == null) {
            accessPerfiles.setAccessConfigUserList(new ArrayList<AccessConfigUser>());
        }
        if (accessPerfiles.getAccessRolesList() == null) {
            accessPerfiles.setAccessRolesList(new ArrayList<AccessRoles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<AccessConfigUser> attachedAccessConfigUserList = new ArrayList<AccessConfigUser>();
            for (AccessConfigUser accessConfigUserListAccessConfigUserToAttach : accessPerfiles.getAccessConfigUserList()) {
                accessConfigUserListAccessConfigUserToAttach = em.getReference(accessConfigUserListAccessConfigUserToAttach.getClass(), accessConfigUserListAccessConfigUserToAttach.getId());
                attachedAccessConfigUserList.add(accessConfigUserListAccessConfigUserToAttach);
            }
            accessPerfiles.setAccessConfigUserList(attachedAccessConfigUserList);
            List<AccessRoles> attachedAccessRolesList = new ArrayList<AccessRoles>();
            for (AccessRoles accessRolesListAccessRolesToAttach : accessPerfiles.getAccessRolesList()) {
                accessRolesListAccessRolesToAttach = em.getReference(accessRolesListAccessRolesToAttach.getClass(), accessRolesListAccessRolesToAttach.getId());
                attachedAccessRolesList.add(accessRolesListAccessRolesToAttach);
            }
            accessPerfiles.setAccessRolesList(attachedAccessRolesList);
            em.persist(accessPerfiles);
            for (AccessConfigUser accessConfigUserListAccessConfigUser : accessPerfiles.getAccessConfigUserList()) {
                AccessPerfiles oldIdPerfilesOfAccessConfigUserListAccessConfigUser = accessConfigUserListAccessConfigUser.getIdPerfiles();
                accessConfigUserListAccessConfigUser.setIdPerfiles(accessPerfiles);
                accessConfigUserListAccessConfigUser = em.merge(accessConfigUserListAccessConfigUser);
                if (oldIdPerfilesOfAccessConfigUserListAccessConfigUser != null) {
                    oldIdPerfilesOfAccessConfigUserListAccessConfigUser.getAccessConfigUserList().remove(accessConfigUserListAccessConfigUser);
                    oldIdPerfilesOfAccessConfigUserListAccessConfigUser = em.merge(oldIdPerfilesOfAccessConfigUserListAccessConfigUser);
                }
            }
            for (AccessRoles accessRolesListAccessRoles : accessPerfiles.getAccessRolesList()) {
                AccessPerfiles oldIdPerfilOfAccessRolesListAccessRoles = accessRolesListAccessRoles.getIdPerfil();
                accessRolesListAccessRoles.setIdPerfil(accessPerfiles);
                accessRolesListAccessRoles = em.merge(accessRolesListAccessRoles);
                if (oldIdPerfilOfAccessRolesListAccessRoles != null) {
                    oldIdPerfilOfAccessRolesListAccessRoles.getAccessRolesList().remove(accessRolesListAccessRoles);
                    oldIdPerfilOfAccessRolesListAccessRoles = em.merge(oldIdPerfilOfAccessRolesListAccessRoles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AccessPerfiles accessPerfiles) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AccessPerfiles persistentAccessPerfiles = em.find(AccessPerfiles.class, accessPerfiles.getId());
            List<AccessConfigUser> accessConfigUserListOld = persistentAccessPerfiles.getAccessConfigUserList();
            List<AccessConfigUser> accessConfigUserListNew = accessPerfiles.getAccessConfigUserList();
            List<AccessRoles> accessRolesListOld = persistentAccessPerfiles.getAccessRolesList();
            List<AccessRoles> accessRolesListNew = accessPerfiles.getAccessRolesList();
            List<String> illegalOrphanMessages = null;
            for (AccessConfigUser accessConfigUserListOldAccessConfigUser : accessConfigUserListOld) {
                if (!accessConfigUserListNew.contains(accessConfigUserListOldAccessConfigUser)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccessConfigUser " + accessConfigUserListOldAccessConfigUser + " since its idPerfiles field is not nullable.");
                }
            }
            for (AccessRoles accessRolesListOldAccessRoles : accessRolesListOld) {
                if (!accessRolesListNew.contains(accessRolesListOldAccessRoles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain AccessRoles " + accessRolesListOldAccessRoles + " since its idPerfil field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<AccessConfigUser> attachedAccessConfigUserListNew = new ArrayList<AccessConfigUser>();
            for (AccessConfigUser accessConfigUserListNewAccessConfigUserToAttach : accessConfigUserListNew) {
                accessConfigUserListNewAccessConfigUserToAttach = em.getReference(accessConfigUserListNewAccessConfigUserToAttach.getClass(), accessConfigUserListNewAccessConfigUserToAttach.getId());
                attachedAccessConfigUserListNew.add(accessConfigUserListNewAccessConfigUserToAttach);
            }
            accessConfigUserListNew = attachedAccessConfigUserListNew;
            accessPerfiles.setAccessConfigUserList(accessConfigUserListNew);
            List<AccessRoles> attachedAccessRolesListNew = new ArrayList<AccessRoles>();
            for (AccessRoles accessRolesListNewAccessRolesToAttach : accessRolesListNew) {
                accessRolesListNewAccessRolesToAttach = em.getReference(accessRolesListNewAccessRolesToAttach.getClass(), accessRolesListNewAccessRolesToAttach.getId());
                attachedAccessRolesListNew.add(accessRolesListNewAccessRolesToAttach);
            }
            accessRolesListNew = attachedAccessRolesListNew;
            accessPerfiles.setAccessRolesList(accessRolesListNew);
            accessPerfiles = em.merge(accessPerfiles);
            for (AccessConfigUser accessConfigUserListNewAccessConfigUser : accessConfigUserListNew) {
                if (!accessConfigUserListOld.contains(accessConfigUserListNewAccessConfigUser)) {
                    AccessPerfiles oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser = accessConfigUserListNewAccessConfigUser.getIdPerfiles();
                    accessConfigUserListNewAccessConfigUser.setIdPerfiles(accessPerfiles);
                    accessConfigUserListNewAccessConfigUser = em.merge(accessConfigUserListNewAccessConfigUser);
                    if (oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser != null && !oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser.equals(accessPerfiles)) {
                        oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser.getAccessConfigUserList().remove(accessConfigUserListNewAccessConfigUser);
                        oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser = em.merge(oldIdPerfilesOfAccessConfigUserListNewAccessConfigUser);
                    }
                }
            }
            for (AccessRoles accessRolesListNewAccessRoles : accessRolesListNew) {
                if (!accessRolesListOld.contains(accessRolesListNewAccessRoles)) {
                    AccessPerfiles oldIdPerfilOfAccessRolesListNewAccessRoles = accessRolesListNewAccessRoles.getIdPerfil();
                    accessRolesListNewAccessRoles.setIdPerfil(accessPerfiles);
                    accessRolesListNewAccessRoles = em.merge(accessRolesListNewAccessRoles);
                    if (oldIdPerfilOfAccessRolesListNewAccessRoles != null && !oldIdPerfilOfAccessRolesListNewAccessRoles.equals(accessPerfiles)) {
                        oldIdPerfilOfAccessRolesListNewAccessRoles.getAccessRolesList().remove(accessRolesListNewAccessRoles);
                        oldIdPerfilOfAccessRolesListNewAccessRoles = em.merge(oldIdPerfilOfAccessRolesListNewAccessRoles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = accessPerfiles.getId();
                if (findAccessPerfiles(id) == null) {
                    throw new NonexistentEntityException("The accessPerfiles with id " + id + " no longer exists.");
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
            AccessPerfiles accessPerfiles;
            try {
                accessPerfiles = em.getReference(AccessPerfiles.class, id);
                accessPerfiles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The accessPerfiles with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<AccessConfigUser> accessConfigUserListOrphanCheck = accessPerfiles.getAccessConfigUserList();
            for (AccessConfigUser accessConfigUserListOrphanCheckAccessConfigUser : accessConfigUserListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AccessPerfiles (" + accessPerfiles + ") cannot be destroyed since the AccessConfigUser " + accessConfigUserListOrphanCheckAccessConfigUser + " in its accessConfigUserList field has a non-nullable idPerfiles field.");
            }
            List<AccessRoles> accessRolesListOrphanCheck = accessPerfiles.getAccessRolesList();
            for (AccessRoles accessRolesListOrphanCheckAccessRoles : accessRolesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This AccessPerfiles (" + accessPerfiles + ") cannot be destroyed since the AccessRoles " + accessRolesListOrphanCheckAccessRoles + " in its accessRolesList field has a non-nullable idPerfil field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(accessPerfiles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AccessPerfiles> findAccessPerfilesEntities() {
        return findAccessPerfilesEntities(true, -1, -1);
    }

    public List<AccessPerfiles> findAccessPerfilesEntities(int maxResults, int firstResult) {
        return findAccessPerfilesEntities(false, maxResults, firstResult);
    }

    private List<AccessPerfiles> findAccessPerfilesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AccessPerfiles.class));
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

    public AccessPerfiles findAccessPerfiles(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AccessPerfiles.class, id);
        } finally {
            em.close();
        }
    }

    public int getAccessPerfilesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AccessPerfiles> rt = cq.from(AccessPerfiles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
