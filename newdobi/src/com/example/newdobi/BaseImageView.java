package com.example.newdobi;

import com.doubi.common.ConstValue;
import com.doubi.logic.ImageManager;
import com.doubi.logic.ImageManager.RectangleManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class BaseImageView extends ImageView {
	protected boolean Begin = true;
	protected Bmp[] mBmps;
	protected ImageManager mImageManager;
	protected int twoPoint = 0;
	protected float startX=0,startY=0;
	protected float centerX=0,centerY=0;
	protected int mode=0;
	/**
	 * 旋转前两指角度
	 */
	protected float preCos = 0f;
	/**
	 * 旋转后两指角度
	 */
	
	/**
	 * 每次执行放大，放大前的两指距离
	 */
	protected float preLength = 480.0f;
	/**
	 * 每次执行放大，放大后的两只距离
	 */
	protected float length = 480.0f;
	
	protected float rotalC[] = new float[2];
	protected float rotalP[] = new float[2];
	protected float rotalP_2[] = new float[2];
	
	protected Context context;
	protected float cos = 0f;
	protected boolean bool = true;
	public BaseImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		mImageManager=new ImageManager();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {  
		case MotionEvent.ACTION_DOWN:  
		    mode = 0;
		    startX=event.getX();
			startY=event.getY();
			order(event.getX(),event.getY());
			Begin = true;
		    break;  
		case MotionEvent.ACTION_UP:  
		    break;  
		case MotionEvent.ACTION_POINTER_UP:
			mode = -1;
		    break;  
		case MotionEvent.ACTION_POINTER_DOWN:
			mode = 1;
			preLength=spacing(event);
			preCos = rotation(event);
			setCenter(MainActivity.current);
		    break;        
		case MotionEvent.ACTION_MOVE:  
		   if(mode==0){
			   float x=event.getX();
			   float y=event.getY();
			   switch (MainActivity.current) {
			   case 0:
				   mBmps[0].matrix.postTranslate(x-startX,y-startY);
				   mBmps[0].preX=mBmps[0].preX+x-startX;
				   mBmps[0].preY=mBmps[0].preY+y-startY;
				   break;
				case 1:
					mBmps[0].matrix.postTranslate(x-startX,y-startY);
					mBmps[1].matrix.postTranslate(x-startX,y-startY);
					break;
				case 2:
					mBmps[0].matrix.postTranslate(x-startX,y-startY);
					mBmps[1].matrix.postTranslate(x-startX,y-startY);
					mBmps[2].matrix.postTranslate(x-startX,y-startY);
					break;
				case 3:
					for(Bmp bmp :mBmps){
						bmp.matrix.postTranslate(x-startX,y-startY);
					}
					break;

				default:
					break;
				}
				setCenter(MainActivity.current);
				startX=x;
				startY=y;
				invalidate();
		   }
		   if(mode==1){
				length = spacing(event);
				cos = rotation(event);
				switch(MainActivity.current) {
				case 0:
					mBmps[0].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[0].matrix.postRotate(cos-preCos,centerX,centerY);
					//twoFinger(event, mBmps[0]);
					break;
				case 1:
					mBmps[0].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[0].matrix.postRotate(cos-preCos,centerX,centerY);
					mBmps[1].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[1].matrix.postRotate(cos-preCos,centerX,centerY);
					break;
				case 2:
					mBmps[0].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[0].matrix.postRotate(cos-preCos,centerX,centerY);
					mBmps[1].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[1].matrix.postRotate(cos-preCos,centerX,centerY);
					mBmps[2].matrix.postScale(length/preLength, length/preLength,centerX,centerY);
					mBmps[2].matrix.postRotate(cos-preCos,centerX,centerY);
					break;
				}
				
				preCos = cos;
				preLength = length;
				invalidate();
		   }
		}

		
		return true;
	}
	
    public float[] rotalPoint(float[] p, float X, float Y,Matrix matrix) {
		float re[] = new float[2];
		float matrixArray[] = new float[9];
		matrix.getValues(matrixArray);
		float a = p[0] - X;
		float b = p[1] - Y;
		re[0] = a * matrixArray[0] - b * matrixArray[1] + X;
		re[1] = -a * matrixArray[3] + b * matrixArray[4] + Y;
		return re;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint=new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		canvas.drawCircle(centerX, centerY, 20, paint);
	}
	
	/**
	 * 初始化画面内容数组，mBmps项目所有项都是新创建的情况必须调用该方法
	 */
	protected void intAllBmps() {
		for (int i = 0; i < 2; i++) {
			if (mBmps[i] != null) {
				// 保存最初尺寸
				mBmps[i].width = mBmps[i].getPic().getWidth();
				mBmps[i].height = mBmps[i].getPic().getHeight();
				// 根据添加顺序为图片设立唯一标示
				mBmps[i].setImgId(i);
			}
		}
	}
	
	
	private float getBmpWidth(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		return values[0]*bmp.getPic().getWidth();
	}
	
	private float getBmpHeight(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		return values[0]*bmp.getPic().getHeight();
	}
	
	
	/** ============内部算法，不需调整===========start======= */
	
	
	public float[] rotalPoint(float[] p, float X, float Y, float width,
			float height, Matrix matrix) {
		float re[] = new float[2];
		float matrixArray[] = new float[9];
		matrix.getValues(matrixArray);
		float a = p[0] - X;
		float b = p[1] - Y;
		re[0] = a * matrixArray[0] - b * matrixArray[1] + X;
		re[1] = -a * matrixArray[3] + b * matrixArray[4] + Y;
		return re;
	}
	
	
	
	private void twoFinger(MotionEvent event,Bmp bmp) {
		if(bmp!=null){
			rotalP = rotalPoint(new float[] { event.getX(0), event.getY(0) },
					bmp.getXY(1), bmp.getXY(2),
					bmp.width / 2, bmp.height / 2,
					bmp.matrix);
			rotalP_2 = rotalPoint(new float[] { event.getX(1), event.getY(1) },
					bmp.getXY(1), bmp.getXY(2),
					bmp.width / 2, bmp.height / 2,
					bmp.matrix);
			
			if (bmp.isCanChange()) {
				if (bool) {
					preLength = spacing(event);
					preCos = rotation(event);
					bool = false;
				}
				// 获取角度和长度
				length = spacing(event);
				cos = rotation(event);

				// 旋转
				if (Math.abs(cos) > 3 && Math.abs(cos) < 177
						&& Math.abs(cos - preCos) < 15) {
					// 旋转
					bmp.matrix.postRotate(cos - preCos);
					// 保存角度
					bmp.setRotateSize(bmp.getRotateSize() + cos
							- preCos);
					this.getT(bmp.width / 2, bmp.height / 2,
							bmp.getXY(1), bmp.getXY(2),
							bmp.matrix);
				}
				preCos = cos;
				preLength = length;
				this.getT(bmp.width / 2, bmp.height / 2,
						bmp.getXY(1), bmp.getXY(2),
						bmp.matrix);
			}
		}
		

	}

	public float[] getT(float preX, float preY, float x, float y, Matrix matrix) {
		float[] re = new float[2];
		float[] matrixArray = new float[9];
		matrix.getValues(matrixArray);
		float a = x - preX * matrixArray[0] - preY * matrixArray[1];
		float b = y - preX * matrixArray[3] - preY * matrixArray[4];
		matrixArray[2] = a;
		matrixArray[5] = b;
		matrix.setValues(matrixArray);
		re[0] = a;
		re[1] = b;
		return re;
	}

	public void scale(float preX, float preY, float x, float y, Matrix matrix) {
		float[] matrixArray = new float[9];
		matrix.getValues(matrixArray);
		float a = x - preX;
		float b = y - preY;
		matrixArray[2] = a;
		matrixArray[5] = b;
		matrix.setValues(matrixArray);
	}

	public void setToO(Matrix matrix) {
		float[] matrixArray = new float[9];
		matrix.getValues(matrixArray);
		float a = 0f;
		float b = 0f;
		matrixArray[2] = a;
		matrixArray[5] = b;
		matrix.setValues(matrixArray);
	}



	// 计算长度
	@SuppressLint("FloatMath")
	protected float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// 取旋转角度
	protected float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}
	private void setCenter(int current){
		switch (current) {
		case 0:
			float[] values=new float[9];
			mBmps[0].matrix.getValues(values);
			centerX=values[2]+mBmps[0].getPic().getWidth()*values[0]/2;
			centerY=values[5]+mBmps[0].getPic().getHeight()*values[0]/2;
			break;
		case 1:
			float[] values1=new float[9];
			float[] values2=new float[9];
			mBmps[0].matrix.getValues(values1);
			mBmps[1].matrix.getValues(values2);
			centerX=values1[2]+(values2[2]+mBmps[0].getPic().getWidth()*values2[0]-values1[2])/2;
			centerY=values1[5]+(values2[5]+mBmps[0].getPic().getHeight()*values2[0]-values1[5])/2;
			break;
		case 2:
			float[][] values_2=new float[9][3];
			values_2[0]=new float[9];
			values_2[1]=new float[9];
			values_2[2]=new float[9];
			mBmps[0].matrix.getValues(values_2[0]);
			mBmps[1].matrix.getValues(values_2[1]);
			mBmps[2].matrix.getValues(values_2[2]);
			float left=getMin(values_2[0][2],values_2[1][2],values_2[2][2]);
			int right=getMaxNum(values_2[0][2],values_2[1][2],values_2[2][2]);
			float top=getMin(values_2[0][5],values_2[1][5],values_2[2][5]);
			int bottom=getMaxNum(values_2[0][5],values_2[1][5],values_2[2][5]);
			centerX=left+(values_2[right][2]+mBmps[right].getPic().getWidth()*values_2[right][0]-left)/2;
			centerY=top+(values_2[bottom][5]+mBmps[bottom].getPic().getWidth()*values_2[bottom][0]-top)/2;
			break;
		case 3:
			

		default:
			break;
		}
	}
	
	private int getMaxNum(float f, float g, float h) {
		float[] values=new float[]{f,g,h};
		int max=0;
		for(int i=0;i<values.length;i++){
			if(values[i]>values[max])
				max=i;
		}
		return max;
	}
	private float getMin(float f, float g, float h) {
		float min=Math.min(f, g);
		min=Math.min(min, h);
		return min;
	}

	/** ===============内部算法，不需调整 ===========end====================* */
	



	/**
	 * 每个图片单体类
	 * 
	 * @author Administrator
	 *
	 */
	public class Bmp {
		/**
		 * 负责移动、放缩、旋转bitmap的类
		 */
		protected Matrix matrix;

		// 放缩前尺寸
		protected float width = 0;
		protected float height = 0;
		/**
		 * 图片
		 */
		private Bitmap pic = null;

		/**
		 * 图片最终显示层级
		 */
		protected int priorityBase = 0;

		/**
		 * 图片中心在控件的X坐标
		 */
		private float preX = 0;
		/**
		 * 图片中心在控件的Y坐标
		 */
		private float preY = 0;

		/**
		 * bitmap对应的当前显示层级别的index
		 */
		private int priority = 0;
		/**
		 * 图片标识
		 */
		private int imgId = 0;
		/**
		 * 是否可调整
		 */
		private boolean canChange;
		/**
		 * 是否只能平移
		 */
		private boolean onlyCanTranslation;

		/**
		 * 是否被选中
		 */
		private boolean isFocus;
		/**
		 * 基础图片
		 */
		private Bitmap basePic;

		/**
		 * 是否容易选中（判断选中状态是否支持触碰透明部分）
		 */
		private boolean isEasySelect;

		/**
		 * 图片已旋转的角度
		 */
		private float rotateSize;

		// 构造器
		private Bmp(Bitmap pic, int piority) {
			this.pic = pic;
			this.basePic = pic;
			this.priority = piority;
			this.priorityBase = piority;
		}

		/**
		 * @param pic
		 *            :the Bitmap to draw
		 * @param priority
		 *            : bitmap对应的index
		 * @param preX
		 *            坐标
		 * @param preY
		 *            坐标
		 * @param iscanChange
		 *            是否可被选中
		 * @param isEasySelect
		 *            是否可点中透明部分
		 * @param isOnlyCanTranslation
		 *            是否只能平移
		 */
		protected Bmp(Bitmap pic, int priority, float preX, float preY,
				boolean iscanChange, boolean isEasySelect,
				boolean isOnlyCanTranslation) {
			this(pic, priority);
			this.preX = preX;// + pic.getWidth() / 2 * 1.5f;
			this.preY = preY;// + pic.getHeight() / 2 * 1.5f;
			this.canChange = iscanChange;
			this.isEasySelect = isEasySelect;
			this.onlyCanTranslation = isOnlyCanTranslation;
			if (matrix == null) {
				matrix = new Matrix();
				this.matrix.preTranslate(this.preX - this.width / 2, this.preY
						- this.height / 2);
			}
		}

		/**
		 * 对初始化过的Bmp重新赋值，保证不影响原来旋转的角度
		 * 
		 * @param pic
		 * @param priority
		 *            bitmap对应的index
		 * @param preX
		 *            坐标
		 * @param preY
		 *            坐标
		 */
		protected void SetBmpInfo(Bitmap pic) {

			this.pic = pic;
			this.basePic = pic;
		}

		// set Priority
		protected void setPiority(int priority) {
			this.priority = priority;
		}

		// return Priority
		protected int getPriority() {
			return this.priority;
		}

		/**
		 * 获取图片中心位置所在坐标
		 * 
		 * @param i
		 *            1X坐标，2Y坐标
		 * @return 图片中心位置所在坐标
		 */
		@SuppressWarnings("null")
		protected float getXY(int i) {
			if (i == 1) {
				return this.preX;
			} else if (i == 2) {
				return this.preY;
			}
			return (Float) null;
		}

		/**
		 * 设置图片中心位置X坐标
		 * 
		 * @param x
		 */
		protected void setPreX(float x) {
			this.preX = x;
		}

		/**
		 * 设置图片中心位置Y坐标
		 * 
		 * @param x
		 */
		protected void setPreY(float y) {
			this.preY = y;
		}

		protected void setPic(Bitmap pic) {
			this.pic = pic;
		}

		/**
		 * getPicture
		 * 
		 * @return
		 */
		public Bitmap getPic() {
			return this.pic;
		}

		/**
		 * 获取放缩前宽度
		 * 
		 * @return
		 */
		protected float getWidth() {
			return width;
		}

		/**
		 * 获取放缩前高度
		 * 
		 * @return
		 */
		protected float getHeight() {
			return height;
		}

		/**
		 * 获取图片唯一标示，从0开始
		 * 
		 * @return
		 */
		protected int getImgId() {
			return imgId;
		}

		/**
		 * 设置唯一标示
		 * 
		 * @param imgId
		 */
		protected void setImgId(int imgId) {
			this.imgId = imgId;
		}

		/**
		 * 获取是否可调整
		 * 
		 * @return
		 */
		protected boolean isCanChange() {
			return canChange;
		}

		/**
		 * 是否可调整
		 * 
		 * @return
		 */
		public void setCanChange(boolean canChange) {
			this.canChange = canChange;
		}

		protected void setBasePic(Bitmap basePic) {
			this.basePic = basePic;
		}

		/**
		 * 增加光圈
		 */
		protected void addHighLight() {

			/*
			 * 津贴图片边缘模式，不够清晰问题未解决 Paint p = new Paint();
			 * p.setColor(Color.BLUE);// 红色的光晕 p.setStyle(Paint.Style.STROKE);//
			 * 设置空心 p.setStrokeWidth(40f);
			 * 
			 * Bitmap b = this.pic; Bitmap bitmap =
			 * Bitmap.createBitmap(b.getWidth(), b.getHeight(),
			 * ConstValue.MY_CONFIG); Canvas canvas = new Canvas(bitmap);
			 * canvas.drawBitmap(b.extractAlpha(), 0, 0, p); StateListDrawable
			 * sld = new StateListDrawable(); sld.addState(new int[] {
			 * android.R.attr.state_pressed }, new BitmapDrawable(bitmap));
			 * DrawViewBase.this.setBackgroundDrawable(sld);
			 * 
			 * canvas.drawBitmap(b, 0, 0, null);
			 */

			Bitmap b = this.pic;
			Bitmap bitmap = Bitmap.createBitmap((int) this.getWidth(),
					(int) this.getHeight(), ConstValue.MY_CONFIG_8888);
			Canvas tempcanvas = new Canvas(bitmap);
			RectF rec = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());// 画边框

			Paint paint = new Paint(); // 设置边框颜色
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE); // 设置边框宽度
			paint.setStrokeWidth(3f);

			tempcanvas.drawBitmap(b, 0, 0, null);
			tempcanvas.drawRoundRect(rec, 20f, 20f, paint);
			this.pic = bitmap;
		}

		/**
		 * 取消光圈
		 */
		protected void cancelHighLight() {
			if (this.width > 0 && this.height > 0) {
				this.pic = null;
				this.pic = Bitmap.createScaledBitmap(this.basePic,
						(int) this.width, (int) this.height, false);
			}
		}

		/**
		 * 是否拥有焦点
		 * 
		 * @return
		 */
		protected boolean isFocus() {
			return isFocus;
		}

		protected void setFocus(boolean isFocus) {
			this.isFocus = isFocus;
		}

		/**
		 * 是否可点中透明部分
		 * 
		 * @return
		 */
		protected boolean isEasySelect() {
			return isEasySelect;
		}

