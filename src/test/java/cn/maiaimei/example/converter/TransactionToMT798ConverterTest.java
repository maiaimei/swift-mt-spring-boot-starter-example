package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.TransactionToMT798Converter;
import cn.maiaimei.framework.swift.model.FieldValue;
import cn.maiaimei.framework.swift.model.MultilineCharacter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT762Transaction;
import cn.maiaimei.framework.swift.model.mt.mt7xx.transaction.MT784Transaction;
import com.prowidesoftware.swift.model.field.Field32B;
import com.prowidesoftware.swift.model.field.Field40C;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
class TransactionToMT798ConverterTest extends BaseTest {

    @Autowired
    private TransactionToMT798Converter transactionToMT798Converter;

    @Test
    void testBuildFieldValue() {
        // Field50: 4*35x
        String applicant = MultilineCharacter.builder()
                .append("Pumpen AG")
                .append("Postfach 123")
                .append("60599 Frankfurt / GERMANY")
                .build();
        assertEquals("Pumpen AG\r\nPostfach 123\r\n60599 Frankfurt / GERMANY", applicant);
        // field with component
        String undertakingAmount = FieldValue.builder(Field32B.class)
                .component(1, "EUR")
                .component(2, "50000,")
                .build();
        assertEquals("EUR50000,", undertakingAmount);
        String applicableRules = FieldValue.builder(Field40C.class).field()
                .setType("ISPR")
                .setNarrative("test1234")
                .getValue();
        assertEquals("ISPR/test1234", applicableRules);
    }

    @Test
    void testConvertMT784TransactionToMT798_02() {
        MT784Transaction transaction = readFileAsObject("mt/mt7xx/MT784Transaction.json", MT784Transaction.class);
        MT798Message message = transactionToMT798Converter.convert(transaction, MT784Transaction.class);
        assertNotNull(message);
        assertNotNull(message.getIndexMessage());
        System.out.println("---------- IndexMessage ----------");
        System.out.println(message.getIndexMessage().message());
        assertNotNull(message.getDetailMessages());
        System.out.println("---------- DetailMessages ----------");
        for (MT798 detailMessage : message.getDetailMessages()) {
            System.out.println(detailMessage.message());
        }
    }

    @Test
    void testConvertMT784TransactionToMT798_01() {
        MT784Transaction.MT784DetailMessage detailMessage = new MT784Transaction.MT784DetailMessage();
        detailMessage.setTransactionReferenceNumber("FGH96373");
        detailMessage.setSubMessageType("760");
        detailMessage.setMessageIndexTotal("2/2");
        detailMessage.setCustomerReferenceNumber("XYZ999-123");
        MT784Transaction.MT784DetailSequenceA sequenceA = new MT784Transaction.MT784DetailSequenceA();
        sequenceA.setSequenceOfTotal("1/1");
        sequenceA.setPurposeOfMessage("ISSU");
        detailMessage.setSequenceA(sequenceA);
        MT784Transaction.MT784DetailSequenceB sequenceB = new MT784Transaction.MT784DetailSequenceB();
        sequenceB.setUndertakingNumber("NONREF");
        sequenceB.setDateOfIssue("200505");
        sequenceB.setFormOfUndertaking("DGAR");
        sequenceB.setApplicableRules("NONE");
        sequenceB.setExpiryType("FIXD");
        sequenceB.setDateOfExpiry("201231");
        sequenceB.setApplicant("Pumpen AG\r\nPostfach 123\r\n60599 Frankfurt / GERMANY");
        sequenceB.setIssuer4Field52A("BOGEDEFFXXX");
        sequenceB.setUndertakingAmount("EUR50000,");
        sequenceB.setUndertakingTermsAndConditions("STANDARD WORDING");
        sequenceB.setDeliveryOfOriginalUndertaking("REGM");
        sequenceB.setDeliveryToCollectionBy("BENE");
        detailMessage.setSequenceB(sequenceB);
        MT784Transaction transaction = new MT784Transaction();
        transaction.setDetailMessages(Collections.singletonList(detailMessage));
        MT798Message mt798Message = transactionToMT798Converter.convert(transaction, MT784Transaction.class);
        assertNotNull(mt798Message);
        System.out.println(mt798Message.getDetailMessages().get(0).message());
    }

    @Test
    void testConvertMT762TransactionToMT798() {
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

        MT762Transaction mt762Transaction = new MT762Transaction();
        mt762Transaction.setIndexMessage(indexMessage);
        MT798Message mt798Message = transactionToMT798Converter.convert(mt762Transaction, MT762Transaction.class);
        assertNotNull(mt798Message);
        System.out.println(mt798Message.getIndexMessage().message());
    }

}
