package cn.maiaimei.example;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.TransactionToMT798Converter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Packets;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.ValidationEngine;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ConfigDataApplicationContextInitializer 可在测试类中加载yml
 */
@Slf4j
@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
public class BaseContextTest extends BaseTest {

    @Autowired
    protected ValidationEngine validationEngine;

    @Autowired
    protected StringToFieldConverter stringToFieldConverter;

    @Autowired
    protected MT798ToTransactionConverter mt798ToTransactionConverter;

    @Autowired
    protected TransactionToMT798Converter transactionToMT798Converter;

    protected MT798 mockMT798(String subMessageType) {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field12(subMessageType));
        mt798.append(new Field77E());
        return mt798;
    }

    protected void validate(String path) {
        final String message = readFileAsString(path);
        final MT798 mt798 = new MT798(message);
        final ValidationResult result = validationEngine.validate(mt798);
        assertAndPrintResult(result);
    }

    protected void validate(String path, String indexMessageType) {
        final String message = readFileAsString(path);
        final MT798 mt798 = new MT798(message);
        final ValidationResult result = validationEngine.validate(mt798, indexMessageType, indexMessageType);
        assertAndPrintResult(result);
    }

    protected void validate(String path, String indexMessageType, String subMessageType) {
        final String message = readFileAsString(path);
        final MT798 mt798 = new MT798(message);
        final ValidationResult result = validationEngine.validate(mt798, indexMessageType, subMessageType);
        assertAndPrintResult(result);
    }

    protected void validateFieldWhenAbsent(MT798 mt798, String tagName) {
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateFieldWhenBlank(MT798 mt798, String tagName) {
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateField(MT798 mt798, String tagName, String tagValue) {
        mt798.append(new Tag(tagName, tagValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateFieldWhenAbsent(MT798 mt798, String startTagName, String tagName) {
        mt798.append(new Tag(startTagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateFieldWhenBlank(MT798 mt798, String startTagName, String tagName) {
        mt798.append(new Tag(startTagName, StringUtils.EMPTY));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateField(MT798 mt798, String startTagName, String tagName, String tagValue) {
        mt798.append(new Tag(startTagName, StringUtils.EMPTY));
        mt798.append(new Tag(tagName, tagValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void assertAndPrintResult(ValidationResult result) {
        assertAndPrintResult(result, null);
    }

    protected void assertAndPrintResult(ValidationResult result, Predicate<String> predicate) {
        List<String> errorMessages = result.getErrorMessages();
        if (predicate != null) {
            errorMessages = errorMessages.stream().filter(predicate).collect(Collectors.toList());
        }
        if (!CollectionUtils.isEmpty(errorMessages)) {
            errorMessages.forEach(log::info);
            assertFalse(CollectionUtils.isEmpty(errorMessages));
        } else {
            assertTrue(CollectionUtils.isEmpty(errorMessages));
        }
    }

    /**
     * <p>first convert MT798 packets to transaction</p>
     * <p>then convert transaction to MT798 packets</p>
     */
    protected <T extends MT798Transaction> void doBidirectionalConversion(MT798 indexMessage, List<MT798> detailMessages, List<MT798> extensionMessages, Class<T> clazz) {
        final MT798Packets sourceMT798Packets = new MT798Packets();
        sourceMT798Packets.setIndexMessage(indexMessage);
        sourceMT798Packets.setDetailMessages(detailMessages);
        sourceMT798Packets.setExtensionMessages(extensionMessages);
        final MT798Transaction transaction = mt798ToTransactionConverter.convert(sourceMT798Packets);
        final MT798Packets convertedMT798Packets = transactionToMT798Converter.convert((T) transaction, clazz);
        System.out.println("===> MT798 convert to transaction begin");
        System.out.println(writeValueAsString(transaction));
        System.out.println("<=== MT798 convert to transaction end");
        print(convertedMT798Packets);
    }

    protected void print(MT798Packets convertedMT798Packets) {
        System.out.println("===> transaction convert to MT798 indexMessage begin");
        System.out.println(convertedMT798Packets.getIndexMessage().message());
        System.out.println("===> transaction convert to MT798 indexMessage end");
        print(convertedMT798Packets.getDetailMessages(), "detailMessage");
        print(convertedMT798Packets.getExtensionMessages(), "extensionMessage");
    }

    private void print(List<MT798> mts, String type) {
        if (!CollectionUtils.isEmpty(mts)) {
            System.out.println("===> transaction convert to MT798 " + type + " begin");
            for (int i = 0; i < mts.size(); i++) {
                System.out.println(mts.get(i).message());
                if (i != mts.size() - 1) {
                    System.out.println("------------------------------------------------------------");
                }
            }
            System.out.println("===> transaction convert to MT798 " + type + " end");
        }
    }
}
