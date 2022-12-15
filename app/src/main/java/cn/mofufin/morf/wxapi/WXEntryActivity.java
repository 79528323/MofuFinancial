package cn.mofufin.morf.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Toast;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cc.ruis.lib.event.RxBus;
import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.ui.util.RxEvent;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Common.WECHAT_APP_ID, false);
        api.registerApp(Common.WECHAT_APP_ID);

        try {
            api.handleIntent(getIntent(), this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);//必须调用此句话
    }

    @Override
    public void onReq(BaseReq baseReq) {
        Logger.w("xxx", "baseReq.getType(); = " + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp baseResp) {
        int type = baseResp.getType();
        if (type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            // 分享

            String result = "";
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "分享成功";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "取消分享";
                    break;
                case BaseResp.ErrCode.ERR_SENT_FAILED:
                    result = "分享失败";
                    break;
                default:
                    result = "未知原因";
                    break;
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            finish();

        } else if (type == ConstantsAPI.COMMAND_SENDAUTH) {
            // 登录

            String result = "";
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    // 授权成功

                    result = "授权成功";
                    SendAuth.Resp sendAuthResp = (SendAuth.Resp) baseResp;
                    String code = sendAuthResp.code;
                    getResult(code);
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    // 授权取消
                    result = "取消授权";
                    RxBus.getInstance().post(RxEvent.WECHAT_REGISTER_SUCCESS, null);
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    // 授权被拒绝
                    result = "授权被拒绝";
                    RxBus.getInstance().post(RxEvent.WECHAT_REGISTER_SUCCESS, null);
                    break;
                default:
                    // 未知错误
                    result = "未知原因";
                    RxBus.getInstance().post(RxEvent.WECHAT_REGISTER_SUCCESS, null);
                    break;
            }
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getResult(final String code) {
        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
                + Common.WECHAT_APP_ID
                + "&secret="
                + Common.WECHAT_SECRET_ID
                + "&code="
                + code
                + "&grant_type=authorization_code";
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.w("xxx", "onFailure = " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**
                 * {
                 * "access_token":"8GjJk0nfAVP9mSlnaC1bCTF03kymQZkFX4YaxnIq9h1dq5DUUNUlPRGJqOPOft6uW_iJOyWQ6gd4STI6HLoSEawx7CYPtavzllA7cMoVskY",
                 * "expires_in":7200,
                 * "refresh_token":"BsmzGtTniSq0QClndrmsx8quE1W2AEge6kl4kiAQ0MTcl_Fb8iVhYLsgl-Y7Dp38IOpYpCPys6FCL2jqqyBeE0MtSNiPVM2ARncpNFMD298",
                 * "openid":"oPc-zwgsqs-z09x5czY80PEAsVV4",
                 * "scope":"snsapi_userinfo",
                 * "unionid":"or_ZDwKWbrFaf3xo5dqudQ8BAoKA"
                 * }
                 */
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String openid = jsonObject.optString("openid").trim();
                    String access_token = jsonObject
                            .optString("access_token").trim();
                    getUID(openid, access_token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取用户唯一标识
     *
     * @param openId
     * @param accessToken
     */
    private void getUID(final String openId, final String accessToken) {

        String path = "https://api.weixin.qq.com/sns/userinfo?access_token="
                + accessToken + "&openid=" + openId;

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(path)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.w("xxx", "onFailure = " + e.getMessage());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        error();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                /**
                 * {"openid":"oPc-zwgsqs-z09x5czY80PEAsVV4",
                 * "nickname":"-",
                 * "sex":1,
                 * "language":"zh_CN",
                 * "city":"Guangzhou",
                 * "province":"Guangdong",
                 * "country":"CN",
                 * "headimgurl":"http:\/\/wx.qlogo.cn\/mmopen\/ibRLxrcn3GezQDHxMOqZfc5sEpQpU7iawERnE0G1tz7YktZKDThoPhljYU2nYdicnasjtEDMAWdmHzwFdkyicTT6T2e1PuMazwYQ\/0",
                 * "privilege":[],
                 * "unionid":"or_ZDwKWbrFaf3xo5dqudQ8BAoKA"}
                 */
//                WechatAuthResult authResult = new Gson().fromJson(response.body().string(), WechatAuthResult.class);
//                RxBus.getInstance().post(RxEvent.WECHAT_REGISTER_SUCCESS, authResult);
                finish();
            }
        });
    }

    private void error() {
        Toast.makeText(this, "拉取信息失败", Toast.LENGTH_SHORT).show();
        finish();
    }
}
