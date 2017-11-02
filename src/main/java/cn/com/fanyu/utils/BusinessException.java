package cn.com.fanyu.utils;



public class BusinessException extends RuntimeException {

    private int retCode = ResultCode.FAILE_CODE;
    private String retMsg;

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

    public BusinessException() {
        super();
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(int retCode, String retMsg) {
        super(retMsg);
        this.retCode = retCode;
        this.retMsg = retMsg;
    }

    public BusinessException(String retMsg) {
        super(retMsg);
        this.retMsg = retMsg;
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

}