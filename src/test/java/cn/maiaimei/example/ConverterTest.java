package cn.maiaimei.example;

import cn.maiaimei.framework.swift.config.SwiftAutoConfiguration;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.model.mt7xx.MT762Transaction;
import cn.maiaimei.framework.swift.model.mt7xx.MT798Messages;
import cn.maiaimei.framework.swift.model.mt7xx.MT798Transaction;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SwiftAutoConfiguration.class)
public class ConverterTest extends BaseTest {

    @Autowired
    private MT798ToTransactionConverter mt798ToTransactionConverter;

    @Test
    void testConvertMT798ToTransaction() {
        MT798 indexMessage = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        List<MT798> detailMessages = Collections.singletonList(readFileAsMT798("mt/mt7xx/MT784_760.txt"));
        List<MT798> extensionMessages = Arrays.asList(
                readFileAsMT798("mt/mt7xx/MT784_761_01.txt"),
                readFileAsMT798("mt/mt7xx/MT784_761_02.txt")
        );
        MT798Messages mt798Messages = new MT798Messages();
        mt798Messages.setIndexMessage(indexMessage);
        mt798Messages.setDetailMessages(detailMessages);
        mt798Messages.setExtensionMessages(extensionMessages);
        MT798Transaction mt798Transaction = mt798ToTransactionConverter.convert(mt798Messages);
        assertNotNull(mt798Transaction);
    }

    @Test
    void testConvertTransactionToMT798() {
        // TODO: testConvertTransactionToMT798
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
    }
}
