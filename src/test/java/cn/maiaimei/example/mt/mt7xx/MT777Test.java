package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT777Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT777Test extends BaseContextTest {
    String MT777_PATH = "mt/mt7xx/MT777_777.txt";

    @Test
    void testValidateMT777() {
        validate(MT777_PATH, "777");
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT777_PATH)),
                Collections.emptyList(),
                Collections.emptyList(),
                MT777Transaction.class);
    }
}
