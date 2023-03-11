package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.validation.ValidationTest;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class MT798Section1ValidationTest extends ValidationTest {

    @Test
    void testValidateField20IsAbsent() {
        MT798 mt798 = new MT798();
        mt798.append(new Field12("784"));
        mt798.append(new Field77E());
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(Field20.NAME));
    }

    @Test
    void testValidateField20IsInvalid() {
        MT798 mt798 = new MT798();
        mt798.append(new Field20(RandomStringUtils.randomAlphanumeric(17)));
        mt798.append(new Field12("784"));
        mt798.append(new Field77E());
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(Field20.NAME));
    }

    @Test
    void testValidateField12IsAbsent() {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field77E());
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(Field12.NAME));
    }

    @Test
    void testValidateField12IsInvalid() {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field12(RandomStringUtils.randomAlphanumeric(4)));
        mt798.append(new Field77E());
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(Field12.NAME));
    }

    @Test
    void testValidateField77EIsAbsent() {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field12("784"));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(Field77E.NAME));
    }

}
