package cn.mofufin.morf.ui.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Justin_Liu
 * on 2019/8/27
 */
public class WxPays {

    /**
     * result_Msg : 操作成功
     * result_Code : 0
     * params : {"appid":"wx35e0d75a6cdd9a30","noncestr":"VIbAsE95g5TWJTE1Ynw7MhbIoepCjRDz","package":"Sign=WXPay","partnerid":"1523342111","prepayid":"wx280958009862451335b037771664950200","timestamp":"1566957483","sign":"C7174E8EE20C6AE8DD981AC81AE5CC7F96CFF0E964C613E57FE8ED2B4B5471AD"}
     */

    public String result_Msg;
    public int result_Code;
    public ParamsBean params;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean implements Serializable {
        /**
         * appid : wx35e0d75a6cdd9a30
         * noncestr : VIbAsE95g5TWJTE1Ynw7MhbIoepCjRDz
         * package : Sign=WXPay
         * partnerid : 1523342111
         * prepayid : wx280958009862451335b037771664950200
         * timestamp : 1566957483
         * sign : C7174E8EE20C6AE8DD981AC81AE5CC7F96CFF0E964C613E57FE8ED2B4B5471AD
         */

        public String appid;
        public String noncestr;
        @SerializedName("package")
        public String packageX;
        public String partnerid;
        public String prepayid;
        public String timestamp;
        public String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
