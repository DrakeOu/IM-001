package io.drake.im.common.function;

import com.google.protobuf.Message;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2021/05/04/19:27
 *
 * @author : Drake
 * Description: 使用方式，实现这个类，调用注册方法注册处理逻辑；传入ctx和数据处理。对外只要这两个方法暴露
 * if the implement is shared in mulpithreads then should be thread-safe
 */
public abstract class AbstractParser {

    public AbstractParser(){
        parserMap = new HashMap<>();
        registerParser();
    }

    //not assign specific type of map
    protected Map<Class<? extends Message>, IMBiConsumer<? extends Message, ChannelHandlerContext>> parserMap;

    //this method is a fixed process called when init a parser
    protected abstract void registerParser();

    protected <T extends Message> void register(Class<T> clazz, IMBiConsumer<T, ChannelHandlerContext> consumer){
        parserMap.put(clazz, consumer);
    }

    public void parseMsg(Message msg, ChannelHandlerContext ctx){
        if(parserMap.containsKey(msg.getClass())){
            IMBiConsumer consumer = parserMap.get(msg.getClass());
            doParse(msg, ctx, msg.getClass(), consumer);
        }
    }

    private <T extends Message> void doParse(Message msg, ChannelHandlerContext ctx, Class<T> clazz, IMBiConsumer<T, ChannelHandlerContext> consumer){
        T m = clazz.cast(msg);
        consumer.accept(m, ctx);
    }

}
