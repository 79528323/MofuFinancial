package cn.mofufin.morf.ui.entity;

/**
 * author：created by Administrator on 2018/8/29 17
 * e-mail:79528323@qq.com
 */
public class SingleBankType {

    /**
     * bool : true
     * data : {"reason":"卡类别鉴定成功","list":{"cardType2":"借记卡","bin":"623058","bankname":"平安银行","cardType1":"平安银行","bankcode":"04100000"}}
     * stateCode : 10000
     * message : 请求成功
     */

    public boolean bool;
    public DataBean data;
    public int stateCode;
    public String message;

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * reason : 卡类别鉴定成功
         * list : {"cardType2":"借记卡","bin":"623058","bankname":"平安银行","cardType1":"平安银行","bankcode":"04100000"}
         */

        private String reason;
        private ListBean list;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * cardType2 : 借记卡
             * bin : 623058
             * bankname : 平安银行
             * cardType1 : 平安银行
             * bankcode : 04100000
             */

            private String cardType2;
            private String bin;
            private String bankname;
            private String cardType1;
            private String bankcode;

            public String getCardType2() {
                return cardType2;
            }

            public void setCardType2(String cardType2) {
                this.cardType2 = cardType2;
            }

            public String getBin() {
                return bin;
            }

            public void setBin(String bin) {
                this.bin = bin;
            }

            public String getBankname() {
                return bankname;
            }

            public void setBankname(String bankname) {
                this.bankname = bankname;
            }

            public String getCardType1() {
                return cardType1;
            }

            public void setCardType1(String cardType1) {
                this.cardType1 = cardType1;
            }

            public String getBankcode() {
                return bankcode;
            }

            public void setBankcode(String bankcode) {
                this.bankcode = bankcode;
            }
        }
    }
}
