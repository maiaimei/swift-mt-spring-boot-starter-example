package cn.maiaimei.example;

import com.prowidesoftware.swift.model.SwiftBlock4;
import com.prowidesoftware.swift.model.Tag;
import com.prowidesoftware.swift.model.field.Field20;
import com.prowidesoftware.swift.model.field.Field23B;
import com.prowidesoftware.swift.model.field.Field57A;
import com.prowidesoftware.swift.model.field.Field71A;
import com.prowidesoftware.swift.model.mt.mt1xx.MT103;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * https://dev.prowidesoftware.com/SRU2022/open-source/core/mt-modify/#rje-readerwriter_1
 */
class MTContentModificationTest extends BaseTest {
    @Test
    void InsertNewFields01() {
        MT103 mt = new MT103();
        mt.append(new Field20("PAY01"));
        mt.append(new Field23B("CRED"));
        mt.append(new Field71A("SHA"));
        SwiftBlock4 b4 = mt.getSwiftMessage().getBlock4();
        assertNull(b4.getTagValue("21"));
        /**
         * The addTag at index adds a tag at the specified position in this tag list, 
         * and shifts the element currently at that position (if any) 
         * and any subsequent elements to the right (adds one to their indices).
         *
         * tag list before: 20, 23B and 71A
         * tag list after: 20, 21, 23B and 71A
         */
        b4.addTag(2, (new Tag("21:RELREF")));
        assertNotNull(b4.getTagValue("21"));
    }

    @Test
    void InsertNewFields02() {
        MT103 mt = new MT103();
        mt.append(new Field20("PAY01"));
        mt.append(new Field23B("CRED"));
        mt.append(new Field71A("SHA"));
        SwiftBlock4 b4 = mt.getSwiftMessage().getBlock4();
        assertNull(b4.getTagValue("21"));
        /**
         * tag list before: 20, 23B and 71A
         * tag list after: 20, 21 and 71A
         */
        b4.setTag(1, (new Tag("21:RELREF")));
        assertNotNull(b4.getTagValue("21"));
    }

    @Test
    void UpdatingFieldValue01() {
        MT103 mt = readFileAsMT103();
        SwiftBlock4 b4 = mt.getSwiftMessage().getBlock4();
        b4.getTagByName("20").setValue("NEWREFERENCE");
        assertEquals("NEWREFERENCE", b4.getTagValue("20"));
    }

    @Test
    void UpdatingFieldValue02() {
        MT103 mt = new MT103();
        mt.append(new Field20("007505327853"));
        mt.append(new Field57A("/abc"));
        SwiftBlock4 b4 = mt.getSwiftMessage().getBlock4();
        Field57A field57A = new Field57A();
        field57A.setAccount("12345");
        field57A.setIdentifierCode("NEWAESMMXXX");
        b4.getTagByName("57A").setValue(field57A.getValue());
        assertEquals("/12345\r\nNEWAESMMXXX", b4.getTagValue("57A"));
    }

    @Test
    void UpdatingFieldValue03() {
        MT103 mt = new MT103();
        mt.append(new Field20("007505327853"));
        mt.append(new Field57A("/abc"));
        SwiftBlock4 b4 = mt.getSwiftMessage().getBlock4();
        Field57A field57A = mt.getField57A().setComponent2("12345").setComponent3("NEWAESMMXXX");
        b4.getTagByName("57A").setValue(field57A.getValue());
        assertEquals("/12345\r\nNEWAESMMXXX", b4.getTagValue("57A"));
    }

    MT103 readFileAsMT103() {
        String message = readFileAsString("mt/mt1xx/MT103.txt");
        return MT103.parse(message);
    }

}
