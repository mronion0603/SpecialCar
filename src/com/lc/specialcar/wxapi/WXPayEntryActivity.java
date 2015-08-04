package com.lc.specialcar.wxapi;



import net.sourceforge.simcpux.Constants;

import com.lc.specialcar.R;
import com.lc.utils.DeleteWXPayApplication;
import com.lc.utils.ExitApplication;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.weixin.paydemo.WXPayActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    private IWXAPI api;
	ImageView iv;
	TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
        setContentView(R.layout.pay_result);
        init();
    	api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }
    public void init(){
    	DeleteWXPayApplication.getInstance().addActivity(this);
    	
    	tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("充值结果");
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				DeleteWXPayApplication.getInstance().exit();
			}
		});
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
    	iv = (ImageView)findViewById(R.id.bg);
    	tv = (TextView)findViewById(R.id.tvtext);
    }
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==0){
				iv.setBackgroundResource(R.drawable.request_success);
				tv.setText("充值成功!");
			}else{
				iv.setBackgroundResource(R.drawable.delete);
				tv.setText("充值失败，请返回重试");
			}
		}
	}
}