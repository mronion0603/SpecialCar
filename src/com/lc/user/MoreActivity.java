package com.lc.user;

import java.io.File;
import org.json.JSONObject;
import com.lc.net.Download;
import com.lc.net.UpdateNet;
import com.lc.specialcar.ChooseUserActivity;
import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends Activity implements OnClickListener {
	TextView tvTitle;
    ImageView ivleft;
    private RelativeLayout rls;
    Button ivSearch;
    private RelativeLayout rl1,rl2,rl3,rl4;
    UpdateNet updateNet = new UpdateNet();
    String geturl="";
   	private final int GET_UNDATAINFO_ERROR = 2;
   	private final int DOWN_ERROR = 4;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_more);
		init();
		
	}

	public void init(){
		ExitApplication.getInstance().addActivity(this);
		tvTitle = (TextView) findViewById(R.id.topTv);
		tvTitle.setText("更多");
		ivSearch = (Button) findViewById(R.id.Search);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		rls = (RelativeLayout) findViewById(R.id.rlslidemenu);
		rls.setOnClickListener(this);
		ivleft = (ImageView) findViewById(R.id.ArrowHead);
		ivleft.setVisibility(View.VISIBLE);
		rl1 = (RelativeLayout) findViewById(R.id.morenotify);
		rl1.setOnClickListener(this);
		rl2 = (RelativeLayout) findViewById(R.id.usecardate);
		rl2.setOnClickListener(this);
		rl3 = (RelativeLayout) findViewById(R.id.usecardate2);
		rl3.setOnClickListener(this);
		rl4 = (RelativeLayout) findViewById(R.id.usecardate4);
		rl4.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rlslidemenu:
			finish();
			break;
		case R.id.morenotify:
		{	
			Intent intent = new Intent();
			intent.setClass(getApplication(), AgreementActivity.class);
			startActivity(intent);
		}	break;
		case R.id.usecardate:
		{	Intent intent = new Intent();
		    intent.setClass(getApplication(), AboutActivity.class);
		    startActivity(intent);
		}	break;
		case R.id.usecardate2:
		{	Intent intent = new Intent();
	        intent.setClass(getApplication(), FeedBackActivity.class);
	        startActivity(intent);
		}	break;
		case R.id.usecardate4:
		{	
			updateNet.setHandler(mHandler);
			updateNet.setDevice(Global.DEVICE);
			updateNet.setVersion(getVersion());
			updateNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			updateNet.getDataFromServer();
		}	break;
		case R.id.Search:
			MySharePreference.clearPersonal(getApplication());
			finish();
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), ChooseUserActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	 @SuppressLint("HandlerLeak")
		private Handler mHandler = new Handler() {
	        public void handleMessage(android.os.Message msg) {
	            switch(msg.what) { 
		            case Global.GETUPDATE:{
		            	try{
							parseJSON((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
		            case DOWN_ERROR:
						//下载apk失败  
			            Toast.makeText(getApplicationContext(), "下载新版本失败", Toast.LENGTH_SHORT).show(); 
						break;
		            case GET_UNDATAINFO_ERROR:
						//服务器超时   
			            Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show(); 
						break;
	            }
	    }};
	    private void parseJSON(String str)throws Exception{  
	    	System.out.println(str);
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	        // Toast.makeText(MoreActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	   	        // finish();
	   	    	JSONObject jsonobj2 = jsonobj.getJSONObject("Data");
	   	    	String getdate = jsonobj2.getString("date");
	   	    	 geturl = jsonobj2.getString("url");
	   	    	String getversion = jsonobj2.getString("returnVersion");
	   	        //对话框通知用户升级程序   
				showUpdataDialog(getdate,geturl,getversion);
	   	    	
	        }else{
	          Toast.makeText(MoreActivity.this,"已是最新版本，不需要更新", Toast.LENGTH_LONG).show();
	        } 
	   }
	    /**
	      * 获取版本号
	      * @return 当前应用的版本号
	      */
	     public String getVersion() {
	         try {
	             PackageManager manager = this.getPackageManager();
	             PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
	             String version = info.versionName;
	            return version;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "找不到版本号";
	       }
	    }
	     
	     /* 
	 	 *  
	 	 * 弹出对话框通知用户更新程序  
	 	 *  
	 	 * 弹出对话框的步骤： 
	 	 *  1.创建alertDialog的builder.   
	 	 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
	 	 *  3.通过builder 创建一个对话框 
	 	 *  4.对话框show()出来   
	 	 */  
	 	protected void showUpdataDialog(String date,String url,String version) {
	 		AlertDialog.Builder builer = new Builder(this);
	 		builer.setTitle("版本升级");
	 		builer.setMessage("更新时间:"+date);
	 		 //当点确定按钮时从服务器上下载 新的apk 然后安装   װ
	 		builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 				rl4.setClickable(false);
	 				downLoadApk();
	 			}
	 		});
	 		builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	 			public void onClick(DialogInterface dialog, int which) {
	 			}
	 		});
	 		AlertDialog dialog = builer.create();
	 		dialog.show();
	 	}
	 	
	 	/* 
		 * 从服务器中下载APK 
		 */  
		protected void downLoadApk() {  
		    final ProgressDialog pd;    //进度条对话框  
		    pd = new  ProgressDialog(this);  
		    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
		    pd.setMessage("正在下载更新");  
		    pd.show();  
		    new Thread(){  
		        @Override  
		        public void run() {  
		            try {  
		                File file = Download.getFileFromServer(geturl, pd);  
		                sleep(3000);  
		                installApk(file);  
		                pd.dismiss(); //结束掉进度条对话框  
		            } catch (Exception e) {  
		                Message msg = new Message();  
		                msg.what = DOWN_ERROR;  
		                mHandler.sendMessage(msg);  
		                rl4.setClickable(true);
		                e.printStackTrace();  
		            }  
		        }}.start();  
		}  
		//安装apk   
		protected void installApk(File file) {  
			 rl4.setClickable(true);
		    Intent intent = new Intent();  
		    //执行动作  
		    intent.setAction(Intent.ACTION_VIEW);  
		    //执行的数据类型  
		    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
		    startActivity(intent);  
		}  
}
