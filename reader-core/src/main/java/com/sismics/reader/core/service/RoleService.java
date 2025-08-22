package com.sismics.reader.core.service;

import com.sismics.reader.core.dao.jpa.RoleBaseFunctionDao;
import com.sismics.reader.core.dao.jpa.RoleDao;
import com.sismics.reader.core.model.jpa.Role;
import com.sismics.reader.core.model.jpa.RoleBaseFunction;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RoleService {
    private RoleDao roleDao;
    private RoleBaseFunctionDao roleBaseFunctionDao;

    public RoleService() {
        this.roleDao = new RoleDao();
        this.roleBaseFunctionDao = new RoleBaseFunctionDao();
    }

    /**
     * Creates a new role.
     * 
     * @param name Role name
     * @return Created role
     */
    public Role createRole(String name) {
        Role role = new Role();
        role.setId(UUID.randomUUID().toString());
        role.setName(name);
        role.setCreateDate(new Date());
        roleDao.create(role);
        return role;
    }

    /**
     * Deletes a role.
     * 
     * @param roleId Role ID
     */
    public void deleteRole(String roleId) {
        Role role = roleDao.getById(roleId);
        if (role != null) {
            role.setDeleteDate(new Date());
            roleDao.update(role);
        }
    }

    /**
     * Gets role base functions.
     * 
     * @param roleId Role ID
     * @return Set of base function IDs
     */
    public Set<String> getRoleBaseFunctions(String roleId) {
        return roleBaseFunctionDao.findByRoleId(roleId);
    }

    /**
     * Assigns a base function to a role.
     * 
     * @param roleId Role ID
     * @param baseFunctionId Base function ID
     */
    public void assignBaseFunction(String roleId, String baseFunctionId) {
        RoleBaseFunction roleBaseFunction = new RoleBaseFunction();
        roleBaseFunction.setId(UUID.randomUUID().toString());
        roleBaseFunction.setRoleId(roleId);
        roleBaseFunction.setBaseFunctionId(baseFunctionId);
        roleBaseFunction.setCreateDate(new Date());
        // Add method to save roleBaseFunction
    }
}