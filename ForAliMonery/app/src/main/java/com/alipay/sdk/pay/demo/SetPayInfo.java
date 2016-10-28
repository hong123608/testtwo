package com.alipay.sdk.pay.demo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import android.os.Message;

import com.alipay.sdk.app.PayTask;

public class SetPayInfo {
	public static String PARTNER = "";
	public static String SELLER = "";
	public static final String RSA_PRIVATE = "MIICXAIBAAKBgQCvWwV7WQxRpgSOi/OGuO+a66/"+
	"YUwvcrfCM9dsKixkIhlYd6cnU9+HgVESKuRzOuNfkmOYB72QNYEO77PG5eV53hZfYPUajhNAwT5xOFpx8"+
			"QY8MHqSkP9jyw4hs2HBGRoRQ1ygYPSZD6jp020F7NvCJltOZx2QZUDVjXYVxiM0zxwIDAQABAo"+
	"GAacDXGYCmsUcSUHWOtJ0rw56IOko7SSeNfzV2mHEy87UwknHASzQDgSrQ+2iQOwu3CCI99MTO7lI0B03q"+
			"qhTMJwDN8LhsZOWQNKh8aTZPfzvgrWPhEU/m05+RCqUNo6aL1GaezsArx9E4gmtUFmXbpUa5T6/"+
	"JjTmymboWi8v4VoECQQDiESnkcZZocx0djCF0RJBbXuuz04TDSdSNNu3k3tfnN9P6ragCsvupjWBicqVE39N"+
			"V0uhVaB2mZvN2iT2mmR7xAkEAxpLtAP7vt9UKvrZkHLQrxGd1ZJU9vb5TdtNixomg0IV+Kqy3sKd"+
	"OYuy9MBfO6WyxKRk2c46DjqfWZB7bFFxuNwJAbjo2NfgYgXdXg/e2vC1OQe8HeFgzTNFr/SsCVe0+UrRK/Ni"+
			"9qgtBYEvZ6kbCRNHqBtfehv5MLr3WCj8iu1+/0QJAB7bZgjplMa0TF8maJgtRz/V8+AVcOqzULT+"+
	"VDMV3++HTvvCqyjAVuX4c82tZXHQehcw281JMBhS7HXSdajMVDQJBAIVr1rKmMltSOWML5Z/bL12p3S2mMvK"+
			"712717btz508bN0T+9bo1uNAXnw5ldgYyBclx8K6JBpMyBo9/KQnFHyY=";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9"+
			"qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAopr"+
			"ih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9z"+
			"pgmLCUYuLkxpLQIDAQAB";
	/**
	 * 直接调用这个函数
	 * @param partnerId 合作者商户ID
	 * @param userAccount 商家账号
	 * @param goodsName 商品名称
	 * @param goodsMessage 商品信息
	 * @param goodsMoney 价格
	 */
	public static String payMoney(String partnerId,String userAccount,String goodsName,String goodsMessage,String goodsMoney){
		PARTNER = partnerId;
		SELLER = userAccount;
		String orderInfo = getOrderInfo(goodsName, goodsMessage, goodsMoney);
		String sign = SignUtils.sign(orderInfo, RSA_PRIVATE);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return orderInfo + "&sign=\"" + sign + "\"&"
				+ "sign_type=\"RSA\"";	
	}
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public static String getOrderInfo(String subject, String body, String price) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://www.lvdidache.com/client/notifyurlorder"
				+ "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}
	/**
	 * get the out_trade_no for an order. 获取外部订单号
	 * 
	 */
	public static String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}
}
