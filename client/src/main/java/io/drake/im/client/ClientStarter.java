package io.drake.im.client;

import io.drake.im.common.domain.http.vo.RelationVO;

import java.util.List;
import java.util.Scanner;

/**
 * Date: 2021/04/20/17:02
 *
 * @author : Drake
 * Description:
 */
public class ClientStarter {

    private static final String baseUrl = "http://192.168.169.2:8080/u/";

    public static void main(String[] args) {
        ImClient imClient = ClientFactory.newClient(baseUrl);

        Scanner scanner = new Scanner(System.in);

        boolean work = true;
        while(work && !Thread.interrupted()){
            String cmd = scanner.nextLine();
            if(":q".equals(cmd)) {
//                work = false;
                imClient.offline();
            }
            // send msg to friend
            if(":m".equals(cmd)){
                System.out.print("enter toUser\n");
                String toUser = scanner.nextLine();
                System.out.print("enter msg\n");
                String msg = scanner.nextLine();
                imClient.sendMsgToFriend(toUser, msg);
            }
            //poll friend list
            if(":f".equals(cmd)){
                System.out.print("poll friends from server...");
                List<RelationVO> friends = imClient.getFriends();
                System.out.print("here are your friend list");
                friends.forEach(x -> System.out.println(x.getUserNameB()));
            }
            //register
            if(":re".equals(cmd)){
                System.out.print("register username & password, split by :");
                String line = scanner.nextLine();
                String[] split = line.split(":");
                imClient.register(split[0], split[1]);
            }
            //login
            if(":lo".equals(cmd)){
                System.out.print("enter username & password, split by :");
                String line = scanner.nextLine();
                String[] split = line.split(":");
                imClient.login(split[0], split[1]);
            }
            if(":nf".equals(cmd)){
                System.out.print("enter username wish to add");
                String line = scanner.nextLine();
                imClient.addFriend(line);
            }
            if(":fri".equals(cmd)){
                System.out.println("enter username with command, like userA:DENY");
                String line = scanner.nextLine();
                String[] split = line.split(":");
                imClient.copeFriendRequest(split[0], split[1]);
            }
            if(":cg".equals(cmd)){
                System.out.println("enter groupname wish to create");
                String line = scanner.nextLine();
                imClient.createGroup(line);
            }
            if(":iu".equals(cmd)){
                System.out.println("enter userName to invite with the groupId, split with :");
                String line = scanner.nextLine();
                String[] split = line.split(":");
                imClient.inviteUser(split[0], Long.parseLong(split[1]));
            }
            if(":gm".equals(cmd)){
                System.out.println("enter groupName and msg, split with :");
                String line = scanner.nextLine();
                String[] split = line.split(":");
                imClient.sendMsgToGroup(split[0], split[1]);
            }
        }

//        imClient

    }
}
