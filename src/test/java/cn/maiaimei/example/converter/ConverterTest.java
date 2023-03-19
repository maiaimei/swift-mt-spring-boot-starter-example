package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseContextTest;
import com.prowidesoftware.swift.model.field.Field27A;
import com.prowidesoftware.swift.model.field.Field32B;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest extends BaseContextTest {

    @Test
    void testStringToField27A() {
        Field27A field27A = stringToFieldConverter.convert("1/1", Field27A.class);
        assertEquals("1/1", field27A.getValue());
    }

    @Test
    void testStringToField32B() {
        Field32B field32B = stringToFieldConverter.convert("EUR50000,", Field32B.class);
        assertEquals("EUR50000,", field32B.getValue());
        assertEquals("EUR", field32B.getCurrency());
        assertEquals("50000,", field32B.getAmount());
    }
}
