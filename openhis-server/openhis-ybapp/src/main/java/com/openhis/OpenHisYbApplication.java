package com.openhis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import com.alibaba.fastjson.JSON;
import com.openhis.pojo.ResultBody;
import com.openhis.utils.EasyGmUtils;

/**
 * 启动程序
 * 
 * @author system
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.core", "com.openhis"})
public class OpenHisYbApplication {
    public static void main(String[] args) throws UnknownHostException {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(OpenHisYbApplication.class, args);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        System.out.println("医保启动");

        String msg = "\n" +
                "\n" +
                "{\"cainfo\":\"0n5jVjERPVUh3stB/tmS5rH1X+Cnh3145P4e/0OFDrWAiwtYtlBa/vxMhbVWV0NBWqVP7pakLzQhVb2cmGYdAA==\",\"err_msg\":\"成功\",\"inf_refmsgid\":\"220000202505301537082844878588\",\"infcode\":\"0\",\"output\":\"PznBlnVSzpDlyl54Vk3/hg==\",\"refmsg_time\":\"20250530153708362\",\"respond_time\":\"20250530153708430\",\"signtype\":\"SM2\"}";
        ResultBody response = JSON.parseObject(msg, ResultBody.class);
        System.out.println(com.alibaba.fastjson2.JSON.toJSONString(response));
        byte[] result = EasyGmUtils.sm4Decrypt("ACC10563D7382BF6".getBytes(StandardCharsets.UTF_8),
            Base64.getDecoder().decode(response.getOutput()));
        response.setOutput(new String(result));

        System.out.println(JSON.toJSONString(response));

        System.out.println("\n----------------------------------------------------------\n\t"
            + "Application OpenHis-YB is running! Access URLs:\n\t" + "Local: \t\thttp://localhost:" + port + path
            + "/\n\t" + "External: \thttp://" + ip + ":" + port + path + "/\n"
            + "----------------------------------------------------------");
    }
}
