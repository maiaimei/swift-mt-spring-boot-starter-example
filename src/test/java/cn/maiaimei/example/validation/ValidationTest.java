package cn.maiaimei.example.validation;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.ValidationTestConfig;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.ValidationEngine;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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

    protected MT798 mockMT798(String subMessageType) {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field12(subMessageType));
        mt798.append(new Field77E());
        return mt798;
    }

    protected void validateField(MT798 mt798, String tagName, String tagValue) {
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.getSwiftMessage().getBlock4().removeTag(tagName);
        mt798.append(new Tag(tagName, tagValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateField(MT798 mt798, String sequenceStartTagName, String tagName, String tagValue) {
        mt798.append(new Tag(sequenceStartTagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.getSwiftMessage().getBlock4().removeTag(tagName);
        mt798.append(new Tag(tagName, tagValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

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

    protected String generateValue(int rowCount, int maxLengthExclusive) {
        String rowValue = RandomStringUtils.randomAlphanumeric(1, maxLengthExclusive)
                .concat(new Random().nextBoolean() ? RandomStringUtils.random(1) : RandomStringUtils.randomAscii(1))
                .concat("\r\n");
        return generateValue(rowCount, rowValue);
    }

    protected String generateValue(int rowCount, String rowValue) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowCount; i++) {
            builder.append(rowValue)
                    .append("\r\n");
        }
        return builder.toString();
    }
}
