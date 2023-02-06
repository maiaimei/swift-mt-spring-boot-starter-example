package cn.maiaimei.example;

import com.prowidesoftware.swift.model.SwiftTagListBlock;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.*;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MT798ValidationConfigCodeGenerator extends BaseTest {

    @Test
    public void testGenerateCode() {
        String path = "validation/mt7xx/MT784_761.txt";
        generateCode(path);
    }

    private void generateCode(String path) {
        MT798 mt798 = readFileAsMT798(path);
        SwiftTagListBlock block = mt798.getSwiftMessage().getBlock4().getSubBlockAfterFirst(Field77E.NAME, false);
        List<Tag> tags = block.getTags();
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("\"messageType\":\"").append(mt798.getField12().getValue()).append("\",");
        builder.append("\"fields\":[");
        for (int i = 0; i < tags.size(); i++) {
            Tag tag = tags.get(i);
            Field field = block.getFieldByName(tag.getName());
            builder.append("{");
            builder.append("\"tag\":\"").append(tag.getName()).append("\",");
            builder.append("\"label\":\"").append(getLabel(field)).append("\",");
            builder.append("\"format\":\"").append(getFormat(field)).append("\",");
            appendPatternIfNecessary(field, builder);
            builder.append("\"isMandatory\":").append(getMandatory(field));
            appendComponentsIfNecessary(field, builder);
            builder.append("}");
            if (i != tags.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        builder.append("}");
        System.out.println(builder);
    }

    private String getLabel(Field field) {
        if (field.getName().equals(Field27A.NAME)) {
            return "Message Index/Total";
        }
        if (field.getName().equals(Field21A.NAME)) {
            return "Customer Reference Number";
        }
        if (field.getName().equals(Field13E.NAME)) {
            return "Message Creation Date Time";
        }
        return String.join(StringUtils.SPACE, field.getComponentLabels());
    }

    private String getFormat(Field field) {
        String fieldName = field.getName();
        if (fieldName.equals(Field27.NAME) || fieldName.equals(Field27A.NAME)) {
            return "1!n/1!n";
        }
        String validatorPattern = field.validatorPattern();
        switch (validatorPattern) {
            case "16x(***)":
                return "16x";
            case "<AMOUNT>12":
                return "12d";
            case "<CUR><AMOUNT>15":
                return "3!a15d";
            case "<DATE4><HHMM>":
                return "8!n4!n";
            case "<DATE2>":
                return "6!n";
            case "<DATE2>[/<DATE2>]":
                return "6!n[/6!n]";
            case "<BIC>":
                return "4!a2!a2!c[3!c]";
            case "35x[$35x]0-3":
                return "4*35x";
            case "35z[$35z]0-5":
                return "6*35z";
            case "65z[$65z]0-49":
                return "50*65z";
            case "65z[$65z]0-149":
                return "150*65z";
            default:
                return validatorPattern;
        }
    }

    private String getMandatory(Field field) {
        if (field.getName().equals(Field27A.NAME)) {
            return "true";
        }
        return "false";
    }

    private void appendPatternIfNecessary(Field field, StringBuilder builder) {
        switch (field.validatorPattern()) {
            case "<DATE4><HHMM>":
                builder.append("\"pattern\":\"").append("YYYYMMDDHHMM").append("\",");
                break;
            case "<DATE2>":
                builder.append("\"pattern\":\"").append("YYMMDD").append("\",");
                break;
        }
    }

    private static final Set<String> validatorPatternSet = new HashSet<>();

    static {
        validatorPatternSet.add("<DATE4><HHMM>");
        validatorPatternSet.add("35x[$35x]0-3");
        validatorPatternSet.add("35z[$35z]0-5");
        validatorPatternSet.add("65z[$65z]0-49");
    }

    private void appendComponentsIfNecessary(Field field, StringBuilder builder) {
        String validatorPattern = field.validatorPattern();
        if (validatorPatternSet.contains(validatorPattern) || field.componentsSize() < 2) {
            return;
        }
        String fieldName = field.getName();
        if (fieldName.equals(Field27.NAME) || fieldName.equals(Field27A.NAME)) {
            return;
        }
        builder.append(",\"components\":[");
        List<String> componentLabels = field.getComponentLabels();
        for (int i = 0; i < componentLabels.size(); i++) {
            builder.append("{");
            builder.append("\"number\":").append(i + 1).append(",");
            builder.append("\"label\":\"").append(componentLabels.get(i)).append("\",");
            builder.append("\"format\":\"\",");
            builder.append("\"isMandatory\":false");
            builder.append("}");
            if (i != componentLabels.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("],");
    }
}
