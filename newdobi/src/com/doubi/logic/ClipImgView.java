package com.doubi.logic;

import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.doubi.common.CommonMethod;
import com.doubi.common.ConstValue;
import com.doubi.logic.svgResolve.SVG;
import com.doubi.logic.svgResolve.SVGParser;

/**
 * 截图
 * 
 * @author Administrator
 *
 */
public class ClipImgView extends ImageView {
	private Bitmap roundConcerImage;
	private ImageManager mImageManager;
	private int moreIndex; // 多人扮演正在修改的头像

	public ClipImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mImageManager = new ImageManager();
	}

	/**
	 * 设置被剪切的图片
	 * 
	 * @param mBitmap
	 */
	public void SetBitmap(Bitmap mBitmap) {
		roundConcerImage = mBitmap;
	}

	public void setMoreIndex(int moreIndex) {
		this.moreIndex = moreIndex;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int baseWidth = this.getWidth();
		Path path=new Path();
		path.quadTo(0, 0, 0, 100);
		path.quadTo(0, 0, 100, 0);
		path.quadTo(100, 0, 100, 100);
		path.quadTo(0, 100, 100, 100);
		Bitmap mBitmap = mImageManager.getBitmapFromPath(path, 100,100);

		mBitmap = mImageManager.getNewSizeMap(mBitmap, baseWidth);

		canvas.drawBitmap(mBitmap, 0, 0, new Paint());
		// 画眼睛、口部位置
		// mPaint.setColor(Color.GREEN);
		// float eyex = baseWidth * 3.45f / 10f;
		// float eyey = baseHeight * 4.5f / 10f;
		// float radius = baseWidth / 45f;
		// canvas.drawCircle(eyex, eyey, radius, mPaint);
		// canvas.drawCircle(baseWidth - eyex, eyey, radius, mPaint);
		// float mouthx = baseWidth / 2f;
		// float mouthy = baseHeight * 7f / 10f;
		// canvas.drawCircle(mouthx, mouthy, radius, mPaint);

		// 设置切割方式
		if (!path.isEmpty()) {
			canvas.clipPath(path, Region.Op.REPLACE);
		}
		
	}
}
