package io.drake.im.transfer;

/**
 * Date: 2021/04/23/15:15
 *
 * @author : Drake
 * Description:
 */
public class TransferStarter {

    public static void main(String[] args) {
        TransferServer transferServer = new TransferServer();
        transferServer.start();
    }
}
