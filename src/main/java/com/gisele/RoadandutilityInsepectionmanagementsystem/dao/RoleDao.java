package com.gisele.RoadandutilityInsepectionmanagementsystem.dao;


import com.gisele.RoadandutilityInsepectionmanagementsystem.Domain.Role;

public interface RoleDao {

	public Role findRoleByName(String theRoleName);
	
}
