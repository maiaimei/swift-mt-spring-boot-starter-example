package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT784Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT784Test extends BaseContextTest {

    String MT784_PATH = "mt/mt7xx/MT784_784.txt";
    String MT760_PATH = "mt/mt7xx/MT784_760.txt";

    @Test
    void testValidateMT784() {
        validate(MT784_PATH);
    }

    @Test
    void testValidateMT760() {
        validate(MT760_PATH);
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT784_PATH)),
                Collections.singletonList(new MT798(readFileAsString(MT760_PATH))),
                Collections.emptyList(),
                MT784Transaction.class);
    }
}
