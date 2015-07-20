package org.example.myapp.client.view;
import org.example.myapp.R;
import org.example.myapp.client.model.Post;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class QuestionPubActivity extends Activity {

	private ImageView mBack;
	private EditText mTitle;
	private EditText mContent;
	private Spinner mCatalog;
	private CheckBox mEmail;
	private Button mPublish;
    private ProgressDialog mProgress;
	private Post post;
	private InputMethodManager imm;
	
	private int currCate = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.question_pub);
		
		this.initView();
		
	}
	
    //鍒濆鍖栬鍥炬帶浠�
    private void initView()
    {    	
    	imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
    	
    	mBack = (ImageView)findViewById(R.id.question_pub_back);
    	mPublish = (Button)findViewById(R.id.question_pub_publish);
    	mTitle = (EditText)findViewById(R.id.question_pub_title);
    	mContent = (EditText)findViewById(R.id.question_pub_content);
    	mCatalog = (Spinner)findViewById(R.id.question_pub_catalog);
    	
    	mBack.setOnClickListener( new View.OnClickListener() {
    		public void onClick(View v) {
    			finish();
    		}});
    		
    	mPublish.setOnClickListener(publishClickListener);
    	mCatalog.setOnItemSelectedListener(catalogSelectedListener);
    	//编辑器添加文本监听
    	//mTitle.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_TITLE));
    	//mContent.addTextChangedListener(UIHelper.getTextWatcher(this, AppConfig.TEMP_POST_CONTENT));
    	
   
    	//mCatalog.setSelection(StringUtils.toInt(position, 0));
    }
	
    private AdapterView.OnItemSelectedListener catalogSelectedListener = new AdapterView.OnItemSelectedListener(){
		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			//保存临时选择的分类
			currCate = position;
		}
		public void onNothingSelected(AdapterView<?> parent) {}
    };
    
	private View.OnClickListener publishClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			//隐藏软键盘
			imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
			
			String title = mTitle.getText().toString();
			if(StringUtils.isEmpty(title)){
				Toast.makeText(QuestionPubActivity.this, "请输入标题 ", Toast.LENGTH_SHORT).show();
				return;
			}
			String content = mContent.getText().toString();
			if(StringUtils.isEmpty(content)){
				Toast.makeText(QuestionPubActivity.this, "请输入提问内容 ", Toast.LENGTH_SHORT).show();
				return;
			}
						
			mProgress = ProgressDialog.show(v.getContext(), null, "发布中···",true,true); 
			
			post = new Post();
			post.setCname(Long.toString(MainActivity.myUser.getId()));
			post.setTitle(title);
			post.setContent(content);
		
			final Handler handler = new Handler(){
				public void handleMessage(Message msg) {
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1){
						ReturnObj res = (ReturnObj)msg.obj;
						if(res.getRet_code() == 0){
							Toast.makeText(QuestionPubActivity.this, "发布帖子成功 ", Toast.LENGTH_SHORT).show();

							//跳转到文章详情
							finish();
						} else {
							Toast.makeText(QuestionPubActivity.this, "发布帖子失败: " + res.getMsg(), Toast.LENGTH_SHORT).show();
							
						}
					}
					else {
						Toast.makeText(QuestionPubActivity.this, "发布帖子失败 ", Toast.LENGTH_SHORT).show();
					}
				}
			};
			new Thread()
			{
				public void run() 
				{
					Message msg = new Message();					
					try {
						ReturnObj ret = MainActivity.client_in_strict_mode.add_user_post(MainActivity.myUser.getId(),
								post.getTitle(), post.getContent(), 1);
						msg.what = 1;
						msg.obj = ret;
		            } catch (Exception e) {
		            	e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
		            }
					handler.sendMessage(msg);
				}
			}.start();
		}
	};
}
