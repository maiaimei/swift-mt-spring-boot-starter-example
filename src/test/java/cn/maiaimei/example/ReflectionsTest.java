package cn.maiaimei.example;

import cn.maiaimei.framework.swift.validation.ValidatorUtils;
import com.prowidesoftware.swift.model.field.Field;
import com.prowidesoftware.swift.model.mt.AbstractMT;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

public class ReflectionsTest {
    @Test
    void testGetMTXxxClassNameByReflections() {
        Reflections reflections = new Reflections("com.prowidesoftware.swift.model.mt");
        Set<Class<? extends AbstractMT>> classNames = reflections.getSubTypesOf(AbstractMT.class);
        Comparator<Class<? extends AbstractMT>> comparing = Comparator.comparing(mt -> {
            String name = mt.getSimpleName();
            return name.substring(2);
        }, Comparator.naturalOrder());
        LinkedHashSet<Class<? extends AbstractMT>> sortedClassNames = classNames.stream()
                .sorted(comparing)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        for (Class<? extends AbstractMT> className : sortedClassNames) {
            String subMessageType = className.getSimpleName().replaceAll("MT", "");
            System.out.println("CLASS_NAME_MAP.put(\"" + subMessageType + "\", \"" + className.getName() + "\");");
        }
    }

    @Test
    void testGetFieldsByReflections() throws InstantiationException, IllegalAccessException {
        Reflections reflections = new Reflections("com.prowidesoftware.swift.model.field");
        Set<Class<? extends Field>> subFields = reflections.getSubTypesOf(Field.class);
        Set<Class<? extends Field>> subFields1 = subFields.stream()
                .filter(w -> w.getSimpleName().startsWith("Field")).collect(Collectors.toSet());
        Comparator<Class<? extends Field>> comparing = Comparator.comparing(field -> {
            String name = field.getSimpleName();
            return name.substring(5);
        }, Comparator.naturalOrder());
        LinkedHashSet<Class<? extends Field>> sortedSubFields1 = subFields1.stream()
                .filter(w -> String.valueOf(ValidatorUtils.getNumber(w.getSimpleName())).length() == 2)
                .sorted(comparing)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        LinkedHashSet<Class<? extends Field>> sortedSubFields2 = subFields1.stream()
                .filter(w -> String.valueOf(ValidatorUtils.getNumber(w.getSimpleName())).length() == 3)
                .sorted(comparing)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        LinkedHashSet<Class<? extends Field>> sortedSubFields = new LinkedHashSet<>();
        sortedSubFields.addAll(sortedSubFields1);
        sortedSubFields.addAll(sortedSubFields2);
        List<String> validatorPatterns = new ArrayList<>();
        for (Class<? extends Field> fieldClass : sortedSubFields) {
            Field field = fieldClass.newInstance();
            String name = field.getName();
            String validatorPattern = field.validatorPattern();
            boolean b1 = ValidatorUtils.isFixedLengthCharacter(validatorPattern);
            boolean b2 = ValidatorUtils.isFixedLengthCharacterStartsWithSlash(validatorPattern);
            boolean b3 = ValidatorUtils.isVariableLengthCharacter(validatorPattern);
            boolean b4 = ValidatorUtils.isVariableLengthCharacterStartsWithSlash(validatorPattern);
            boolean b5 = ValidatorUtils.isMultilineSwiftSet(validatorPattern);
            boolean b = b1 || b2 || b3 || b4 || b5;
            if (!b && !validatorPatterns.contains(validatorPattern)) {
                validatorPatterns.add(validatorPattern);
                System.out.println(name + " " + validatorPattern);
            }
        }
        System.out.println();
    }
}
