package org.example.myapp.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.example.myapp.R;
import org.example.myapp.client.model.ArchivesBean;
import org.example.myapp.client.model.CommentList;
import org.example.myapp.client.model.EducationArticle;
import org.example.myapp.client.model.PostList;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppContext extends Application {

	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public static final int PAGE_SIZE = 20;// 姒涙顓婚崚鍡涖�夋径褍鐨�
	private static final int CACHE_TIME = 60 * 60000;// 缂傛挸鐡ㄦ径杈ㄦ櫏閺冨爼妫�

	private List<EducationArticle> articles;
	private ArchivesBean bean;
	private boolean login = false; // 閻ц缍嶉悩鑸碉拷锟�
	private int loginUid = 0; // 閻ц缍嶉悽銊﹀煕閻ㄥ埇d
	private Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
	private static AppContext instance;
	private String saveImagePath;// 娣囨繂鐡ㄩ崶鍓у鐠侯垰绶�

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		// 濞夈劌鍞紸pp瀵倸鐖跺畷鈺傜皾婢跺嫮鎮婇崳锟�
		Thread.setDefaultUncaughtExceptionHandler(AppException
				.getAppExceptionHandler());
		init();
		initImageLoaderConfiguration(AppContext.this);
	}

	public static AppContext getInstance() {
		return instance;
	}

	public ArchivesBean getBean() {
		return bean;
	}

	public void setBean(ArchivesBean bean) {
		this.bean = bean;
	}

	/**
	 * 閸掓繂顫愰崠锟�
	 */
	private void init() {
		// 鐠佸墽鐤嗘穱婵嗙摠閸ュ墽澧栭惃鍕熅瀵帮拷
		saveImagePath = getProperty(AppConfig.SAVE_IMAGE_PATH);
		if (StringUtils.isEmpty(saveImagePath)) {
			setProperty(AppConfig.SAVE_IMAGE_PATH,
					AppConfig.DEFAULT_SAVE_IMAGE_PATH);
			saveImagePath = AppConfig.DEFAULT_SAVE_IMAGE_PATH;
		}
	}

	/**
	 * 濡拷濞村缍嬮崜宥囬兇缂佺喎锛愰棅铏Ц閸氾缚璐熷锝呯埗濡�崇础
	 * 
	 * @return
	 */
	public boolean isAudioNormal() {
		AudioManager mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		return mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	/**
	 * 鎼存梻鏁ょ粙瀣碍閺勵垰鎯侀崣鎴濆毉閹绘劗銇氶棅锟�
	 * 
	 * @return
	 */
	public boolean isAppSound() {
		return isAudioNormal() && isVoice();
	}

	/**
	 * 濡拷濞村缍夌紒婊勬Ц閸氾箑褰查悽锟�
	 * 
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 閼惧嘲褰囪ぐ鎾冲缂冩垹绮剁猾璇茬��
	 * 
	 * @return 0閿涙碍鐥呴張澶岀秹缂侊拷 1閿涙瓙IFI缂冩垹绮� 2閿涙瓙AP缂冩垹绮� 3閿涙瓊ET缂冩垹绮�
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!StringUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 閸掋倖鏌囪ぐ鎾冲閻楀牊婀伴弰顖氭儊閸忕厧顔愰惄顔界垼閻楀牊婀伴惃鍕煙濞夛拷
	 * 
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null)
			info = new PackageInfo();
		return info;
	}

	public String getAppId() {
		String uniqueID = getProperty(AppConfig.CONF_APP_UNIQUEID);
		if (StringUtils.isEmpty(uniqueID)) {
			uniqueID = UUID.randomUUID().toString();
			setProperty(AppConfig.CONF_APP_UNIQUEID, uniqueID);
		}
		return uniqueID;
	}

	/**
	 * 閻€劍鍩涢弰顖氭儊閻ц缍�
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return login;
	}

	/**
	 * 閼惧嘲褰囬惂璇茬秿閻€劍鍩沬d
	 * 
	 * @return
	 */
	public int getLoginUid() {
		return this.loginUid;
	}

	/**
	 * 閻€劍鍩涘▔銊╂敘
	 */
	public void Logout() {
		// ApiClient.cleanCookie();
		this.cleanCookie();
		this.login = false;
		this.loginUid = 0;
	}

	/**
	 * 娣囨繂鐡ㄩ悽銊﹀煕婢舵潙鍎�
	 * 
	 * @param fileName
	 * @param bitmap
	 */
	public void saveUserFace(String fileName, Bitmap bitmap) {
		try {
			ImageUtils.saveImage(this, fileName, bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 閼惧嘲褰囬悽銊﹀煕婢舵潙鍎�
	 * 
	 * @param key
	 * @return
	 * @throws AppException
	 */
	public Bitmap getUserFace(String key) throws AppException {
		FileInputStream fis = null;
		try {
			fis = openFileInput(key);
			return BitmapFactory.decodeStream(fis);
		} catch (Exception e) {
			throw AppException.run(e);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 閺勵垰鎯侀崝鐘烘祰閺勫墽銇氶弬鍥╃彿閸ュ墽澧�
	 * 
	 * @return
	 */
	public boolean isLoadImage() {
		String perf_loadimage = getProperty(AppConfig.CONF_LOAD_IMAGE);
		// 姒涙顓婚弰顖氬鏉炵晫娈�
		if (StringUtils.isEmpty(perf_loadimage))
			return true;
		else
			return StringUtils.toBool(perf_loadimage);
	}

	/**
	 * 鐠佸墽鐤嗛弰顖氭儊閸旂姾娴囬弬鍥╃彿閸ュ墽澧�
	 * 
	 * @param b
	 */
	public void setConfigLoadimage(boolean b) {
		setProperty(AppConfig.CONF_LOAD_IMAGE, String.valueOf(b));
	}

	/**
	 * 閺勵垰鎯侀崣鎴濆毉閹绘劗銇氶棅锟�
	 * 
	 * @return
	 */
	public boolean isVoice() {
		String perf_voice = getProperty(AppConfig.CONF_VOICE);
		// 姒涙顓婚弰顖氱磻閸氼垱褰佺粈鍝勶紣闂婏拷
		if (StringUtils.isEmpty(perf_voice))
			return true;
		else
			return StringUtils.toBool(perf_voice);
	}

	/**
	 * 鐠佸墽鐤嗛弰顖氭儊閸欐垵鍤幓鎰仛闂婏拷
	 * 
	 * @param b
	 */
	public void setConfigVoice(boolean b) {
		setProperty(AppConfig.CONF_VOICE, String.valueOf(b));
	}

	/**
	 * 閺勵垰鎯侀崥顖氬З濡拷閺屻儲娲块弬锟�
	 * 
	 * @return
	 */
	public boolean isCheckUp() {
		String perf_checkup = getProperty(AppConfig.CONF_CHECKUP);
		// 姒涙顓婚弰顖氱磻閸氾拷
		if (StringUtils.isEmpty(perf_checkup))
			return true;
		else
			return StringUtils.toBool(perf_checkup);
	}

	/**
	 * 鐠佸墽鐤嗛崥顖氬З濡拷閺屻儲娲块弬锟�
	 * 
	 * @param b
	 */
	public void setConfigCheckUp(boolean b) {
		setProperty(AppConfig.CONF_CHECKUP, String.valueOf(b));
	}

	/**
	 * 閺勵垰鎯佸锕�褰稿鎴濆З
	 * 
	 * @return
	 */
	public boolean isScroll() {
		String perf_scroll = getProperty(AppConfig.CONF_SCROLL);
		// 姒涙顓婚弰顖氬彠闂傤厼涔忛崣铏拨閸旓拷
		if (StringUtils.isEmpty(perf_scroll))
			return false;
		else
			return StringUtils.toBool(perf_scroll);
	}

	/**
	 * 鐠佸墽鐤嗛弰顖氭儊瀹革箑褰稿鎴濆З
	 * 
	 * @param b
	 */
	public void setConfigScroll(boolean b) {
		setProperty(AppConfig.CONF_SCROLL, String.valueOf(b));
	}

	public boolean isHttpsLogin() {
		String perf_httpslogin = getProperty(AppConfig.CONF_HTTPS_LOGIN);
		// 姒涙顓婚弰鐥焧tp
		if (StringUtils.isEmpty(perf_httpslogin))
			return false;
		else
			return StringUtils.toBool(perf_httpslogin);
	}

	/**
	 * 鐠佸墽鐤嗛弰顖涙Ц閸氼毊ttps閻ц缍�
	 * 
	 * @param b
	 */
	public void setConfigHttpsLogin(boolean b) {
		setProperty(AppConfig.CONF_HTTPS_LOGIN, String.valueOf(b));
	}

	/**
	 * 濞撳懘娅庢穱婵嗙摠閻ㄥ嫮绱︾�涳拷
	 */
	public void cleanCookie() {
		removeProperty(AppConfig.CONF_COOKIE);
	}

	/**
	 * 閸掋倖鏌囩紓鎾崇摠閺佺増宓侀弰顖氭儊閸欘垵顕�
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isReadDataCache(String cachefile) {
		return readObject(cachefile) != null;
	}

	/**
	 * 閸掋倖鏌囩紓鎾崇摠閺勵垰鎯佺�涙ê婀�
	 * 
	 * @param cachefile
	 * @return
	 */
	private boolean isExistDataCache(String cachefile) {
		boolean exist = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists())
			exist = true;
		return exist;
	}

	/**
	 * 閸掋倖鏌囩紓鎾崇摠閺勵垰鎯佹径杈ㄦ櫏
	 * 
	 * @param cachefile
	 * @return
	 */
	public boolean isCacheDataFailure(String cachefile) {
		boolean failure = false;
		File data = getFileStreamPath(cachefile);
		if (data.exists()
				&& (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
			failure = true;
		else if (!data.exists())
			failure = true;
		return failure;
	}

	/**
	 * 濞撳懘娅巃pp缂傛挸鐡�
	 */
	public void clearAppCache() {
		// 濞撳懘娅巜ebview缂傛挸鐡�
		File file = getCacheDir();
		if (file != null && file.exists() && file.isDirectory()) {
			for (File item : file.listFiles()) {
				item.delete();
			}
			file.delete();
		}
		deleteDatabase("webview.db");
		deleteDatabase("webview.db-shm");
		deleteDatabase("webview.db-wal");
		deleteDatabase("webviewCache.db");
		deleteDatabase("webviewCache.db-shm");
		deleteDatabase("webviewCache.db-wal");
		// 濞撳懘娅庨弫鐗堝祦缂傛挸鐡�
		clearCacheFolder(getFilesDir(), System.currentTimeMillis());
		clearCacheFolder(getCacheDir(), System.currentTimeMillis());
		// 2.2閻楀牊婀伴幍宥嗘箒鐏忓棗绨查悽銊х处鐎涙娴嗙粔璇插煂sd閸楋紕娈戦崝鐔诲厴
		if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
			clearCacheFolder(MethodsCompat.getExternalCacheDir(this),
					System.currentTimeMillis());
		}
		// 濞撳懘娅庣紓鏍帆閸ｃ劋绻氱�涙娈戞稉瀛樻閸愬懎顔�
		Properties props = getProperties();
		for (Object key : props.keySet()) {
			String _key = key.toString();
			if (_key.startsWith("temp"))
				removeProperty(_key);
		}
	}

	/**
	 * 濞撳懘娅庣紓鎾崇摠閻╊喖缍�
	 * 
	 * @param dir
	 *            閻╊喖缍�
	 * @param numDays
	 *            瑜版挸澧犵化鑽ょ埠閺冨爼妫�
	 * @return
	 */
	private int clearCacheFolder(File dir, long curTime) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, curTime);
					}
					if (child.lastModified() < curTime) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	/**
	 * 鐏忓棗顕挒鈥茬箽鐎涙ê鍩岄崘鍛摠缂傛挸鐡ㄦ稉锟�
	 * 
	 * @param key
	 * @param value
	 */
	public void setMemCache(String key, Object value) {
		memCacheRegion.put(key, value);
	}

	/**
	 * 娴犲骸鍞寸�涙绱︾�涙ü鑵戦懢宄板絿鐎电钖�
	 * 
	 * @param key
	 * @return
	 */
	public Object getMemCache(String key) {
		return memCacheRegion.get(key);
	}

	/**
	 * 娣囨繂鐡ㄧ壕浣烘磸缂傛挸鐡�
	 * 
	 * @param key
	 * @param value
	 * @throws IOException
	 */
	public void setDiskCache(String key, String value) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
			fos.write(value.getBytes());
			fos.flush();
		} finally {
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 閼惧嘲褰囩壕浣烘磸缂傛挸鐡ㄩ弫鐗堝祦
	 * 
	 * @param key
	 * @return
	 * @throws IOException
	 */
	public String getDiskCache(String key) throws IOException {
		FileInputStream fis = null;
		try {
			fis = openFileInput("cache_" + key + ".data");
			byte[] datas = new byte[fis.available()];
			fis.read(datas);
			return new String(datas);
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 娣囨繂鐡ㄧ�电钖�
	 * 
	 * @param ser
	 * @param file
	 * @throws IOException
	 */
	public boolean saveObject(Serializable ser, String file) {
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		try {
			fos = openFileOutput(file, MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(ser);
			oos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				oos.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 鐠囪褰囩�电钖�
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public Serializable readObject(String file) {
		if (!isExistDataCache(file))
			return null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = openFileInput(file);
			ois = new ObjectInputStream(fis);
			return (Serializable) ois.readObject();
		} catch (FileNotFoundException e) {
		} catch (Exception e) {
			e.printStackTrace();
			// 閸欏秴绨崚妤�瀵叉径杈Е - 閸掔娀娅庣紓鎾崇摠閺傚洣娆�
			if (e instanceof InvalidClassException) {
				File data = getFileStreamPath(file);
				data.delete();
			}
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return null;
	}

	public boolean containsProperty(String key) {
		Properties props = getProperties();
		return props.containsKey(key);
	}

	public void setProperties(Properties ps) {
		AppConfig.getAppConfig(this).set(ps);
	}

	public Properties getProperties() {
		return AppConfig.getAppConfig(this).get();
	}

	public void setProperty(String key, String value) {
		AppConfig.getAppConfig(this).set(key, value);
	}

	public String getProperty(String key) {
		return AppConfig.getAppConfig(this).get(key);
	}

	public void removeProperty(String... key) {
		AppConfig.getAppConfig(this).remove(key);
	}

	/**
	 * 文章当前显示的List
	 * */
	public List<EducationArticle> getArticles() {
		return articles;
	}

	public void setArticles(List<EducationArticle> articles) {
		this.articles = articles;
	}

	/**
	 * 閼惧嘲褰囬崘鍛摠娑擃厺绻氱�涙ê娴橀悧鍥╂畱鐠侯垰绶�
	 * 
	 * @return
	 */
	public String getSaveImagePath() {
		return saveImagePath;
	}

	/**
	 * 鐠佸墽鐤嗛崘鍛摠娑擃厺绻氱�涙ê娴橀悧鍥╂畱鐠侯垰绶�
	 * 
	 * @return
	 */
	public void setSaveImagePath(String saveImagePath) {
		this.saveImagePath = saveImagePath;
	}

	public ReturnObj pubComment(int _catalog, int _id, int _uid,
			String _content, int _isPostToMyZone) {
		// TODO Auto-generated method stub
		return null;
	}

	public CommentList getCommentList(int catalog, int id, int pageIndex,
			boolean isRefresh) {
		// TODO Auto-generated method stub
		return null;
	}

	public PostList getPostListByTag(String tag, int pageIndex,
			boolean isRefresh) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
	public void initImageLoaderConfiguration(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), "wolan/cache/images");// 缓存地址
		DisplayImageOptions displayImageOption = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.doctordefault)
				// 设置图片在下载期间显示的图片
				.showImageOnFail(R.drawable.doctordefault)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageForEmptyUri(R.drawable.doctordefault)
				// 设置图片Uri为空或是错误的时候显示的图片
				.delayBeforeLoading(0)
				// 加载延迟
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.memoryCacheExtraOptions(480, 800)
				// 即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)
				// 线程池内加载的数量 default 3
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// 1.将保存的时候的URI名称用MD5
				// 加密
				// 2.new
				// HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// 设置图片下载和显示的工作队列排序
				.discCacheFileCount(100)
				// 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))
				// 自定义缓存路径
				.defaultDisplayImageOptions(displayImageOption)
				// 设置默认的图片显示选项
				// connectTimeout(5s), readTimeout(30s)超时时间
				.imageDownloader(
						new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
				.writeDebugLogs() // Remove for release app
				.build();// 开始构建
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);// 全局初始化此配置
	}

}
