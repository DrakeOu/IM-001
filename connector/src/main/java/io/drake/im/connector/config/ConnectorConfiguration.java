package io.drake.im.connector.config;

import lombok.Data;

/**
 * Date: 2021/05/24/15:50
 *
 * @author : Drake
 * Description:
 */
@Data
public class ConnectorConfiguration {

    private Integer serverPort;

    private String redisHost;

    private String redisPassword;

    private Integer redisPort;

    private String transferHost;

    private Integer transferPort;

}
