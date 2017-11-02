package cn.com.fanyu.utils;

public class ResultJson {

    /**
     * 200表示成功  其它错误代码处定义
     */
    public int retCode;
    /**
     * 信息
     */
    public String retMsg;
    /**
     * 错误信息
     */
    public String errorMsg;
    public String data;
    public int getRetCode() {
        return retCode;
    }
    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }
    public String getRetMsg() {
        return retMsg;
    }
    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
    
    /**
     * 完全形态
     * @param retCode
     * @param retMsg
     * @param errorMsg
     * @param data
     */
    public ResultJson(int retCode, String retMsg, String errorMsg, String data) {
        super();
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.errorMsg = errorMsg;
        this.data = data;
    }
    
    /**
     * 成功形态
     * @param retCode
     * @param retMsg
     */
    public ResultJson( int retCode, String retMsg, String data) {
        this.retCode = retCode;
        this.retMsg = retMsg;
        this.data = data;
        this.errorMsg = "";
    }
    
    /**
     * 失败形态
     * @param retCode
     * @param errorMsg
     */
    public ResultJson( int retCode, String errorMsg) {
        this.retCode = retCode;
        this.errorMsg = errorMsg;
        this.retMsg="";
        this.data="";
    }
    
    @Override
    public String toString() {
        if(retCode==200){
            return "{\"retCode\":200,\"retMsg\":\""+retMsg+"\",\"data\":"+(data==null||data.equals("")?"[]":data)+"}";
        }else{
            return "{\"retCode\":"+retCode+",\"errorMsg\":\""+errorMsg+"\"}";
        }
    }
}
