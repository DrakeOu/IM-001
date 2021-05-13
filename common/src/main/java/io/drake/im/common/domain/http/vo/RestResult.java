package io.drake.im.common.domain.http.vo;

import lombok.Data;

/**
 * Date: 2021/04/19/16:24
 *
 * @author : Drake
 * Description:
 */
@Data
public class RestResult<T> {

    private Integer code;
    private String info;
    private T data;

    public static <T> RestResult<T> success(T data){
        RestResult<T> result = new RestResult<>();
        result.setCode(200);
        result.setInfo("success");
        result.setData(data);
        return result;
    }
    //todo http的通信需要提供更多的错误码，客户端也需要开放对非200的处理
    public static RestResult<String> fail(String errorMsg){
        RestResult<String> result = new RestResult<>();
        result.setCode(400);
        result.setInfo(errorMsg);
        result.setData(null);
        return result;
    }

}
