package com.xq.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@SpringBootApplication
// mapper 接口类扫描包配置
@MapperScan("com.xq.live.dao")
@Controller
public class LiveServiceApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LiveServiceApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LiveServiceApplication.class, args);
    }

    @RequestMapping(value="/",method= RequestMethod.GET)
    public String getScan(Model model){
        return "index";
    }
}
