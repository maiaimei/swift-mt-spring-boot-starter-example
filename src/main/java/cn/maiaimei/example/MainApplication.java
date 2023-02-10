package cn.maiaimei.example;

import cn.maiaimei.framework.swift.validation.config.MessageValidationConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class MainApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainApplication.class, args);
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(MessageValidationConfig.class);
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("{} -> {}", beanDefinitionName, applicationContext.getBean(beanDefinitionName));
        }
    }
}
