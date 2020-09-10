package com.xinyu.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xinyu.bean.wxmsg.resp.Article;
import com.xinyu.bean.wxmsg.resp.NewsMessage;
import com.xinyu.dao.UserDao;
import com.xinyu.service.IWxService;
import com.xinyu.util.WeiXinUtil;

@Service
public class WxService implements IWxService {

	@Autowired
	private UserDao suserDAO;
	
	@Override
	public String processRequest(HttpServletRequest request) {
		String respMessage = "";
		try
		{
			// 默认返回的文本消息内容
			String respContent = WeiXinUtil.getMainMenu();

			// xml请求解析
			Map<String, String> requestMap = WeiXinUtil.parseXml(request);

			// 发送方帐号（open_id）
			String fromUserName = requestMap.get("FromUserName");
			// 公众帐号
			String toUserName = requestMap.get("ToUserName");
			// 消息类型
			String msgType = requestMap.get("MsgType");
			
			// 回复文本消息
/*			TextMessage textMessage = new TextMessage();
			textMessage.setToUserName(fromUserName);
			textMessage.setFromUserName(toUserName);
			textMessage.setCreateTime(new Date().getTime());
			textMessage.setMsgType(WeiXinUtil.RESP_MESSAGE_TYPE_TEXT);
			textMessage.setFuncFlag(0);*/

			// 文本消息
			if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_TEXT))
			{
				String content = requestMap.get("Content");
			}
			// 图片消息
			else if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_IMAGE))
			{
				respContent = "";
			}
			// 地理位置消息
			else if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_LOCATION))
			{
				respContent = "";
			}
			// 链接消息
			else if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_LINK))
			{
				respContent = "";
			}
			// 音频消息
			else if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_VOICE))
			{
				respContent = "";
			}
			// 事件推送
			else if (msgType.equals(WeiXinUtil.REQ_MESSAGE_TYPE_EVENT))
			{
				// 事件类型
				String eventType = requestMap.get("Event");
				// 订阅
				if (eventType.equals(WeiXinUtil.EVENT_TYPE_SUBSCRIBE))
				{
					respContent = "";
				}
				// 取消订阅
				else if (eventType.equals(WeiXinUtil.EVENT_TYPE_UNSUBSCRIBE))
				{
					suserDAO.unbankOpenID(fromUserName);
				}
				// 自定义菜单点击事件
				else if (eventType.equals(WeiXinUtil.EVENT_TYPE_CLICK))
				{
					
					
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return respMessage;
	}
	

}
