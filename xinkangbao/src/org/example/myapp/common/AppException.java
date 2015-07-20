package org.example.myapp.common;
import org.apache.http.HttpException;
import org.example.myapp.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Looper;
import android.widget.Toast;

/**
 * 鎼存梻鏁ょ粙瀣碍瀵倸鐖剁猾浼欑窗閻€劋绨幑鏇″箯瀵倸鐖堕崪灞惧絹缁�娲晩鐠囶垯淇婇幁锟�
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class AppException extends Exception implements UncaughtExceptionHandler{

	private final static boolean Debug = false;//閺勵垰鎯佹穱婵嗙摠闁挎瑨顕ら弮銉ョ箶
	
	/** 鐎规矮绠熷鍌氱埗缁鐎� */
	public final static byte TYPE_NETWORK 	= 0x01;
	public final static byte TYPE_SOCKET	= 0x02;
	public final static byte TYPE_HTTP_CODE	= 0x03;
	public final static byte TYPE_HTTP_ERROR= 0x04;
	public final static byte TYPE_XML	 	= 0x05;
	public final static byte TYPE_IO	 	= 0x06;
	public final static byte TYPE_RUN	 	= 0x07;
	public final static byte TYPE_JSON	 	= 0x08;
	
	private byte type;
	private int code;
	
	/** 缁崵绮烘妯款吇閻ㄥ垊ncaughtException婢跺嫮鎮婄猾锟� */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	
	private AppException(){
		this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}
	
	private AppException(byte type, int code, Exception excp) {
		super(excp);
		this.type = type;
		this.code = code;		
		if(Debug){
			this.saveErrorLog(excp);
		}
	}
	public int getCode() {
		return this.code;
	}
	public int getType() {
		return this.type;
	}
	
	/**
	 * 閹绘劗銇氶崣瀣偨閻ㄥ嫰鏁婄拠顖欎繆閹拷
	 * @param ctx
	 */
	public void makeToast(Context ctx){
		switch(this.getType()){
		case TYPE_HTTP_CODE:
			String err = ctx.getString(R.string.http_status_code_error, this.getCode());
			Toast.makeText(ctx, err, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_HTTP_ERROR:
			Toast.makeText(ctx, R.string.http_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_SOCKET:
			Toast.makeText(ctx, R.string.socket_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_NETWORK:
			Toast.makeText(ctx, R.string.network_not_connected, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_XML:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_JSON:
			Toast.makeText(ctx, R.string.xml_parser_failed, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_IO:
			Toast.makeText(ctx, R.string.io_exception_error, Toast.LENGTH_SHORT).show();
			break;
		case TYPE_RUN:
			Toast.makeText(ctx, R.string.app_run_code_error, Toast.LENGTH_SHORT).show();
			break;
		}
	}
	
	/**
	 * 娣囨繂鐡ㄥ鍌氱埗閺冦儱绻�
	 * @param excp
	 */
	public void saveErrorLog(Exception excp) {
		String errorlog = "errorlog.txt";
		String savePath = "";
		String logFilePath = "";
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			//閸掋倖鏌囬弰顖氭儊閹稿倽娴囨禍鍝燚閸楋拷
			String storageState = Environment.getExternalStorageState();		
			if(storageState.equals(Environment.MEDIA_MOUNTED)){
				savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OSChina/Log/";
				File file = new File(savePath);
				if(!file.exists()){
					file.mkdirs();
				}
				logFilePath = savePath + errorlog;
			}
			//濞屸剝婀侀幐鍌濇祰SD閸椻槄绱濋弮鐘崇《閸愭瑦鏋冩禒锟�
			if(logFilePath == ""){
				return;
			}
			File logFile = new File(logFilePath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fw = new FileWriter(logFile,true);
			pw = new PrintWriter(fw);
			pw.println("--------------------"+(new Date().toLocaleString())+"---------------------");	
			excp.printStackTrace(pw);
			pw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();		
		}finally{ 
			if(pw != null){ pw.close(); } 
			if(fw != null){ try { fw.close(); } catch (IOException e) { }}
		}

	}
	
	public static AppException http(int code) {
		return new AppException(TYPE_HTTP_CODE, code, null);
	}
	
	public static AppException http(Exception e) {
		return new AppException(TYPE_HTTP_ERROR, 0 ,e);
	}

	public static AppException socket(Exception e) {
		return new AppException(TYPE_SOCKET, 0 ,e);
	}
	
	public static AppException io(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new AppException(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof IOException){
			return new AppException(TYPE_IO, 0 ,e);
		}
		return run(e);
	}
	
	public static AppException xml(Exception e) {
		return new AppException(TYPE_XML, 0, e);
	}
	
	public static AppException json(Exception e) {
		return new AppException(TYPE_JSON, 0, e);
	}
	
	public static AppException network(Exception e) {
		if(e instanceof UnknownHostException || e instanceof ConnectException){
			return new AppException(TYPE_NETWORK, 0, e);
		}
		else if(e instanceof HttpException){
			return http(e);
		}
		else if(e instanceof SocketException){
			return socket(e);
		}
		return http(e);
	}
	
	public static AppException run(Exception e) {
		return new AppException(TYPE_RUN, 0, e);
	}

	/**
	 * 閼惧嘲褰嘇PP瀵倸鐖跺畷鈺傜皾婢跺嫮鎮婄�电钖�
	 * @param context
	 * @return
	 */
	public static AppException getAppExceptionHandler(){
		return new AppException();
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if(!handleException(ex) && mDefaultHandler != null) {
			mDefaultHandler.uncaughtException(thread, ex);
		}

	}
	/**
	 * 閼奉亜鐣炬稊澶婄磽鐢顦╅悶锟�:閺�鍫曟肠闁挎瑨顕ゆ穱鈩冧紖&閸欐垿锟戒線鏁婄拠顖涘Г閸涳拷
	 * @param ex
	 * @return true:婢跺嫮鎮婃禍鍡氼嚉瀵倸鐖舵穱鈩冧紖;閸氾箑鍨潻鏂挎礀false
	 */
	private boolean handleException(Throwable ex) {
		if(ex == null) {
			return false;
		}
		
		final Context context = AppManager.getAppManager().currentActivity();
		
		if(context == null) {
			return false;
		}
		
		final String crashReport = getCrashReport(context, ex);
		//閺勫墽銇氬鍌氱埗娣団剝浼�&閸欐垿锟戒焦濮ら崨锟�
		new Thread() {
			public void run() {
				Looper.prepare();
				//UIHelper.sendAppCrashReport(context, crashReport);
				Looper.loop();
			}

		}.start();
		return true;
	}
	/**
	 * 閼惧嘲褰嘇PP瀹曗晜绨濆鍌氱埗閹躲儱鎲�
	 * @param ex
	 * @return
	 */
	private String getCrashReport(Context context, Throwable ex) {
		PackageInfo pinfo = ((AppContext)context.getApplicationContext()).getPackageInfo();
		StringBuffer exceptionStr = new StringBuffer();
		exceptionStr.append("Version: "+pinfo.versionName+"("+pinfo.versionCode+")\n");
		exceptionStr.append("Android: "+android.os.Build.VERSION.RELEASE+"("+android.os.Build.MODEL+")\n");
		exceptionStr.append("Exception: "+ex.getMessage()+"\n");
		StackTraceElement[] elements = ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			exceptionStr.append(elements[i].toString()+"\n");
		}
		return exceptionStr.toString();
	}
}
