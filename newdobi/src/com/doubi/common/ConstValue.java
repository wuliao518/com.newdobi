package com.doubi.common;

import android.annotation.SuppressLint;
import android.graphics.Bitmap.Config;

/**
 * 
 * @author Administrator ��ͬ����
 */
public class ConstValue {
	public static final String DOBI = "dobi";// ������Key
	@SuppressLint("SdCardPath")
	public static final String ROOT_PATH = "/doubi/";// ��Ŀ¼
	public static final String HAIR_PATH = "hair/"; // �����ļ���
	public static final String DEFAULT_HAIR_PATH = "defaulthair/"; // ��ױ����Ĭ�Ϸ����ļ���
	public static final String EYEBROWS_PATH = "eyebrows/"; // üë�ļ���
	public static final String BEARD_PATH = "beard/"; // �����ļ���
	public static final String BLUSHER_PATH = "blusher/";// ����
	public static final String FACE_PATH = "face/"; // �����ļ���
	public static final String CLOTHES_PATH = "clothes/"; // �·��ļ���
	public static final String DEFAULT_CLOTHES_PATH = "defaultclothes/"; // ��ױ����Ĭ���·��ļ���
	public static final String SCENE_PATH = "scene/"; // �����ļ���
	public static final String PROP_PATH = "prop/"; // �����ļ���

	public static final String NEWHAIR_PATH = "modern/";// �ִ������ļ���
	public static final String OLDHAIR_PATH = "ancient/";// �Ŵ������ļ���
	public static final String ARMORED_PATH = "armored/";// װ������ļ���
	public static final String OPERA_PATH = "opera/";// Ϸ�������ļ���
	public static final String GOLDEN_PATH = "golden/";// ���������ļ���
	public static final String YOUNGTH_PATH = "youngth/";// ��Ѫ�ഺ�ļ���

	public static final String BUBBLE_PATH = "bubble/";// ���������ļ���
	public static final String PET_PATH = "pet/"; // ����ϵ���ļ���
	public static final String PROPS_PATH = "props/"; // ����ϵ���ļ���

	public static final String WOODEN_PATH = "wooden/"; // ��ľ��ϵ���ļ���
	public static final String TRAVE_PATH = "trave/"; // �����ĺ��ļ���
	public static final String MOVE_PATH = "move/"; // �ƶ�������ļ���

	public static final String MORE_SCENE_PATH = "moreScene/"; // �����ļ���
	public static final String MORE_SCENE_DEFAULT = "default/";// ���˰��ݳ���Ĭ��

	public static final String CLOUD_PATH = "cloud/"; // ߳������ļ���
	public static final String HAPPY_PATH = "happy/"; // �ֲ�˼���ļ���
	public static final String FRIEND_PATH = "friend/"; // Ħ��ʱ���ļ���
	// public static final String OPERA_PATH = "opera/"; // ��ˮ�����ļ���
	public static final String WORLD_PATH = "world/"; // ������˫�ļ���
	public static final String THREE_PATH = "three/"; // �ݺ��ĺ��ļ���

	public static final String MORE_CLIP_FACE = "moerClipFace/";// ���˰��ݼ���������

	public static final String VERSION_URL = "http://download.d-bi.cn/Version.txt";
	// public static final String VERSION_URL =
	// "http://192.168.1.11:8081/Version.txt";

	/**
	 * ��ͼ�������ߴ� 0: ��1����. �Ϻ���ͼ��׼Ϊ95, 140.�����߲����ʣ����Ϊ��׼
	 */
	public static final int[] FACE_SIZE = new int[] { 400, 485 };
	/**
	 * �·�ͷ���ز�������Ϊ�óߴ�Ϊ��׼ 95
	 */
	public static final int FACE_BASE_WIDTH = 95;

	/**
	 * ���崫ֵ���õ�Key
	 * 
	 * @author Administrator
	 *
	 */
	public enum ExtruaKey {
		/**
		 * ��Ƭ����
		 */
		PhotoType,
		/**
		 * ���˰����У�����Ϊ��������������Ƭ
		 */
		MoreFaceIndex,
	}

	/**
	 * ͼ������
	 * 
	 * @author Administrator
	 *
	 */
	public enum ImgSourceType {
		/**
		 * ǰ�á�
		 */
		front,
		/**
		 * ���á�
		 */
		back,
		/**
		 * ѡ��
		 */
		select
	}

	/**
	 * ��Ҫ���浽sd����ͼƬ����
	 * 
	 * @author Administrator
	 *
	 */
	public enum ImgName {
		/**
		 * ���ձ���
		 */
		photo,
		/**
		 * ���պ��ͼ����
		 */
		singlePhotoClip, morePhotoClip,
		/**
		 * ȡ��ѡ��
		 */
		cancelSelect,
		/**
		 * ���շ����ͼƬ
		 */
		resultImg,
		/**
		 * ���˷���
		 */
		moreShareImg,

	}

	/**
	 * ���˰���װ�粽��
	 * 
	 * @author Administrator
	 *
	 */
	public enum Stage {
		/**
		 * �޲���
		 */
		None,
		/**
		 * ��ױ
		 */
		Face,
		/**
		 * ѡ����
		 */
		Hair,
		/**
		 * ѡ���·�
		 */
		Body,
		/**
		 * ѡ�񳡾�
		 */
		Scene,
		/**
		 * ѡ�����
		 */
		Prop
	}

	/**
	 * �½�Bitmap����
	 */
	public final static Config MY_CONFIG_4444 = Config.ARGB_4444;
	/**
	 * �½�Bitmap����
	 */
	public final static Config MY_CONFIG_8888 = Config.ARGB_8888;

	/**
	 * Sharepreference ��key�ļ���
	 * 
	 * @author Administrator
	 *
	 */
	public enum SharepreferenceKey {
		/**
		 * ǰ������ͷĬ����ת�Ƕ�
		 */
		CameraFrontDegree,
		/**
		 * ��������ͷĬ����ת�Ƕ�
		 */
		CameraBackDegree,
	}
}
