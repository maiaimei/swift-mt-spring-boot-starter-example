package cn.maiaimei.example.converter;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import com.prowidesoftware.swift.model.field.Field27A;
import com.prowidesoftware.swift.model.field.Field32B;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
class StringToFieldConverterTest {
    @Autowired
    private StringToFieldConverter stringToFieldConverter;

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
