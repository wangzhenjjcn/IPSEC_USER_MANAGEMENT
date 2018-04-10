package org.myazure.vpn.schedule;


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
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ScheduleMananger.class);

	public ScheduleMananger(){
		
	}
	public ScheduleMananger(PrimaryConfiguration primaryConfiguration) {
		this.primaryConfiguration=primaryConfiguration;
	}

	@Scheduled(cron = "0/1 * *  * * ? ")
	protected void secScanner() {
	}
	
	@Scheduled(cron = "0/30 * *  * * ? ")
	protected void thirtySecScanner() {
	}

	@Scheduled(cron = "0 0/1 *  * * ? ")
	protected void minScanner() {
	}
	@Scheduled(cron = "0 0/10 *  * * ? ")
	protected void tenMinScanner() {
		vpnTrafficeController.readTrafficNow();
	}
	@Scheduled(cron = "0 0/30 *  * * ? ")
	protected void halfHourScanner() {
		vpnTrafficeController. readAllUserUsage();
	}
	@Scheduled(cron = "0 0 0/1  * * ? ")
	protected void hourScanner() {
	}

	@Scheduled(cron = "0 0 0 0/1 * ? ")
	protected void daylyScanner() {
	}
	
	
}