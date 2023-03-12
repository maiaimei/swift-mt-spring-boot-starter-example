package cn.maiaimei.example.converter;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.TransactionToMT798Converter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT762Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
class TransactionToMT798ConverterTest {

    @Autowired
    private TransactionToMT798Converter transactionToMT798Converter;

    @Test
    void testConvertMT762IndexMessageToMT798() {
        MT762Transaction mt762Transaction = new MT762Transaction();
        mt762Transaction.setIndexMessage(generateMT762IndexMessage());
        MT798Message mt798Message = transactionToMT798Converter.convert(mt762Transaction);
        assertNotNull(mt798Message);
        System.out.println(mt798Message.getIndexMessage().message());
    }

    private MT762Transaction.MT762IndexMessage generateMT762IndexMessage() {
        MT762Transaction.MT762IndexMessage indexMessage = new MT762Transaction.MT762IndexMessage();
        indexMessage.setTransactionReferenceNumber("BOG456873");
        indexMessage.setSubMessageType("762");
        indexMessage.setMessageIndexTotal("1/2");
        indexMessage.setCustomerReferenceNumber("XZZ888-123");
        indexMessage.setCustomerBusinessReference("XZZ888");
        indexMessage.setBankReferenceNumber("ABC66578-123");
        indexMessage.setBankBusinessReference("ABC66578");
        indexMessage.setUndertakingNumber("PGFFA0765");
        indexMessage.setTextPurpose("FINAL");
        indexMessage.setMessageCreationDateTime("202005111501");
        return indexMessage;
    }

}
