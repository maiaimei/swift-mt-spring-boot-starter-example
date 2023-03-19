package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT728Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT728Test extends BaseContextTest {
    String MT728_PATH = "mt/mt7xx/MT728_728.txt";
    String MT787_PATH = "mt/mt7xx/MT728_787.txt";

    @Test
    void testValidateMT728() {
        validate(MT728_PATH);
    }

    @Test
    void testValidateMT787() {
        validate(MT787_PATH);
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT728_PATH)),
                Collections.singletonList(new MT798(readFileAsString(MT787_PATH))),
                Collections.emptyList(),
                MT728Transaction.class);
    }
}
