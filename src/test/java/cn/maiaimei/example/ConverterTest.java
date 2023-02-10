package cn.maiaimei.example;

import cn.maiaimei.framework.swift.config.SwiftAutoConfiguration;
import cn.maiaimei.framework.swift.converter.MTXxxToTransactionConverter;
import cn.maiaimei.framework.swift.model.mt7xx.*;
import cn.maiaimei.framework.swift.util.SwiftUtils;
import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SwiftAutoConfiguration.class)
public class ConverterTest extends BaseTest {

    @Autowired
    private MTXxxToTransactionConverter mtXxxToTransactionConverter;

    @Test
    void testConvert() {
        MT798 indexMessage = readFileAsMT798("mt/mt7xx/MT784_784.txt");
        List<MT798> detailMessages = Collections.singletonList(readFileAsMT798("mt/mt7xx/MT784_760.txt"));
        List<MT798> extensionMessages = Arrays.asList(
                readFileAsMT798("mt/mt7xx/MT784_761_01.txt"),
                readFileAsMT798("mt/mt7xx/MT784_761_02.txt")
        );
        MT798Transaction transaction = convert(indexMessage, detailMessages, extensionMessages,
                MT784Transaction.MT784IndexMessage::new,
                MT784Transaction.MT784DetailMessage::new,
                MT784Transaction.MT784ExtensionMessage::new);
        System.out.println();
    }

    private MT798Transaction convert(MT798 indexMessage,
                                     List<MT798> detailMessages,
                                     List<MT798> extensionMessages,
                                     Supplier<MT798IndexMessage> indexSupplier,
                                     Supplier<MT798DetailMessage> detailSupplier,
                                     Supplier<MT798ExtensionMessage> extensionSupplier) {
        MT798IndexMessage indexMsg = indexSupplier.get();
        mt798ToBaseMessage(indexMessage, indexMsg);
        List<MT798DetailMessage> detailMsgs = null;
        if (!CollectionUtils.isEmpty(detailMessages)) {
            detailMsgs = new ArrayList<>();
            for (MT798 detailMessage : detailMessages) {
                MT798DetailMessage detailMsg = detailSupplier.get();
                mt798ToBaseMessage(detailMessage, detailMsg);
                detailMsgs.add(detailMsg);
            }
        }
        List<MT798ExtensionMessage> extensionMsgs = null;
        if (!CollectionUtils.isEmpty(extensionMessages)) {
            extensionMsgs = new ArrayList<>();
            for (MT798 extensionMessage : extensionMessages) {
                MT798ExtensionMessage extensionMsg = extensionSupplier.get();
                mt798ToBaseMessage(extensionMessage, extensionMsg);
                extensionMsgs.add(extensionMsg);
            }
        }
        MT798Transaction transaction = new MT784Transaction();
        transaction.setIndexMessage(indexMsg);
        transaction.setDetailMessages(detailMsgs);
        transaction.setExtensionMessages(extensionMsgs);
        return transaction;
    }

    @SneakyThrows
    private void mt798ToBaseMessage(MT798 mt, MT798Message message) {
        SwiftBlock4 block4 = mt.getSwiftMessage().getBlock4();
        SwiftTagListBlock subBlockBeforeFirst77E = block4.getSubBlockBeforeFirst(Field77E.NAME, Boolean.FALSE);
        SwiftTagListBlock subBlockAfterFirst77E = block4.getSubBlockAfterFirst(Field77E.NAME, Boolean.FALSE);
        message.setTransactionReferenceNumber(subBlockBeforeFirst77E.getTagByName(Field20.NAME).getValue());
        message.setSubMessageType(subBlockBeforeFirst77E.getTagByName(Field12.NAME).getValue());
        SwiftUtils.populateMessage(subBlockAfterFirst77E, message);
    }
}
