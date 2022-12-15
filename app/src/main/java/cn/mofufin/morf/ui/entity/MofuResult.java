package cn.mofufin.morf.ui.entity;

public class MofuResult {
    /**
     * result_Msg : 查询成功
     * vCreateDate : 2018-09-07 09:56:53
     * vName : app-debug.apk
     * vOfficeCode : S20180816230703671
     * result_Code : 0
     * vCode : 1.0
     * vPath : http://120.78.213.181/upload/officeapk/
     */

    public String result_Msg;
    public String vCreateDate;
    public String vName;
    public String vOfficeCode;
    public int result_Code;
    public String vCode;
    public String vPath;
    public String payDateBegin;
    public String payDateEnd;

    public String phone;

    public String getResult_Msg() {
        return result_Msg;
    }

    public void setResult_Msg(String result_Msg) {
        this.result_Msg = result_Msg;
    }

    public String getVCreateDate() {
        return vCreateDate;
    }

    public void setVCreateDate(String vCreateDate) {
        this.vCreateDate = vCreateDate;
    }

    public String getVName() {
        return vName;
    }

    public void setVName(String vName) {
        this.vName = vName;
    }

    public String getVOfficeCode() {
        return vOfficeCode;
    }

    public void setVOfficeCode(String vOfficeCode) {
        this.vOfficeCode = vOfficeCode;
    }

    public int getResult_Code() {
        return result_Code;
    }

    public void setResult_Code(int result_Code) {
        this.result_Code = result_Code;
    }

    public String getVCode() {
        return vCode;
    }

    public void setVCode(String vCode) {
        this.vCode = vCode;
    }

    public String getVPath() {
        return vPath;
    }

    public void setVPath(String vPath) {
        this.vPath = vPath;
    }
//    public String result_Msg;
//    public int result_Code;

//    /**
//     *   Apk版本号
//     */
//    public String vCode;
//    /**
//     *   创建时间
//     */
//    public String vCreateDate;
//    /**
//     *   文件路径
//     */
//    public String vPath;
//    /**
//     *   文件名
//     */
//    public String vName;
//    public String vOfficeCode;



}
