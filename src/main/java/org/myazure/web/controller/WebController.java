package org.myazure.web.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myazure.configuration.PrimaryConfiguration;
import org.myazure.response.StatusResponse;
import org.myazure.service.MyazureDataService;
import org.myazure.vpn.controller.VPNTrafficeController;
import org.myazure.vpn.controller.VPNUserController;
import org.myazure.vpn.entity.PurchasedData;
import org.myazure.vpn.entity.TrafficData;
import org.myazure.vpn.entity.UserData;
import org.myazure.vpn.response.UsersListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

@Controller
public class WebController {
	private static final Logger LOG = LoggerFactory.getLogger(WebController.class);
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	MyazureDataService myazureDataService;
	@Autowired
	VPNTrafficeController vpnTrafficeController;
	@Autowired
	VPNUserController vpnUserController;

	public WebController() {
	}

	@RequestMapping(path = "/hongkong/adduser", method = RequestMethod.POST)
	public void addUser(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("username") != null && request.getParameter("passwd") != null && request.getParameter("data") != null && request.getParameter("token") != null) {
			try {
				String username = request.getParameter("username");
				String passwd = request.getParameter("passwd");
				long data = Long.valueOf(request.getParameter("data").replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("T", "000000000000")
						.replace("B", ""));
				vpnUserController.addUser(username, passwd);
				vpnUserController.addUserData(username, data);
				StatusResponse res = new StatusResponse("AddUser[" + request.getParameter("username") + "]Sucess", true);
				sentResponse(response, res);
				LOG.debug("ALLDONE");
			} catch (Exception e) {
				StatusResponse res = new StatusResponse(e.getMessage(), false);
				sentResponse(response, res);
			}
		} else {
			StatusResponse res = new StatusResponse("缺少参数", false);
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
			sentResponse(response, res);
		}
		return;
	}

	@RequestMapping(path = "/hongkong/deluser", method = RequestMethod.POST)
	public void delUser(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("username") != null && request.getParameter("token") != null) {
			try {
				String username = request.getParameter("username");
				vpnUserController.deleteUser(username);
				StatusResponse res = new StatusResponse("DeleteUser[" + request.getParameter("username") + "]Sucess", true);
				sentResponse(response, res);
				LOG.debug("ALLDONE");
			} catch (Exception e) {
				StatusResponse res = new StatusResponse(e.getMessage(), false);
				sentResponse(response, res);
			}
		} else {
			StatusResponse res = new StatusResponse("缺少参数", false);
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
			sentResponse(response, res);
		}
		return;
	}

	@RequestMapping(path = "/hongkong/moduser", method = RequestMethod.POST)
	public void modifyUser(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("username") != null && request.getParameter("passwd") != null && request.getParameter("token") != null) {
			try {
				String username = request.getParameter("username");
				String passwd = request.getParameter("passwd");
				vpnUserController.modifyUser(username, passwd);
				StatusResponse res = new StatusResponse("ModifyUser[" + request.getParameter("username") + "]Sucess", true);
				sentResponse(response, res);
				LOG.debug("ALLDONE");
			} catch (Exception e) {
				StatusResponse res = new StatusResponse(e.getMessage(), false);
				sentResponse(response, res);
			}
		} else {
			StatusResponse res = new StatusResponse("缺少参数", false);
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
			sentResponse(response, res);
		}
		return;
	}

	@RequestMapping(path = "/hongkong/adddata", method = RequestMethod.POST)
	public void addUserData(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("username") != null && request.getParameter("data") != null && request.getParameter("token") != null) {
			try {
				String username = request.getParameter("username");
				long data = Long.valueOf(request.getParameter("data").replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("T", "000000000000")
						.replace("B", ""));
				vpnUserController.addUserData(username, data);
				StatusResponse res = new StatusResponse("AddUserData[" + request.getParameter("username") + "]Sucess", true);
				sentResponse(response, res);
			} catch (Exception e) {
				StatusResponse res = new StatusResponse(e.getMessage(), false);
				sentResponse(response, res);
			}
		} else {
			StatusResponse res = new StatusResponse("缺少参数", false);
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
			sentResponse(response, res);
		}
		return;
	}

	@RequestMapping(path = "/hongkong/search", method = RequestMethod.GET)
	public void viewAllUser(HttpServletRequest request, HttpServletResponse response) {
		UsersListResponse res = new UsersListResponse();
		List<UserData> users = new ArrayList<UserData>();
		for (String username : VPNUserController.userList) {
			users.add(VPNUserController.users.get(username));
		}
		res.setUsers(users);
		sentResponse(response, res);
	}

	@RequestMapping(path = "/hongkong/addpaydata", method = RequestMethod.POST)
	public void addPurchasedData(HttpServletRequest request, HttpServletResponse response) {
		StatusResponse res = new StatusResponse("缺少参数", false);
		if (request.getParameter("username") != null && request.getParameter("pay") != null && request.getParameter("data") != null && request.getParameter("orderid") != null
				&& request.getParameter("paytime") != null && request.getParameter("token") != null) {
			String username = request.getParameter("username");
			String orderId = request.getParameter("orderid");
			long pay = Long.valueOf(request.getParameter("pay"));
			long data = Long.valueOf(request.getParameter("data").replace("K", "000").replace("M", "000000").replace("G", "000000000").replace("T", "000000000000")
					.replace("B", ""));
			long paytime = Long.valueOf(request.getParameter("paytime"));
			PurchasedData purchasedData = new PurchasedData(username, pay, paytime, data, orderId);
			myazureDataService.save(username + "_Purchased" + orderId, JSON.toJSONString(purchasedData));
			myazureDataService.save("ORDER_" + orderId, JSON.toJSONString(purchasedData));
			res = new StatusResponse();
		} else {
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
		}
		sentResponse(response, res);
		return;
	}

	@RequestMapping(path = "/hongkong/userdata", method = RequestMethod.GET)
	public void viewUserData(HttpServletRequest request, HttpServletResponse response) {
		if (request.getParameter("username") != null && request.getParameter("token") != null) {
			String username = request.getParameter("username");
			UsersListResponse res = new UsersListResponse();
			List<UserData> users = new ArrayList<UserData>();
			 users.add(VPNUserController.users.get(username));
			 
			 
			if (users.size() < 1) {
				long allin = 0;
				long allout = 0;
				List<TrafficData> usageData = new ArrayList<TrafficData>();
				List<PurchasedData> purchasedDatas = new ArrayList<PurchasedData>();
				long purchasedDataBytes = 0;
				for (int i = 0; i < 7; i++) {
					TrafficData trafficdata = new TrafficData();
					PurchasedData purchasedData = new PurchasedData(username, 12, System.currentTimeMillis(), 2000000L);
					trafficdata.setConnetIp("21.23.2.2");
					trafficdata.setConnetTime("" + System.currentTimeMillis());
					trafficdata.setDataIn("12312" + i);
					trafficdata.setDataOut("12333" + i);
					allin += Long.valueOf(trafficdata.getDataIn());
					allout += Long.valueOf(trafficdata.getDataOut());
					trafficdata.setLinkId("23" + i);
					trafficdata.setUsername(username);
					usageData.add(trafficdata);
					purchasedDatas.add(purchasedData);
					purchasedDataBytes += 2000000L;
				}
				users.add(new UserData(username, allout, allin, 22222222222L));
				users.get(0).setLastLinkDate(new Date(System.currentTimeMillis()));
				users.get(0).setActive(true);
				users.get(0).setPurchasedDataBytes(purchasedDataBytes);
				users.get(0).setUsageData(usageData);
				users.get(0).setPurchasedData(purchasedDatas);
			}
			res.setUsers(users);
			sentResponse(response, res);
		} else {
			StatusResponse res = new StatusResponse("缺少参数", false);
			if (request.getParameter("token") == null) {
				res = new StatusResponse("授权失败", false);
			}
			sentResponse(response, res);
		}
		return;
	}

	protected void sentResponse(HttpServletResponse response, Object object) {
		try {
			response.getWriter().write(JSON.toJSONString(object));
			response.getWriter().close();
			return;
		} catch (IOException e) {
			LOG.debug(e.getMessage());
			e.printStackTrace();
		}
	}
}
