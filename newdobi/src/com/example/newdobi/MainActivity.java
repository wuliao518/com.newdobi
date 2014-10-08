package com.example.newdobi;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements OnClickListener{
	private Button one,two,three,four;
	private FrameLayout mLFrameLayout;
	public static int current=0;
	private OneImage image;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mLFrameLayout=(FrameLayout) findViewById(R.id.center);
		one=(Button) findViewById(R.id.one);
		two=(Button) findViewById(R.id.two);
		three=(Button) findViewById(R.id.three);
		four=(Button) findViewById(R.id.four);
		one.setOnClickListener(this);
		two.setOnClickListener(this);
		three.setOnClickListener(this);
		four.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.one:
			current=0;
			if(mLFrameLayout.getChildCount()==0){
				image=new OneImage(getApplicationContext(), null);
				mLFrameLayout.addView(image);
			}
			break;
		case R.id.two:
			current=1;
			Bitmap pic=BitmapFactory.decodeResource(getResources(), R.drawable.a);
			image.mBmps[0].setPic(pic);
			image.invalidate();
			break;
		case R.id.three:
			current=2;
			Bitmap pic2=BitmapFactory.decodeResource(getResources(), R.drawable.b);
			image.mBmps[1].setPic(pic2);
			image.invalidate();
			break;
		case R.id.four:
			current=3;
			break;
		default:
			break;
		}
		
	}
		

	

}
