package cc.wanghl.im.ws.web.domain;

public class Response {
    private String code;
    private String msg;

    public Response(String code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public static Response gen(){
        return new Response("00", "success");
    }
    public static Response gen(String code,String msg){
        return new Response(code, msg);
    }
    @Override
    public String toString() {
        return "{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String toJSON() {
        return "{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
