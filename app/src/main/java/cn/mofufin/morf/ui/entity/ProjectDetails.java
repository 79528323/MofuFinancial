package cn.mofufin.morf.ui.entity;

import java.util.List;

public class ProjectDetails {

    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * planDetailsList : [{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":4.05,"deIsRebate":1,"deExecuteGroup":"35929171","deExecuteDate":"10:27:00","deOrderCode":"HKM20190723170523359969","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":663},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.85,"deIsRebate":1,"deExecuteGroup":"35929171","deExecuteDate":"11:37:00","deOrderCode":"HKM20190723170523588859","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":631},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":4.19,"deIsRebate":1,"deExecuteGroup":"35929171","deExecuteDate":"12:54:00","deOrderCode":"HKM20190723170523103526","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":686},{"deState":0,"deType":1,"rpOrderCode":"HK20190723170523484465","dePayFee":1.5,"deIsRebate":1,"deExecuteGroup":"35929171","deExecuteDate":"13:49:00","deOrderCode":"HKM20190723170523111043","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":1966.41},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.56,"deIsRebate":1,"deExecuteGroup":"58086223","deExecuteDate":"15:07:00","deOrderCode":"HKM20190723170523868532","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":583},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.34,"deIsRebate":1,"deExecuteGroup":"58086223","deExecuteDate":"16:25:00","deOrderCode":"HKM20190723170523550958","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":547},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.4,"deIsRebate":1,"deExecuteGroup":"58086223","deExecuteDate":"17:26:00","deOrderCode":"HKM20190723170523798835","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":557},{"deState":0,"deType":1,"rpOrderCode":"HK20190723170523484465","dePayFee":1.5,"deIsRebate":1,"deExecuteGroup":"58086223","deExecuteDate":"18:54:00","deOrderCode":"HKM20190723170523672508","deExecuteDay":"2019-07-24 00:00:00","dePayMoney":1675.2},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.73,"deIsRebate":1,"deExecuteGroup":"79546098","deExecuteDate":"09:59:00","deOrderCode":"HKM20190723170523480495","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":611},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.52,"deIsRebate":1,"deExecuteGroup":"79546098","deExecuteDate":"11:11:00","deOrderCode":"HKM20190723170523542079","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":576},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.54,"deIsRebate":1,"deExecuteGroup":"79546098","deExecuteDate":"12:35:00","deOrderCode":"HKM20190723170523767491","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":580},{"deState":0,"deType":1,"rpOrderCode":"HK20190723170523484465","dePayFee":1.5,"deIsRebate":1,"deExecuteGroup":"79546098","deExecuteDate":"13:57:00","deOrderCode":"HKM20190723170523318648","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":1754.71},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":3.1,"deIsRebate":1,"deExecuteGroup":"32572495","deExecuteDate":"15:09:00","deOrderCode":"HKM20190723170523233180","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":507},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":2.92,"deIsRebate":1,"deExecuteGroup":"32572495","deExecuteDate":"16:19:00","deOrderCode":"HKM20190723170523233963","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":478},{"deState":0,"deType":0,"rpOrderCode":"HK20190723170523484465","dePayFee":2.94,"deIsRebate":1,"deExecuteGroup":"32572495","deExecuteDate":"17:23:00","deOrderCode":"HKM20190723170523861277","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":481},{"deState":0,"deType":1,"rpOrderCode":"HK20190723170523484465","dePayFee":1.5,"deIsRebate":1,"deExecuteGroup":"32572495","deExecuteDate":"18:43:00","deOrderCode":"HKM20190723170523627071","deExecuteDay":"2019-07-25 00:00:00","dePayMoney":1455.54}]
     */

    public String result_Msg;
    public int result_Code;
    public List<PlanDetailsListBean> planDetailsList;

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

    public List<PlanDetailsListBean> getPlanDetailsList() {
        return planDetailsList;
    }

    public void setPlanDetailsList(List<PlanDetailsListBean> planDetailsList) {
        this.planDetailsList = planDetailsList;
    }

    public static class PlanDetailsListBean {
        /**
         * deState : 0
         * deType : 0
         * rpOrderCode : HK20190723170523484465
         * dePayFee : 4.05
         * deIsRebate : 1
         * deExecuteGroup : 35929171
         * deExecuteDate : 10:27:00
         * deOrderCode : HKM20190723170523359969
         * deExecuteDay : 2019-07-24 00:00:00
         * dePayMoney : 663
         */

        public int deState;
        public int deType;
        public String rpOrderCode;
        public double dePayFee;
        public int deIsRebate;
        public String deExecuteGroup;
        public String deExecuteDate;
        public String deOrderCode;
        public String deExecuteDay;
        public double dePayMoney;

        public int getDeState() {
            return deState;
        }

        public void setDeState(int deState) {
            this.deState = deState;
        }

        public int getDeType() {
            return deType;
        }

        public void setDeType(int deType) {
            this.deType = deType;
        }

        public String getRpOrderCode() {
            return rpOrderCode;
        }

        public void setRpOrderCode(String rpOrderCode) {
            this.rpOrderCode = rpOrderCode;
        }

        public double getDePayFee() {
            return dePayFee;
        }

        public void setDePayFee(double dePayFee) {
            this.dePayFee = dePayFee;
        }

        public int getDeIsRebate() {
            return deIsRebate;
        }

        public void setDeIsRebate(int deIsRebate) {
            this.deIsRebate = deIsRebate;
        }

        public String getDeExecuteGroup() {
            return deExecuteGroup;
        }

        public void setDeExecuteGroup(String deExecuteGroup) {
            this.deExecuteGroup = deExecuteGroup;
        }

        public String getDeExecuteDate() {
            return deExecuteDate;
        }

        public void setDeExecuteDate(String deExecuteDate) {
            this.deExecuteDate = deExecuteDate;
        }

        public String getDeOrderCode() {
            return deOrderCode;
        }

        public void setDeOrderCode(String deOrderCode) {
            this.deOrderCode = deOrderCode;
        }

        public String getDeExecuteDay() {
            return deExecuteDay;
        }

        public void setDeExecuteDay(String deExecuteDay) {
            this.deExecuteDay = deExecuteDay;
        }

//        public int getDePayMoney() {
//            return dePayMoney;
//        }

        public void setDePayMoney(int dePayMoney) {
            this.dePayMoney = dePayMoney;
        }
    }
}
