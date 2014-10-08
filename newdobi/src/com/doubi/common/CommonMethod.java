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
 * @author Administrator 共同方法
 */
public class CommonMethod {

	/**
	 * 屏幕密度
	 */
	private static float density = 0;

	/**
	 * 场景类型，0：单人扮演 ，1：多人扮演
	 */
	private static int sceneType = 0;

	/**
	 * 获取屏幕宽度
	 * 
	 * @param activity
	 * @return
	 */
	// 进度加载提示Dialog
	private static Dialog note;
	
	private static SharedPreferences mySharedPreferences;

	public static float GetDensity(Activity activity) {
		if (density == 0) {
			// 获取屏幕
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
	 * 获取场景类型，
	 * 
	 * @return 0：单人扮演 ，1：多人扮演,2:初次进入多人扮演
	 */
	public static int GetSingleOrMore() {
		return sceneType;
	}

	/**
	 * 设置场景类型
	 * 
	 * @param i
	 *            0：单人扮演 ，1：多人扮演
	 */
	public static void SetSingleOrMore(int i) {
		sceneType = i;
	}

	/**
	 * 获取剪切前图片宽高比例
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
	 * Dialog 是否正在显示
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
	 * 获取Sharepreference的值
	 * 
	 * @param context
	 * @param key
	 * @return 如果未储存值返回-1
	 * 
	 */
	public static int GetSharepreferenceValue(Context context,
			ConstValue.SharepreferenceKey key) {
		if (mySharedPreferences == null) {
			mySharedPreferences = context.getSharedPreferences(ConstValue.DOBI,
					Context.MODE_PRIVATE); // 私有数据
		}

		int result = mySharedPreferences.getInt(key.toString(), -1);

		return result;

	}

	/**
	 * 设置Sharepreference的值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void SetSharepreferenceValue(Context context,
			ConstValue.SharepreferenceKey key, int value) {
		if (mySharedPreferences == null) {
			mySharedPreferences = context.getSharedPreferences(ConstValue.DOBI,
					Context.MODE_PRIVATE); // 私有数据
		}
		Editor editor = mySharedPreferences.edit();// 获取编辑器
		editor.putInt(key.toString(), value);
		editor.commit();
	}

}
