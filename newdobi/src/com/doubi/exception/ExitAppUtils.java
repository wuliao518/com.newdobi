package com.doubi.exception;

import java.util.LinkedList;  
import java.util.List;  
  
import android.app.Activity;  
  
/** 
 * android�˳�����Ĺ����࣬ʹ�õ���ģʽ 
 * 1.��Activity��onCreate()�ķ����е���addActivity()������ӵ�mActivityList 
 * 2.�������Activity��onDestroy()�ķ����е���delActivity()��ɾ���Ѿ����ٵ�Activityʵ�� 
 * ����������mActivityList�������ж����ʵ����Ӱ������˳��ٶ� 
 * @author xiaanming 
 * 
 */  
public class ExitAppUtils {  
    /** 
     * ת��Activity������ 
     */  
    private List<Activity> mActivityList = new LinkedList<Activity>();  
    private static ExitAppUtils instance = new ExitAppUtils();  
      
    /** 
     * �����캯��˽�л� 
     */  
    private ExitAppUtils(){};  
      
    /** 
     * ��ȡExitAppUtils��ʵ������ֻ֤��һ��ExitAppUtilsʵ������ 
     * @return 
     */  
    public static ExitAppUtils getInstance(){  
        return instance;  
    }  
  
      
    /** 
     * ���Activityʵ����mActivityList�У���onCreate()�е��� 
     * @param activity 
     */  
    public void addActivity(Activity activity){  
        mActivityList.add(activity);  
    }  
      
    /** 
     * ��������ɾ�������Activityʵ������onDestroy()�е��� 
     * @param activity 
     */  
    public void delActivity(Activity activity){  
        mActivityList.remove(activity);  
    }  
      
      
    /** 
     * �˳�����ķ��� 
     */  
    public void exit(){  
        for(Activity activity : mActivityList){  
            activity.finish();  
        }  
          
        System.exit(0);  
    }  
      
  
}