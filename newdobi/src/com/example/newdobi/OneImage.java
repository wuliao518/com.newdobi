package com.example.newdobi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;

public class OneImage extends BaseImageView {

	public OneImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		Bitmap bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		this.mBmps=new Bmp[4];
		mBmps[0]=new Bmp(bitmap, 0, 10, 10, true, false, false);
		mBmps[1]=new Bmp(bitmap, 1, 100, 100, true, false, false);
		mBmps[2]=new Bmp(bitmap, 2, 200, 200, true, false, false);
		mBmps[3]=new Bmp(bitmap, 3, 300, 300, true, false, false);
		this.intAllBmps();
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		for(Bmp bmp : mBmps){
			canvas.drawBitmap(bmp.getPic(), bmp.matrix, null);
		}
		super.onDraw(canvas);
	}

}
