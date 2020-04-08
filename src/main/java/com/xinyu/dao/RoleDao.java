package com.xinyu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xinyu.bean.PageBean;
import com.xinyu.model.Role;

/**
 * 用户角色dao
 * @author zhuwenxin
 * 
 */
@Mapper
public interface RoleDao {

	public List<Role> getAllRole(String name);

	public int getCountRoleCriteriaQuery(@Param("d")Role d);

	public List<Role> getRoleList(@Param("page")PageBean pagebean, @Param("d")Role d);

	public Role getRoleById(Long id);

	public void updateRole(@Param("d")Role d);

	public void deleteRoleMenu(Long id);

	public void saveRoleMenu(Long id, List<Long> list);

    
}
