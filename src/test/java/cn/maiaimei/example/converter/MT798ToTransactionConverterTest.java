package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT784Transaction;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
class MT798ToTransactionConverterTest extends BaseTest {

    @Autowired
    private MT798ToTransactionConverter mt798ToTransactionConverter;

    @Autowired
    private StringToFieldConverter stringToFieldConverter;

    @SneakyThrows
    @Test
    void testConvertMT798ToTransaction() {
        MT798 indexMessage = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        List<MT798> detailMessages = Collections.singletonList(readFileAsMT798("mt/mt7xx/MT784_760.txt"));
        MT798Message mt798Message = new MT798Message();
        mt798Message.setIndexMessage(indexMessage);
        mt798Message.setDetailMessages(detailMessages);
        MT798Transaction mt798Transaction = mt798ToTransactionConverter.convert(mt798Message);
        assertNotNull(mt798Transaction);

        ObjectMapper objectMapper = new ObjectMapper();
        // ignore NULL
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        System.out.println(objectMapper.writeValueAsString(mt798Transaction));

        List<MT784Transaction.MT784DetailMessage> detailMsgs = mt798Transaction.convert(mt798Transaction.getDetailMessages(), MT784Transaction.MT784DetailMessage.class);
        MT784Transaction.MT784DetailMessage mt784DetailMessage = detailMsgs.get(0);
        MT784Transaction.MT784DetailSequenceA sequenceA = mt784DetailMessage.getSequenceA();
        MT784Transaction.MT784DetailSequenceB sequenceB = mt784DetailMessage.getSequenceB();
        MT784Transaction.MT784DetailSequenceC sequenceC = mt784DetailMessage.getSequenceC();
        Field32B field32B = stringToFieldConverter.convert(sequenceB.getUndertakingAmount(), Field32B.class);
        assertEquals("EUR", field32B.getCurrency());
        assertEquals("50000,", field32B.getAmount());
    }
}
