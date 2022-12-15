package cn.mofufin.morf.ui.entity;

import java.util.List;

/**
 * 兑换历史物品
 */
public class ExchangeHistory {


    /**
     * bool : true
     * data : {"reason":"查询成功","list":[{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-26 09:52:34","gdGoodsMoney":500,"gcGoodsNumber":"2819E086762844EF9F6503426E4B204D"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-25 17:49:01","gdGoodsMoney":500,"gcGoodsNumber":"0BADB2BF6F1D4E85892F9F38E2F15970"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-25 17:30:15","gdGoodsMoney":500,"gcGoodsNumber":"A70FE4C5C8C74304926BF4412CDB236C"},{"gdGoodsName":"快捷减免券","gdGoodsCode":"863762","gcDate":"2018-12-25 12:03:04","gdGoodsMoney":1000,"gcGoodsNumber":"85034222F7AE4CCDB0C723A60F39C6CE"},{"gdGoodsName":"钻石会员券","gdGoodsCode":"585460","gcDate":"2018-12-25 11:11:47","gdGoodsMoney":8888,"gcGoodsNumber":"938DD26C7C604E3BB0D8DC9CD65F8A46"},{"gdGoodsName":"金牌会员券","gdGoodsCode":"551292","gcDate":"2018-12-25 11:11:41","gdGoodsMoney":3888,"gcGoodsNumber":"151AF41DE5584FD4A28949A15AF3E784"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-24 18:00:58","gdGoodsMoney":500,"gcGoodsNumber":"32FBDA745C8A4C4BB650A9BE38FBD674"},{"gdGoodsName":"代付减免券","gdGoodsCode":"993779","gcDate":"2018-12-24 16:09:03","gdGoodsMoney":500,"gcGoodsNumber":"465BF5CC4F2640E0B77EE97FA6BC5AFF"},{"gdGoodsName":"境外减免券","gdGoodsCode":"766685","gcDate":"2018-12-24 15:57:22","gdGoodsMoney":1888,"gcGoodsNumber":"6002145E073D41B6908FCDD74654FC6D"}]}
     * stateCode : 10000
     * message : Request Success!
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
         * reason : 查询成功
         * list : [{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-26 09:52:34","gdGoodsMoney":500,"gcGoodsNumber":"2819E086762844EF9F6503426E4B204D"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-25 17:49:01","gdGoodsMoney":500,"gcGoodsNumber":"0BADB2BF6F1D4E85892F9F38E2F15970"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-25 17:30:15","gdGoodsMoney":500,"gcGoodsNumber":"A70FE4C5C8C74304926BF4412CDB236C"},{"gdGoodsName":"快捷减免券","gdGoodsCode":"863762","gcDate":"2018-12-25 12:03:04","gdGoodsMoney":1000,"gcGoodsNumber":"85034222F7AE4CCDB0C723A60F39C6CE"},{"gdGoodsName":"钻石会员券","gdGoodsCode":"585460","gcDate":"2018-12-25 11:11:47","gdGoodsMoney":8888,"gcGoodsNumber":"938DD26C7C604E3BB0D8DC9CD65F8A46"},{"gdGoodsName":"金牌会员券","gdGoodsCode":"551292","gcDate":"2018-12-25 11:11:41","gdGoodsMoney":3888,"gcGoodsNumber":"151AF41DE5584FD4A28949A15AF3E784"},{"gdGoodsName":"快捷返现券","gdGoodsCode":"955981","gcDate":"2018-12-24 18:00:58","gdGoodsMoney":500,"gcGoodsNumber":"32FBDA745C8A4C4BB650A9BE38FBD674"},{"gdGoodsName":"代付减免券","gdGoodsCode":"993779","gcDate":"2018-12-24 16:09:03","gdGoodsMoney":500,"gcGoodsNumber":"465BF5CC4F2640E0B77EE97FA6BC5AFF"},{"gdGoodsName":"境外减免券","gdGoodsCode":"766685","gcDate":"2018-12-24 15:57:22","gdGoodsMoney":1888,"gcGoodsNumber":"6002145E073D41B6908FCDD74654FC6D"}]
         */

        public String reason;
        public List<ListBean> list;

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * gdGoodsName : 快捷返现券
             * gdGoodsCode : 955981
             * gcDate : 2018-12-26 09:52:34
             * gdGoodsMoney : 500
             * gcGoodsNumber : 2819E086762844EF9F6503426E4B204D
             */

            public String gdGoodsName;
            public String gdGoodsCode;
            public String gcDate;
            public int gdGoodsMoney;
            public String gcGoodsNumber;

            public String getGdGoodsName() {
                return gdGoodsName;
            }

            public void setGdGoodsName(String gdGoodsName) {
                this.gdGoodsName = gdGoodsName;
            }

            public String getGdGoodsCode() {
                return gdGoodsCode;
            }

            public void setGdGoodsCode(String gdGoodsCode) {
                this.gdGoodsCode = gdGoodsCode;
            }

            public String getGcDate() {
                return gcDate;
            }

            public void setGcDate(String gcDate) {
                this.gcDate = gcDate;
            }

            public int getGdGoodsMoney() {
                return gdGoodsMoney;
            }

            public void setGdGoodsMoney(int gdGoodsMoney) {
                this.gdGoodsMoney = gdGoodsMoney;
            }

            public String getGcGoodsNumber() {
                return gcGoodsNumber;
            }

            public void setGcGoodsNumber(String gcGoodsNumber) {
                this.gcGoodsNumber = gcGoodsNumber;
            }
        }
    }
}
