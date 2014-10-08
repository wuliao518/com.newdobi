package com.example.newdobi;

import java.util.ArrayList;
import java.util.List;

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
	 * ��תǰ��ָ�Ƕ�
	 */
	protected float preCos = 0f;
	/**
	 * ��ת����ָ�Ƕ�
	 */
	
	/**
	 * ÿ��ִ�зŴ󣬷Ŵ�ǰ����ָ����
	 */
	protected float preLength = 480.0f;
	/**
	 * ÿ��ִ�зŴ󣬷Ŵ�����ֻ����
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
					changeBmp(mBmps[0]);
					break;
				case 1:
					changeBmp(mBmps[0],mBmps[1]);
					break;
				case 2:
					changeBmp(mBmps[0],mBmps[1],mBmps[2]);
					break;
				case 3:
					changeBmp(mBmps[0],mBmps[1],mBmps[2],mBmps[3]);
					break;
				}
				
				preCos = cos;
				preLength = length;
				invalidate();
		   }
		}

		
		return true;
	}
	private void changeBmp(Bmp...bmps) {
		for(Bmp bmp : bmps){
			bmp.matrix.postScale(length/preLength, length/preLength,centerX,centerY);
			bmp.matrix.postRotate(cos-preCos,centerX,centerY);
		}
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
	 * ��ʼ�������������飬mBmps��Ŀ��������´��������������ø÷���
	 */
	protected void intAllBmps() {
		for (int i = 0; i < 2; i++) {
			if (mBmps[i] != null) {
				// ��������ߴ�
				mBmps[i].width = mBmps[i].getPic().getWidth();
				mBmps[i].height = mBmps[i].getPic().getHeight();
				// �������˳��ΪͼƬ����Ψһ��ʾ
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
	
	
	/** ============�ڲ��㷨���������===========start======= */
	
	
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
				// ��ȡ�ǶȺͳ���
				length = spacing(event);
				cos = rotation(event);

				// ��ת
				if (Math.abs(cos) > 3 && Math.abs(cos) < 177
						&& Math.abs(cos - preCos) < 15) {
					// ��ת
					bmp.matrix.postRotate(cos - preCos);
					// ����Ƕ�
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



	// ���㳤��
	@SuppressLint("FloatMath")
	protected float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	// ȡ��ת�Ƕ�
	protected float rotation(MotionEvent event) {
		double delta_x = (event.getX(0) - event.getX(1));
		double delta_y = (event.getY(0) - event.getY(1));
		double radians = Math.atan2(delta_y, delta_x);
		return (float) Math.toDegrees(radians);
	}
	private void setCenter(int current){
		float[] center=new float[2];
		switch (current) {
		case 0:
			center=getCenter(mBmps[0]);
			centerX=center[0];
			centerY=center[1];
			break;
		case 1:
			center=getCenter(mBmps[0],mBmps[1]);
			centerX=center[0];
			centerY=center[1];
			break;
		case 2:
			center=getCenter(mBmps[0],mBmps[1],mBmps[2]);
			centerX=center[0];
			centerY=center[1];
			break;
		case 3:
			center=getCenter(mBmps[0],mBmps[1],mBmps[2],mBmps[3]);
			centerX=center[0];
			centerY=center[1];

		default:
			break;
		}
	}
	
	

	/** ===============�ڲ��㷨��������� ===========end====================* */
	



	/**
	 * ÿ��ͼƬ������
	 * 
	 * @author Administrator
	 *
	 */
	public class Bmp {
		/**
		 * �����ƶ�����������תbitmap����
		 */
		protected Matrix matrix;

		// ����ǰ�ߴ�
		protected float width = 0;
		protected float height = 0;
		/**
		 * ͼƬ
		 */
		private Bitmap pic = null;

		/**
		 * ͼƬ������ʾ�㼶
		 */
		protected int priorityBase = 0;

		/**
		 * ͼƬ�����ڿؼ���X����
		 */
		private float preX = 0;
		/**
		 * ͼƬ�����ڿؼ���Y����
		 */
		private float preY = 0;

		/**
		 * bitmap��Ӧ�ĵ�ǰ��ʾ�㼶���index
		 */
		private int priority = 0;
		/**
		 * ͼƬ��ʶ
		 */
		private int imgId = 0;
		/**
		 * �Ƿ�ɵ���
		 */
		private boolean canChange;
		/**
		 * �Ƿ�ֻ��ƽ��
		 */
		private boolean onlyCanTranslation;

		/**
		 * �Ƿ�ѡ��
		 */
		private boolean isFocus;
		/**
		 * ����ͼƬ
		 */
		private Bitmap basePic;

		/**
		 * �Ƿ�����ѡ�У��ж�ѡ��״̬�Ƿ�֧�ִ���͸�����֣�
		 */
		private boolean isEasySelect;

		/**
		 * ͼƬ����ת�ĽǶ�
		 */
		private float rotateSize;

		// ������
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
		 *            : bitmap��Ӧ��index
		 * @param preX
		 *            ����
		 * @param preY
		 *            ����
		 * @param iscanChange
		 *            �Ƿ�ɱ�ѡ��
		 * @param isEasySelect
		 *            �Ƿ�ɵ���͸������
		 * @param isOnlyCanTranslation
		 *            �Ƿ�ֻ��ƽ��
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
		 * �Գ�ʼ������Bmp���¸�ֵ����֤��Ӱ��ԭ����ת�ĽǶ�
		 * 
		 * @param pic
		 * @param priority
		 *            bitmap��Ӧ��index
		 * @param preX
		 *            ����
		 * @param preY
		 *            ����
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
		 * ��ȡͼƬ����λ����������
		 * 
		 * @param i
		 *            1X���꣬2Y����
		 * @return ͼƬ����λ����������
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
		 * ����ͼƬ����λ��X����
		 * 
		 * @param x
		 */
		protected void setPreX(float x) {
			this.preX = x;
		}

		/**
		 * ����ͼƬ����λ��Y����
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
		 * ��ȡ����ǰ���
		 * 
		 * @return
		 */
		protected float getWidth() {
			return width;
		}

		/**
		 * ��ȡ����ǰ�߶�
		 * 
		 * @return
		 */
		protected float getHeight() {
			return height;
		}

		/**
		 * ��ȡͼƬΨһ��ʾ����0��ʼ
		 * 
		 * @return
		 */
		protected int getImgId() {
			return imgId;
		}

		/**
		 * ����Ψһ��ʾ
		 * 
		 * @param imgId
		 */
		protected void setImgId(int imgId) {
			this.imgId = imgId;
		}

		/**
		 * ��ȡ�Ƿ�ɵ���
		 * 
		 * @return
		 */
		protected boolean isCanChange() {
			return canChange;
		}

		/**
		 * �Ƿ�ɵ���
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
		 * ���ӹ�Ȧ
		 */
		protected void addHighLight() {

			/*
			 * ����ͼƬ��Եģʽ��������������δ��� Paint p = new Paint();
			 * p.setColor(Color.BLUE);// ��ɫ�Ĺ��� p.setStyle(Paint.Style.STROKE);//
			 * ���ÿ��� p.setStrokeWidth(40f);
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
			RectF rec = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());// ���߿�

			Paint paint = new Paint(); // ���ñ߿���ɫ
			paint.setColor(Color.WHITE);
			paint.setStyle(Paint.Style.STROKE); // ���ñ߿���
			paint.setStrokeWidth(3f);

			tempcanvas.drawBitmap(b, 0, 0, null);
			tempcanvas.drawRoundRect(rec, 20f, 20f, paint);
			this.pic = bitmap;
		}

		/**
		 * ȡ����Ȧ
		 */
		protected void cancelHighLight() {
			if (this.width > 0 && this.height > 0) {
				this.pic = null;
				this.pic = Bitmap.createScaledBitmap(this.basePic,
						(int) this.width, (int) this.height, false);
			}
		}

		/**
		 * �Ƿ�ӵ�н���
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
		 * �Ƿ�ɵ���͸������
		 * 
		 * @return
		 */
		protected boolean isEasySelect() {
			return isEasySelect;
		}

//		/**
//		 * ���μ��ص�ͼƬ��ʼ��λ�ã���֤������Ϊ����ת
//		 */
//		protected void intBitmap() {
//			this.matrix.preTranslate(this.preX - this.width / 2, this.preY
//					- this.height / 2);
//		}

		/**
		 * �Ƿ�ֻ��ƽ��
		 * 
		 * @return
		 */
		protected boolean isOnlyCanTranslation() {
			return onlyCanTranslation;
		}

		/**
		 * ��ȡ�Ѿ���ת�ĽǶ�
		 * 
		 * @return
		 */
		protected float getRotateSize() {
			return rotateSize;
		}

		/**
		 * �����Ѿ���ת�ĽǶ�
		 * 
		 * @param rotateSize
		 */
		protected void setRotateSize(float rotateSize) {
			this.rotateSize = rotateSize;
		}

		/**
		 * ��ȡ��Ӧ�ľ�����
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
		 * �ͷ�ͼƬ�ڴ�
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
	 * ����������ʾ˳�򣬵���Ǹ�ͼƬ���Ǹ�ͼƬ��ʾ������
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
	 * ����Ϊi��ͼƬ�Ƿ񱻵����
	 * 
	 * @param i
	 * @return
	 */
	private boolean isPoint(Bmp bmp, float X, float Y) {
		boolean result = false;
		float[] point=new float[]{bmp.getPic().getWidth()/2,bmp.getPic().getHeight()/2};
		bmp.matrix.mapPoints(point);
		System.out.println(X+"...."+Y);
		System.out.println(point[0]+"...."+point[1]);
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
	private float[] getCenter(Bmp bmp){
		float[] values=new float[9];
		bmp.matrix.getValues(values);
		float[] point=new float[]{bmp.getPic().getWidth()/2,bmp.getPic().getHeight()/2};
		bmp.matrix.mapPoints(point);
		System.out.println(point[0]+"....."+point[1]);
		return point;
	}
	private float[] getCenter(Bmp... bmps){
		List<float[]> apexs = new ArrayList<float[]>();
		for(Bmp bmp :bmps){
			float[][] rect = getRect(bmp);
			// ��ÿ��������ڼ�����
			for (float[] f : rect) {
				apexs.add(f);
			}
		}
		// �ҳ����е�������Χ�ĵ�
		float minX = apexs.get(0)[0], minY = apexs.get(0)[1], maxX = 0, maxY = 0;
		for (float[] apex : apexs) {
			minX = minX < apex[0] ? minX : apex[0];
			minY = minY < apex[1] ? minY : apex[1];
			maxX = maxX < apex[0] ? apex[0] : maxX;
			maxY = maxY < apex[1] ? apex[1] : maxY;
		}
		return new float[]{(maxX + minX)/2,(maxY + minY)/2};
	}
	
	private float[][] getRect(Bmp bmp){
		if(bmp!=null){
			float[] f = new float[9];
			bmp.matrix.getValues(f);
			float[][] rect=new float[4][2];
			rect[0][0] = f[0] * 0 + f[1] * 0 + f[2];
			rect[0][1] = f[3] * 0 + f[4] * 0 + f[5];
			rect[1][0] = f[0] * bmp.getPic().getWidth() + f[1] * 0 + f[2];
			rect[1][1] = f[3] * bmp.getPic().getWidth() + f[4] * 0 + f[5];
			rect[2][0] = f[0] * 0 + f[1] * bmp.getPic().getHeight() + f[2];
			rect[2][1] = f[3] * 0 + f[4] * bmp.getPic().getHeight() + f[5];
			rect[3][0] = f[0] * bmp.getPic().getWidth() + f[1] * bmp.getPic().getHeight() + f[2];
			rect[3][1] = f[3] * bmp.getPic().getWidth() + f[4] * bmp.getPic().getHeight() + f[5];
			return rect;
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
}
