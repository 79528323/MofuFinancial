package cn.mofufin.morf.ui.entity;

import java.util.List;

public class PushMerchant {


    /**
     * result_Msg : 查询成功
     * directList : [{"merchantPhone":"15721666866","fdMerIdentityCardName":"范伟林","memberType":3},{"merchantPhone":"18612797555","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"18302014992","fdMerIdentityCardName":"梁晓莉","memberType":3},{"merchantPhone":"13535305252","fdMerIdentityCardName":"潘振锋","memberType":3},{"merchantPhone":"17750903840","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"18565477401","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"18125237319","fdMerIdentityCardName":"黄炳金","memberType":3},{"merchantPhone":"13533059504","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"13421994292","fdMerIdentityCardName":"谭景总","memberType":1},{"merchantPhone":"13691504314","fdMerIdentityCardName":"钱海燕","memberType":2},{"merchantPhone":"13480964985","fdMerIdentityCardName":"王艳","memberType":3},{"merchantPhone":"13712224247","fdMerIdentityCardName":"陈勇辉","memberType":3},{"merchantPhone":"13826084108","fdMerIdentityCardName":null,"memberType":3},{"merchantPhone":"13539963528","fdMerIdentityCardName":"梁鹏章","memberType":3},{"merchantPhone":"18588623826","fdMerIdentityCardName":"范志杰","memberType":3},{"merchantPhone":"13533615210","fdMerIdentityCardName":"王琳","memberType":3},{"merchantPhone":"13295118053","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"15734566234","fdMerIdentityCardName":"杨莹","memberType":3},{"merchantPhone":"18202018252","fdMerIdentityCardName":"倪俊","memberType":1},{"merchantPhone":"18022343941","fdMerIdentityCardName":"李伟清","memberType":1},{"merchantPhone":"13570273901","fdMerIdentityCardName":"张克武","memberType":1},{"merchantPhone":"18807314099","fdMerIdentityCardName":"龙志敏","memberType":1},{"merchantPhone":"13570245168","fdMerIdentityCardName":"黎国棠","memberType":2},{"merchantPhone":"13873155833","fdMerIdentityCardName":"罗小敏","memberType":1},{"merchantPhone":"13730111176","fdMerIdentityCardName":"宋仕帅","memberType":1},{"merchantPhone":"18611431884","fdMerIdentityCardName":"白霞","memberType":1},{"merchantPhone":"15586680086","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"17520365821","fdMerIdentityCardName":"谢明","memberType":1},{"merchantPhone":"13720520148","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"15652073386","fdMerIdentityCardName":"高祥","memberType":1},{"merchantPhone":"13929569292","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"17773412518","fdMerIdentityCardName":"胡翠红","memberType":1},{"merchantPhone":"18684580077","fdMerIdentityCardName":"黄华","memberType":1},{"merchantPhone":"13212202875","fdMerIdentityCardName":"齐丽娜","memberType":1},{"merchantPhone":"18816677717","fdMerIdentityCardName":null,"memberType":3},{"merchantPhone":"13611015170","fdMerIdentityCardName":"张君杰","memberType":1},{"merchantPhone":"18809888380","fdMerIdentityCardName":"邵靖博","memberType":3},{"merchantPhone":"18239398110","fdMerIdentityCardName":"郭鹏","memberType":1},{"merchantPhone":"15970906220","fdMerIdentityCardName":"胡英英","memberType":1},{"merchantPhone":"15830882223","fdMerIdentityCardName":"白露露","memberType":1},{"merchantPhone":"18218048571","fdMerIdentityCardName":"李运雄","memberType":3},{"merchantPhone":"13552774041","fdMerIdentityCardName":"刘亭","memberType":1},{"merchantPhone":"18857886267","fdMerIdentityCardName":"丰国富","memberType":3},{"merchantPhone":"17358720923","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"18773407107","fdMerIdentityCardName":"陶聪","memberType":1},{"merchantPhone":"18932110059","fdMerIdentityCardName":"崔南君","memberType":1},{"merchantPhone":"18610883398","fdMerIdentityCardName":"沈文杰","memberType":1},{"merchantPhone":"18075883728","fdMerIdentityCardName":null,"memberType":1},{"merchantPhone":"17702265282","fdMerIdentityCardName":"刘芳","memberType":1},{"merchantPhone":"18565494555","fdMerIdentityCardName":null,"memberType ":3},{"merchantPhone ":"15307476336 ","fdMerIdentityCardName ":"钟晓安 ","memberType ":1}]
     * result_Code  : 0
     * queryType  : 0
     */

    public String result_Msg;
    public int result_Code;
    public String queryType;
    public List<DirectListBean> directList;

    public String officeName;
    public int indirectTotalPersonNumber;

    public static class DirectListBean {
        /**
         * merchantPhone : 15721666866
         * fdMerIdentityCardName : 范伟林
         * memberType : 3
         * memberType  : 3
         * merchantPhone  : 15307476336
         * fdMerIdentityCardName  : 钟晓安
         */

        public String merchantPhone;
        public String fdMerIdentityCardName;
        public int memberType;
    }
}
