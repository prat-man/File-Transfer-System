package tk.pratanumandal.fts.app;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import tk.pratanumandal.fts.util.FtsConstants;

@SpringBootApplication
@ComponentScan(basePackages={"tk.pratanumandal.fts"})
public class FtsApplication {

	public static void main(String[] args) {
		if (args.length > 0) {
			FtsConstants.CONFIG_FILE = new File(args[0]).getAbsolutePath();
		}
		
		SpringApplication.run(FtsApplication.class);
	}
	
	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(FtsApplication.class);
	}

}
