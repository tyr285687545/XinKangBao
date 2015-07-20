package com.example.myapp.utils;

import java.io.InputStream;
import java.util.ArrayList;

import org.example.myapp.client.model.UpDataInfo;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class PaserService {
    
    public static ArrayList<UpDataInfo> getUpdataInfos(InputStream inputStream) throws Exception{
        
        ArrayList<UpDataInfo> UpdataInfos = null;
        UpDataInfo UpdataInfo = null;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser paser = factory.newPullParser();
        
        paser.setInput(inputStream, "UTF-8");
        int eventType = paser.getEventType();
        
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT:
            	
                UpdataInfos = new ArrayList<UpDataInfo>();
                break;
            case XmlPullParser.START_TAG:
            	
                if ("android".equals(paser.getName())) {
                    UpdataInfo = new UpDataInfo();
//                    UpdataInfo.setUpdataInfoid(paser.getAttributeValue(0));   //��ýڵ����Եĵ�һ��ֵ���ڶ���ֵ��Ϊ�����1
                } else if ("versionCode".equals(paser.getName())){
                    UpdataInfo.setVersionCode(Integer.valueOf(paser.nextText()));    //��ýڵ����������
                } else if ("phone".equals(paser.getName())){
                    UpdataInfo.setVersionName(paser.nextText());
                }
                break;
            case XmlPullParser.END_TAG:
                if ("android".equals(paser.getName())) {
                    UpdataInfos.add(UpdataInfo);
                    UpdataInfo = null;
                }
                break;
            default:
                break;
            }
            eventType = paser.next();    //���ڵ��ѭ����������
        }        
        return UpdataInfos;
    }
}