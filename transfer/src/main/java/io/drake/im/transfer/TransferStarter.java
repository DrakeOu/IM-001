package io.drake.im.transfer;

import io.drake.im.common.exception.IMException;
import io.drake.im.transfer.config.TransferConfiguration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Date: 2021/04/23/15:15
 *
 * @author : Drake
 * Description:
 */
public class TransferStarter {

    public static void main(String[] args) throws IOException {
        TransferConfiguration configuration = initConfig();
        TransferServer transferServer = new TransferServer(configuration);
        transferServer.start();
    }


    public static TransferConfiguration initConfig() throws IOException {
        Properties properties = loadProperties();
        TransferConfiguration configuration = new TransferConfiguration();

        configuration.setServerPort(Integer.parseInt(properties.getProperty("server.port")));

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