//		/**
//		 * 初次加载的图片初始化位置，保证以中心为轴旋转
//		 */
//		protected void intBitmap() {
//			this.matrix.preTranslate(this.preX - this.width / 2, this.preY
//					- this.height / 2);
//		}

		/**
		 * 是否只能平移
		 * 
		 * @return
		 */
		protected boolean isOnlyCanTranslation() {
			return onlyCanTranslation;
		}

		/**
		 * 获取已经旋转的角度
		 * 
		 * @return
		 */
		protected float getRotateSize() {
			return rotateSize;
		}

		/**
		 * 设置已经旋转的角度
		 * 
		 * @param rotateSize
		 */
		protected void setRotateSize(float rotateSize) {
			this.rotateSize = rotateSize;
		}

		/**
		 * 获取对应的矩形类
		 * 
		 * @return
		 */
		protected RectangleManager getRectangle() {
			RectangleManager result = mImageManager.new RectangleManager(
					this.preX, this.preY, this.pic.getWidth(),
					this.pic.getHeight(), this.rotateSize);
			return result;
		}

		/**
		 * 释放图片内存
		 */
		protected void recycleMap() {
			if (this.basePic != null) {
				this.basePic.recycle();
				this.basePic = null;
			}
			if (this.pic != null) {
				this.pic.recycle();
				this.pic = null;
			}
		}

		@Override
		public String toString() {
			return "Bmp [width=" + width + ", height=" + height
					+ ", priorityBase=" + priorityBase + ", preX=" + preX
					+ ", preY=" + preY + ", priority=" + priority + ", imgId="
					+ imgId + ",  rotateSize="
					+ rotateSize + "]";
		}
	}

	/**
	 * 控制上下显示顺序，点击那个图片，那个图片显示到上面
	 * 
	 * @param event
	 */
	protected void order(float x, float y) {
		for(int i=0;i<mBmps.length;i++){
			if(isPoint(mBmps[i], x, y)){
				
			}
		}
	
	}

	/**
	 * 次序为i的图片是否被点击到
	 * 
	 * @param i
	 * @return
	 */
	private boolean isPoint(Bmp bmp, float X, float Y) {
		boolean result = false;
		float[] point=new float[]{0,0};
		bmp.matrix.mapPoints(point);
		
		
		return result;
	}
	private float getBmpLeft(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		return values[2];
	}
	private float getBmpTop(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		return values[5];
	}
	private float getScale(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		return (float)Math.sqrt(Math.pow(values[0], 2)+Math.pow(values[3], 2));
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
