package jpaBook.jpaShop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import jpaBook.jpaShop.domain.Address;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpaShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpaShopApplication.class, args);
	}
}
