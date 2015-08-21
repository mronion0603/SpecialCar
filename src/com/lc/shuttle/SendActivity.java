package com.lc.shuttle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.lc.innercity.AddressActivity;
import com.lc.innercity.CarDemandActivity;
import com.lc.innercity.GroupAdapter;
import com.lc.innercity.ModifyNameActivity;
import com.lc.innercity.SelectCarActivity;
import com.lc.innercity.SendDealActivity;
import com.lc.net.AddShuttleNet;
import com.lc.net.GetAddressNet;
import com.lc.net.GetAirportNet;
import com.lc.net.RouteMatrixNet;
import com.lc.popupwindow.AddressPopupWindow;
import com.lc.popupwindow.TimePopupWindow;
import com.lc.progressbutton.CircularProgressButton;
import com.lc.specialcar.R;
import com.lc.user.Discount2Activity;
import com.lc.utils.ButtonEffect;
import com.lc.utils.ExitApplication;
import com.lc.utils.Global;
import com.lc.utils.MySharePreference;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SendActivity extends Activity implements OnClickListener {
	public static final int REQUSET_NAMEPHONE = 1;
	public static final int REQUSET_ADDRESS = 2;
	public static final int REQUSET_DEMAND = 3;
	public static final int REQUSET_SELECTCAR = 5;
	public static final int REQUSET_DISCOUNT = 6;
    TextView feeRule,txdate,tvname,tvphone,tvendaddress;
    TextView etAirport,tvdemand;
    CircularProgressButton ivSearch;
    private RelativeLayout rlusecar,rldate,rlmodifyname,rlstartaddress,rlAirportAddress,rlselectcar,rldiscount;
    private ImageView imAddress;
	private View originview; 
	GroupAdapter groupAdapter;
	AddressPopupWindow menuWindow;	//自定义的弹出框类
	TimePopupWindow timepWindow;
	GetAddressNet getaddressnet = new GetAddressNet();
	private List<HashMap<String , Object>> groups1= new ArrayList<HashMap<String , Object>>();
	String getCity;
	GetAirportNet getAirportNet = new GetAirportNet();
	AddressPopupWindow airportWindow;
	private List<HashMap<String , Object>> groups2= new ArrayList<HashMap<String , Object>>();
	AddShuttleNet addShuttleNet = new AddShuttleNet();
	String type = "1";
	//EditText etflight;
	String vMoneystr="0";	String voucherNumstr="";
	double elat=0,elont=0;
	double slat=0,slont=0;
	TextView tvmoney,tvtype;
	String ruleStr1 = "13";
	String ruleStr2 = "元+";
	String ruleStr3 = "1.5";
	String ruleStr4 = "元/公里+";
	String ruleStr5 = "0.24";
	String ruleStr6 = "元/分钟";
	double basicmoney=13,pricedis=1.5,pricedura=0.24;
	int duration=0, distance =0;
	RouteMatrixNet routeMatrixNet = new RouteMatrixNet();
	boolean mSwitch = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 无标题
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.shuttle_send);
		SDKInitializer.initialize(getApplicationContext());  
		init();	
	}

	public void init(){
		getCity = MySharePreference.getStringValue(getApplication(), MySharePreference.CITY);
		//System.out.println("getCity"+getCity);
		ExitApplication.getInstance().addActivity(this);
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		originview = layoutInflater.inflate(R.layout.activity_innercity_carinfo, null);  
		rlselectcar = (RelativeLayout) findViewById(R.id.selectcar);
		rlselectcar.setOnClickListener(this);
		rldiscount= (RelativeLayout) findViewById(R.id.rldiscount);
		rldiscount.setOnClickListener(this);
		tvmoney = (TextView) findViewById(R.id.money2);
		tvname = (TextView) findViewById(R.id.Name);
		tvphone = (TextView) findViewById(R.id.Phone);
		tvendaddress = (TextView) findViewById(R.id.getoffAddress);
		ivSearch = (CircularProgressButton) findViewById(R.id.Search);
		ivSearch.setIndeterminateProgressMode(true);
		ivSearch.setOnClickListener(this);
		ButtonEffect.setButtonStateChangeListener(ivSearch);
		tvdemand = (TextView) findViewById(R.id.Demand);
		rlusecar = (RelativeLayout) findViewById(R.id.usecar);
		rlusecar.setOnClickListener(this);
		etAirport= (TextView) findViewById(R.id.airport);
		rlAirportAddress= (RelativeLayout) findViewById(R.id.AirportAddress);
		rlAirportAddress.setOnClickListener(this);
		rlmodifyname = (RelativeLayout) findViewById(R.id.rlmodifyname);
		rlmodifyname.setOnClickListener(this);
		rlstartaddress= (RelativeLayout) findViewById(R.id.startaddress);
		rlstartaddress.setOnClickListener(this);
		//etflight= (EditText) findViewById(R.id.flightnum);
		rldate = (RelativeLayout) findViewById(R.id.usecardate);
		rldate.setOnClickListener(this);
		txdate = (TextView) findViewById(R.id.txdate);
		tvtype = (TextView) findViewById(R.id.financialType);
		imAddress = (ImageView) findViewById(R.id.Star);
		imAddress.setOnClickListener(this);
		feeRule=(TextView)findViewById(R.id.FeeRule);
		getaddressnet.setHandler(mHandler);
	    getaddressnet.setDevice(Global.DEVICE);
	    getaddressnet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
	    getaddressnet.getCodeFromServer();
	    if(getCity!=null){
	    if(getCity.length()>=1){
		    getAirportNet.setHandler(mHandler);
		    getAirportNet.setDevice(Global.DEVICE);
		    getAirportNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
		    getCity=getCity.substring(0, getCity.length()-1);
		    //System.out.println(getCity);
		    getAirportNet.setCity(getCity);
		    getAirportNet.getDataFromServer();
	    }
	    }
	    String username = MySharePreference.getStringValue(getApplication(), MySharePreference.USERNAME);
		if(username==null){
			tvname.setText( MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
		}else{
			tvname.setText(username);
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.usecardate:{
			timepWindow = new TimePopupWindow(SendActivity.this,itemsOnClick);
			timepWindow.showAsDropDown(originview, 0, 0); 		
		}	break;
		case R.id.AirportAddress:{
			airportWindow = new AddressPopupWindow(SendActivity.this,itemOnClick2,groups2,"附近机场");//实例化AddressPopupWindow
			airportWindow.showAsDropDown(originview, 0, 0); //显示窗口
		}	break;
		case R.id.Star:
		{	menuWindow = new AddressPopupWindow(SendActivity.this,itemOnClick,groups1);//实例化AddressPopupWindow
			menuWindow.showAsDropDown(originview, 0, 0); //显示窗口
			if(groups1.size()<=0 && mSwitch){
				Toast.makeText(SendActivity.this,"暂无收藏地址,请到地址管理页面添加", Toast.LENGTH_LONG).show();
			}
		}	break;
		case R.id.Search:{
			
			String startTime = txdate.getText().toString();
			//String flightstr = etflight.getText().toString();
			String airportstr =etAirport.getText().toString();
			String endaddressstr = tvendaddress.getText().toString();
			String strbill = feeRule.getText().toString();
			if(startTime.equals("请输入送机时间")){
				Toast.makeText(getApplication(), "请输入送机时间", Toast.LENGTH_SHORT).show();
			}else if(airportstr.equals("请输入机场")){
				Toast.makeText(getApplication(), "请输入机场", Toast.LENGTH_SHORT).show();
			}else if(endaddressstr.equals("请输入上车地点")|endaddressstr.equals("获取地址失败")
        			|endaddressstr.equals("地址获取中...")){
				Toast.makeText(getApplication(), "请输入上车地点", Toast.LENGTH_SHORT).show();
			}else if(strbill==null|strbill.length()<=0){
				Toast.makeText(getApplication(), "请选择车型", Toast.LENGTH_SHORT).show();
			}else{
			ivSearch.setClickable(false);
			ivSearch.setProgress(50);
			addShuttleNet.setHandler(mHandler);
			addShuttleNet.setAuthn(MySharePreference.getStringValue(getApplication(), MySharePreference.AUTHN));
			addShuttleNet.setDevice(Global.DEVICE);
			addShuttleNet.setCartype(type);
			//addShuttleNet.setFlightnum(flightstr);
            if(tvdemand.getText().toString()!=null)
            	addShuttleNet.setComment(tvdemand.getText().toString());
            addShuttleNet.setsLatitude(String.valueOf(elat));
            addShuttleNet.setsLongitude(String.valueOf(elont));
            addShuttleNet.setStartAddress(tvendaddress.getText().toString());
            addShuttleNet.setLongFootMoney("");
            addShuttleNet.setMileage(String.valueOf(distance));
            addShuttleNet.setRealMoney(String.valueOf(estimate(basicmoney,pricedis,pricedura)));
            addShuttleNet.setRiderName(tvname.getText().toString());
            if(tvphone.getText().toString().equals("本人")){
            	addShuttleNet.setRiderPhone(MySharePreference.getStringValue(getApplication(), MySharePreference.PHONE));
            }else{
            	addShuttleNet.setRiderPhone(tvphone.getText().toString());
            }
            addShuttleNet.seteLatitude(String.valueOf(slat));
            addShuttleNet.seteLongitude(String.valueOf(slont));
            addShuttleNet.setEndAddress(airportstr);
            addShuttleNet.setStartTime(txdate.getText().toString());
            //System.out.println("vMoneystr:"+vMoneystr+" voucherNumstr:"+voucherNumstr);
            addShuttleNet.setVoucherMoney(vMoneystr);
            addShuttleNet.setVoucherNum(voucherNumstr);
            addShuttleNet.setServiceTypeId("2");
            addShuttleNet.getDataFromServer();
			}
			
		}	break;
		case R.id.usecar:
			Intent intent3 = new Intent();
			intent3.setClass(SendActivity.this,CarDemandActivity.class);
			startActivityForResult(intent3, REQUSET_DEMAND);  
			break;
		
		case R.id.rlmodifyname:
			Intent intent5 = new Intent();
			intent5.setClass(SendActivity.this,ModifyNameActivity.class);
			startActivityForResult(intent5, REQUSET_NAMEPHONE);  
			break;
		case R.id.startaddress:
			Intent intent6 = new Intent();
			intent6.setClass(SendActivity.this,AddressActivity.class);
			startActivityForResult(intent6, REQUSET_ADDRESS); 
			break;
		case R.id.selectcar:
		{	Intent intent4 = new Intent();
			intent4.setClass(SendActivity.this,SelectCarActivity.class);
			startActivityForResult(intent4, REQUSET_SELECTCAR);
		}	break;
		case R.id.rldiscount:
		{	Intent intent = new Intent();
			intent.setClass(SendActivity.this,Discount2Activity.class);
			startActivityForResult(intent,REQUSET_DISCOUNT);
		}	break;
		default:
			break;
		}
	}
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.comfirm:
				String time = timepWindow.getTimeUpload2();
				Message message = Message.obtain();  
			    message.obj = time;  
				message.what = Global.TIME_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				timepWindow.dismiss();   
				break;
			case R.id.canceltime:	
				timepWindow.dismiss();   
				break;
			default:
				break;
			}	
		}
    };
  //为弹出窗口实现监听类
    private OnItemClickListener  itemOnClick = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				String address = menuWindow.getItemStr2(arg2);
				HashMap<String , Object> map = menuWindow.getMap(arg2);
				String strlat = (String)map.get("latidute");
				String strlont = (String)map.get("longitude");
				elat =Double.parseDouble(strlat) ;
				elont =Double.parseDouble(strlont);
				Message message = Message.obtain();  
			    message.obj = address;  
				message.what = Global.ADDRESS_END_MESSAGE;  
				mHandler.sendMessageDelayed(message, 50);
				menuWindow.dismiss();   
		}
    };
    //为飞机场窗口实现监听类
    private OnItemClickListener  itemOnClick2 = new OnItemClickListener(){
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				HashMap<String , Object> map = airportWindow.getMap(arg2);
				//System.out.println("map2:"+map.toString());
				String strlat = (String)map.get("latitude");
				String strlont = (String)map.get("longitude");
				slat =Double.parseDouble(strlat) ;
				slont =Double.parseDouble(strlont);
			
			    String getairportname = airportWindow.getItemStr2(arg2);
				Message message = Message.obtain();  
			    message.obj = getairportname;  
				message.what = Global.CHOOSECITY;  
				mHandler.sendMessageDelayed(message, 50);
				airportWindow.dismiss();   
		}
    };
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
	            case Global.TIME_MESSAGE:{
	            	String getdate = (String)msg.obj;
	            	txdate.setText(getdate);
	            	txdate.setTextColor(getResources().getColor(R.color.black));
	            break;
                }
	            case Global.ADDRESS_END_MESSAGE:{
	            	String getaddress = (String)msg.obj;
	            	tvendaddress.setText(getaddress);
	            	tvendaddress.setTextColor(getResources().getColor(R.color.black));
	            	if(etAirport.getText().toString().equals("请输入机场")){
	            		tvmoney.setText("");
	            	}else{
	            		routeMatrixNet.setHandler(mHandler);
		            	routeMatrixNet.setOrigins(String.valueOf(slat)+","+String.valueOf(slont));
		            	routeMatrixNet.setDestinations(String.valueOf(elat)+","+String.valueOf(elont));
		            	routeMatrixNet.getCodeFromServer();
	            	}
	            break;
                }
	            case Global.GETADDRESS: {
					try {
						parseADDRESS((String) msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
	            case Global.AIRPORT: {
					try {
						parseAIRPORT((String) msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
	            case Global.CHOOSECITY: {
	            	etAirport.setText((String) msg.obj);
	            	etAirport.setTextColor(getResources().getColor(R.color.black));
	            	if(tvendaddress.getText().toString().equals("请输入上车地点")|tvendaddress.getText().toString().equals("获取地址失败")
	            			|tvendaddress.getText().toString().equals("地址获取中...")){
	            		tvmoney.setText("");
	            	}else{	
	            		routeMatrixNet.setHandler(mHandler);
		            	routeMatrixNet.setOrigins(String.valueOf(slat)+","+String.valueOf(slont));
		            	routeMatrixNet.setDestinations(String.valueOf(elat)+","+String.valueOf(elont));
		            	routeMatrixNet.getCodeFromServer();
	            	}
					break;
				}
	            case Global.GETBAIDUROUTE: {
					try {
						parseRoute((String) msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
	            case Global.ADDINNER: {
					try {
						parseInner((String) msg.obj);
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;
				}
            }
    }};
    private void parseInner(String str)throws Exception{ 
    	//System.out.println("返回:"+str);
    	JSONObject jsonobj = new JSONObject(str); 
    	int result = jsonobj.getInt("ResultCode");
   	    if(result==Global.SUCCESS){	
   	         Intent intent2 = new Intent();
			 intent2.setClass(SendActivity.this,SendDealActivity.class);
			 intent2.putExtra("orderNum", jsonobj.getJSONObject("Data").getString("orderNum"));
			 startActivity(intent2);
			 //ivSearch.setProgress(0);
			 ivSearch.setClickable(true);
			 finish();
        }else{
           ivSearch.setProgress(0);
           ivSearch.setClickable(true);
           Toast.makeText(SendActivity.this,jsonobj.getString("Message"), Toast.LENGTH_LONG).show();
        } 
    }
	private void parseRoute(String str) throws Exception {
		System.out.println("route:" + str);
		JSONObject jsonobj = new JSONObject(str);
		int result = jsonobj.getInt("status");
		if (result == 0) {
			JSONObject jsonobj2 = jsonobj.getJSONObject("result");
			JSONArray jsonarray = jsonobj2.getJSONArray("elements");
			for (int x = 0; x < jsonarray.length(); x++) {
				JSONObject jsonobj3 = (JSONObject) jsonarray.get(x);
				duration = jsonobj3.getJSONObject("duration").getInt("value");
				distance = jsonobj3.getJSONObject("distance").getInt("value");
			}
			int price = (int) estimate(basicmoney, pricedis, pricedura);
			tvmoney.setText("￥" + price + "起");
		} else {
			Toast.makeText(SendActivity.this, result + "", Toast.LENGTH_LONG).show();
		}
	}
    private void parseADDRESS(String str)throws Exception{ 
	    groups1.clear();
	    //System.out.println(str);
   	    JSONObject jsonobj = new JSONObject(str); 
   	    if(jsonobj.has("Data")){
	    	mSwitch = true;
        JSONArray jsonarray = jsonobj.getJSONArray("Data");
        for(int x=0;x<jsonarray.length();x++){
       	 JSONObject jsonobj2 = (JSONObject)jsonarray.get(x); 
        	 HashMap<String , Object> map = new HashMap<String , Object>();
			 map.put("groupItem",jsonobj2.getString("commAddressId"));
			 map.put("userId",jsonobj2.getString("userId"));
			 map.put("address",jsonobj2.getString("address"));
			 map.put("longitude",jsonobj2.getString("longitude"));
			 map.put("latidute",jsonobj2.getString("latitude"));
			 //System.out.println(map.toString());
			 groups1.add(map);
        }
   	    }
    }
    private void parseAIRPORT(String str)throws Exception{ 
    	//System.out.println(str);
   	    JSONObject jsonobj = new JSONObject(str); 
   	    int result = jsonobj.getInt("ResultCode");
	    if(result==Global.SUCCESS){
	    	JSONArray jsonarray = jsonobj.getJSONArray("Data");
	    	 for(int x=0;x<jsonarray.length();x++){
	           	 JSONObject jsonobj2 = (JSONObject)jsonarray.get(x); 
	            	 HashMap<String , Object> map = new HashMap<String , Object>();
	    			 map.put("city",jsonobj2.getString("city"));   //城市名
	    			 map.put("longitude",jsonobj2.getString("longitude"));
	    			 map.put("latitude",jsonobj2.getString("latitude"));
	    			 map.put("address",jsonobj2.getString("airportName")); //机场名
	    			 //System.out.println(map.toString());
	    			 groups2.add(map);
	         }
	    }
    }
	//重写的结果返回方法  
	@Override  
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
	        super.onActivityResult(requestCode, resultCode, data);  
	        if (requestCode == REQUSET_NAMEPHONE && resultCode == RESULT_OK) {
	        	String name ="";
	        	String phone ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  name = extras.getString("name");
	            	  phone = extras.getString("phone");
	            	  if(name.length()<=0||phone.length()<=0){
	            		  
	            	  }else{
	            	   tvphone.setText(phone);
	            	   tvname.setText(name);
	            	  }
	              }
	        }  
	        if (requestCode == REQUSET_ADDRESS && resultCode == RESULT_OK) {
	        	  String address ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  address = extras.getString("address");
	            	  elat = extras.getDouble("latidute");
	            	  elont = extras.getDouble("longitude");
	            	  tvendaddress.setText(address);
	            	  tvendaddress.setTextColor(getResources().getColor(R.color.black));
	            	  if(etAirport.getText().toString().equals("请输入机场")){
		            		tvmoney.setText("");
		              }else{
		            		routeMatrixNet.setHandler(mHandler);
			            	routeMatrixNet.setOrigins(String.valueOf(slat)+","+String.valueOf(slont));
			            	routeMatrixNet.setDestinations(String.valueOf(elat)+","+String.valueOf(elont));
			            	routeMatrixNet.getCodeFromServer();
		              }
	              }
	        }  
	        if (requestCode == REQUSET_DEMAND && resultCode == RESULT_OK) {
	        	  String demand ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  demand = extras.getString("demand");
	            	  tvdemand.setText(demand);
	              }
	        }  
	        if (requestCode == REQUSET_SELECTCAR && resultCode == RESULT_OK) {
	        	  //String type ="";
	        	  Bundle extras = data.getExtras();
	              if(extras != null){
	            	  type = extras.getString("type");	 
	            	  ruleStr1= extras.getString("bascMoney");
	            	  ruleStr3 = extras.getString("mileageMoney");
	            	  ruleStr5= extras.getString("timeMoney");
	            	  String newMessageInfo = "<font color='black'><b>" + ruleStr1+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr2+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr3+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr4+ "</b></font>"
		                       +"<font color='black'><b>" + ruleStr5+ "</b></font>"
		                       +"<font color='gray'><b>" + ruleStr6+ "</b></font>";
	            	  feeRule.setText(Html.fromHtml(newMessageInfo));
	            	  basicmoney=Double.parseDouble(ruleStr1);
	            	  pricedis=Double.parseDouble(ruleStr3);
	            	  pricedura=Double.parseDouble(ruleStr5);
	            	  tvtype.setText(extras.getString("strtype"));
	            	  int price = (int)estimate(basicmoney,pricedis,pricedura);
	         		  tvmoney.setText("￥"+price+"起");
	              }
	        }  
	    }  
	   
	  /**预估价格*/
    public double estimate(double basicmoney,double pricedis, double pricedura){
    	double totalmoney=0;
    	double dis = (double)distance;
    	double dura=(double)duration;
    	dis = dis/1000;
        dura = dura/60;
    	totalmoney = basicmoney + pricedis*dis +pricedura*dura;
        return totalmoney;
    }
}
