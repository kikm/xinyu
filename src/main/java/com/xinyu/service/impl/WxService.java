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
		String respMessage = "休息一下";
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
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = requestMap.get("EventKey");
					
					NewsMessage newsMessage = new NewsMessage();
					newsMessage.setToUserName(fromUserName);
					newsMessage.setFromUserName(toUserName);
					newsMessage.setCreateTime(new Date().getTime());
					newsMessage.setMsgType(WeiXinUtil.RESP_MESSAGE_TYPE_NEWS);
					newsMessage.setFuncFlag(0);
					
					List<Article> articleList = new ArrayList<Article>();
					if (eventKey.equals("11"))
					{
						Article article = new Article();
						article.setTitle("吃在闲吧,乐享生活");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/1.jpeg");
						article.setUrl("http://lijmhj.duapp.com/Introduction.jsp");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("12"))
					{
						Article article = new Article();
						article.setTitle("美食在这里等着你");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/location.png");
						article.setUrl("http://lijmhj.duapp.com/Location.jsp");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("21"))
					{
						Article article = new Article();
						article.setTitle("小吃的世界");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/1-1.jpg");
						article.setUrl("http://lijmhj.duapp.com/MenuServlet?type=21");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("22"))
					{
						Article article = new Article();
						article.setTitle("极速快餐，自备安全带");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/2-1.jpg");
						article.setUrl("http://lijmhj.duapp.com/MenuServlet?type=22");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("23"))
					{
						Article article = new Article();
						article.setTitle("夜生活的开始");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/3-1.jpg");
						article.setUrl("http://lijmhj.duapp.com/MenuServlet?type=23");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("24"))
					{
						Article article = new Article();
						article.setTitle("好喝好喝好喝！");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/4-1.jpg");
						article.setUrl("http://lijmhj.duapp.com/MenuServlet?type=24");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("25"))
					{
						Article article = new Article();
						article.setTitle("慢慢品味的炒菜");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/5-1.jpg");
						article.setUrl("http://lijmhj.duapp.com/MenuServlet?type=25");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					else if (eventKey.equals("32"))
					{
						Article article = new Article();
						article.setTitle("优惠送送送！");
						String description = "一心打造的休闲小吃吧，带给你不只是休闲的美食，还有恬静的午后时光";
						article.setDescription(description);
						article.setPicUrl("http://lijmhj.duapp.com/images/3-2.jpg");
						article.setUrl("http://lijmhj.duapp.com/Preferential.jsp");
						articleList.add(article);
						// 设置图文消息个数
						newsMessage.setArticleCount(articleList.size());
						// 设置图文消息包含的图文集合
						newsMessage.setArticles(articleList);
						// 将图文消息对象转换成xml字符串
						respMessage = WeiXinUtil.newsMessageToXml(newsMessage);
						return respMessage;
					}
					
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return respMessage;
	}
	

}
