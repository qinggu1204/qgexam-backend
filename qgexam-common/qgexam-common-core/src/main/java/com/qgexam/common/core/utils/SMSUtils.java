package com.qgexam.common.core.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Random;

/**
 * 短信发送工具类
 */


public class SMSUtils {
    private static String accessKeyId = "LTAI5tEGdHSpVjh6MtXNage9";

    private static String secret = "005JWn31itvDQXqegmkf3mNDOEaWjy";


    /**
     * 发送短信
     *
     * @param phoneNumbers 手机号
     * @param param        参数
     */
    public static boolean sendMessage( String phoneNumbers, String param) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5tEGdHSpVjh6MtXNage9", "005JWn31itvDQXqegmkf3mNDOEaWjy");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", "阿里云短信测试");
        request.putQueryParameter("TemplateCode", "SMS_154950909");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + param + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            String json = response.getData();
            Gson g = new Gson();
            HashMap result = g.fromJson(json, HashMap.class);
            if("OK".equals(result.get("Message"))) {
                return true;
            }else{
                System.out.println("短信发送失败，原因："+result.get("Message"));
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;

    }
//    public static void main(String[] args){
//        Random r = new Random();
//        int num=r.nextInt(900000)+100000;//100000-999999
//        sendMessage("17816139690",num+"");
//    }

}