package io.drake.im.connector;

import org.slf4j.impl.StaticLoggerBinder;
/**
 * Date: 2021/04/19/15:21
 *
 * @author : Drake
 * Description:
 */
public class ConnectorStarter {

    public static void main(String[] args) {
        TransferClient transferClient = new TransferClient();
        transferClient.start();
        ConnectorServer.start(transferClient);
    }
}