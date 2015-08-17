package com.weixin.paydemo;

import java.io.*;
import java.util.*;

import android.util.Xml;
import android.widget.TextView;
import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lc.net.AddOrderNet;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;
import com.lc.utils.DeleteWXPayApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xmlpull.v1.XmlPullParser;


@SuppressLint("HandlerLeak")
public class WXPayActivity extends Activity {

	private static final String TAG = "MicroMsg.SDKSample.PayActivity";

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	//TextView show;
	Map<String,String> resultunifiedorder;
	StringBuffer sb;
	
	private AddOrderNet addOrderNet=new AddOrderNet();
	//private String getAuthn="";
	//private String getUserid="";
	//private String getServiceid="";
	private String getOrderid="";
	String price="1";
	String name="专车充值";
	String content="专车充值";
	private ProgressBar pro2;  
	TextView tvTitle,tvprice;
	private RelativeLayout rls;
	ImageView ivleft;
	TextView tvname,tvcontent;
	CircularProgressButton appayBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		setContentView(R.layout.pay);
		//ExitApplication.getInstance().addActivity(this);
		init();
		//show =(TextView)findViewById(R.id.editText_prepay_id);
		req = new PayReq();
		sb=new StringBuffer();
		msgApi.registerApp(Constants.APP_ID);

		appayBtn = (CircularProgressButton) findViewById(R.id.appay_btn);
		appayBtn.setIndeterminateProgressMode(true);
		appayBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				appayBtn.setClickable(false);
				appayBtn.setProgress(50);
				sendPayReq();
			}
		});

	}
	
	
	public void init(){
		 tvname = (TextView) findViewById(R.id.product_subject);
	        tvcontent = (TextView) findViewById(R.id.product_content);
	        tvprice = (TextView) findViewById(R.id.product_price);
		DeleteWXPayApplication.getInstance().addActivity(this);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("微信充值");
		pro2 = (ProgressBar)findViewById(R.id.progress2); 
		pro2.setProgress(0);  
		pro2.setIndeterminate(true);
		Bundle extras = getIntent().getExtras();
        if(extras != null){
        	price = extras.getString("itemprice");
        	tvprice.setText(price+"元");
        	double pricedb = Double.parseDouble(price);
        	pricedb = 100*pricedb;
        	int priceint = (int)pricedb;
        	price = String.valueOf(priceint);
            name = extras.getString("itemname");
            content = extras.getString("itemcontent");
            //getServiceid = extras.getString("serviceid");
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
       
        tvname.setText(name);
        tvcontent.setText(content);
       
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10: {
				try {
					parseJSON(((String)msg.obj));
				} catch (Exception e) {
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
		 //System.out.println(str);
	     JSONObject jsonobj = new JSONObject(str);  
         int result = jsonobj.getInt("ResultCode");  
         if(result==Global.SUCCESS){  
        	 JSONObject jsonobj2 =  (JSONObject) jsonobj.getJSONObject("Data");
        	 getOrderid  = jsonobj2.getString("orderNum");
        	 //System.out.println(getOrderid);
        	 //Toast.makeText(PayActivity.this, result, Toast.LENGTH_LONG).show();
        	 pro2.setVisibility(View.GONE); 
        	 GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
     		 getPrepayId.execute();
         }else{
        	 Toast.makeText(WXPayActivity.this, jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
         }
         
	 }
	/**
	 生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);
		
		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",packageSign);
		return packageSign;
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

        this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion",appSign);
		return appSign;
	}
	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<"+params.get(i).getName()+">");

			sb.append(params.get(i).getValue());
			sb.append("</"+params.get(i).getName()+">");
		}
		sb.append("</xml>");

		Log.e("orion",sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String,String>> {
		private ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(WXPayActivity.this, getString(R.string.app_tip), getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String,String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n"+result.get("prepay_id")+"\n\n");
			//show.setText(sb.toString());

			resultunifiedorder=result;
			appayBtn.setVisibility(View.VISIBLE);
			
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String,String>  doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion",entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Log.e("orion", content);
			Map<String,String> xml=decodeXml(content);

			return xml;
		}
	}


	public Map<String,String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName=parser.getName();
				switch (event) {
					case XmlPullParser.START_DOCUMENT:

						break;
					case XmlPullParser.START_TAG:

						if("xml".equals(nodeName)==false){
							//实例化student对象
							xml.put(nodeName,parser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion",e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}
	
	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

   //
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String	nonceStr = genNonceStr();

			xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", content));
			packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", "http://112.74.82.102/specialCar/weChatpay"));
			packageParams.add(new BasicNameValuePair("out_trade_no",getOrderid));
			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", price));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

		   String xmlstring =toXml(packageParams);
           //改变拼接之后xml字符串格式
           return new String(xmlstring.toString().getBytes(), "ISO8859-1");
			//return xmlstring;

		} catch (Exception e) {
			Log.e(TAG, "genProductArgs fail, ex = " + e.getMessage());
			return null;
		}
		

	}
	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n"+req.sign+"\n\n");

		//show.setText(sb.toString());

		Log.e("orion", signParams.toString());

	}
	private void sendPayReq() {
		//生成签名参数
		genPayReq();
		
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

}

