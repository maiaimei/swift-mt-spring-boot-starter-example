package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.validation.ValidationTest;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class MT784784ValidationTest extends ValidationTest {

    void validateField(String tagName, String invalidValue) {
        validateField(mockMT798("784"), tagName, invalidValue);
    }

    @Test
    void testValidateMT784() {
        MT798 mt798 = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        ValidationResult result = validationEngine.validate(mt798);
        assertAndPrintResult(result);
    }

    @Test
    void testValidateField27A() {
        validateField(Field27A.NAME, RandomStringUtils.randomAlphanumeric(3));
    }

    @Test
    void testValidateField21A() {
        validateField(Field21A.NAME, RandomStringUtils.randomAlphanumeric(17));
    }

    @Test
    void testValidateField21T() {
        validateField(Field21T.NAME, RandomStringUtils.randomAlphanumeric(17));
    }

    @Test
    void testValidateField25F() {
        validateField(Field25F.NAME, RandomStringUtils.randomAlphanumeric(6));
    }

    @Test
    void testValidateField21P() {
        validateField(Field21P.NAME, RandomStringUtils.randomAlphanumeric(17));
    }

    @Test
    void testValidateField21S() {
        validateField(Field21S.NAME, RandomStringUtils.randomAlphanumeric(17));
    }

    @Test
    void testValidateField13E() {
        validateField(Field13E.NAME, RandomStringUtils.randomAlphanumeric(13));
        validateField(Field13E.NAME, "202313010101");
    }

    @Test
    void testValidateField23E() {
        validateField(Field23E.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field23E.NAME, RandomStringUtils.randomAlphanumeric(4));
        validateField(Field23E.NAME, "TELE/".concat(RandomStringUtils.randomAlphanumeric(31)));
    }

    @Test
    void testValidateField22K() {
        validateField(Field22K.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field22K.NAME, RandomStringUtils.randomAlphanumeric(4));
        validateField(Field22K.NAME, "APAY/".concat(RandomStringUtils.randomAlphanumeric(36)));
    }

    @Test
    void testValidateField12H() {
        validateField(Field12H.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field12H.NAME, RandomStringUtils.randomAlphanumeric(4));
        validateField(Field12H.NAME, "STND/".concat(RandomStringUtils.randomAlphanumeric(36)));
    }

    @Test
    void testValidateField22B() {
        validateField(Field22B.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field22B.NAME, RandomStringUtils.randomAlphanumeric(4));
    }

    @Test
    void testValidateField12L() {
        validateField(Field12L.NAME, RandomStringUtils.randomAlphanumeric(1));
        validateField(Field12L.NAME, RandomStringUtils.randomAlphanumeric(3));
    }

    @Test
    void testValidateField31S() {
        validateField(Field31S.NAME, RandomStringUtils.randomAlphanumeric(5));
        validateField(Field31S.NAME, "231301");
    }

    @Test
    void testValidateField53C() {
        validateField(Field53C.NAME, RandomStringUtils.randomAlphanumeric(34));
        validateField(Field53C.NAME, "/".concat(RandomStringUtils.randomAlphanumeric(35)));
    }

    @Test
    void testValidateField25A() {
        validateField(Field25A.NAME, RandomStringUtils.randomAlphanumeric(34));
        validateField(Field25A.NAME, "/".concat(RandomStringUtils.randomAlphanumeric(35)));
    }

    @Test
    void testValidateField20E() {
        validateField(Field20E.NAME, RandomStringUtils.randomAlphanumeric(3));
        validateField(Field20E.NAME, RandomStringUtils.randomAlphanumeric(4));
        validateField(Field20E.NAME, "TEND/".concat(RandomStringUtils.randomAlphanumeric(36)));
    }

    @Test
    void testValidateField31R() {
        validateField(Field31R.NAME, RandomStringUtils.randomAlphanumeric(5));
        validateField(Field31R.NAME, "231301");
        validateField(Field31R.NAME, "230101/");
        validateField(Field31R.NAME, "230101/23");
        validateField(Field31R.NAME, "230101/231301");
    }

    @Test
    void testValidateField71F() {
        validateField(Field71F.NAME, RandomStringUtils.randomAlphanumeric(18));
    }

    @Test
    void testValidateField37J() {
        validateField(Field37J.NAME, RandomStringUtils.randomAlphanumeric(15));
    }

    @Test
    void testValidateField49Z() {
        validateField(Field49Z.NAME, generateValue(50, 65));
        validateField(Field49Z.NAME, generateValue(50, 65));
    }

    @Test
    void testValidateField29A() {
        validateField(Field29A.NAME, generateValue(4, 35));
        validateField(Field29A.NAME, generateValue(4, 35));
    }

    @Test
    void testValidateField29D() {
        validateField(Field29D.NAME, generateValue(4, 35));
        validateField(Field29D.NAME, generateValue(4, 35));
    }

    @Test
    void testValidateField29P() {
        validateField(Field29P.NAME, RandomStringUtils.randomAlphanumeric(12));
    }
}
