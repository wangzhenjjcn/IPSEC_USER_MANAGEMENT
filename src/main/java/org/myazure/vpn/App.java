package org.myazure.vpn;

import org.myazure.vpn.configuration.MyazureEmbeddedServletContainerCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author wangzhen Hello world!
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
@ServletComponentScan
@ComponentScan
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
public class App {
	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletCustomizer() {
		return new MyazureEmbeddedServletContainerCustomizer();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@SuppressWarnings("unused")
	private ClientHttpRequestFactory clientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(10000);
		factory.setConnectTimeout(10000);
		return factory;
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		LOG.info("System Ready!!");
	}
}
