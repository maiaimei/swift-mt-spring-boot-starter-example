package cn.maiaimei.example;

import cn.maiaimei.framework.swift.validation.ValidationEngine;
import cn.maiaimei.framework.swift.validation.ValidationResult;
import cn.maiaimei.framework.swift.validation.engine.GenericValidationEngine;
import com.prowidesoftware.swift.io.parser.SwiftParser;
import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.SwiftMessage;
import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@SpringBootTest
public class ValidationTest extends BaseTest {
    @Autowired
    ValidationEngine validationEngine;

    @Autowired
    GenericValidationEngine genericValidationEngine;

    @Test
    void testValidateMT798() {
        MT798 mt798 = readFileAsMT798("validation/mt7xx/MT798_784.txt");
        ValidationResult result = validationEngine.validate(mt798);
        if (!CollectionUtils.isEmpty(result.getErrorMessages())) {
            System.out.println("Validate error");
            for (String errorMessage : result.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println("Validate success");
        }
    }

    @SneakyThrows
    @Test
    void testValidateMT() {
        //MT798 mt = readFileAsMT798("validation/mt7xx/MT798_784.txt");
        String message = readFileAsString("validation/mt9xx/MT940.txt");
        ValidationResult result = genericValidationEngine.validate(message, "940");
        if (!CollectionUtils.isEmpty(result.getErrorMessages())) {
            System.out.println("Validate error");
            for (String errorMessage : result.getErrorMessages()) {
                System.out.println(errorMessage);
            }
        } else {
            System.out.println("Validate success");
        }
    }

    @SneakyThrows
    @Test
    void testSwiftMessageParser() {
        String message = readFileAsString("validation/mt7xx/MT798_760.txt");
        SwiftParser parser = new SwiftParser(message);
        SwiftMessage swiftMessage = parser.message();
        String type = swiftMessage.getType(); // Message Type?
        SwiftBlock4 block4 = swiftMessage.getBlock4();
        List<Tag> allTags = block4.getTags();
        Class<? extends GenericValidationEngine> clazz = genericValidationEngine.getClass();
        Method method = clazz.getDeclaredMethod("getSequenceBlockMap", String.class, SwiftTagListBlock.class);
        method.setAccessible(true);
        Object sequenceBlockMap = method.invoke(genericValidationEngine, "760", block4);
        method.setAccessible(false);
        System.out.println();
    }
}
