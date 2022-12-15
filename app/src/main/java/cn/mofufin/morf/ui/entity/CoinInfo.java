package cn.mofufin.morf.ui.entity;

public class CoinInfo {


    /**
     * result_Msg : 查询成功
     * set : {"shareRegGetWay":1,"mposGetWay":0.01,"moneyTreeGetWayBegin":1,"scanGetWay":0.01,"overseasGetWay":0.01,"moneyTreeGetWayEnd":2,"shareReNameGetWay":5,"refundGetWay":0.01,"moneyGetWay":0.01,"fastGetWay":0.01}
     * result_Code : 0
     */

    public String result_Msg;
    public SetBean set;
    public int result_Code;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public SetBean getSet() {
        return set;
    }

    public void setSet(SetBean set) {
        this.set = set;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public static class SetBean {
        /**
         * shareRegGetWay : 1
         * mposGetWay : 0.01
         * moneyTreeGetWayBegin : 1
         * scanGetWay : 0.01
         * overseasGetWay : 0.01
         * moneyTreeGetWayEnd : 2
         * shareReNameGetWay : 5
         * refundGetWay : 0.01
         * moneyGetWay : 0.01
         * fastGetWay : 0.01
         */

        public int shareRegGetWay;
        public double mposGetWay;
        public int moneyTreeGetWayBegin;
        public double scanGetWay;
        public double overseasGetWay;
        public int moneyTreeGetWayEnd;
        public int shareReNameGetWay;
        public double refundGetWay;
        public double moneyGetWay;
        public double fastGetWay;

        public int getShareRegGetWay() {
            return shareRegGetWay;
        }

        public void setShareRegGetWay(int shareRegGetWay) {
            this.shareRegGetWay = shareRegGetWay;
        }

        public double getMposGetWay() {
            return mposGetWay;
        }

        public void setMposGetWay(double mposGetWay) {
            this.mposGetWay = mposGetWay;
        }

        public int getMoneyTreeGetWayBegin() {
            return moneyTreeGetWayBegin;
        }

        public void setMoneyTreeGetWayBegin(int moneyTreeGetWayBegin) {
            this.moneyTreeGetWayBegin = moneyTreeGetWayBegin;
        }

        public double getScanGetWay() {
            return scanGetWay;
        }

        public void setScanGetWay(double scanGetWay) {
            this.scanGetWay = scanGetWay;
        }

        public double getOverseasGetWay() {
            return overseasGetWay;
        }

        public void setOverseasGetWay(double overseasGetWay) {
            this.overseasGetWay = overseasGetWay;
        }

        public int getMoneyTreeGetWayEnd() {
            return moneyTreeGetWayEnd;
        }

        public void setMoneyTreeGetWayEnd(int moneyTreeGetWayEnd) {
            this.moneyTreeGetWayEnd = moneyTreeGetWayEnd;
        }

        public int getShareReNameGetWay() {
            return shareReNameGetWay;
        }

        public void setShareReNameGetWay(int shareReNameGetWay) {
            this.shareReNameGetWay = shareReNameGetWay;
        }

        public double getRefundGetWay() {
            return refundGetWay;
        }

        public void setRefundGetWay(double refundGetWay) {
            this.refundGetWay = refundGetWay;
        }

        public double getMoneyGetWay() {
            return moneyGetWay;
        }

        public void setMoneyGetWay(double moneyGetWay) {
            this.moneyGetWay = moneyGetWay;
        }

        public double getFastGetWay() {
            return fastGetWay;
        }

        public void setFastGetWay(double fastGetWay) {
            this.fastGetWay = fastGetWay;
        }
    }
}
