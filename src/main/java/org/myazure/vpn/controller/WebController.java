package org.myazure.vpn.controller;

import org.myazure.vpn.configuration.PrimaryConfiguration;
import org.myazure.vpn.service.MyazureDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class WebController {
	private static final Logger LOG = LoggerFactory
			.getLogger(WebController.class);
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	private MyazureDataService myazureDataService;
	
	
	public WebController(){
		
	}
	
	
	
}
