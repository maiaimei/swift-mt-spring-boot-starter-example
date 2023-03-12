package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.validation.ValidationTest;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class MT784761ValidationTest extends ValidationTest {
    void validateField(String tagName, String tagValue) {
        MT798 mt798 = mockMT798("761");
        validateField(mt798, tagName, tagValue);
    }

    @Test
    void testValidate52a() {
        MT798 mt798 = mockMT798("761");
        mt798.append(new Tag(Field27A.NAME, "1/1"));
        mt798.append(new Tag(Field21A.NAME, RandomStringUtils.randomAlphanumeric(16)));
        mt798.append(new Tag(Field27.NAME, "1/1"));
        mt798.append(new Tag(Field20.NAME, RandomStringUtils.randomAlphanumeric(16)));
        ValidationResult result = validationEngine.validate(mt798);
        assertAndPrintResult(result);
    }

    @Test
    void testValidate52A() {
        validateField(Field52A.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field52A.NAME, RandomStringUtils.randomAlphanumeric(11));
        validateField(Field52A.NAME, "HSBCHKHHHKH");
        validateField(Field52A.NAME, "/XHSBCHKHHHKH");
        validateField(Field52A.NAME, "/X/XHSBCHKHHHKH");
        validateField(Field52A.NAME, "/X/X\r\nHSBCHKHHHKH");
        validateField(Field52A.NAME, "/C/X\r\nHSBCHKHHHKH");
        validateField(Field52A.NAME, "/D/X\r\nHSBCHKHHHKH");
    }

    @Test
    void testValidate52D() {
        validateField(Field52D.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field52D.NAME, RandomStringUtils.randomAlphanumeric(11));
    }

    @Test
    void testValidate77U() {
        validateField(Field77U.NAME, generateValue(151, RandomStringUtils.randomAlphanumeric(10)));
        validateField(Field77U.NAME, generateValue(2, RandomStringUtils.randomAlphanumeric(10).concat(RandomStringUtils.random(1))));
    }
}
