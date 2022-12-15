package cn.mofufin.morf.ui.entity;

public class Audit {

    /**
     * result_Msg : 查询成功
     * parameter : {"appVersion":"1.2.8","appState":1,"appUseMposSdk":0}
     * result_Code : 0
     */

    public String result_Msg;
    public ParameterBean parameter;
    public int result_Code;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public ParameterBean getParameter() {
        return parameter;
    }

    public void setParameter(ParameterBean parameter) {
        this.parameter = parameter;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public static class ParameterBean {
        /**
         * appVersion : 1.2.8
         * appState : 1
         * appUseMposSdk : 0
         */

        public String appVersion;
        public int appState;
        public int appUseMposSdk;

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public int getAppState() {
            return appState;
        }

        public void setAppState(int appState) {
            this.appState = appState;
        }

        public int getAppUseMposSdk() {
            return appUseMposSdk;
        }

        public void setAppUseMposSdk(int appUseMposSdk) {
            this.appUseMposSdk = appUseMposSdk;
        }
    }
}
