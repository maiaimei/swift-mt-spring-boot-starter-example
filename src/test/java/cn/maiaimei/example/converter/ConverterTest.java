package cn.maiaimei.example.converter;

import cn.maiaimei.example.BaseTest;
import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.annotation.Tag;
import cn.maiaimei.framework.swift.converter.mt.mt7xx.MT798ToTransactionConverter;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT762Transaction;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Messages;
import cn.maiaimei.framework.swift.model.mt.mt7xx.MT798Transaction;
import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, initializers = ConfigDataApplicationContextInitializer.class)
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

    @SneakyThrows
    @Test
    void testConvertTransactionToMT798() {
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

        List<Field> fields = new ArrayList<>();
        String classNamePrefix = "com.prowidesoftware.swift.model.field.Field";
        List<java.lang.reflect.Field> declaredFields = getDeclaredFields(indexMessage.getClass());
        for (int i = 0; i < declaredFields.size(); i++) {
            if (i == 2) {
                fields.add(new Field77E());
            }
            java.lang.reflect.Field declaredField = declaredFields.get(i);
            Tag tagAnnotation = declaredField.getAnnotation(Tag.class);
            if (tagAnnotation == null) {
                continue;
            }
            Class<?> clazz = Class.forName(classNamePrefix.concat(tagAnnotation.value()));
            Constructor<?> constructor = clazz.getConstructor(String.class);
            declaredField.setAccessible(Boolean.TRUE);
            Object value = declaredField.get(indexMessage);
            if (value != null && StringUtils.isNotBlank(value.toString())) {
                Object o = constructor.newInstance(value.toString());
                fields.add((Field) o);
            }
            declaredField.setAccessible(Boolean.FALSE);
        }
        MT798 mt798 = new MT798();
        mt798.append(fields.toArray(new Field[0]));
        System.out.println(mt798.message());
    }

    @Test
    void testMT798() {
        MT798 mt798 = new MT798();
        mt798.addField(new Field20("BOG456873"));
        mt798.addField(new Field12("762"));
        mt798.addField(new Field77E());
        mt798.addField(new Field27A("1/2"));
        mt798.addField(new Field21A("XZZ888-123"));
        mt798.addField(new Field21T("XZZ888"));
        mt798.addField(new Field21P("ABC66578-123"));
        mt798.addField(new Field21S("ABC66578"));
        mt798.addField(new Field20("PGFFA0765"));
        mt798.addField(new Field25F("FINAL"));
        mt798.addField(new Field13E("202005111501"));
        mt798.addField(new Field22K("PERF"));
        SwiftTagListBlock block = mt798.getSwiftMessage().getBlock4().getSubBlockAfterFirst(Field77E.NAME, Boolean.FALSE);
        Field field27A = block.getFieldByName(Field27A.NAME);
        System.out.println(mt798.message());
    }

    List<java.lang.reflect.Field> getDeclaredFields(Class<?> clazz) {
        List<java.lang.reflect.Field> fields = new ArrayList<>();
        List<Class<?>> clazzList = new ArrayList<>();
        clazzList.add(clazz);
        while (clazz != null) {
            clazzList.add(clazz.getSuperclass());
            clazz = clazz.getSuperclass();
        }
        for (int i = clazzList.size() - 1; i >= 0; i--) {
            Class<?> aClass = clazzList.get(i);
            if (aClass == null) {
                continue;
            }
            java.lang.reflect.Field[] declaredFields = aClass.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
        }
        return fields;
    }
}
