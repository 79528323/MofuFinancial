package cn.mofufin.morf.ui.base;

/**
 * 作者：created by Liuboda on 2018-8-29 17:00
 * @param <T>
 */
public class BaseResponse<T> {
//    public int code;
//    public String tips;
    public int result_Code;
    public String result_Msg;
    public boolean bool;
    public int stateCode;
    public String message;
    public T data;

    public final static int STATUS_SUCCESS=10000;
    public final static int STATUS_ADD_SUCCESS=201;
    public final static int STATUS_DELETE_SUCCESS=204;
    public final static int STATUS_LOGIN_TIMEOUT=401;
    public final static int STATUS_LOGIN_UNBIND=402;
    public final static int STATUS_TIME_OUT=504;
    public final static int STATUS_CREAT_ROOM=410;

    public final static int STATUS_FAIL = 10009;
}
