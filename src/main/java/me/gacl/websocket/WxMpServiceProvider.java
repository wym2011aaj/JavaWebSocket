package me.gacl.websocket;

import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

/**
 * @author Neo
 * @date 2018/3/28 13:55
 */
public class WxMpServiceProvider
{
    /**
     * 微信服务类
     */
    private static WxMpServiceImpl wxMpService;

    private WxMpServiceProvider()
    {
    }

    public static WxMpServiceImpl getWxMpService() throws Exception
    {
        if (wxMpService == null)
        {
            wxMpService = new WxMpServiceImpl();
            WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
            wxMpInMemoryConfigStorage.setSecret("510b12d7964cb178822d5c3d0d667247");
            wxMpInMemoryConfigStorage.setAppId("wxd62447c0c1778530");
            wxMpInMemoryConfigStorage.setToken("yiuman");
            wxMpService.setWxMpConfigStorage(wxMpInMemoryConfigStorage);
        }
        return wxMpService;
    }

    public static WxMpUserService getUserService() throws Exception
    {
        return getWxMpService().getUserService();
    }
}
