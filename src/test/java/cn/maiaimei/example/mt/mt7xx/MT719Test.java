package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT719Transaction;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT719Test extends BaseContextTest {

    String path = "mt/mt7xx/MT719_719.txt";

    @Test
    void testValidate() {
        final String message = readFileAsString(path);
        final MT798 mt798 = new MT798(message);
        final ValidationResult result = validationEngine.validate(mt798);
        assertAndPrintResult(result);
    }

    @Test
    void testConvert() {
        final String message = readFileAsString(path);
        final MT798 mt798IndexMessage = new MT798(message);
        convert(mt798IndexMessage, Collections.emptyList(), Collections.emptyList(), MT719Transaction.class);
    }

}
