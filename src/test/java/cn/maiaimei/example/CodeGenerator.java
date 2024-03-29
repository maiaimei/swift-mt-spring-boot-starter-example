package cn.maiaimei.example;

import cn.maiaimei.example.config.TestConfig;
import cn.maiaimei.framework.swift.model.mt.config.FieldInfo;
import cn.maiaimei.framework.swift.model.mt.config.MessageConfig;
import cn.maiaimei.framework.swift.model.mt.config.SequenceInfo;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = TestConfig.class,
        initializers = ConfigDataApplicationContextInitializer.class)
public class CodeGenerator {
    @Autowired private ApplicationContext applicationContext;

    @Test
    void testGeneratePropertiesByMessageValidationConfig() {
        String beanName = "mt727785Config";
        MessageConfig messageConfig = applicationContext.getBean(beanName, MessageConfig.class);
        List<FieldInfo> fields = messageConfig.getFields();
        List<SequenceInfo> sequences = messageConfig.getSequences();
        generateProperty(fields);
        if (!CollectionUtils.isEmpty(sequences)) {
            for (SequenceInfo sequenceInfo : sequences) {
                System.out.println(
                        "--------------- " + sequenceInfo.getName() + " ---------------");
                generateProperty(sequenceInfo.getFields());
            }
        }
    }

    void generateProperty(List<FieldInfo> fields) {
        for (FieldInfo fieldInfo : fields) {
            System.out.println("@Tag(\"" + fieldInfo.getTag() + "\")");
            System.out.println("private String " + getPropertyName(fieldInfo.getFieldName()) + ";");
        }
    }

    String getPropertyName(String fieldName) {
        StringBuilder builder = new StringBuilder();
        String[] words = fieldName.split(StringUtils.SPACE);
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            if (i == 0) {
                word = word.substring(0, 1).toLowerCase().concat(word.substring(1));
            } else {
                word = word.substring(0, 1).toUpperCase().concat(word.substring(1));
            }
            builder.append(word);
        }
        return builder.toString();
    }
}
