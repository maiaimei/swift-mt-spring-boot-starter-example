package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.TransactionToMT798Converter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT784Transaction;
import com.prowidesoftware.swift.model.field.Field27A;
import com.prowidesoftware.swift.model.field.Field32B;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
public class ConverterTest extends BaseTest {

    @Autowired
    private StringToFieldConverter stringToFieldConverter;

    @Autowired
    private MT798ToTransactionConverter mt798ToTransactionConverter;

    @Autowired
    private TransactionToMT798Converter transactionToMT798Converter;

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

    // TODO: testMT784TransactionConversion
    @Test
    void testMT784TransactionConversion() {
        MT798 indexMessage = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        List<MT798> detailMessages = Collections.singletonList(readFileAsMT798("mt/mt7xx/MT784_760.txt"));
        MT798Message mt798Message = new MT798Message();
        mt798Message.setIndexMessage(indexMessage);
        mt798Message.setDetailMessages(detailMessages);
        MT798Transaction mt798Transaction = mt798ToTransactionConverter.convert(mt798Message);
        assertNotNull(mt798Transaction);
        System.out.println("---------- After Convert Transaction ----------");
        System.out.println(writeValueAsString(mt798Transaction));

        final MT784Transaction mt784Transaction = (MT784Transaction) mt798Transaction;
        MT798Message message = transactionToMT798Converter.convert(mt784Transaction, MT784Transaction.class);
        assertNotNull(message);
        assertNotNull(message.getIndexMessage());
        System.out.println("---------- Before Convert IndexMessage ----------");
        System.out.println(indexMessage.message());
        System.out.println("---------- After Convert IndexMessage ----------");
        System.out.println(message.getIndexMessage().message());
        assertNotNull(message.getDetailMessages());
        System.out.println("---------- Before Convert DetailMessages ----------");
        for (MT798 detailMessage : detailMessages) {
            System.out.println(detailMessage.message());
        }
        System.out.println("---------- After Convert DetailMessages ----------");
        for (MT798 detailMessage : message.getDetailMessages()) {
            System.out.println(detailMessage.message());
        }
    }

}
