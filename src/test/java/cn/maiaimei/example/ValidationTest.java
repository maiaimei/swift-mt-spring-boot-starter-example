package cn.maiaimei.example;

import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.GenericMTValidationEngine;
import cn.maiaimei.framework.swift.validation.engine.MT798ValidationEngine;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ConfigDataApplicationContextInitializer 可在测试类中加载yml
 */
@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
public class ValidationTest extends BaseTest {
    @Autowired
    MT798ValidationEngine mt798ValidationEngine;

    @Autowired
    GenericMTValidationEngine genericMtValidationEngine;

    @Test
    void testValidateMT798() {
        MT798 mt798 = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        ValidationResult result = mt798ValidationEngine.validate(mt798);
        printValidationResult(result);
    }

    @Test
    void testValidateMT9xx() {
        String message = readFileAsString("mt/mt9xx/MT940.txt");
        ValidationResult result = genericMtValidationEngine.validate(message);
        printValidationResult(result);
    }

    void printValidationResult(ValidationResult result) {
        if (!CollectionUtils.isEmpty(result.getErrorMessages())) {
            result.getErrorMessages().forEach(log::info);
            assertFalse(CollectionUtils.isEmpty(result.getErrorMessages()));
        } else {
            log.info("Validate success");
            assertTrue(CollectionUtils.isEmpty(result.getErrorMessages()));
        }
    }
}
