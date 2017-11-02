package cn.com.fanyu.utils;

public class LayuiResultJson {

    /**
     * 200表示成功  其它错误代码处定义
     */
    public int code;
    public long count;
    /**
     * 信息
     */
    public String msg;
    public String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    /**
     * 完全形态
     *
     * @param code
     * @param msg
     * @param count
     * @param data
     */
    public LayuiResultJson(int code, String msg, long count, String data) {
        super();
        this.code = code;
        this.msg = msg;
        this.count = count;
        this.data = data;
    }

    @Override
    public String toString() {
        return "{\"code\":" + code + "," +
                "\"msg\":\"" + msg + "\"," +
                "\"count\":\"" + count + "\"," +
                "\"data\":" + (data.equals("") ? "[]" : data) + "}";
    }
}
