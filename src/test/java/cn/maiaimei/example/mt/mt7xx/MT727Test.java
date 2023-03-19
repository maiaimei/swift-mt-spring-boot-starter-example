package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT727Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT727Test extends BaseContextTest {
    String MT727_PATH = "mt/mt7xx/MT727_727.txt";
    String MT785_PATH = "mt/mt7xx/MT727_785.txt";

    @Test
    void testValidateMT727() {
        validate(MT727_PATH, "727");
    }

    @Test
    void testValidateMT785() {
        validate(MT785_PATH, "727", "785");
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT727_PATH)),
                Collections.singletonList(new MT798(readFileAsString(MT785_PATH))),
                Collections.emptyList(),
                MT727Transaction.class);
    }
}
