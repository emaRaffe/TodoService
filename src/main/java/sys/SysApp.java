package sys;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration
@SpringBootApplication
@EnableScheduling
public class SysApp {
    public static void main(final String[] args) {
	SpringApplication.run(SysApp.class, args);
    }

    @Bean(value = "datasource")
    @ConfigurationProperties("app.datasource")
    public DataSource dataSource() {
	return DataSourceBuilder.create().build();
    }
}
