package cn.maiaimei.example;

import cn.maiaimei.framework.swift.config.SwiftAutoConfiguration;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.GenericMTValidationEngine;
import cn.maiaimei.framework.swift.validation.engine.MT798ValidationEngine;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {SwiftAutoConfiguration.class})
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

    @SneakyThrows
        //@Test
    void testValidateMTXxx() {
        String message = readFileAsString("mt/mt9xx/MT940.txt");
        ValidationResult result = genericMtValidationEngine.validate(message, "940");
        printValidationResult(result);
    }

    void printValidationResult(ValidationResult result) {
        if (!CollectionUtils.isEmpty(result.getErrorMessages())) {
            assertFalse(CollectionUtils.isEmpty(result.getErrorMessages()));
            log.info("Validate error");
            for (String errorMessage : result.getErrorMessages()) {
                log.info(errorMessage);
            }
        } else {
            assertTrue(CollectionUtils.isEmpty(result.getErrorMessages()));
            log.info("Validate success");
        }
    }
}
