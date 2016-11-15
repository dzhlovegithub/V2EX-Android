package com.sola.v2ex_android.util;

import com.sola.v2ex_android.model.LoginResult;
import com.sola.v2ex_android.model.MyFollowing;
import com.sola.v2ex_android.model.MyNode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wei on 2016/11/11.
 */

public class JsoupUtil {


    public static LoginResult parseLoginResult(String response) {
        LogUtil.d("JsoupUtil","response" + response);
        LoginResult loginResult = null;
        Element body = Jsoup.parse(response).body();
        Element td = body.select("div#Rightbar").select("div.box").get(0).getElementsByTag("td").get(0);
        String userId = td.getElementsByTag("a").get(0).attr("href").replace("/member/", "");
        String src = "http:" + td.getElementsByTag("img").get(0).attr("src");
         LogUtil.d("JsoupUtil","userid = " + userId + " src = " + src);
        loginResult = new LoginResult(userId, src);
        return loginResult;
    }

    public static HashMap parseUserNameAndPwd(String stringResponse, String username, String password) {
        Element body = Jsoup.parse(stringResponse);
        Elements boxes = body.getElementsByClass("box");
        HashMap params = new HashMap();
        for (Element el : boxes) {
            Elements cell = el.getElementsByClass("cell");
            for (Element c : cell) {
                String nameVal = c.getElementsByAttributeValue("type", "text").attr("name");
                String passwordVal = c.getElementsByAttributeValue("type", "password").attr("name");
                String once = c.getElementsByAttributeValue("name", "once").attr("value");
                if (nameVal.isEmpty() || passwordVal.isEmpty()) continue;
                params.put(nameVal, username);
                params.put("once", once);
                params.put(passwordVal, password);
                break;
            }
        }
        params.put("next", "/");
        return params;
    }

    /**
     * 解析我关注的节点
     * @param response
     * @return
     */
    public static List<MyNode> parseMyNodeInfo(String response){
        List<MyNode> myNodeList = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Element body = doc.body();
        Elements elements = body.getElementsByAttributeValue("id", "MyNodes");
        for (Element el : elements) {
            Elements aNodes = el.getElementsByTag("a");

            for (Element tdNode : aNodes) {
                MyNode mynode = new MyNode();
                String hrefStr = tdNode.attr("href");
                Elements avatarNode = tdNode.getElementsByTag("img");
                if (avatarNode != null) {
                    String avatarString = avatarNode.attr("src");
                    if (avatarString.startsWith("//")) {
                        mynode.imgSrc = "http:" + avatarString;
                    }
                }
                Elements attentionNode = tdNode.getElementsByClass("fade f12");
                mynode.followCount = attentionNode.text();
                mynode.nodeName = hrefStr.substring(4);
                myNodeList.add(mynode);
            }
        }
        return myNodeList;
    }

    public static List<MyFollowing> parseMyfollowing(String response){
        List<MyFollowing> myfollowingList = new ArrayList<>();
        Document doc = Jsoup.parse(response);
        Element body = doc.body();
        Element mainElement = body.getElementById("Main");
        Elements itemElements =  mainElement.getElementsByClass("cell item");
        for (Element itemElement : itemElements) {
            MyFollowing myfollow = new MyFollowing();
            Elements titleElements = itemElement.getElementsByClass("item_title");
            Elements nodeElements = itemElement.getElementsByClass("node");
            Elements hrefElements = itemElement.getElementsByAttribute("href");
            myfollow.title = titleElements.text();
            myfollow.nodeName =  nodeElements.text();
            myfollow.userName = hrefElements.first().attr("href").substring(8);
            myfollow.commentCount = itemElement.getElementsByClass("count_livid").text();
            Elements avatarNode = itemElement.getElementsByTag("img");
            if (avatarNode != null) {
                String avatarString = avatarNode.attr("src");
                if (avatarString.startsWith("//")) {
                    myfollow.userIconUrl = "http:" + avatarString;
                }
            }
            myfollowingList.add(myfollow);
        }
        return myfollowingList;

    }
}