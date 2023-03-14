package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT784Transaction;
import com.prowidesoftware.swift.model.field.Field32B;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
class MT798ToTransactionConverterTest extends BaseTest {

    @Autowired
    private MT798ToTransactionConverter mt798ToTransactionConverter;

    @Autowired
    private StringToFieldConverter stringToFieldConverter;

    @SneakyThrows
    @Test
    void testConvertToMT784Transaction() {
        MT798 indexMessage = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        List<MT798> detailMessages = Collections.singletonList(readFileAsMT798("mt/mt7xx/MT784_760.txt"));
        MT798Message mt798Message = new MT798Message();
        mt798Message.setIndexMessage(indexMessage);
        mt798Message.setDetailMessages(detailMessages);
        MT798Transaction mt798Transaction = mt798ToTransactionConverter.convert(mt798Message);
        assertNotNull(mt798Transaction);
        System.out.println(objectMapper.writeValueAsString(mt798Transaction));
        MT784Transaction mt784Transaction = (MT784Transaction) mt798Transaction;

        MT784Transaction.MT784IndexMessage mt784IndexMessage = mt784Transaction.getIndexMessage();
        List<MT784Transaction.MT784DetailMessage> mt784DetailMessages = mt784Transaction.getDetailMessages();
        List<MT784Transaction.MT784ExtensionMessage> mt784ExtensionMessages = mt784Transaction.getExtensionMessages();
        assertNotNull(mt784IndexMessage);
        assertNotNull(mt784DetailMessages);
        assertNull(mt784ExtensionMessages);
        MT784Transaction.MT784DetailMessage mt784DetailMessage = mt784DetailMessages.get(0);
        MT784Transaction.MT784DetailSequenceA sequenceA = mt784DetailMessage.getSequenceA();
        MT784Transaction.MT784DetailSequenceB sequenceB = mt784DetailMessage.getSequenceB();
        MT784Transaction.MT784DetailSequenceC sequenceC = mt784DetailMessage.getSequenceC();
        assertNotNull(sequenceA);
        assertNotNull(sequenceB);
        assertNotNull(sequenceC);
        Field32B field32B = stringToFieldConverter.convert(sequenceB.getUndertakingAmount(), Field32B.class);
        assertEquals("EUR", field32B.getCurrency());
        assertEquals("50000,", field32B.getAmount());
    }
}
