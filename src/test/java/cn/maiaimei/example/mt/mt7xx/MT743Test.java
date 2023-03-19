package cn.maiaimei.example.mt.mt7xx;

import cn.maiaimei.example.BaseContextTest;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT743Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;

import java.util.Collections;

public class MT743Test extends BaseContextTest {
    String MT743_PATH = "mt/mt7xx/MT743_743.txt";
    String MT767_PATH = "mt/mt7xx/MT743_767.txt";

    @Test
    void testValidateMT743() {
        validate(MT743_PATH, "743");
    }

    @Test
    void testValidateMT767() {
        validate(MT767_PATH, "743", "767");
    }

    @Test
    void testConvert() {
        doBidirectionalConversion(
                new MT798(readFileAsString(MT743_PATH)),
                Collections.singletonList(new MT798(readFileAsString(MT767_PATH))),
                Collections.emptyList(),
                MT743Transaction.class);
    }
}
