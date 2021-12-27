package cc.wanghl.im.ws.web.domain;

public class Command {
    private String action;
    private String from;
    private String to;
    private String data;

    @Override
    public String toString() {
        return "Command{" +
                "action='" + action + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
