package cn.maiaimei.example;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prowidesoftware.swift.model.mt.mt7xx.MT798;
import lombok.SneakyThrows;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.InputStreamReader;

public class BaseTest {
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 序列化的时候忽略NULL
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
        // 反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);
    }

    protected MT798 readFileAsMT798(String path) {
        return MT798.parse(readFileAsString(path));
    }

    @SneakyThrows
    protected String readFileAsString(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        InputStream inputStream = resource.getInputStream();
        return FileCopyUtils.copyToString(new InputStreamReader(inputStream));
    }

    @SneakyThrows
    protected <T> T readFileAsObject(String path, Class<T> valueType) {
        String json = readFileAsString(path);
        return objectMapper.readValue(json, valueType);
    }

    @SneakyThrows
    protected <T> T readFileAsObject(String path, TypeReference<T> valueTypeRef) {
        String json = readFileAsString(path);
        return objectMapper.readValue(json, valueTypeRef);
    }

}
