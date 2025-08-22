package com.sismics.reader.core.dao.jpa;

import com.sismics.reader.core.model.jpa.Role;
import com.sismics.util.context.ThreadLocalContext;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class RoleDao {
    
    /**
     * Creates a new role.
     * 
     * @param role Role to create
     * @return Created role
     */
    public Role create(Role role) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(role);
        return role;
    }
    
    /**
     * Updates a role.
     * 
     * @param role Role to update
     * @return Updated role
     */
    public Role update(Role role) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        return em.merge(role);
    }
    
    /**
     * Gets a role by ID.
     * 
     * @param id Role ID
     * @return Role
     */
    public Role getById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        return em.find(Role.class, id);
    }
}