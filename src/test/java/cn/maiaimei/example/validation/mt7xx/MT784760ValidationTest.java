package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.validation.ValidationTest;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field15A;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

public class MT784760ValidationTest extends ValidationTest {
    @Test
    void testValidateMT760() {
        MT798 mt798 = readFileAsMT798("mt/mt7xx/MT784_760.txt");
        ValidationResult result = validationEngine.validate(mt798);
        assertAndPrintResult(result);
    }

    @Test
    void testValidate15A() {
        MT798 mt798 = mockMT798("760");
        mt798.append(new Tag(Field15A.NAME, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798));
    }
}
