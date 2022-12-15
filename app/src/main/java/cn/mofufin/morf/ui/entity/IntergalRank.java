package cn.mofufin.morf.ui.entity;

import java.util.List;

/**
 * Created by Justin_Liu
 * on 2019/8/28
 */
public class IntergalRank {

    /**
     * result_Msg : 查询成功
     * activity : {"fdCurrentMonthList":[{"merchantCode":"M20180818150448709","phone":"13530003245","integral":4464748,"name":"袁景华 "},{"merchantCode":"M20181001182241757","phone":"13761584587","integral":2169532,"name":"黄磊"},{"merchantCode":"M20181015103519327","phone":"13660180395","integral":2099638,"name":"陆声珍"},{"merchantCode":"M20180830185312687","phone":"13548888831","integral":1676007,"name":"印近近"},{"merchantCode":"M20180923142613140","phone":"18566057025","integral":1319389,"name":"杨阳"}],"faHref":"https://mp.weixin.qq.com/s/ryfbOs-8nB9NIFtT7k5YHg","faUpMonthList":"","faState":0}
     * result_Code : 0
     */

    public String result_Msg;
    public ActivityBean activity;
    public int result_Code;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public ActivityBean getActivity() {
        return activity;
    }

    public void setActivity(ActivityBean activity) {
        this.activity = activity;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public static class ActivityBean {
        /**
         * fdCurrentMonthList : [{"merchantCode":"M20180818150448709","phone":"13530003245","integral":4464748,"name":"袁景华 "},{"merchantCode":"M20181001182241757","phone":"13761584587","integral":2169532,"name":"黄磊"},{"merchantCode":"M20181015103519327","phone":"13660180395","integral":2099638,"name":"陆声珍"},{"merchantCode":"M20180830185312687","phone":"13548888831","integral":1676007,"name":"印近近"},{"merchantCode":"M20180923142613140","phone":"18566057025","integral":1319389,"name":"杨阳"}]
         * faHref : https://mp.weixin.qq.com/s/ryfbOs-8nB9NIFtT7k5YHg
         * faUpMonthList :
         * faState : 0
         */

        public String faHref;
//        public String faUpMonthList;
        public int faState;
        public int faDayState;
        public List<FdMonthListBean> fdCurrentMonthList;
        public List<FdMonthListBean> faUpMonthList;

        public List<FdDayListBean> faUpDayList;
        public List<FdDayListBean> faCurrentDayList;

        public String getFaHref() {
            return faHref;
        }

        public void setFaHref(String faHref) {
            this.faHref = faHref;
        }

//        public String getFaUpMonthList() {
//            return faUpMonthList;
//        }
//
//        public void setFaUpMonthList(String faUpMonthList) {
//            this.faUpMonthList = faUpMonthList;
//        }

        public int getFaState() {
            return faState;
        }

        public void setFaState(int faState) {
            this.faState = faState;
        }

        public List<FdMonthListBean> getFdCurrentMonthList() {
            return fdCurrentMonthList;
        }

        public void setFdCurrentMonthList(List<FdMonthListBean> fdCurrentMonthList) {
            this.fdCurrentMonthList = fdCurrentMonthList;
        }

        public static class FdMonthListBean {
            /**
             * merchantCode : M20180818150448709
             * phone : 13530003245
             * integral : 4464748
             * name : 袁景华
             */

            public String merchantCode;
            public String phone;
            public int integral;
            public String name;

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }



        public static class FdDayListBean {
            /**
             * merchantCode : M20180818150448709
             * phone : 13530003245
             * integral : 4464748
             * name : 袁景华
             */

            public String merchantCode;
            public String phone;
            public int integral;
            public String name;

            public String getMerchantCode() {
                return merchantCode;
            }

            public void setMerchantCode(String merchantCode) {
                this.merchantCode = merchantCode;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
