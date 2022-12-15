package cn.mofufin.morf.ui.util.thirdPlatform;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cc.ruis.lib.util.Logger;
import cn.mofufin.morf.ui.entity.WxPays;
import cn.mofufin.morf.ui.util.Common;
import cn.mofufin.morf.R;

public class WechatPlatform {
    private IWXAPI api;


    private void register(Context context, String appId) {
        api = WXAPIFactory.createWXAPI(context, appId);
        api.registerApp(appId);
    }

    private boolean isWXAppInstalled(Context context, String appId){
        api = WXAPIFactory.createWXAPI(context, appId);
        return api.isWXAppInstalled();
    }

    public static boolean isWXAppInstalled(Context context){
        WechatPlatform wechatPlatform = new WechatPlatform();
        return wechatPlatform.isWXAppInstalled(context, Common.WECHAT_APP_ID);
    }


    /**
     * 微信支付
     * @param context
     * @param paramsBean
     */
    public static void PayForWeChat(Context context,WxPays.ParamsBean paramsBean){
        if (WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.pay(context,paramsBean);
        }else {
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 链接分享到朋友圈
     * @param context
     * @param url
     * @param title
     * @param description
     * @param thumbBitmap
     */
    public static void shareMomentsURL(Context context, String url, String title, String description, Bitmap thumbBitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareMomentsURL(context, Common.WECHAT_APP_ID, url, title, description, thumbBitmap);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }

    }


    /**
     * 链接分享到会话
     * @param context
     * @param url
     * @param title
     * @param description
     * @param thumbBitmap
     */
    public static void shareSessionURL(Context context, String url, String title, String description, Bitmap thumbBitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareSessionURL(context, Common.WECHAT_APP_ID, url, title, description, thumbBitmap);
        }else{
            Toast.makeText(context,context.getString(R.string.wechat_not_installed),Toast.LENGTH_LONG).show();
        }

    }

    /**
     * 图片分享到微信会话
     * @param context
     * @param description
     * @param bitmap
     */
    public static void shareImageSession(Context context,String description,Bitmap bitmap){
        if(WechatPlatform.isWXAppInstalled(context)){
            WechatPlatform wechatPlatform=new WechatPlatform();
            wechatPlatform.shareImageSession(context,Common.WECHAT_APP_ID,description,bitmap);
        }else{
            Toast.makeText(context,R.string.wechat_not_installed,Toast.LENGTH_LONG).show();
        }

    }

    private  void shareImageSession(Context context,String appid,String description,Bitmap bitmap){
        shardImage(context,appid,description,bitmap, SendMessageToWX.Req.WXSceneSession);
    }

    private void shareMomentsURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap){
        shareURL(context, appId, url, title, description, thumbBitmap, SendMessageToWX.Req.WXSceneTimeline);
    }

    private void shareSessionURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap){
        shareURL(context, appId, url, title, description, thumbBitmap, SendMessageToWX.Req.WXSceneSession);
    }

    private void shareURL(Context context, String appId, String url, String title, String description, Bitmap thumbBitmap, int scene){
//        Logger.i(TAG, "shareURL url=" + url + ",title=" + title);
        register(context, appId);

        WXWebpageObject wxWebpageObject=new WXWebpageObject();
        wxWebpageObject.webpageUrl=url;
        //wxWebpageObject.extInfo="ext";

        WXMediaMessage msg=new WXMediaMessage(wxWebpageObject);
        if(TextUtils.isEmpty(title)) {
            msg.title = " ";
        }else{
            msg.title = title;
        }
        msg.description=description;
        msg.setThumbImage(thumbBitmap);

        SendMessageToWX.Req req=new SendMessageToWX.Req();
        req.transaction=String.valueOf(System.currentTimeMillis());
        Log.i("WXD","String.valueOf==>"+System.currentTimeMillis());
        req.message=msg;
        req.scene = scene;

        api.sendReq(req);
    }

    private void shardImage(Context context,String appId,String description,Bitmap bitmap,int scene){
        register(context, appId);

        WXImageObject wxImageObject = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = wxImageObject;
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap,200,200,true);
        bitmap.recycle();

//        msg.thumbData =
        msg.description=description;
        msg.setThumbImage(thumbBmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = scene;
        api.sendReq(req);
    }


    private void pay(Context context,WxPays.ParamsBean params){
        register(context, Common.WECHAT_APP_ID);

        PayReq payReq = new PayReq();
        payReq.appId = params.appid;
        payReq.nonceStr = params.noncestr;
        payReq.partnerId = params.partnerid;
        payReq.prepayId = params.prepayid;
        String value = params.packageX;
        Logger.e("value "+value);
        payReq.packageValue = value;
        payReq.timeStamp = params.timestamp;
        payReq.sign = params.sign;

        api.sendReq(payReq);
    }
}
