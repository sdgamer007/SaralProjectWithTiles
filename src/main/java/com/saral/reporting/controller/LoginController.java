package com.saral.reporting.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.google.gson.Gson;
import com.saral.reporting.DAO.LoginDAO;
import com.saral.reporting.model.AttributeMasterData;
import com.saral.reporting.model.ReportBean;
import com.saral.reporting.model.ReportSelectColumn;
import com.saral.reporting.model.ServiceMaster;
import com.saral.reporting.service.AttributeMasterDataService;
import com.saral.reporting.service.ReportBeanService;
import com.saral.reporting.service.ServiceMasterService;

@Transactional
@Controller
@SessionAttributes({ "sign_no", "user_id", "user_name", "hm", "department_level_name", "department_id",
		"designation_id", "designation_name" })
public class LoginController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private ServiceMasterService serviceMasterService;

	@Autowired
	private AttributeMasterDataService attributeMasterDataService;

	@Autowired
	private ReportBeanService reportBeanService;
	
	@Autowired
	private LoginDAO loginDAO;

	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.GET)
	public String showLoginPage(ModelMap model) {

		return "login";
	}
	
	@RequestMapping(value = { "/login", "/" }, method = RequestMethod.POST)
	public String showWelcomePage(ModelMap model, @RequestParam String sign_no, @RequestParam String password) {
		
		List<Object[]> values = loginDAO.validateUser(sign_no, password);
		if (sign_no.equals(null) || sign_no == "") {

			System.out.println("here123");
			model.put("nameErrorMessage", "UserName cannnot be empty");
			return "login";

		}

		else if ((password.equals(null) || password == "")) {
			System.out.println("here123");
			model.put("passwordErrorMessage", "Password cannnot be empty");
			return "login";
		}

		Object[] l1 = values.get(0);

		String sign_no1 = (String) l1[0];
		Integer user_id = (Integer) l1[1];
		String user_name = (String) l1[2];
		String department_level_name = (String) l1[5];
		Integer department_id = (Integer) l1[6];
		Integer designation_id = (Integer) l1[7];
		String designation_name = (String) l1[8];
		HashMap<Integer, String> hm = new HashMap<>();

		for (Object[] result : values) {
			Integer role_id = (Integer) result[3];
			String role_name = (String) result[4];
			hm.put(role_id, role_name);
		}

		String json = new Gson().toJson(hm);

		System.out.println("ss");
		model.put("sign_no", sign_no1);
		model.put("user_id", user_id);
		model.put("user_name", user_name);
		model.put("department_level_name", department_level_name);
		model.put("department_id", department_id);
		model.put("designation_id", designation_id);
		model.put("designation_name", designation_name);
		model.put("hm", json);

		return "welcome";
	}

	@RequestMapping(value = "/DesignReport", method = { RequestMethod.GET }, produces = "application/json")
	public ResponseEntity<?> showDesignReport(ModelMap model, @RequestParam String deptid, @RequestParam String action,
			HttpServletRequest request) throws ServletException, IOException {
		String action1 = action;

		System.out.println(action1);
		if (action.equals("fetchService")) {
			System.out.println("In side fetch service");
			List<ServiceMaster> serviceList = serviceMasterService.findByDeptCode(deptid);

			Map<String, String> mapList = new HashMap<String, String>();
			for (ServiceMaster i : serviceList)
				mapList.put(i.getServiceCode(), i.getServiceName());

			model.put("serviceList", mapList);
			String json = new Gson().toJson(mapList);
			return ResponseEntity.ok(json);
		}
		return (ResponseEntity<?>) ResponseEntity.ok();
	}

	@RequestMapping(value = "/DesignReportCol", method = { RequestMethod.POST }, produces = "application/json")
	public ResponseEntity<?> showDesignReportCol(ModelMap model, @RequestParam String deptid,
			@RequestParam String action, @RequestParam String serviceid, HttpServletRequest request)
			throws ServletException, IOException {
		String action1 = action;

		System.out.println(action1);
		if (action.equals("fetchColumns")) {
			System.out.println("In side fetch columns");
			List<AttributeMasterData> list = attributeMasterDataService.findByTaskid(serviceid);
			Map<String, String> mapColList = new HashMap<String, String>();
			for (AttributeMasterData i : list)
				mapColList.put(i.getAttributeId(), i.getAttributeLabel());

			model.put("serviceList", mapColList);
			String jsonCol = new Gson().toJson(mapColList);
			return ResponseEntity.ok(jsonCol);

		}
		return (ResponseEntity<?>) ResponseEntity.ok();
	}

	@RequestMapping(value = { "/SaveReport" }, method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<?> saveDesignerReport(HttpServletRequest request) {
		ReportBean reportBean = new ReportBean();

		Long departmentID = Long.parseLong(request.getParameter("departmentID"));
		Long serviceID = Long.parseLong(request.getParameter("serviceID"));
		System.out.println("service ID is :" + serviceID);
		System.out.println("dep ID is :" + departmentID);

		reportBean.setReportName(request.getParameter("rpName"));
		reportBean.setTableColor(request.getParameter("rpColor"));
		reportBean.setDepartmentId(departmentID);
		reportBean.setServiceId(serviceID);
		reportBean.setUserId(request.getParameter("userID"));
		reportBean.setVersionNo(0L); // need to get front end
		reportBean.setDesignationId(111L); // need to get front end
		reportBean.setTooltip(request.getParameter("tooltip"));
		reportBean.setWhereCondition(request.getParameter("rpWhrCondition"));
		/* report.setGrouping(request.getParameter("rpGrpBy")); */
		String grpby = request.getParameter("grpIdName");
		if (grpby.equals("0-Please Select")) {
			reportBean.setGrouping("");
		} else {
			reportBean.setGrouping(grpby);
		}

		/* report.setGrouping(request.getParameter("grpIdName")); */
		System.out.println("Group by :" + reportBean.getGrouping());
		reportBean.setBackgroundText(request.getParameter("bgtext"));
		reportBean.setFilterCls(""); // need to discuss
		reportBean.setHavingCls(""); // need to discuss
		reportBean.setTableFormat(""); // need to discuss
		// reportBean.setFooter_image_id(""); // need to discuss
		// reportBean.setFooter_image_id(""); // need to discuss
		System.out.println("Report Header is" + request.getParameter("rpHeader"));
		reportBean.setReport_header(request.getParameter("rpHeader"));
		reportBean.setReport_footer(request.getParameter("rpFooter"));

		String selectedColList = request.getParameter("selectedColList");
		System.out.println("Selected selectedColList list : " + selectedColList);

		JSONArray jsonArrObject = new JSONArray(selectedColList);
		System.out.println("jsonob " + jsonArrObject);
		List<ReportSelectColumn> reportSelectColumnlist = new ArrayList<ReportSelectColumn>();
		int len = jsonArrObject.length();

		for (int i = 0; i < len; i++) {
			ReportSelectColumn col = new ReportSelectColumn();
			JSONObject objc = jsonArrObject.getJSONObject(i);
			col.setReportSelectedColumnId(objc.getString("Value"));
			col.setReportSelectedColumnName(objc.getString("key"));
			reportSelectColumnlist.add(col);
			reportBean.addReportSelectColumn(col);

			System.out.println(col);

		}
		reportBean.setReportSelectColumnList(reportSelectColumnlist);
		System.out.println("1" + reportSelectColumnlist + " sss" + reportBean.getReportId());
		reportBean.setReportSelectColumnList(reportSelectColumnlist);
		reportBeanService.save(reportBean);

		return null;

	}

}