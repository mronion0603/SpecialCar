package com.lc.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.json.JSONObject;







import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lc.net.AddOrderNet;
import com.lc.specialcar.R;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

public class PayActivity extends FragmentActivity {
	private TextView title;
	private ImageView backbt;
	private TextView contentTV,priceTV,nameTV;
	String price="0.01";
	String name="测试1";
	String content="测试内容";
    /*pid*/
	public static final String PARTNER = "2088021098229292";
	/*卖家账号*/
	public static final String SELLER = "3247757393@qq.com";
	/*RSA私钥*/
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJwKFPs3Yq9ohMNl"
			+"J36ma8ATGSlA6Mi4yTf8ACaq/BxYrz9rTQTJoaXrURq3cj/NwCCAIslnUG91qmhg"
			+"P5B+6DbEwV3dZhJQuAdTW7ztdz7YbjMzkfcqJOcbS7iH7nbVRcqnn48wzYaByr0/"
			+"L13mCFZJwFiOW35fyleRv/QXqLElAgMBAAECgYBEiQUYJgZ5Rq3xaI+FSDFRZtIJ"
			+"VZIo/0iaB1xFn5omFSm78DQe4zTVvFqeIm7S6Vr2q3EcBv3ERuk2+PdSTvtMy2JH"
			+"VKRAHe17oX4rg5yqAzonFPf1vcZAZgQrVC5PDx1JQF4zcNVSkU6b753SbA1bxVvm"
			+"5PEY1CqIzglkpMhurQJBAMha4p3XHOz62Vad91hcNoduLfyuqETZp7Bs34NTM3nQ"
			+"6gFU7t1q2ADjNJn/QpUpWl4wv1JWd4ERyri7TCtaOrcCQQDHYGINu1SaIF02SwTF"
			+"2XI0IVd16nqcFSENdzekVlzlS05T2nsNfAiUlY9d5AW0rRNAGMUd0TumlO6QjVEO"
			+"+QcDAkEAwGHMskUJCGCCatJ53AWHcpmQXM4U0y+v/g88u+wbRIAfAjJcvo1GZevE"
			+"6Y4akAHKPSFPxK80RuIfpyxpokMclwJAJeAXgIhP4B+51XCW7YcLfP/Af7QVqsUQ"
			+"zGATf/KwHxcZQDDHI56meqOW4r1IoDQo7gpGmA2BiXnNgER4W6HPRQJAJWJMqnl5"
			+"+nIKLqEv2o7PZc2WS7RrtoD4+4xcG1ew9/H7QYxU9jT4bGKkhK1c1ZfLQa/TFGQI"
			+"G2YESPzXU/BmPA==";
	/*支付宝公钥*/
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcChT7N2KvaITDZSd+pmvAExkpQOjIuMk3/AAmqvwcWK8/a00EyaGl61Eat3I/zcAggCLJZ1BvdapoYD+Qfug2xMFd3WYSULgHU1u87Xc+2G4zM5H3KiTnG0u4h+521UXKp5+PMM2Ggcq9Py9d5ghWScBYjlt+X8pXkb/0F6ixJQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;

	private static final int SDK_CHECK_FLAG = 2;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					//Intent intent = new Intent();
					//intent.setClass(PayActivity.this, PayOrdActivity.class);
					//startActivity(intent);
					Toast.makeText(PayActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					//finish();
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000” 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(PayActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			case SDK_CHECK_FLAG: {
				Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 10: {
				try {
					parseJSON(((String)msg.obj));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			default:
				break;
			}
		};
	};
	 private void parseJSON(String str)throws Exception{  
		 System.out.println(str);
		 
	     JSONObject jsonobj = new JSONObject(str);  
         int result = jsonobj.getInt("ResultCode");  
         if(result==Global.SUCCESS){  
        	 JSONObject jsonobj2 =  (JSONObject) jsonobj.getJSONObject("Data");
        	 getOrderid  = jsonobj2.getString("orderNum");
        	 System.out.println(getOrderid);
        	 //Toast.makeText(PayActivity.this, result, Toast.LENGTH_LONG).show();
        	 pro2.setVisibility(View.GONE); 
         }else{
        	 Toast.makeText(PayActivity.this, jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
         }
         
	 }
	private AddOrderNet addOrderNet=new AddOrderNet();
	private String getAuthn="";
	private String getUserid="";
	private String getServiceid="";
	private String getOrderid="";
	private ProgressBar pro2;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);  
		 setContentView(R.layout.pay_main);
		 ExitApplication.getInstance().addActivity(this);
		 init();
		 
	}
	public void init(){
		pro2 = (ProgressBar)findViewById(R.id.progress2); 
		pro2.setProgress(0);  
		pro2.setIndeterminate(true);
		getAuthn();
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	price = extras.getString("itemprice");
            name = extras.getString("itemname");
            content = extras.getString("itemcontent");
            getServiceid = extras.getString("serviceid");
        	if(extras.getString("type").equals("new")){
	             addOrderNet.setHandler(mHandler);
	             addOrderNet.setDevice(Global.DEVICE);
		   		 addOrderNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		   		 addOrderNet.setAddMoney(price);
		   		 addOrderNet.addOrderServer();
        	}else{
        		 getOrderid = extras.getString("orderid");
        		 pro2.setVisibility(View.GONE); 
        	}
        }
       
		title = (TextView)findViewById(R.id.topTv);
		title.setText("支付宝充值");
		backbt = (ImageView)findViewById(R.id.ArrowHead);
		backbt.setVisibility(View.VISIBLE);
		backbt.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();  
                setResult(RESULT_OK, intent);  
				finish();	
			}
		});
		contentTV = (TextView)findViewById(R.id.product_content);
		priceTV = (TextView)findViewById(R.id.product_price);
		nameTV = (TextView)findViewById(R.id.product_subject);
		nameTV.setText(name);
		priceTV.setText(price+"元");
		contentTV.setText(content);
	}
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(View v) {
		String orderInfo = getOrderInfo(name, content, price, getOrderid);
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayActivity.this);
				// 调用支付接口
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(PayActivity.this);
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};

		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();

	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price,String orderid) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderid + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://120.24.172.238:8080/specialCar/alipay"
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
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	 public void getAuthn(){
			SharedPreferences sharedata;
			if(getSharedPreferences("userinfo", 0)!=null){
			   sharedata = getSharedPreferences("userinfo", 0); 
			   if(sharedata.getString("authn",null)!=null)
				   getAuthn = sharedata.getString("authn", null);
			       getUserid= sharedata.getString("userid", null);
			}
		}
}
