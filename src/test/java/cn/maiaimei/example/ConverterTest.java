package cn.maiaimei.example;

import cn.maiaimei.framework.swift.annotation.SwiftMTTag;
import cn.maiaimei.framework.swift.model.BaseMessage;
import cn.maiaimei.framework.swift.model.mt7xx.BaseMT798Message;
import cn.maiaimei.framework.swift.model.mt7xx.MT784Transaction;
import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConverterTest extends BaseTest {

    @Test
    void test() {
        MT784Transaction.MT784IndexMessage indexMessage = new MT784Transaction.MT784IndexMessage();
        MT798 mt = readFileAsMT798("validation/mt7xx/MT784_784.txt");
        SwiftTagListBlock block = mt.getSwiftMessage().getBlock4().getSubBlockAfterFirst(Field77E.NAME, false);
        mt798ToBaseMessage(mt, block, indexMessage);
        MT784Transaction transaction = new MT784Transaction();
        transaction.setIndexMessage(indexMessage);
        transaction.setDetailMessages(null);
        transaction.setExtensionMessages(null);
        System.out.println();
    }

    @SneakyThrows
    private void mt798ToBaseMessage(MT798 mt, SwiftTagListBlock block, BaseMT798Message message) {
        message.setTransactionReferenceNumber(mt.getField20().getValue());
        message.setSubMessageType(mt.getField12().getValue());
        mtXxxToBaseMessage(mt, block, message);
    }

    @SneakyThrows
    private void mtXxxToBaseMessage(AbstractMT mt, SwiftTagListBlock block, BaseMessage message) {
        Class<? extends BaseMessage> clazz = message.getClass();
        List<Field> declaredFields = getDeclaredFields(clazz);
        for (Field declaredField : declaredFields) {
            SwiftMTTag swiftMTTag = declaredField.getAnnotation(SwiftMTTag.class);
            com.prowidesoftware.swift.model.field.Field field = block.getFieldByName(swiftMTTag.value());
            if (field == null) {
                continue;
            }
            declaredField.setAccessible(Boolean.TRUE);
            declaredField.set(message, swiftMTTag.index() == -1 ? field.getValue() : field.getComponent(swiftMTTag.index()));
            declaredField.setAccessible(Boolean.FALSE);
        }
    }

    private List<Field> getDeclaredFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null) {
            Field[] declaredFields = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(declaredFields));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
