package org.myazure.web.controller;

import org.myazure.configuration.PrimaryConfiguration;
import org.myazure.service.MyazureDataService;
import org.myazure.vpn.controller.VPNTrafficeController;
import org.myazure.vpn.controller.VPNUserController;
import org.myazure.vpn.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller("OrderController")
public class OrderController {
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	MyazureDataService myazureDataService;
	@Autowired
	VPNTrafficeController vpnTrafficeController;
	@Autowired
	private VPNUserController vpnUserController;

	public OrderController() {

	}

	public Order creatOrder(String username, String price, String data) {
		return new Order();
	}

}
