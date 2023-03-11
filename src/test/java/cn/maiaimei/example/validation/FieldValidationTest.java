package cn.maiaimei.example.validation;

import com.prowidesoftware.swift.model.field.Field52A;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

public class FieldValidationTest extends ValidationTest {
    void validateField(String tagName, String invalidValue) {
        MT798 mt798 = mockMT798("761");
        validateField(mt798, tagName, invalidValue);
    }

    @Test
    void testValidate52a() {
        validateField(Field52A.NAME, RandomStringUtils.randomAlphanumeric(11));
    }
}
