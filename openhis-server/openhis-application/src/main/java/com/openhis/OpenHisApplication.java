package com.openhis;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.openhis.web.ybmanage.config.YbServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 启动程序
 * 
 * @author system
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.core", "com.openhis"})
@EnableConfigurationProperties(YbServiceConfig.class)
public class OpenHisApplication {
    public static void main(String[] args) throws UnknownHostException {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(OpenHisApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("\n----------------------------------------------------------\n\t"
            + "Application OpenHis is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:" + port + path
            + "/\n\t" + "External: \thttp://" + ip + ":" + port + path + "/\n"
            + "----------------------------------------------------------");
    }
}
