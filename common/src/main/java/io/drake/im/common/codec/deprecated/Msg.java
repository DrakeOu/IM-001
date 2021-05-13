package io.drake.im.common.codec.deprecated;



public abstract class Msg {

    protected String chatMsg;

    protected String fromId;

    public abstract MsgType getType();

    /**
     * chat msg type enum
     */
    public enum MsgType{

        SINGLE(1), GROUP(2), ACK(0), PERSIST(3), GREET(4);

        private final int type;

        MsgType(int type){
            this.type = type;
        }

        public int getType(){
            return this.type;
        }

    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }
}
