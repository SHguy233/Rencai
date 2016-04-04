package com.example.godkiller.rencai.position;

import android.os.Message;
import android.util.Xml;
import android.os.Handler;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析行业类别，具体行业和岗位
 */
public class PullPosition {
    public static final int PARSESUCC = 0x5200;
    private Handler handler;
    public PullPosition(Handler handler) {
        this.handler = handler;
    }

    public void getTrades(final InputStream inputStream) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    XmlPullParser pullParser = Xml.newPullParser();
                    pullParser.setInput(inputStream, "UTF-8");
                    int event = pullParser.getEventType();
                    //行业类别
                    Map<String, Map<String, List<String>>> trades = new HashMap<String, Map<String, List<String>>>();
                    //具体行业
                    Map<String, List<String>> categorys = null;
                    //具体岗位
                    List<String> positions = null;
                    String tradeName = "";
                    String categoryName = "";
                    String positionName = "";
                    String currentName = "";
                    while(event != XmlPullParser.END_DOCUMENT) {
                        currentName = pullParser.getName();
                        switch (event) {
                            case XmlPullParser.START_TAG:
                                if ("trade".equals(currentName)) {
                                    tradeName = pullParser.getAttributeValue(0);
                                    categorys = new HashMap<String, List<String>>();
                                } else if ("category".equals(currentName)) {
                                    categoryName = pullParser.getAttributeValue(0);
                                    positions = new ArrayList<String>();
                                } else if ("position".equals(currentName)) {
                                    positionName = pullParser.getAttributeValue(0);
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (currentName.equals("position")) {
                                    positions.add(positionName);
                                } else if (currentName.equals("category")) {
                                    categorys.put(categoryName, positions);
                                } else if (currentName.equals("trade")) {
                                    trades.put(tradeName, categorys);
                                }
                                break;
                        }
                        event = pullParser.next();
                    }
                    Message message = new Message();
                    message.obj = trades;
                    message.what = PARSESUCC;
                    handler.sendMessage(message);

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
