package cn.maiaimei.example;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.converter.StringToFieldConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.TransactionToMT798Converter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Message;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.ValidationEngine;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Random;
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

    protected void validateField(MT798 mt798, String tagName, String tagValue) {
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.getSwiftMessage().getBlock4().removeTag(tagName);
        mt798.append(new Tag(tagName, tagValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected void validateField(MT798 mt798, String sequenceStartTagName, String tagName, String tagValue) {
        mt798.append(new Tag(sequenceStartTagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.getSwiftMessage().getBlock4().removeTag(tagName);
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

    protected String generateValue(int rowCount, int maxLengthExclusive) {
        String rowValue = RandomStringUtils.randomAlphanumeric(1, maxLengthExclusive)
                .concat(new Random().nextBoolean() ? RandomStringUtils.random(1) : RandomStringUtils.randomAscii(1))
                .concat("\r\n");
        return generateValue(rowCount, rowValue);
    }

    protected String generateValue(int rowCount, String rowValue) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rowCount; i++) {
            builder.append(rowValue)
                    .append("\r\n");
        }
        return builder.toString();
    }

    protected <T extends MT798Transaction> void convert(MT798 indexMessage1, List<MT798> detailMessages1, List<MT798> extensionMessages1, Class<T> clazz) {
        final MT798Message mt798Message1 = new MT798Message();
        mt798Message1.setIndexMessage(indexMessage1);
        mt798Message1.setDetailMessages(detailMessages1);
        mt798Message1.setExtensionMessages(extensionMessages1);
        final MT798Transaction transaction = mt798ToTransactionConverter.convert(mt798Message1);
        System.out.println("===> MT798 convert to transaction begin");
        System.out.println(writeValueAsString(transaction));
        System.out.println("<=== MT798 convert to transaction end");
        final MT798Message mt798Message2 = transactionToMT798Converter.convert((T) transaction, clazz);
        final MT798 indexMessage2 = mt798Message2.getIndexMessage();
        final List<MT798> detailMessages2 = mt798Message2.getDetailMessages();
        final List<MT798> extensionMessages2 = mt798Message2.getExtensionMessages();
        System.out.println("===> transaction convert to MT798 indexMessage begin");
        System.out.println(indexMessage2.message());
        System.out.println("===> transaction convert to MT798 indexMessage end");
        if (!CollectionUtils.isEmpty(detailMessages2)) {
            System.out.println("===> transaction convert to MT798 detailMessage begin");
            for (int i = 0; i < detailMessages2.size(); i++) {
                System.out.println(detailMessages2.get(i).message());
                if (i != detailMessages2.size() - 1) {
                    System.out.println("------------------------------------------------------------");
                }
            }
            System.out.println("===> transaction convert to MT798 detailMessage end");
        }
        if (!CollectionUtils.isEmpty(extensionMessages2)) {
            System.out.println("===> transaction convert to MT798 extensionMessage begin");
            for (int i = 0; i < extensionMessages2.size(); i++) {
                System.out.println(extensionMessages2.get(i).message());
                if (i != extensionMessages2.size() - 1) {
                    System.out.println("------------------------------------------------------------");
                }
            }
            System.out.println("===> transaction convert to MT798 extensionMessage end");
        }
    }
}
