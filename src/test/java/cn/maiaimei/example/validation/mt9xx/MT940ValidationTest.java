package cn.maiaimei.example.validation.mt9xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import org.junit.jupiter.api.Test;

class MT940ValidationTest extends BaseContextTest {
    @Test
    void testValidateMT9xx() {
        String message = readFileAsString("mt/mt9xx/MT940.txt");
        ValidationResult result = validationEngine.validate(message);
        assertAndPrintResult(result);
    }
}
