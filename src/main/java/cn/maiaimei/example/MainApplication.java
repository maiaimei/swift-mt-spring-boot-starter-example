package cn.maiaimei.example;

import cn.maiaimei.framework.swift.annotation.EnableSwiftMT;
import cn.maiaimei.framework.swift.validation.config.model.MessageValidationCfg;
import cn.maiaimei.framework.swift.validation.engine.GenericValidationEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@EnableSwiftMT
//@EnableSwiftMT7xx
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(MessageValidationCfg.class);
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("{} -> {}", beanDefinitionName, applicationContext.getBean(beanDefinitionName));
        }
        GenericValidationEngine genericValidationEngine = applicationContext.getBean(GenericValidationEngine.class);
        System.out.println();
    }
}
