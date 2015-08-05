package com.lc.innercity;

import com.lc.specialcar.R;
import com.lc.utils.ConnectUrl;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MyDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.webkit.WebViewClient;
public class BillingRuleActivity extends Activity implements OnClickListener {
	    TextView tvTitle;
	    ImageView ivleft;
	    private RelativeLayout rls;
	    
	    Dialog dia;
	    private WebView webView;
		//private ProgressBar pb; 
		@Override
		public void onCreate(Bundle savedInstanceState) {
			requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_innercity_bill);
			init();
		}
		public void init(){
			
			ExitApplication.getInstance().addActivity(this);
			//pb = (ProgressBar)findViewById(R.id.progress); 
			
			tvTitle = (TextView) findViewById(R.id.topTv);
			tvTitle.setText("费用明细");
			
			rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
			rls.setOnClickListener(this);
			ivleft = (ImageView) findViewById(R.id.ArrowHead);
			ivleft.setVisibility(View.VISIBLE);
			
			//dia = MyDialog.createLoadingDialog(BillingRuleActivity.this, "正在加载");
			//dia.show();
			webView = (WebView) findViewById(R.id.webView);
			webView.loadUrl(ConnectUrl.bill);
			//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		    webView.setWebViewClient(new WebViewClient(){
		           @Override
		        public boolean shouldOverrideUrlLoading(WebView view, String url) {
		            // TODO Auto-generated method stub
		               //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
		             view.loadUrl(url);
		            return true;
		        }
		          
		       });
		    webView.setWebChromeClient(new WebChromeClient() {
	            @Override
	            public void onProgressChanged(WebView view, int newProgress) {
	                // TODO Auto-generated method stub
	                if (newProgress == 100) {
	                    // 网页加载完成
                        //dia.dismiss();
	                } else {
	                    // 加载中
	                	//dia = MyDialog.createLoadingDialog(BillingRuleActivity.this, "正在加载");
	        			//dia.show();
	                }

	            }
	        });
		}
		
		
		 @SuppressLint("HandlerLeak")
			private Handler mHandler = new Handler() {
		        public void handleMessage(android.os.Message msg) {
		            switch(msg.what) { 
			            case Global.GETCARTYPE:{
			            	try {
								//parseJSON((String)msg.obj);
							} catch (Exception e) {	
								e.printStackTrace();
							}      	
			            break;
		                }
		            }
		        }};
		        
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.rlslidemenu:
				finish();
				break;

			default:
				break;
			}
		}
}
