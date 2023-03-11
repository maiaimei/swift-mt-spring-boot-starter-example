package cn.maiaimei.example.validation;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.ValidationTestConfig;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.ValidationEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ConfigDataApplicationContextInitializer 可在测试类中加载yml
 */
@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = ValidationTestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
public class ValidationTest extends BaseTest {

    @Autowired
    protected ValidationEngine validationEngine;

    protected void assertAndPrintResult(ValidationResult result) {
        assertAndPrintResult(result, null);
    }

    protected void assertAndPrintResult(ValidationResult result, Predicate<String> predicate) {
        List<String> errorMessages = result.getErrorMessages();
        if (predicate != null) {
            errorMessages = errorMessages.stream().filter(predicate).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(errorMessages)) {
            errorMessages.forEach(log::info);
            assertFalse(CollectionUtils.isEmpty(errorMessages));
        } else {
            assertTrue(CollectionUtils.isEmpty(errorMessages));
        }
    }

    protected String generateValue(int rowcount, int maxlength) {
        String str = new Random().nextBoolean() ? RandomStringUtils.random(1) : RandomStringUtils.randomAscii(1);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowcount; i++) {
            builder.append(RandomStringUtils.randomAlphanumeric(1, maxlength))
                    .append(str)
                    .append("\r\n");
        }
        return builder.toString();
    }
}
