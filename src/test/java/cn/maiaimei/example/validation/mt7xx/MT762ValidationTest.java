package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

public class MT762ValidationTest extends BaseContextTest {
    @Test
    void validateMT762() {
        MT798 mt798 = readFileAsMT798("mt/mt7xx/MT762_762.txt");
        ValidationResult result = validationEngine.validate(mt798, "762", "762");
        assertAndPrintResult(result);
    }

    @Test
    void validateMT760() {
        MT798 mt798 = readFileAsMT798("mt/mt7xx/MT762_760.txt");
        ValidationResult result = validationEngine.validate(mt798, "762", "760");
        assertAndPrintResult(result);
    }
}
