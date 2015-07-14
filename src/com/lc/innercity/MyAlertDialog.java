package com.lc.innercity;

import com.lc.specialcar.R;
import com.lc.utils.ButtonEffect;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyAlertDialog
{
	private AlertDialog dialog;
	private LayoutInflater inflater;
	private LinearLayout titleLay, bottomLay;
	private LinearLayout topLay;
	private TextView title,lefttx,righttx,contenttx;
	private LinearLayout rightbutton,leftbutton;
	private Context context;
	private View dialogView;
	public MyAlertDialog(Context context)
	{
		this.context = context;
		
		
		inflater = LayoutInflater.from(context);
		dialogView = inflater.inflate(R.layout.dialog_alert, null);
		contenttx = (TextView) dialogView.findViewById(R.id.littleIcon);
		lefttx = (TextView) dialogView.findViewById(R.id.lefttx);
		righttx = (TextView) dialogView.findViewById(R.id.righttx);
		bottomLay = (LinearLayout) dialogView.findViewById(R.id.dialog_title_lay2);
		topLay = (LinearLayout) dialogView.findViewById(R.id.dialog_title_lay);
		leftbutton = (LinearLayout) dialogView.findViewById(R.id.button);
		rightbutton = (LinearLayout) dialogView.findViewById(R.id.rightbutton);
		dialog = new AlertDialog.Builder(context).create();
	}
	
	
	public void show() {
		dialog.show();
		dialog.getWindow().setContentView(dialogView);
	}
	
	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}
	
	public boolean isShowing() {
		if (dialog != null)
			return dialog.isShowing();
		return false;
	}
	
	public void setCancelable(boolean bool) {
		dialog.setCancelable(bool);
	}
	public void setContentText(String content) {
		contenttx.setText(content);
	
	}
	public void setButtonText(String left,String right) {
		lefttx.setText(left);
		righttx.setText(right);
	}
	public void setOnPositiveButton( View.OnClickListener listener) {
		bottomLay.setVisibility(View.VISIBLE);
		leftbutton.setOnClickListener(listener);
		ButtonEffect.setButtonStateChangeListener(leftbutton);
	}
	public void setOnNegativeButton( View.OnClickListener listener) {
		bottomLay.setVisibility(View.VISIBLE);
		rightbutton.setVisibility(View.VISIBLE);
		rightbutton.setOnClickListener(listener);
		ButtonEffect.setButtonStateChangeListener(rightbutton);
	}

}

