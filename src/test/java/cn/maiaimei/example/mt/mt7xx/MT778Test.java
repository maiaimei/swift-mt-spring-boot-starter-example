package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT778Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT778Test extends BaseContextTest {
    String MT778_PATH = "mt/mt7xx/MT778_778.txt";

    @Test
    void testValidateMT778() {
        validate(MT778_PATH);
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT778_PATH)),
                Collections.emptyList(),
                Collections.emptyList(),
                MT778Transaction.class);
    }
}
