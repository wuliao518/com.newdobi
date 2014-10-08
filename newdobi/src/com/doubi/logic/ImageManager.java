package com.doubi.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.MediaColumns;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.doubi.common.ConstValue;
import com.doubi.common.ImageLoader;
import com.doubi.logic.ImageFilter.IImageFilter;
import com.doubi.logic.ImageFilter.Image;
import com.doubi.logic.ImageFilter.SoftGlowFilter;
import com.doubi.logic.svgResolve.SVG;
import com.doubi.logic.svgResolve.SVGParser;

/**
 * ͼƬ��ȡ
 * 
 * @author Administrator
 *
 */
@SuppressLint({ "SdCardPath", "DefaultLocale" })
public class ImageManager {

	private String extJPG = ".jpg";
	private String extPNG = ".png";
	private ImageLoader mImageLoader;
	public ImageManager(){
		mImageLoader=new ImageLoader();
	}
	/**
	 * ���ص��˰�������ͼƬ
	 * 
	 * @return Bitmap
	 */
	public boolean loadImg() {
		String filepath = Environment.getExternalStorageDirectory()
				+ ConstValue.ROOT_PATH
				+ ConstValue.ImgName.singlePhotoClip.toString() + this.extJPG;
		File file = new File(filepath);

		return file.exists();
	}

	/**
	 * ���ض��˰�������ͼƬ
	 * 
	 * @return Bitmap
	 */
	public boolean loadImgForMore() {
		String filepath = Environment.getExternalStorageDirectory()
				+ ConstValue.ROOT_PATH + ConstValue.MORE_CLIP_FACE
				+ ConstValue.ImgName.morePhotoClip.toString() + "0"
				+ this.extJPG;
		File file = new File(filepath);

		return file.exists();
	}

	/**
	 * ����·����ȡ��չ��Ϊpng��jpg��Bitmap
	 * 
	 * @param file
	 * @return
	 */
	public Bitmap getBitmapFromPath(String path) {
		File file = new File(path);
		Bitmap bm = null;
		if (file != null && file.exists()) {
			bm = getBitmapFromFile(file, 0);
		}
		return bm;
	}

	/**
	 * ����File ��ȡ��չ��Ϊpng��jpg��Bitmap
	 * 
	 * @param file
	 * @return
	 */
	public Bitmap getBitmapFromFile(File file) {
		Bitmap bm = getBitmapFromFile(file, 0);
		return bm;
	}

	/**
	 * ����File ��ȡ��չ��Ϊpng��jpg��Bitmap
	 * ���ӻ���
	 * 
	 * @param file
	 * @param maxWidth
	 *            ����ȣ�Ϊ0��������
	 * @return
	 */
	public Bitmap getBitmapFromFile(File file, int maxWidth) {
		Bitmap bm = mImageLoader.getBitmapFromMemoryCache(file.getAbsolutePath());
		if(bm!=null){
			return bm;
		}
		if (file.exists()) {
			String ext = "." + getExtensionName(file.getPath());
			if (ext.equals(this.extPNG) || ext.equals(this.extJPG)) {
				try {
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;// ֻȡ�ߴ�
					bm = BitmapFactory.decodeFile(file.getPath(), options);
					int newWidth = 1000;
					if (maxWidth != 0) {
						maxWidth = maxWidth > options.outWidth ? options.outWidth
								: maxWidth;
						newWidth = maxWidth;
					} else {
						newWidth = newWidth > options.outWidth ? options.outWidth
								: newWidth;
					}

					options.inJustDecodeBounds = false;
					options.inPurgeable = true;// ʹͼƬ������
					options.inInputShareable = true;// ��ȡ��Դ

					if (newWidth < options.outWidth) {
						int scale = options.outWidth / newWidth;
						options.inJustDecodeBounds = false;
						options.inSampleSize = scale;
					}

					bm = BitmapFactory.decodeFile(file.getPath(), options);
					//���뻺��
					mImageLoader.addBitmapToMemoryCache(file.getAbsolutePath(), bm);

				} catch (Throwable ex) {
					// TODO �ڴ������ʾ
					Log.v("ImageManager", "ͼƬ����ʧ�ܣ�" + file.getPath());
					// mToast.show();
				}
			}
		}

		return bm;
	}

