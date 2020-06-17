package com.water.melon.ui.game;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.water.melon.application.MyApplication;
import com.water.melon.net.NetConstant;
import com.water.melon.ui.game.utils.BaseBeanCallback;
import com.water.melon.ui.game.utils.GamePlayCallBack;
import com.water.melon.utils.LogUtil;
import com.water.melon.utils.RSAUtils;
import com.water.melon.utils.SharedPreferencesUtil;
import com.water.melon.utils.XGUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import okhttp3.MediaType;

public class HttpHelper {
    public static void loginPlayGame(GamePlayCallBack advBeanCallback) {

        String phone = "vp" + SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_USER_NAME, SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_USER_ID, "12345") + "678");
        String device = MyApplication.getSystemInfo().imei;
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature = "";


        String url = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_GAME_URL, NetConstant.XG_GAME_URL) + NetConstant.XG_API_TAL;
//        String url = "https://mobiletal.cgdemotest.com" + "/api/tal/authV2";
        JSONObject obj = new JSONObject();
        try {
            String sign = "userName=" + phone + "&loginDevice=" + device + "&timeStamp=" + timestamp + "&key=5eac9ecd9b8e48aba294ef272c739e58";
//            Log.e("loginGamen11", "sign = " + sign);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            // 加密
            byte[] digest = md5.digest(sign.getBytes("UTF-8"));
            // 十六进制的字符
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5',
                    '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
            StringBuffer sb = new StringBuffer();
            // 处理成十六进制的字符串(通常)
            for (byte bb : digest) {
                sb.append(chars[(bb >> 4) & 15]);
                sb.append(chars[bb & 15]);
            }

            signature = sb.toString();
//            Log.e("loginGamen11", "signature = " + signature);
            obj.put("userName", getkey(phone));
            obj.put("loginDevice", device);

            obj.put("timestamp", timestamp);
            obj.put("signature", signature);

            obj.put("invitationCode", XGUtil.getAppMetaData("CHANNEL_ID"));  //游戏渠道号

//            obj.put("userName", phone);
//            obj.put("passWord", password);
//            obj.put("loginDevice", device);

        } catch (Exception e) {
            e.printStackTrace();
        }

//        Log.e("loginGamen11", "obj = " + obj.toString());
        LogUtil.e("game", "loginPlayGame json = " + obj.toString());
//        url += ("?userName=" + getkey(AppConfig.getUserNameID(context)) + "&passWord=" + getkey(genRandomNum()) + "&loginDevice=" + BaseApplication.getInstance().getSystemInfo().imei);
        LogUtil.e("game", "loginPlayGame URL = " + url);
        OkHttpUtils
                .postString()
                .url(url)
                .mediaType(MediaType.parse("application/json"))
                .content(String.valueOf(obj))
//                .content(json)
                .addHeader("Content-type", "application/json")
                .build()
                .execute(advBeanCallback);
    }

    private static String getkey(String content) {
        String encryptedString = "";
        try {
            encryptedString = Base64.encodeToString(RSAUtils.encrypt(content, RSAUtils.publicKey), Base64.DEFAULT);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encryptedString;
    }

    //登录初始化游戏
    public static void checkGameToken( BaseBeanCallback advBeanCallback) {

        String url = SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_GAME_URL, NetConstant.XG_GAME_URL) + "/api/vip/getbalance";

        LogUtil.e("game", "CheckPlayGame URL = " + url);
        OkHttpUtils
                .get()
                .url(url)
//                .mediaType(MediaType.parse("application/json"))
//                .content("")
//                .content(json)
                .addHeader("Content-type", "application/json")
                .addHeader("Authorization", "Bearer " +  SharedPreferencesUtil.getInstance().getString(SharedPreferencesUtil.XG_GAME_TOKEN, ""))
                .build()
                .execute(advBeanCallback);
    }


    public static String sendJsonPost(String apiUrl, String Json) {
        // HttpClient 6.0被抛弃了
        LogUtil.e("loginPlayGame", "apiUrl = " + apiUrl + ", json = " + Json);
        String result = "";
        BufferedReader reader = null;
        try {
//            String urlPath = apiUrl + method;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                LogUtil.e("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
            }
            LogUtil.e("loginPlayGame", "conn.getResponseCode() = " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        LogUtil.e("loginPlayGame", "result = " + result);
        return result;
    }
}
