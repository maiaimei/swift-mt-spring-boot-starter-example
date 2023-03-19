package cn.maiaimei.example.config;

import cn.maiaimei.framework.swift.config.SwiftAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(SwiftAutoConfiguration.class)
@PropertySource("classpath:application.yml")
public class TestConfig {}
