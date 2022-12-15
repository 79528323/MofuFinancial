package cn.mofufin.morf.ui.entity;

import java.util.List;

public class FallProvinceCity {


    /**
     * result_Msg : 查询成功
     * result_Code : 0
     * proCityList : [{"cityList":[{"cityCode":"9247","cityName":"万州区"},{"cityCode":"9248","cityName":"涪陵区"},{"cityCode":"9249","cityName":"渝中区"},{"cityCode":"9250","cityName":"大渡口区"},{"cityCode":"9251","cityName":"江北区"},{"cityCode":"9252","cityName":"沙坪坝区"},{"cityCode":"9253","cityName":"九龙坡区"},{"cityCode":"9254","cityName":"南岸区"},{"cityCode":"9255","cityName":"北碚区"},{"cityCode":"9256","cityName":"万盛区"},{"cityCode":"9257","cityName":"双桥区"},{"cityCode":"9258","cityName":"渝北区"},{"cityCode":"9259","cityName":"巴南区"},{"cityCode":"9260","cityName":"黔江区"},{"cityCode":"9261","cityName":"长寿区"},{"cityCode":"9262","cityName":"江津区"},{"cityCode":"9263","cityName":"合川区"},{"cityCode":"9264","cityName":"永川区"},{"cityCode":"9265","cityName":"南川区"},{"cityCode":"9266","cityName":"綦江县"},{"cityCode":"9267","cityName":"潼南县"},{"cityCode":"9269","cityName":"铜梁县"},{"cityCode":"9270","cityName":"大足县"},{"cityCode":"9271","cityName":"荣昌县"},{"cityCode":"9272","cityName":"璧山县"},{"cityCode":"9273","cityName":"梁平县"},{"cityCode":"9274","cityName":"城口县"},{"cityCode":"9275","cityName":"丰都县"},{"cityCode":"9276","cityName":"垫江县"},{"cityCode":"9277","cityName":"武隆县"},{"cityCode":"9278","cityName":"忠　县"},{"cityCode":"9279","cityName":"开　县"},{"cityCode":"9280","cityName":"云阳县"},{"cityCode":"9281","cityName":"奉节县"},{"cityCode":"9282","cityName":"巫山县"},{"cityCode":"9283","cityName":"巫溪县"},{"cityCode":"9284","cityName":"石柱土家族自治县"},{"cityCode":"9285","cityName":"秀山土家族苗族自治县"},{"cityCode":"9286","cityName":"酉阳土家族苗族自治县"},{"cityCode":"9287","cityName":"彭水苗族土家族自治县"}],"provinceCode":"1001","provinceName":"重庆"}]
     */

    private String result_Msg;
    private int result_Code;
    private List<ProCityListBean> proCityList;

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

    public List<ProCityListBean> getProCityList() {
        return proCityList;
    }

    public void setProCityList(List<ProCityListBean> proCityList) {
        this.proCityList = proCityList;
    }

    public static class ProCityListBean {
        /**
         * cityList : [{"cityCode":"9247","cityName":"万州区"},{"cityCode":"9248","cityName":"涪陵区"},{"cityCode":"9249","cityName":"渝中区"},{"cityCode":"9250","cityName":"大渡口区"},{"cityCode":"9251","cityName":"江北区"},{"cityCode":"9252","cityName":"沙坪坝区"},{"cityCode":"9253","cityName":"九龙坡区"},{"cityCode":"9254","cityName":"南岸区"},{"cityCode":"9255","cityName":"北碚区"},{"cityCode":"9256","cityName":"万盛区"},{"cityCode":"9257","cityName":"双桥区"},{"cityCode":"9258","cityName":"渝北区"},{"cityCode":"9259","cityName":"巴南区"},{"cityCode":"9260","cityName":"黔江区"},{"cityCode":"9261","cityName":"长寿区"},{"cityCode":"9262","cityName":"江津区"},{"cityCode":"9263","cityName":"合川区"},{"cityCode":"9264","cityName":"永川区"},{"cityCode":"9265","cityName":"南川区"},{"cityCode":"9266","cityName":"綦江县"},{"cityCode":"9267","cityName":"潼南县"},{"cityCode":"9269","cityName":"铜梁县"},{"cityCode":"9270","cityName":"大足县"},{"cityCode":"9271","cityName":"荣昌县"},{"cityCode":"9272","cityName":"璧山县"},{"cityCode":"9273","cityName":"梁平县"},{"cityCode":"9274","cityName":"城口县"},{"cityCode":"9275","cityName":"丰都县"},{"cityCode":"9276","cityName":"垫江县"},{"cityCode":"9277","cityName":"武隆县"},{"cityCode":"9278","cityName":"忠　县"},{"cityCode":"9279","cityName":"开　县"},{"cityCode":"9280","cityName":"云阳县"},{"cityCode":"9281","cityName":"奉节县"},{"cityCode":"9282","cityName":"巫山县"},{"cityCode":"9283","cityName":"巫溪县"},{"cityCode":"9284","cityName":"石柱土家族自治县"},{"cityCode":"9285","cityName":"秀山土家族苗族自治县"},{"cityCode":"9286","cityName":"酉阳土家族苗族自治县"},{"cityCode":"9287","cityName":"彭水苗族土家族自治县"}]
         * provinceCode : 1001
         * provinceName : 重庆
         */

        private String provinceCode;
        private String provinceName;
        private List<CityListBean> cityList;

        public String getProvinceCode() {
            return provinceCode;
        }

        public void setProvinceCode(String provinceCode) {
            this.provinceCode = provinceCode;
        }

        public String getProvinceName() {
            return provinceName;
        }

        public void setProvinceName(String provinceName) {
            this.provinceName = provinceName;
        }

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            /**
             * cityCode : 9247
             * cityName : 万州区
             */

            private String cityCode;
            private String cityName;

            public String getCityCode() {
                return cityCode;
            }

            public void setCityCode(String cityCode) {
                this.cityCode = cityCode;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }
        }
    }
}
