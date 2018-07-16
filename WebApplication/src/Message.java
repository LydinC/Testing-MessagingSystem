public class Message {

    private String sourceAgentID;
    private String targetAgentID;
    private long timestamp;
    private String messageContent;

    public Message(String sourceAgentID, String targetAgentID, long l, String message) {
        this.sourceAgentID = sourceAgentID;
        this.targetAgentID = targetAgentID;
        this.timestamp = l;
        this.messageContent = message;
    }

    public Message() {}

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public String getSourceAgentID() {
        return sourceAgentID;
    }

    public String getTargetAgentID() {
        return targetAgentID;
    }
}