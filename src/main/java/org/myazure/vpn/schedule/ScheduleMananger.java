package org.myazure.vpn.schedule;

import java.sql.Date;

import org.myazure.vpn.configuration.PrimaryConfiguration;
import org.myazure.vpn.controller.VPNTrafficeController;
import org.myazure.vpn.controller.VPNUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleMananger {
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	@Autowired
	VPNUserController vpnUserController;
	@Autowired
	VPNTrafficeController vpnTrafficeController;
	private static final Logger LOG = LoggerFactory
			.getLogger(ScheduleMananger.class);

	public ScheduleMananger(){
		
	}
	public ScheduleMananger(PrimaryConfiguration primaryConfiguration) {
		this.primaryConfiguration=primaryConfiguration;
	}

	@Scheduled(cron = "0/1 * *  * * ? ")
	protected void secScanner() {
		System.out.print(".");
	}
	
	@Scheduled(cron = "0/30 * *  * * ? ")
	protected void thirtySecScanner() {
		System.out.println("$");
	}

	@Scheduled(cron = "0 0/1 *  * * ? ")
	protected void minScanner() {
		System.out.println("!");
	}
	@Scheduled(cron = "0 0/10 *  * * ? ")
	protected void tenMinScanner() {
		System.out.println("~");
		vpnTrafficeController.readTrafficNow();
	}
	@Scheduled(cron = "0 0/30 *  * * ? ")
	protected void halfHourScanner() {
		System.out.println("~");
		vpnTrafficeController.trafficStatisticsAllusers();
	}
	@Scheduled(cron = "0 0 0/1  * * ? ")
	protected void hourScanner() {
		System.out.println("#");
		vpnTrafficeController.readTrafficHistory();
	}

	@Scheduled(cron = "0 0 0 0/1 * ? ")
	protected void daylyScanner() {
		System.out.println("&");
		vpnUserController.readAllUser();
	}
	
	
}
