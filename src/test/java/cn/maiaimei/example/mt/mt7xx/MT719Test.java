package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT719Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT719Test extends BaseContextTest {

    String MT719_PATH = "mt/mt7xx/MT719_719.txt";

    @Test
    void testValidate() {
        validate(MT719_PATH);
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT719_PATH)),
                Collections.emptyList(),
                Collections.emptyList(),
                MT719Transaction.class);
    }
}
