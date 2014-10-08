package com.doubi.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 
 * @author Administrator ��ͬ����
 */
public class CommonMethod {

	/**
	 * ��Ļ�ܶ�
	 */
	private static float density = 0;

	/**
	 * �������ͣ�0�����˰��� ��1�����˰���
	 */
	private static int sceneType = 0;

	/**
	 * ��ȡ��Ļ���
	 * 
	 * @param activity
	 * @return
	 */
	// ���ȼ�����ʾDialog
	private static Dialog note;
	
	private static SharedPreferences mySharedPreferences;

	public static float GetDensity(Activity activity) {
		if (density == 0) {
			// ��ȡ��Ļ
			DisplayMetrics dm = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
			density = dm.density;
		}
		return density;
	}
	
	public static int getHeight(Activity activity){
		return activity.getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public static int getWidth(Activity activity){
		return activity.getWindowManager().getDefaultDisplay().getWidth();
	}
	/**
	 * ��ȡ�������ͣ�
	 * 
	 * @return 0�����˰��� ��1�����˰���,2:���ν�����˰���
	 */
	public static int GetSingleOrMore() {
		return sceneType;
	}

	/**
	 * ���ó�������
	 * 
	 * @param i
	 *            0�����˰��� ��1�����˰���
	 */
	public static void SetSingleOrMore(int i) {
		sceneType = i;
	}

	/**
	 * ��ȡ����ǰͼƬ��߱���
	 * 
	 * @return
	 */
	public static double GetFaceForClipScale() {
		return (double) 480 / (double) 601;
	}



	public static void CloseDialog() {
		if (note != null) {
			note.dismiss();
		}
	}

	/**
	 * Dialog �Ƿ�������ʾ
	 * 
	 * @return
	 */
	public static boolean IsDialogShowing() {
		if (note == null) {
			return false;
		} else {
			return note.isShowing();
		}
	}

	/**
	 * ��ȡSharepreference��ֵ
	 * 
	 * @param context
	 * @param key
	 * @return ���δ����ֵ����-1
	 * 
	 */
	public static int GetSharepreferenceValue(Context context,
			ConstValue.SharepreferenceKey key) {
		if (mySharedPreferences == null) {
			mySharedPreferences = context.getSharedPreferences(ConstValue.DOBI,
					Context.MODE_PRIVATE); // ˽������
		}

		int result = mySharedPreferences.getInt(key.toString(), -1);

		return result;

	}

	/**
	 * ����Sharepreference��ֵ
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void SetSharepreferenceValue(Context context,
			ConstValue.SharepreferenceKey key, int value) {
		if (mySharedPreferences == null) {
			mySharedPreferences = context.getSharedPreferences(ConstValue.DOBI,
					Context.MODE_PRIVATE); // ˽������
		}
		Editor editor = mySharedPreferences.edit();// ��ȡ�༭��
		editor.putInt(key.toString(), value);
		editor.commit();
	}

}