	/**
	 * �ļ����� ��ȡ�ļ���չ��
	 * 
	 * @param filename
	 * @return
	 */
	public String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * �ļ����� ��ȡ������չ�����ļ���
	 * 
	 * @param filename
	 * @return
	 */
	public String getFileNameNoEx(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length()))) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * ������������Ƭ�����SD����
	 * 
	 * @param data
	 * @throws IOException
	 */
	public void saveToSDCard(byte[] data) throws IOException {
		FileOutputStream outputStream = creatFile(
				ConstValue.ImgName.photo.toString(), this.extJPG, "");
		outputStream.write(data); // д��sd����
		outputStream.close(); // �ر������
	}

	/**
	 * ����ͼƬ��ĳ��Ŀ¼
	 * 
	 * @param path
	 *            dobi��Ŀ¼�µ���Ŀ¼�������ڸ�Ŀ¼��������
	 * @param bitmap
	 *            Ҫ�����ͼƬ
	 * @param imgName
	 *            ͼƬ����
	 * @param format
	 *            ͼƬ����
	 * @throws IOException
	 */
	public void saveToSDCard(String path, Bitmap bitmap, String imgName,
			Bitmap.CompressFormat format) throws IOException {
		FileOutputStream outputStream = null;
		if (format.equals(Bitmap.CompressFormat.JPEG))
			outputStream = creatFile(imgName, this.extJPG, path);
		else if (format.equals(Bitmap.CompressFormat.PNG))
			outputStream = creatFile(imgName, this.extPNG, path);
		bitmap.compress(format, 100, outputStream);
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * ���ĺõ�ͼƬ���浽�������
	 */

	public Uri saveToAlbum(Activity singleActivity, Bitmap bitmap)
			throws IOException {
		ContentValues values = new ContentValues(8);
		String newname = DateFormat.format("doubi-" + "yyyy-MM-dd kk.mm.ss",
				System.currentTimeMillis()).toString();
		values.put(MediaStore.Images.Media.TITLE, newname);// ����
		values.put(MediaStore.Images.Media.DISPLAY_NAME, newname);
		values.put(MediaStore.Images.Media.DESCRIPTION, "");// ����
		values.put(MediaStore.Images.Media.DATE_TAKEN,
				System.currentTimeMillis());// ͼ�������ʱ�䣬��ʾʱ�����������
		values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");// Ĭ��Ϊjpg��ʽ
		values.put(MediaStore.Images.Media.ORIENTATION, 0);//

		final String CAMERA_IMAGE_BUCKET_NAME = "/sdcard/dcim/camera";
		final String CAMERA_IMAGE_BUCKET_ID = String
				.valueOf(CAMERA_IMAGE_BUCKET_NAME.hashCode());
		File parentFile = new File(CAMERA_IMAGE_BUCKET_NAME);
		String name = parentFile.getName().toLowerCase();

		values.put(Images.ImageColumns.BUCKET_ID, CAMERA_IMAGE_BUCKET_ID);// id
		values.put(Images.ImageColumns.BUCKET_DISPLAY_NAME, name);

		// �ȵõ��µ�URI
		Uri uri = singleActivity.getContentResolver().insert(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
		String path=getFilePathByContentResolver(singleActivity,uri);
		Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		Uri newUri=Uri.fromFile(new File(path));
		intent.setData(newUri);     
		singleActivity.sendBroadcast(intent);  
		try {
			// д������
			OutputStream outStream = singleActivity.getContentResolver()
					.openOutputStream(uri);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
			outStream.close();
			bitmap.recycle();
		} catch (Exception e) {
			// Log.e(TAG, "exception while writing image", e);
		}

		return uri;
	}

	/**
	 * ��������λ���ļ���
	 * 
	 * @param imgName
	 *            ö���е��ļ���
	 * @param exp
	 *            ��չ��
	 * @param childFoder
	 *            ��Ŀ¼֮�����Ŀ¼
	 * @return
	 * @throws FileNotFoundException
	 */
	private FileOutputStream creatFile(String imgName, String exp,
			String childFoder) throws FileNotFoundException {
		String filename = imgName.toString();
		filename += exp;
		File fileFolder = new File(Environment.getExternalStorageDirectory()
				+ ConstValue.ROOT_PATH + childFoder);
		File jpgFile = new File(fileFolder, filename);
		FileOutputStream outputStream;

		outputStream = new FileOutputStream(jpgFile);
		// �ļ������

		return outputStream;
	}

	/**
	 * �Կ�Ϊ��׼���ͬ��������
	 * 
	 * @param bitMap
	 * @param xSize
	 *            ��ͼƬ���
	 * @return
	 */
	public Bitmap getNewSizeMap(Bitmap bitMap, int xSize) {
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		if (xSize > 0) {
			int newHeight, newWidth;

			// ������Ҫ�Ĵ�С
			newWidth = xSize;
			newHeight = newWidth * height / width;

			// �������ű���
			float scaleWidth = ((float) newWidth) / width;
			float scaleHeight = ((float) newHeight) / height;

			// ȡ����Ҫ���ŵ�matrix����
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			// �õ��µ�ͼƬ
			bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix,
					true);

		}
		return bitMap;
	}

	/**
	 * ��ȡ�ļ������ļ����ļ�������
	 * 
	 * @param filepath
	 *            �ļ���·��
	 * @return
	 */
	public int GetFileCount(String filepath) {
		File directory = new File(filepath);
		File files[] = directory.listFiles();
		return files.length;
	}

	/**
	 * ��ȡ�ļ�����ͼƬ����
	 * 
	 * @param filepath
	 *            �ļ���·��
	 * @return
	 */
	public List<Bitmap> GetAllBitmaps(String filepath) {
		List<Bitmap> mapList = new ArrayList<Bitmap>();

		File directory = new File(filepath);
		File files[] = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (!f.isDirectory()) {
					mapList.add(getBitmapFromFile(f));
				}
			}
		}

		return mapList;
	}

	

	/**
	 * ��ȡ�ļ�����ͼƬ����
	 * 
	 * @param filepath
	 *            �ļ���·��
	 * @return
	 */
	public List<SVG> GetAllSVGs(String filepath) {
		List<SVG> mapList = new ArrayList<SVG>();

		File directory = new File(filepath);
		File files[] = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (!f.isDirectory()) {
					InputStream mInputStream = null;
					try {
						mInputStream = new FileInputStream(f);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					SVG mSVG = SVGParser.getSVGFromResource(mInputStream);
					mapList.add(mSVG);
				}
			}
		}

		return mapList;
	}

	/**
	 * ��ȡ�ļ�����ͼƬ����
	 * 
	 * @param filepath
	 *            �ļ���·��
	 * @return
	 */
	public List<String> GetAllFilePaths(String filepath) {
		List<String> mapList = new ArrayList<String>();

		File directory = new File(filepath);
		File files[] = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (!f.isDirectory()) {
					mapList.add(f.getPath());
				}
			}
		}

		return mapList;
	}

	/**
	 * ��ȡ�ļ����ڵ��ļ����б�
	 * 
	 * @param filepath
	 * @return
	 */
	public List<String> getCurrentFoders(String filepath, int pageIndex,
			int pageSize) {
		List<String> strList = new ArrayList<String>();
		String tmpFilepath;
		for (int i = 0; i < pageSize; i++) {

			tmpFilepath = filepath + ((pageIndex - 1) * pageSize + 1 + i);
			File directory = new File(tmpFilepath);
			if (directory != null && directory.isDirectory()
					&& directory.exists()) {
				strList.add(directory.getPath());
			}
		}

		return strList;
	}

	/**
	 * ��ȡ�ļ����ڵ��ļ����б�
	 * 
	 * @param filepath
	 * @return
	 */
	public List<String> getAllFoders(String filepath) {
		File directory = new File(filepath);
		List<String> strList = new ArrayList<String>();
		File files[] = directory.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					strList.add(f.getPath());
				}
			}
		}

		return strList;
	}

	/**
	 * ����path�滭bitmap��ɫ����ͼ
	 * 
	 * @param path
	 * @param w
	 * @param h
	 * @return
	 */
	public Bitmap getBitmapFromPath(Path path, int w, int h) {
		Bitmap mBitmap = Bitmap.createBitmap(w, h, ConstValue.MY_CONFIG_4444);
		Canvas saveToPhoneCanvas = new Canvas(mBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.BLACK);// ���û�����ɫ
		mPaint.setStyle(Paint.Style.STROKE);// ���ÿ���
		mPaint.setStrokeWidth(3f);// �����ߵĴ�ϸ
		mPaint.setAntiAlias(true);// �������
		PathEffect effects = new DashPathEffect(new float[] { 15, 15, 15, 15 },
				1);// ��������
		mPaint.setPathEffect(effects);
		saveToPhoneCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));// �������
		saveToPhoneCanvas.drawPath(path, mPaint);
		return mBitmap;
	}

	/**
	 * ���δ�����
	 */
	public class RectangleManager {
		/**
		 * �ĸ���������[x,y]
		 */
		private float[] d1 = { 0, 0 }, d2 = { 0, 0 };
		private float[] d3 = { 0, 0 }, d4 = { 0, 0 };

		/**
		 * �����ĸ����������
		 */
		private float[][] apex = { d1, d2, d3, d4 };

		/**
		 * ���ĵ�X����
		 */
		private float preX;
		/**
		 * ���ĵ�Y����
		 */
		private float preY;
		/**
		 * ���ο��
		 */
		private float width;
		/**
		 * ���θ߶�
		 */
		private float height;
		/**
		 * ��ת�Ƕ�
		 */
		private float cos;

		/**
		 * ��ʼ��
		 * 
		 * @param mpreX
		 *            ���ĵ�X����
		 * @param mpreY
		 *            ���ĵ�Y����
		 * @param mwidth
		 *            ���ο��
		 * @param mheight
		 *            ���θ߶�
		 * @param mcos
		 *            ��ת�Ƕ�
		 */
		public RectangleManager(float mpreX, float mpreY, float mwidth,
				float mheight, float mcos) {
			preX = mpreX;
			preY = mpreY;
			width = mwidth;
			height = mheight;
			cos = mcos;

		}

		/**
		 * ��ȡ�ĸ�����[x,y]
		 * 
		 * @return
		 */
		public float[][] geTapex() {
			// ����x,y
			d1[0] = preX - width / 2;
			d1[1] = preY - height / 2;
			// ����x,y
			d2[0] = preX + width / 2;
			d2[1] = preY - height / 2;
			// ����x,y
			d3[0] = preX + width / 2;
			d3[1] = preY + height / 2;
			// ����x,y
			d4[0] = preX - width / 2;
			d4[1] = preY + height / 2;

			d1 = getRevolve(d1);
			d2 = getRevolve(d2);
			d3 = getRevolve(d3);
			d4 = getRevolve(d4);

			return apex;
		}

		/**
		 * ��ȡ��ת�������
		 * 
		 * @param d
		 * @return
		 */
		private float[] getRevolve(float[] d) {
			float[] n = { 0, 0 };
			if (cos > 0) {
				n[0] = (float) Math.sin(d[0]);
				n[1] = (float) Math.cos(d[1]);
			} else {
				n[0] = (float) Math.cos(d[0]);
				n[1] = (float) Math.sin(d[1]);
			}
			return n;
		}

		public float getPreX() {
			return preX;
		}

		public void setPreX(float preX) {
			this.preX = preX;
		}

		public float getPreY() {
			return preY;
		}

		public void setPreY(float preY) {
			this.preY = preY;
		}

		public float getWidth() {
			return width;
		}

		public void setWidth(float width) {
			this.width = width;
		}

		public float getHeight() {
			return height;
		}

		public void setHeight(float height) {
			this.height = height;
		}

		public float getCos() {
			return cos;
		}

		public void setCos(float cos) {
			this.cos = cos;
		}

	}

	/**
	 * ���������Σ��������о���λ�ô����ܽ����о��κ������е���ֱ�¾���
	 * 
	 * @param mRectangleManager
	 * @return
	 */
	public RectangleManager getNewRectangle(
			RectangleManager... mRectangleManagers) {
		RectangleManager result = null;

		List<float[]> apexs = new ArrayList<float[]>();
		// �����е���ڼ�����
		for (RectangleManager mRectangleManager : mRectangleManagers) {
			if (mRectangleManager != null) {
				// ��ȡ�ĸ�����
				float[][] apexF = mRectangleManager.geTapex();
				// ��ÿ��������ڼ�����
				for (float[] f : apexF) {
					apexs.add(f);
				}
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

		// ��������Χ�ĵ����괴������
		result = new RectangleManager((maxX + minX) / 2, (maxY + minY) / 2,
				maxX - minX, maxY - minY, 0);
		return result;
	}

	/**
	 * ������д��bitmap
	 * 
	 * @param mBitmap
	 * @param text
	 * @return
	 */
	public Bitmap setTextToBitmap(Bitmap mBitmap, String text) {
		String w = text;
		int height = mBitmap.getHeight();
		int width = mBitmap.getWidth();
		int len = text.getBytes().length;// ���ֳ���s
		int count = 0;// ����
		// ÿ�е��ֽ���
		int firstLen = 0;
		int secondLen = 0;
		int thirdLen = 0;
		// ÿ�е��ַ���
		int firstCount = 0;
		int secondCount = 0;
		int thirdCount = 0;
		// int line = ((width * 3) / 4) * 5 / height;// ÿ��������
		for (int i = 0; i < text.length(); i++) {
			// String word=text.substring(i,i+1);
			char word = text.charAt(i);
			if ((firstLen * height) / 10 < width) {
				count = 1;
				// int extent=word.getBytes().length;
				if ((word >= 0x0000 && word <= 0x00FF)) {
					if (word >= 'a' && word <= 'z' || word == '1') {
						firstLen++;
					} else {
						firstLen = firstLen + 2;
					}
				} else {
					firstLen = firstLen + 2;
				}
				firstCount++;
			} else if ((secondLen * height) / 10 < width) {
				count = 2;
				// int extent=word.getBytes().length;
				if ((word >= 0x0000 && word <= 0x00FF)) {
					if (word >= 'a' && word <= 'z' || word == '1') {
						secondLen++;
					} else {
						secondLen = secondLen + 2;
					}
				} else {
					secondLen = secondLen + 2;
				}
				secondCount++;
			} else if ((thirdLen * height) / 10 < width) {
				count = 3;
				// int extent=word.getBytes().length;
				if ((word >= 0x0000 && word <= 0x00FF)) {
					if (word >= 'a' && word <= 'z' || word == '1') {
						thirdLen++;
					} else {
						thirdLen = thirdLen + 2;
					}
				} else {
					thirdLen = thirdLen + 2;
				}
				thirdCount++;
			} else {
				count = 4;
				break;
			}
		}

		/*
		 * int line = 50;// (20 * width) / (4 * height) + 1; if (len > 0 && len
		 * <= line) { count = 1; } else if (len > line && len <= line * 2) {
		 * count = 2; } else if (len > line * 2 && len <= line * 3) { count = 3;
		 * } else if (len > line * 3) { count = 4; }
		 */
		String[] str = new String[3];

		// String a = text;
		// a = a + 1;

		Bitmap resultBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), ConstValue.MY_CONFIG_8888);

		Canvas cv = new Canvas(resultBitmap);
		cv.drawBitmap(mBitmap, 0, 0, null);
		Paint p = new Paint();
		String familyName = "sans";
		Typeface font = Typeface.create(familyName, Typeface.NORMAL);
		p.setColor(Color.BLACK);
		p.setStyle(Style.FILL); 
		p.setStrokeWidth(1.0f);
		p.setTextSize(height / 9);
		// ������д��ͼƬ
		switch (count) {
		case 1:
			str[0] = w.substring(0);
			cv.drawText(str[0], paddingLeft(width, height, firstLen),
					height * 1 / 2, p);
			break;
		case 2:
			str[0] = w.substring(0, firstCount);
			str[1] = w.substring(firstCount);
			cv.drawText(str[0], paddingLeft(width, height, firstLen),
					height * 4 / 9, p);
			cv.drawText(str[1], paddingLeft(width, height, secondLen),
					height * 5 / 9, p);
			break;
		case 3:
			str[0] = w.substring(0, firstCount);
			str[1] = w.substring(firstCount, secondCount + firstCount);
			str[2] = w.substring(secondCount + firstCount);
			cv.drawText(str[0], paddingLeft(width, height, firstLen),
					height * 3 / 9, p);
			cv.drawText(str[1], paddingLeft(width, height, secondLen),
					height * 1 / 2, p);
			cv.drawText(str[2], paddingLeft(width, height, thirdLen),
					height * 6 / 9, p);
			break;
		case 4:
			str[0] = w.substring(0, firstCount);
			str[1] = w.substring(firstCount, secondCount + firstCount);
			str[2] = w.substring(secondCount + firstCount + 2, secondCount
					+ firstCount + thirdCount)
					+ "...";
			cv.drawText(str[0], paddingLeft(width, height, firstLen),
					height * 3 / 9, p);
			cv.drawText(str[1], paddingLeft(width, height, secondLen),
					height * 1 / 2, p);
			cv.drawText(str[2], width / 5, height * 6 / 9, p);
			break;

		}
		// cv.drawText(text, width / 8, height / 2, p);

		return resultBitmap;
	}

	/**
	 * ���ֻ���ԭͼ��һ�������س�
	 * 
	 * @param mBitmap
	 * @param mSVG
	 * @return
	 */
	public Bitmap ClipBitmapOnSVG(Bitmap mBitmap, SVG mSVG) {
		int w = mBitmap.getWidth();
		Path mPath = mSVG.getPath();
		mBitmap = this.getNewSizeMap(mBitmap, mSVG.getPicture().getWidth());
		mBitmap = this.ClipBitmapOnPath(mBitmap, mPath);
		mBitmap = this.getNewSizeMap(mBitmap, w);
		return mBitmap;
	}

	/**
	 * ��ͼƬ����ĳ��·���иʣ�ಿ��Ϊ͸��
	 * 
	 * @param canvasBitmap
	 * @param mPath
	 * @return
	 */
	public Bitmap ClipBitmapOnPath(Bitmap canvasBitmap, Path mPath) {
		Bitmap roundConcerImage = null;
		// ����һ����ԭʼͼƬһ����Сλͼ
		roundConcerImage = Bitmap.createBitmap(canvasBitmap.getWidth(),
				canvasBitmap.getHeight(), ConstValue.MY_CONFIG_8888);
		// ��������λͼroundConcerImage�Ļ���
		Canvas mCanvas = new Canvas(roundConcerImage);
		mCanvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));// �������
		mCanvas.clipPath(mPath, Region.Op.REPLACE);// �����иʽ
		mCanvas.drawBitmap(canvasBitmap, 0, 0, null);

		// ѹ��ͼƬ
		int newWidth = ConstValue.FACE_SIZE[0];
		int newHeight = newWidth * roundConcerImage.getHeight()
				/ roundConcerImage.getWidth();
		roundConcerImage = Bitmap.createScaledBitmap(roundConcerImage,
				newWidth, newHeight, false);

		if (canvasBitmap != roundConcerImage) {
			canvasBitmap.recycle();
		}
		return roundConcerImage;
	}

	/**
	 * ��ȡͼƬ��5*5�������꼯��
	 * 
	 * @param bitmap
	 * @param count
	 *            ͷ������
	 * @return
	 */
	public List<int[]> getRed(Bitmap bitmap, int count) {
		List<int[]> list = new ArrayList<int[]>();
		int flag = 0;
		int red = 225, green = 50, blue = 50;
		for (int i = 0; i < bitmap.getWidth() - 5; i++) {
			for (int j = 0; j < bitmap.getHeight() - 5; j++) {
				int l = bitmap.getPixel(i, j);// ��ȡ���ص��ϵ�agrb
				int r = (l & 0x00ff0000) >> 16;// ȡ����λ(R)
				int g = (l & 0x0000ff00) >> 8; // (G)
				int b = (l & 0x000000ff);// ȡ����λ(B)
				if (r >= red && g < green && b < blue) {

					for (int m = i; m < i + 5; m++) {
						for (int n = j; n < j + 5; n++) {
							int l2 = bitmap.getPixel(m, n);
							int r2 = (l2 & 0x00ff0000) >> 16; // ȡ����
							int g2 = (l2 & 0x0000ff00) >> 8; // (G)
							int b2 = (l2 & 0x000000ff);// ȡ����λ(B)
							if (r2 >= red && g2 < green && b2 < blue) {
								flag++;
							} else {
								break;
							}
						}
					}
					if (flag == 25) {
						int k[] = { i + 2, j + 2 };
						list.add(k);
						if (list.size() == count) {
							break;
						}
						i = i + 10;
					}
					flag = 0;

				}
			}
			if (list.size() == count) {
				break;
			}
		}
		return list;
	}

	/**
	 * �ֵȼ�����ͼ�񣬹�������
	 * 
	 * @param mBitmap
	 * @param filterLevel
	 *            0:�� , 1:��, 2:ǿ, 3:����
	 * @param speed
	 *            ��Ⱦ�ٶȣ�1~4��������Խ���ٶ�Խ�죬����Խ��
	 * @return
	 */
	public Bitmap LoadBitmapFilter(Bitmap mBitmap, int filterLevel, float speed) {
		mBitmap = this.getNewSizeMap(mBitmap,
				(int) (mBitmap.getWidth() / speed));
		// ==============ɫ�ࡢ���Ͷȡ����ȴ���===============
		// f ��ʾ���ȱ�����ȡֵС��1����ʾ���ȼ���������������ǿ
		float mHueValue = 1;
		// saturation ���Ͷ�ֵ����С����Ϊ0����ʱ��Ӧ���ǻҶ�ͼ(Ҳ�����׻��ġ��ڰ�ͼ��)��
		// Ϊ1��ʾ���ͶȲ��䣬���ô���1������ʾ������
		float mSaturationValue = 1;
		// hueColor����ɫ����ת�ĽǶ�,��ֵ��ʾ˳ʱ����ת����ֵ��ʾ��ʱ����ת
		// �����൱�ڸı����ȫͼ��ɫ��
		float mLightnessValue = 0;

		// =============================�ҵ�����===================================

		switch (filterLevel) {
		case 0:
			mHueValue = 1.0f;
			mSaturationValue = 1.0f;
			mLightnessValue = 0f;
			break;
		case 1:
			mHueValue = 1.025f;
			mSaturationValue = 1.0f;
			mLightnessValue = 0f;
			break;
		case 2:
			mHueValue = 1.05f;
			mSaturationValue = 1.0f;
			mLightnessValue = 0f;
			break;
		case 3:
			mHueValue = 1.0f;
			mSaturationValue = 1.0f;
			mLightnessValue = 0f;
			break;

		}

		// ɫ��
		ColorMatrix mHueMatrix = new ColorMatrix();
		mHueMatrix.reset();
		mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);
		// ���Ͷ�
		ColorMatrix mSaturationMatrix = new ColorMatrix();
		mSaturationMatrix.reset();
		mSaturationMatrix.setSaturation(mSaturationValue);
		// ����
		ColorMatrix mLightnessMatrix = new ColorMatrix();
		mLightnessMatrix.reset(); // ��ΪĬ��ֵ
		mLightnessMatrix.setRotate(0, mLightnessValue); // �����ú�ɫ����ɫ������תhueColor��Ƕ�
		mLightnessMatrix.setRotate(1, mLightnessValue); // �������̺�ɫ����ɫ������תhueColor��Ƕ�
		mLightnessMatrix.setRotate(2, mLightnessValue); // ��������ɫ����ɫ������תhueColor��Ƕ�

		ColorMatrix mAllMatrix = new ColorMatrix();
		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix); // Ч������
		mAllMatrix.postConcat(mLightnessMatrix); // Ч������

		Paint paint = new Paint(); // �½�paint
		paint.setAntiAlias(true); // ���ÿ����,Ҳ���Ǳ�Ե��ƽ������
		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// ������ɫ�任Ч��
		Bitmap bmp = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), ConstValue.MY_CONFIG_8888);
		// ����һ����ͬ�ߴ�Ŀɱ��λͼ��,���ڻ��Ƶ�ɫ���ͼƬ
		Canvas canvas = new Canvas(bmp); // �õ����ʶ���
		canvas.drawBitmap(mBitmap, 0, 0, paint); // ����ɫ�仯���ͼƬ������´�����λͼ��
		// �����µ�λͼ��Ҳ����ɫ������ͼƬ
		mBitmap = bmp;

		// =============��������⴦��==��Ҫʱ��ϳ�===========
		IImageFilter filter = null;
		switch (filterLevel) {
		case 0:
			filter = new SoftGlowFilter(4, -0.6f, 0f);
			break;
		case 1:
			// filter = new SoftGlowFilter(6, -0.3f, 0.02f);
			filter = new SoftGlowFilter(3, -0.5f, 0f);
			break;
		case 2:
			// filter = new SoftGlowFilter(7, -0.2f, 0.05f);
			filter = new SoftGlowFilter(3, -0.4f, 0.02f);
			break;
		case 3:
			filter = new SoftGlowFilter(0, -1.0f, 0f);
			break;
		default:
			break;
		}
		Image img = null;
		try {
			img = new Image(mBitmap);
			if (filter != null) {
				img = filter.process(img);
				img.copyPixelsFromBuffer();
			}
			mBitmap = img.getImage();

		} catch (Exception e) {
			if (img != null && img.destImage.isRecycled()) {
				img.destImage.recycle();
				img.destImage = null;
				System.gc(); // ����ϵͳ��ʱ����
			}
		} finally {
			if (img != null && img.image.isRecycled()) {
				img.image.recycle();
				img.image = null;
				System.gc(); // ����ϵͳ��ʱ����
			}
		}

		// =============================================
		mBitmap = this.getNewSizeMap(mBitmap,
				(int) (mBitmap.getWidth() * speed));
		return mBitmap;
	}

	/**
	 * ��ȡͼƬ����ת�ĽǶ�
	 *
	 * @param path
	 *            ͼƬ����·��
	 * @return ͼƬ����ת�Ƕ�
	 */
	public int GetBitmapDegree(String path) {
		int degree = 0;
		try {
			// ��ָ��·���¶�ȡͼƬ������ȡ��EXIF��Ϣ
			ExifInterface exifInterface = new ExifInterface(path);
			// ��ȡͼƬ����ת��Ϣ
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * ��ͼƬ����ĳ���ǶȽ�����ת
	 *
	 * @param bm
	 *            ��Ҫ��ת��ͼƬ
	 * @param degree
	 *            ��ת�Ƕ�
	 * @return ��ת���ͼƬ
	 */
	public Bitmap getNewDegreeMap(Bitmap bm, int degree) {
		// ������ת�Ƕȣ�������ת����
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// ��ԭʼͼƬ������ת���������ת�����õ��µ�ͼƬ
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					matrix, true);
		} catch (OutOfMemoryError e) {
		}
		return bm;
	}

	/**
	 * byte ����ת��ΪBitmap
	 * 
	 * @param b
	 * @return
	 */
	public Bitmap BytesToBimap(byte[] b,Context context) {
		if (b.length != 0) {
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,options);
			int scale=1;
			float bitWidth=options.outWidth;
			float bitHeight=options.outHeight;
			WindowManager wm=(WindowManager) context.getSystemService("window");
			Display display=wm.getDefaultDisplay();
			float width=display.getWidth();
			float height=display.getHeight();
			float scaleX=(float)bitWidth/width;
			float scaleY=(float)bitHeight/height;
			scale=(int) Math.max(scaleX,scaleY);
			if(scale>1){
				options.inJustDecodeBounds=false;
				options.inSampleSize=scale;
				bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,options);
			}else{
				options.inJustDecodeBounds=false;
				options.inSampleSize=1;
				bitmap = BitmapFactory.decodeByteArray(b, 0, b.length,options);
			}
			return bitmap;
		} else {
			return null;
		}
	}

	/**
	 * ����λ����ʾλ��
	 * 
	 * @param widthͼƬ�Ŀ�
	 * @param heightͼƬ�ĸ�
	 * @param countÿ�е��ֽ���
	 * @return
	 */
	public float paddingLeft(int width, int height, int count) {
		float distance = 0f;
		distance = (float) ((width - (height / 16) * count) / 2);
		return distance;
	}
	private  String getFilePathByContentResolver(Context context, Uri uri) {  
        if (null == uri) {  
            return null;  
        }  
        Cursor c = context.getContentResolver().query(uri, null, null, null, null);  
        String filePath  = null;  
        if (null == c) {  
            throw new IllegalArgumentException(  
                    "Query on " + uri + " returns null result.");  
        }  
        try {  
            if ((c.getCount() != 1) || !c.moveToFirst()) {  
            } else {  
                filePath = c.getString(  
                        c.getColumnIndexOrThrow(MediaColumns.DATA));  
            }  
        } finally {  
            c.close();  
        }  
        return filePath;  
    } 
}
