package io.drake.im.client;

import io.drake.im.client.api.ClientRestClient;
import io.drake.im.client.api.RestClientService;

/**
 * Date: 2021/04/20/17:38
 *
 * @author : Drake
 * Description:
 */
public class ClientFactory {

    public static ImClient newClient(String baseUrl){
        ImClient imClient = new ImClient(new RestClientService(ClientRestClient.class, baseUrl));
        //todo
        return imClient;
    }

}
