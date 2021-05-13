package io.drake.im.common.codec.deprecated;

/**
 * Date: 2021/04/22/11:54
 *
 * @author : Drake
 * Description:
 */
public abstract class Internal {

    private String from;
    private String to;
    private String content;

    enum Type{
        MSG(1), ACK(0);

        Type(int code){
            this.code = code;
        }
        private int code;
        public int getCode(){return code;}
    }

}
