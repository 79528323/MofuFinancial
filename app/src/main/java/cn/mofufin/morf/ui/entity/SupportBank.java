package cn.mofufin.morf.ui.entity;

import java.util.List;

public class SupportBank {

    public String result_Msg;
    public int result_Code;
    public List<SupportBankBean> supportBankList;

    public static class SupportBankBean{
        public String bankNumber;// 银行编号
        public String bankName;//   银行名称
        public String singlePenQuota;// 单笔消费限额
        public String singleDayCardQuota;// 单日单卡消费限额
    }
}
