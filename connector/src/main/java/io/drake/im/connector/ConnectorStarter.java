package io.drake.im.connector;

import io.drake.im.common.exception.IMException;
import io.drake.im.connector.config.ConnectorConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Date: 2021/04/19/15:21
 *
 * @author : Drake
 * Description:
 */
public class ConnectorStarter {

    public static void main(String[] args) throws IOException {
        ConnectorConfiguration configuration = initConfig();

        TransferClient transferClient = new TransferClient();
        transferClient.start(configuration);

        ConnectorServer.start(transferClient, configuration);
    }

    public static ConnectorConfiguration initConfig() throws IOException {
        Properties properties = loadProperties();
        ConnectorConfiguration configuration = new ConnectorConfiguration();

        configuration.setTransferHost(properties.getProperty("transfer.host", "transfer"));
        configuration.setTransferPort(Integer.parseInt(properties.getProperty("transfer.port", "6062")));

        configuration.setServerPort(Integer.parseInt(properties.getProperty("server.port", "6061")));
        configuration.setRedisHost(properties.getProperty("redis.host", "localhost"));
        configuration.setRedisPort(Integer.parseInt(properties.getProperty("redis.port", "6379")));
        configuration.setRedisPassword(properties.getProperty("redis.password", "foobared"));

        return configuration;
    }

    public static Properties loadProperties() throws IOException {
        InputStream inputStream;
        String path = System.getProperty("config");
        if (path == null) {
            throw new IMException("transfer.properties is not defined");
        } else {
            inputStream = new FileInputStream(path);
        }

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;

    }
}
