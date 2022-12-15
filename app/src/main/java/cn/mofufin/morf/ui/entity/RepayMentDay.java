package cn.mofufin.morf.ui.entity;

import java.util.ArrayList;
import java.util.List;

public class RepayMentDay {


    /**
     * result_Msg : 生成成功
     * result_Code : 0
     * refundDay : [{"dayType":"d","dayParams":13},{"dayType":"d","dayParams":14},{"dayType":"d","dayParams":15},{"dayType":"d","dayParams":16},{"dayType":"d","dayParams":17},{"dayType":"d","dayParams":18},{"dayType":"d","dayParams":19},{"dayType":"d","dayParams":20},{"dayType":"d","dayParams":21},{"dayType":"d","dayParams":22},{"dayType":"d","dayParams":23},{"dayType":"d","dayParams":24},{"dayType":"d","dayParams":25},{"dayType":"d","dayParams":26},{"dayType":"d","dayParams":27}]
     */

    private String result_Msg;
    private int result_Code;
    private ArrayList<RefundDayBean> refundDay;

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

    public ArrayList<RefundDayBean> getRefundDay() {
        return refundDay;
    }

    public void setRefundDay(ArrayList<RefundDayBean> refundDay) {
        this.refundDay = refundDay;
    }

    public static class RefundDayBean {
        /**
         * dayType : d
         * dayParams : 13
         */

        private String dayType;
        private int dayParams;

        public String getDayType() {
            return dayType;
        }

        public void setDayType(String dayType) {
            this.dayType = dayType;
        }

        public int getDayParams() {
            return dayParams;
        }

        public void setDayParams(int dayParams) {
            this.dayParams = dayParams;
        }
    }
}
