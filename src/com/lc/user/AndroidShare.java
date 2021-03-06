package com.lc.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.lc.specialcar.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


@SuppressLint("NewApi")
public class AndroidShare extends Dialog implements AdapterView.OnItemClickListener {
	private LinearLayout mLayout;
	private GridView mGridView;
	private float mDensity;
	private String msgText;
	private String mImgPath;
	private int mScreenOrientation;
	private List<ShareItem> mListData;
	private Handler mHandler = new Handler();
    TextView iv;
	private Runnable work = new Runnable() {
		public void run() {
			int orient = getScreenOrientation();
			if (orient != mScreenOrientation) {
				if (orient == 0)
					mGridView.setNumColumns(4);
				else {
					mGridView.setNumColumns(6);
				}
				mScreenOrientation = orient;
				((AndroidShare.MyAdapter) mGridView.getAdapter()).notifyDataSetChanged();
			}
			mHandler.postDelayed(this, 1000L);
		}
	};

	public AndroidShare(Context context) {
		super(context, R.style.shareDialogTheme);
	}

	public AndroidShare(Context context, int theme, String msgText, final String imgUri) {
		super(context, theme);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri, getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	public AndroidShare(Context context, String msgText, final String imgUri) {
		super(context, R.style.shareDialogTheme);
		this.msgText = msgText;

		if (Patterns.WEB_URL.matcher(imgUri).matches())
			new Thread(new Runnable() {
				public void run() {
					try {
						mImgPath = getImagePath(imgUri,getFileCache());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}).start();
		else
			this.mImgPath = imgUri;
	}

	void init(Context context) {
		//msgText = context.getResources().getString(R.string.sharecontent);
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		this.mDensity = dm.density;
		this.mListData = new ArrayList<ShareItem>();
		this.mListData.add(new ShareItem("微信", R.drawable.share_weixin,
				"com.tencent.mm.ui.tools.ShareImgUI", "com.tencent.mm"));
		//this.mListData.add(new ShareItem("朋友圈", R.drawable.logo_wechatmoments,
		//		"com.tencent.mm.ui.tools.ShareToTimeLineUI", "com.tencent.mm"));
		this.mListData.add(new ShareItem("QQ", R.drawable.share_qq,
				"com.tencent.mobileqq.activity.JumpActivity","com.tencent.mobileqq"));
		//this.mListData.add(new ShareItem("qq空间", R.drawable.logo_qzone,
		//		"com.qzone.ui.operation.QZonePublishMoodActivity","com.qzone"));
		this.mListData.add(new ShareItem("新浪", R.drawable.share_weibo,
				"com.sina.weibo.EditActivity", "com.sina.weibo"));
		//this.mListData.add(new ShareItem("腾讯微博", R.drawable.logo_tencentweibo,
		//		"com.tencent.WBlog.intentproxy.TencentWeiboIntent","com.tencent.WBlog"));
		this.mListData.add(new ShareItem("短信", R.drawable.share_message,
				"",""));

		this.mLayout = new LinearLayout(context);
		this.mLayout.setOrientation(1);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
		params.leftMargin = ((int) (10.0F * this.mDensity));
		params.rightMargin = ((int) (10.0F * this.mDensity));
		this.mLayout.setLayoutParams(params);
		this.mLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));

		this.iv = new TextView(context);
		//this.iv.setScaleType(ImageView.ScaleType.CENTER);;
		//this.iv.setBackgroundResource(R.drawable.share_logo);
		this.iv.setTextColor(context.getResources().getColor(R.color.black));
		this.iv.setTextSize((int) (6.0F * this.mDensity));
		this.iv.setText("分享到");
		LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(-1, -2);
		params2.topMargin = ((int) (10.0F * this.mDensity));
		params2.bottomMargin = ((int) (8.0F * this.mDensity));
		params2.leftMargin = ((int) (15.0F * this.mDensity));
		params2.rightMargin = ((int) (15.0F * this.mDensity));
		this.iv.setLayoutParams(params2);
		this.mLayout.addView(this.iv);
		
		this.mGridView = new GridView(context);
		this.mGridView.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
		this.mGridView.setGravity(17);
		this.mGridView.setHorizontalSpacing((int) (10.0F * this.mDensity));
		this.mGridView.setVerticalSpacing((int) (10.0F * this.mDensity));
		this.mGridView.setStretchMode(1);
		this.mGridView.setColumnWidth((int) (90.0F * this.mDensity));
		this.mGridView.setHorizontalScrollBarEnabled(false);
		this.mGridView.setVerticalScrollBarEnabled(false);
		this.mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		this.mLayout.addView(this.mGridView);
	}

	public List<ComponentName> queryPackage() {
		List<ComponentName> cns = new ArrayList<ComponentName>();
		Intent i = new Intent("android.intent.action.SEND");
		i.setType("image/*");
		List<ResolveInfo> resolveInfo = getContext().getPackageManager().queryIntentActivities(i, 0);
		for (ResolveInfo info : resolveInfo) {
			ActivityInfo ac = info.activityInfo;
			ComponentName cn = new ComponentName(ac.packageName, ac.name);
			cns.add(cn);
		}
		return cns;
	}

	public boolean isAvilible(Context context, String packageName) {
		PackageManager packageManager = context.getPackageManager();

		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		
		for (int i = 0; i < pinfo.size(); i++) {
			if (((PackageInfo) pinfo.get(i)).packageName.equalsIgnoreCase(packageName)){
				//System.out.println("PackageInfo"+((PackageInfo) pinfo.get(i)).packageName+"");
				//System.out.println(packageName);
				return true;
			}
		}
		return false;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Context context = getContext();
		init(context);
		setContentView(this.mLayout);

		getWindow().setGravity(80);

		if (getScreenOrientation() == 0) {
			this.mScreenOrientation = 0;
			this.mGridView.setNumColumns(4);
		} else {
			this.mGridView.setNumColumns(6);
			this.mScreenOrientation = 1;
		}
		this.mGridView.setAdapter(new MyAdapter());
		this.mGridView.setOnItemClickListener(this);

		this.mHandler.postDelayed(this.work, 1000L);

		setOnDismissListener(new DialogInterface.OnDismissListener() {
			public void onDismiss(DialogInterface dialog) {
				mHandler.removeCallbacks(work);
			}
		});
	}

	public void show() {
		super.show();
	}

	public int getScreenOrientation() {
		int landscape = 0;
		int portrait = 1;
		Point pt = new Point();
		getWindow().getWindowManager().getDefaultDisplay().getSize(pt);
		int width = pt.x;
		int height = pt.y;
		return width > height ? portrait : landscape;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ShareItem share = (ShareItem) this.mListData.get(position);
		shareMsg(getContext(), "分享到...", this.msgText, this.mImgPath, share);
	}

	private void shareMsg(Context context, String msgTitle, String msgText,
			String imgPath, ShareItem share) {
		if (!share.packageName.isEmpty() && !isAvilible(getContext(), share.packageName)) {
			Toast.makeText(getContext(), "请先安装" + share.title, Toast.LENGTH_SHORT).show();
			return;
		}

        try{
		Intent intent = new Intent("android.intent.action.SEND");
		if ((imgPath == null) || (imgPath.equals(""))) {
			intent.setType("text/plain");
		} else {
			File f = new File(imgPath);
			if ((f != null) && (f.exists()) && (f.isFile())) {
				intent.setType("image/png");
				intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
			}
		}

		intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
		intent.putExtra(Intent.EXTRA_TEXT, msgText);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if(!share.packageName.isEmpty()) {
			intent.setComponent(new ComponentName(share.packageName,share.activityName));
			context.startActivity(intent);
		}
		else {
			//context.startActivity(Intent.createChooser(intent, msgTitle));
			Uri smsToUri = Uri.parse( "smsto:" );  
		    Intent sendIntent =  new  Intent(Intent.ACTION_VIEW, smsToUri);  
		     //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话   
		    sendIntent.putExtra( "sms_body" ,  msgText );  
		    sendIntent.setType( "vnd.android-dir/mms-sms" );  
		    context.startActivity(sendIntent );
		}
        }catch (ActivityNotFoundException  e){
            Toast.makeText(context, "您的手机没有安装新浪微博客户端或微博版本过低", Toast.LENGTH_LONG).show();
        }catch(Exception  e){
            Toast.makeText(context, "您的手机系统版本过低，暂不支持此功能", Toast.LENGTH_LONG).show();
        } 
	}

	private File getFileCache() {
		File cache = null;

		if (Environment.getExternalStorageState().equals("mounted"))
			cache = new File(Environment.getExternalStorageDirectory() + "/." + getContext().getPackageName());
		else {
			cache = new File(getContext().getCacheDir().getAbsolutePath() + "/." + getContext().getPackageName());
		}
		if ((cache != null) && (!cache.exists())) {
			cache.mkdirs();
		}
		return cache;
	}

	public String getImagePath(String imageUrl, File cache) throws Exception {
		String name = imageUrl.hashCode() + imageUrl.substring(imageUrl.lastIndexOf("."));
		File file = new File(cache, name);

		if (file.exists()) {
			return file.getAbsolutePath();
		}

		URL url = new URL(imageUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5000);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		if (conn.getResponseCode() == 200) {
			InputStream is = conn.getInputStream();
			FileOutputStream fos = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			is.close();
			fos.close();

			return file.getAbsolutePath();
		}

		return null;
	}

	private final class MyAdapter extends BaseAdapter {
		private static final int image_id = 256;
		private static final int tv_id = 512;

		public MyAdapter() {
		}

		public int getCount() {
			return mListData.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0L;
		}

		private View getItemView() {
			LinearLayout item = new LinearLayout(getContext());
			item.setOrientation(1);
			int padding = (int) (10.0F * mDensity);
			item.setPadding(padding, padding, padding, padding);
			item.setGravity(17);

			ImageView iv = new ImageView(getContext());
			item.addView(iv);
			iv.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
			iv.setId(image_id);

			TextView tv = new TextView(getContext());
			item.addView(tv);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
			layoutParams.topMargin = ((int) (5.0F * mDensity));
			tv.setLayoutParams(layoutParams);
			tv.setTextColor(Color.parseColor("#212121"));
			tv.setTextSize(16.0F);
			tv.setId(tv_id);

			return item;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getItemView();
			}
			ImageView iv = (ImageView) convertView.findViewById(image_id);
			TextView tv = (TextView) convertView.findViewById(tv_id);
			AndroidShare.ShareItem item = (AndroidShare.ShareItem) mListData.get(position);
			iv.setImageResource(item.logo);
			tv.setText(item.title);
			return convertView;
		}
	}

	private class ShareItem {
		String title;
		int logo;
		String activityName;
		String packageName;

		public ShareItem(String title, int logo, String activityName, String packageName) {
			this.title = title;
			this.logo = logo;
			this.activityName = activityName;
			this.packageName = packageName;
		}
	}
}
