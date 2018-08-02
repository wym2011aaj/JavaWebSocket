package me.gacl.websocket;

import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.w3c.dom.Document;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * 微信事件推送响应
 * 响应微信推送过来的用户关注/取消关注事件，菜单点击事件，用户发送消息（文本消息、图片消息、语音消息、视频消息、小视频消息、地理位置消息、链接消息）
 *
 * @author Neo
 * @date 2018/4/23 15:16
 */
public class MessageReceiveServlet extends HttpServlet
{
    public MessageReceiveServlet()
    {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //接口url配置时微信会通过get的方式进行验证，直接返回微信访问参数中的随机字符串即可
        String echoStr = request.getParameter("echostr");
        PrintWriter out = response.getWriter();
        out.print(echoStr);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        process(request, response);
    }

    protected void process(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String result = "success";
        try
        {
            InputStream ins = request.getInputStream();
            Document document = XmlUtils.createDocument(ins);
            Map<String, String> map = XmlUtils.xmlToMap(document);
            String msgType = map.get("MsgType");
            String type = ("event").equals(msgType) ? map.get("Event") : msgType;
            String openId = map.get("FromUserName");
            if ("text".equalsIgnoreCase(type))
            {
                WxMpUser wxMpUser = WxMpServiceProvider.getUserService().userInfo(openId);
                String content = map.get("Content");
                //群发消息
                for (WebSocketTest item : WebSocketTest.webSocketSet)
                {
                    try
                    {
                        String i = "{\"img\": \"" + wxMpUser.getHeadImgUrl() + "\", \"info\": \"" + content + "\"}";
                        item.sendMessage(i);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            out.println(result);
            out.close();
        }
    }
}
