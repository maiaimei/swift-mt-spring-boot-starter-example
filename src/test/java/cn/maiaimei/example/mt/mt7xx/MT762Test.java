package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT762Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT762Test extends BaseContextTest {

    String MT762_PATH = "mt/mt7xx/MT762_762.txt";
    String MT760_PATH = "mt/mt7xx/MT762_760.txt";

    @Test
    void testValidateMT762() {
        validate(MT762_PATH, "762");
    }

    @Test
    void testValidateMT760() {
        validate(MT760_PATH, "762", "760");
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT762_PATH)),
                Collections.singletonList(new MT798(readFileAsString(MT760_PATH))),
                Collections.emptyList(),
                MT762Transaction.class);
    }
}
