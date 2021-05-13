package io.drake.im.common.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.drake.im.common.exception.IMException;

import java.io.IOException;

public class JSONSerializer {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static byte[] serializeToBytes(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(object);
    }

    public static  <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }

    public static <T> T deserializeFromStr(Class<T> clazz, String str) throws IOException {
        return objectMapper.readValue(str, clazz);
    }

    public static String serializeToStr(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

    static JsonFormat.Printer printer;
    static JsonFormat.Parser parser;

    static {
        printer = JsonFormat.printer();
        parser = JsonFormat.parser();
    }

    public static String serializeProtoBuf(Message message){
        try {
            return printer.print(message);
        } catch (InvalidProtocolBufferException e) {
            throw new IMException("PROTOBUF CONVERT FAIL", e);
        }
    }

    public static void deserializeProtoBuf(String json, Message.Builder builder){
        try {
            parser.merge(json, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new IMException("PARSE JSON ERROR", e);
        }
    }

}
