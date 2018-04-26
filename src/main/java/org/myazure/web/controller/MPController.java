package org.myazure.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myazure.configuration.PrimaryConfiguration;
import org.myazure.vpn.controller.VPNTrafficeController;
import org.myazure.vpn.controller.VPNUserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.xmlmessage.XMLMessage;
import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

@Controller
public class MPController {
	private static final Logger LOG = LoggerFactory
			.getLogger(MPController.class);
	@Autowired
	VPNUserController vpnUserController;
	@Autowired
	VPNTrafficeController vpnTrafficeController;
	@Autowired
	PrimaryConfiguration primaryConfiguration;
	WXBizMsgCrypt wxBizMsgCrypt = null;
	private static ExpireKey expireKey = new DefaultExpireKey();

	public MPController() {

	}

	@RequestMapping(path = "/", method = RequestMethod.GET)
	public void getRoot(HttpServletRequest request, HttpServletResponse response)
			throws IOException, AesException {
		LOG.debug("A GET Request==================================");
		EventMessage eventMessage = getEventMessage(request, response);
		if (eventMessage == null) {
			return;
		}
		if (eventMessage.getMsgType() == null) {
			return;
		}
		LOG.debug("MMMMMMMMMMSG:" + JSON.toJSONString(eventMessage));
		return;
	}

	@RequestMapping(path = "/", method = RequestMethod.POST)
	public void postRoot(HttpServletRequest request,
			HttpServletResponse response) throws IOException, AesException {
		EventMessage eventMessage = getEventMessage(request, response);
		if (eventMessage == null) {
			return;
		} else if (eventMessage.getMsgType() == null) {
			return;
		}
		LOG.debug("A fressssh connnnnnnnnnnnnnnnnnnt");
		return;
	}

	@RequestMapping(path = "/event/authorize", method = RequestMethod.POST)
	public void acceptAuthorizeEvent(HttpServletRequest request,
			HttpServletResponse response) throws IOException, AesException {
	
		LOG.debug("event authorize!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		writeOutput(response, "success");
	}

	public static void writeOutput(HttpServletResponse response, String value) {
		try {
			PrintWriter pw = response.getWriter();
			pw.write(value);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void sent(HttpServletResponse response, XMLMessage msg,
			WXBizMsgCrypt wxBizMsgCrypt) throws IOException {
		msg.outputStreamWrite(response.getOutputStream(), wxBizMsgCrypt);
	}

	private EventMessage getEventMessage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		EventMessage eventMessage = null;
		ServletInputStream inputStream = request.getInputStream();
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String encrypt_type = request.getParameter("encrypt_type");
		String msg_signature = request.getParameter("msg_signature");
		LOG.debug("signature:" + signature);
		LOG.debug("timestamp:" + timestamp);
		LOG.debug("nonce:" + nonce);
		LOG.debug("signature:" + signature);
		LOG.debug("signature:" + signature);
		boolean isAes = "aes".equals(encrypt_type);
		if (isAes) {
			try {
				wxBizMsgCrypt = new WXBizMsgCrypt(
						primaryConfiguration.getEncodeToken(),
						primaryConfiguration.getEncodeKey(),
						primaryConfiguration.getCompAppId());
			} catch (AesException e) {
				e.printStackTrace();
			}
		}
		if (isAes && echostr != null) {
			try {
				echostr = URLDecoder.decode(echostr, "utf-8");
				String echostr_decrypt = wxBizMsgCrypt.verifyUrl(msg_signature,
						timestamp, nonce, echostr);
				writeOutput(response, echostr_decrypt);
				return null;
			} catch (AesException e) {
				e.printStackTrace();
			}
		} else if (echostr != null) {
			writeOutput(response, echostr);
			return null;
		}
		if (isAes) {
			try {
				String postData = StreamUtils.copyToString(inputStream,
						Charset.forName("utf-8"));
				String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature,
						timestamp, nonce, postData);
				eventMessage = XMLConverUtil.convertToObject(
						EventMessage.class, xmlData);
			} catch (AesException e) {
				e.printStackTrace();
			}
		} else {
			if (signature == null) {
				LOG.debug("signature:is null");
			}
			LOG.debug("signature:" + signature);
			LOG.debug("timestamp:" + timestamp);
			LOG.debug("nonce:" + nonce);
			LOG.debug("signature:" + signature);
			LOG.debug("signature:" + signature);
			if (!signature.equals(SignatureUtil.generateEventMessageSignature(
					primaryConfiguration.getEncodeToken(), timestamp, nonce))) {
				LOG.debug("The request signature is invalid");
				response.getWriter().append("unsuccess");
				response.getWriter().flush();
				return null;
			}
			if (inputStream != null) {
				eventMessage = XMLConverUtil.convertToObject(
						EventMessage.class, inputStream);
			}
			response.getWriter().append("success");
			response.getWriter().flush();
			response.getWriter().close();
		}
		String key = eventMessage.getFromUserName() + "__"
				+ eventMessage.getToUserName() + "__" + eventMessage.getMsgId()
				+ "__" + eventMessage.getCreateTime();
		if (expireKey.exists(key)) {
			return null;
		} else {
			expireKey.add(key);
		}
		LOG.debug(JSON.toJSONString(eventMessage));
		return eventMessage;
	}
}
