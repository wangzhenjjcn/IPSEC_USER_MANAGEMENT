package org.myazure.vpn.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import weixin.popular.support.TokenManager;


@WebListener
public class TokenManagerListener implements ServletContextListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(TokenManagerListener.class);
	@Value("${weixin.compAppId}")
	private String appid;
	@Value("${weixin.compAppSecret}")
	private String secret;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// WEB容器 初始化时调用
//		TokenManager.setPoolSize(8);
//		TokenManager.setDaemon(true);
//		TokenManager.init(appid, secret);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// WEB容器 关闭时调用
//		TokenManager.destroyed();
	}
}