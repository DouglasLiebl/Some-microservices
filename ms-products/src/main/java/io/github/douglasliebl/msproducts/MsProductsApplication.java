package io.github.douglasliebl.msproducts;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import javax.management.modelmbean.ModelMBean;

@SpringBootApplication
@EnableDiscoveryClient
public class MsProductsApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	public static void main(String[] args) {
		SpringApplication.run(MsProductsApplication.class, args);
	}

}
