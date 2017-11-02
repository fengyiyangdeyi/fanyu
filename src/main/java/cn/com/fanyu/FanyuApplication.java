package cn.com.fanyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class FanyuApplication {

	public static void main(String[] args) {
		SpringApplication.run(FanyuApplication.class, args);
	}
}
