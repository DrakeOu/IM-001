package io.drake.im.common.domain.conn;

/**
 * Date: 2021/04/19/14:36
 *
 * @author : Drake
 * Description: manager conn
 */
public interface ConnContext<C extends Conn> {

    C getConn(Integer id);

    Boolean add(C c);

    Boolean remove(Integer id);
}
