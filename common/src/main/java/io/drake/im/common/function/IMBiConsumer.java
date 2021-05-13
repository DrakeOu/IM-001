package io.drake.im.common.function;

/**
 * Date: 2021/05/04/19:24
 *
 * @author : Drake
 * Description:
 */
@FunctionalInterface
public interface IMBiConsumer<T, U> {

    void accept(T t, U u);

}
