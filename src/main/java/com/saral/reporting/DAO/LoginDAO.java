package com.saral.reporting.DAO;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service 
public class LoginDAO {
	

	@PersistenceContext
	private EntityManager manager;
	
	  @Transactional
	public List<Object[]> validateUser(String sign_no, String password) {
		// TODO Auto-generated method stub'
System.out.println("sign _no"+sign_no);
		String queryStr = "SELECT loginData.signNo,loginData.userId,loginData.userName,"
				+ " roleAssignment.roleId, roleMaster.roleName, userLocation.departmentLevelName,"
				+ " userLocation.departmentId, userLocationDesignation.designationId, "
				+ " userLocationDesignation.designationName FROM  RoleAssignment roleAssignment ,LoginData loginData,"
				+ " RoleMaster roleMaster, UserLocation userLocation,UserLocationDesignation userLocationDesignation"
				+ " WHERE (loginData.userId = roleAssignment.userId) AND (roleAssignment.roleId =  roleMaster.roleId)"
				+ " AND (loginData.userId = userLocation.userId) AND (userLocation.userLocId = userLocationDesignation.userLocId)"
				+ " AND loginData.signNo ='" + sign_no + "'  AND loginData.passwd= '" + password + "' ";

		try {
		
			List<Object[]> results = manager.createQuery(queryStr).getResultList();

			return results;

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}


}
