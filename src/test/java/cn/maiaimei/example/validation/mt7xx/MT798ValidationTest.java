package cn.maiaimei.example.validation.mt7xx;

import cn.maiaimei.example.validation.ValidationTest;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field12;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field77E;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import org.apache.commons.lang3.StringUtils;

public abstract class MT798ValidationTest extends ValidationTest {

    protected MT798 mockMT798() {
        MT798 mt798 = new MT798();
        mt798.append(new Field20("FGH96372"));
        mt798.append(new Field12(getSubMessageType()));
        mt798.append(new Field77E());
        return mt798;
    }

    protected void validateField(String tagName, String invalidValue) {
        MT798 mt798 = mockMT798();
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.append(new Tag(tagName, StringUtils.EMPTY));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
        mt798.getSwiftMessage().getBlock4().removeTag(tagName);
        mt798.append(new Tag(tagName, invalidValue));
        assertAndPrintResult(validationEngine.validate(mt798), t -> t.startsWith(tagName));
    }

    protected abstract String getSubMessageType();

}
