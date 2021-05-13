package io.drake.im.common.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.drake.im.common.domain.http.vo.RestResult;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;

/**
 * Date: 2021/04/20/17:53
 *
 * @author : Drake
 * Description:
 */
@Slf4j
public abstract class AbstractRestService<R>{

    protected R restClient;

    public AbstractRestService(Class<R> clazz, String url){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        this.restClient = retrofit.create(clazz);
    }


    protected <T> T doRequest(RestFunction<T> function){

        try {
            Response<RestResult<T>> response = function.doRequest();
            if(!response.isSuccessful()){

            }
            if(response.body() == null){

            }
            if(response.body().getCode() != 200){
                log.debug("req fail not 200, recv:{}", response.body().getInfo());
            }
            return response.body().getData();
        } catch (IOException e) {
            throw new RuntimeException("request error");
        }


    }


    @FunctionalInterface
    protected interface RestFunction<T>{

        Response<RestResult<T>> doRequest() throws IOException;

    }

}
