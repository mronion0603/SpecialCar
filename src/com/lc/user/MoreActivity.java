package com.lc.user;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import com.lc.net.ExitNet;
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
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RemoteViews;

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
   	ExitNet exitnet = new ExitNet();
   	private ProgressBar pro; 
    //通知栏进度条
    private NotificationManager mNotificationManager=null;
    private Notification mNotification;
    private final int NOTIFICATION = 10;
    private final int NOTIFICATION_DOWNLOAD_SUCCESSS = 11;
    int fileSize,downLoadFileSize;
    int tempCurrentSize;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_more);
		init();
		
	}

	public void init(){
		pro = (ProgressBar)findViewById(R.id.progress2); 
		pro.setProgress(0);  
		pro.setIndeterminate(true);
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
		case R.id.morenotify: {
			Intent intent = new Intent();
			intent.setClass(getApplication(), AgreementActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.usecardate: {
			Intent intent = new Intent();
			intent.setClass(getApplication(), AboutActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.usecardate2: {
			Intent intent = new Intent();
			intent.setClass(getApplication(), FeedBackActivity.class);
			startActivity(intent);
		}
			break;
		case R.id.usecardate4: {
			updateNet.setHandler(mHandler);
			updateNet.setDevice(Global.DEVICE);
			updateNet.setVersion(getVersion());
			updateNet.setAuthn(MySharePreference.getStringValue(
					getApplication(), MySharePreference.AUTHN));
			updateNet.getDataFromServer();
		}
			break;
		case R.id.Search: {
			ivSearch.setClickable(false);
			pro.setVisibility(View.VISIBLE);
			exitnet.setHandler(mHandler);
			exitnet.setDevice(Global.DEVICE);
			exitnet.setAuthn(MySharePreference.getStringValue(getApplication(),
					MySharePreference.AUTHN));
			exitnet.getCodeFromServer();
		}
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
			            mNotificationManager.cancel(0);
			            break;
		            case GET_UNDATAINFO_ERROR:
						//服务器超时   
			            Toast.makeText(getApplicationContext(), "获取服务器更新信息失败", Toast.LENGTH_SHORT).show(); 
						break;
		            case Global.EXIT:{
		            	try{
							parseExit((String)msg.obj);
						} catch (Exception e) {	
							e.printStackTrace();
						}      	
		            break;
	                }
		            case NOTIFICATION:{
		            	 int result = downLoadFileSize * 100 / fileSize;
		                 mNotification.contentView.setTextViewText(R.id.content_view_text1, result + "%");
		                 mNotification.contentView.setProgressBar(R.id.content_view_progress, fileSize, downLoadFileSize, false);
		                 mNotificationManager.notify(0, mNotification);  
		                 //Log.e("size", "文件"+downLoadFileSize+":"+fileSize+":"+result);
		                 break;
		            }
		            case NOTIFICATION_DOWNLOAD_SUCCESSS:{
		            	//Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show(); 
		            	mNotificationManager.cancel(0);
		            	break;
		            }
	            }
	    }};
	    private void parseExit(String str)throws Exception{  
	    	//System.out.println(str);
	    	pro.setVisibility(View.GONE);
	    	ivSearch.setClickable(true);
	    	JSONObject jsonobj = new JSONObject(str); 
	    	int result = jsonobj.getInt("ResultCode");
	   	    if(result==Global.SUCCESS){
	   	    	MySharePreference.clearPersonal(getApplication());
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), ChooseUserActivity.class);
				startActivity(intent);	
				finish();
	        }else{
	            Toast.makeText(MoreActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
	        } 
	   }
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
			notificationInit();
		    final ProgressDialog pd;    //进度条对话框  
		    pd = new  ProgressDialog(this);  
		    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
		    pd.setMessage("正在下载更新");  
		    pd.show();  
		    
		    //创建一个通知  
	        //notification = new Notification(R.drawable.ic_launcher, "开始下载", System.currentTimeMillis());  
	        //notification.setLatestEventInfo(this, "正在下载新版本", "", null);  
	        //用NotificationManager的notify方法通知用户生成标题栏消息通知  
	        //nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
	       // nManager.notify(100, notification);//id是应用中通知的唯一标识  
	        
		    new Thread(){  
		        @Override  
		        public void run() {  
		            try {  
		                File file = getFileFromServer(geturl, pd);  
		                sleep(2000);  
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
		
		
		public File getFileFromServer(String path, ProgressDialog pd) throws Exception{
			//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				//获取到文件的大小 
				fileSize = conn.getContentLength();
				pd.setMax(fileSize);
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name)+".apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len ;
				//int total=0;
				downLoadFileSize = 0;
				int downloadCount = 0;
				while((len =bis.read(buffer))!=-1){
					fos.write(buffer, 0, len);
					downLoadFileSize+= len;
					//获取当前下载量
					 pd.setProgress(downLoadFileSize);
					 int tmp = (int) (downLoadFileSize * 100 / fileSize);
					 if(downloadCount==0||tmp-10>downloadCount){
					 downloadCount+=10;
					 sendMsg(NOTIFICATION);
					}
				}
				sendMsg(NOTIFICATION_DOWNLOAD_SUCCESSS);// 通知下载完成
				fos.close();
				bis.close();
				is.close();
				return file;
			}
			else{
				return null;
			}
		}
		
		 private void sendMsg(int flag) {
			  Message msg = new Message();
			  msg.what = flag;
			  mHandler.sendMessage(msg);
	     }
		 
		 private void notificationInit(){
			  //通知栏内显示下载进度条
			  Intent intent=new Intent(this,MoreActivity.class);//点击进度条，进入程序
			  PendingIntent pIntent=PendingIntent.getActivity(this, 0, intent, 0);
			  mNotificationManager=(NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
			  mNotification=new Notification();
			  mNotification.icon=R.drawable.ic_launcher;
			  mNotification.tickerText="开始下载";
			  mNotification.contentView=new RemoteViews(getPackageName(),R.layout.userinfo_notification);//通知栏中进度布局
			  mNotification.contentIntent=pIntent;
			//  mNotificationManager.notify(0,mNotification);
		}
}
