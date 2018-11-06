package cn.shenzhenlizuosystemapp.Common.DataAnalysis;

public class Json {
    String Success;
    String msg;

    public String getSuccess() {
        return Success;
    }

    public String getMsg() {
        return msg;
    }

    public Json(String Success, String Msg) {
        this.Success =Success;
        this.msg = Msg;
    }
}
