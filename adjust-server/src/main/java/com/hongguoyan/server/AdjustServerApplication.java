package com.hongguoyan.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目的启动类
 * @author dd
 */
@SuppressWarnings("SpringComponentScan") // 忽略 IDEA 无法识别 ${adjust.info.base-package}
@SpringBootApplication(scanBasePackages = {"${adjust.info.base-package}.server", "${adjust.info.base-package}.module"})
public class AdjustServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdjustServerApplication.class, args);
    }

}
