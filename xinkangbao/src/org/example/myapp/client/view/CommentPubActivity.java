package org.example.myapp.client.view;
import org.example.myapp.R;
import org.example.myapp.client.model.CommentList;
import org.example.myapp.common.StringUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * ��������
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class CommentPubActivity extends Activity {

	public final static int CATALOG_NEWS = 1;
	public final static int CATALOG_POST = 2;
	public final static int CATALOG_TWEET = 3;
	public final static int CATALOG_ACTIVE = 4;
	public final static int CATALOG_MESSAGE = 4;//��̬�����Զ�������Ϣ����
	public final static int CATALOG_BLOG = 5;
	
	private ImageView mBack;
	private EditText mContent;
	private CheckBox mZone;
	private Button mPublish;
	private LinkView mQuote;
    private ProgressDialog mProgress;
	
	private int _catalog;
	private int _postId;
	private int _uid;
	private String _content;
	private int _isPostToMyZone;
	
	//-------�����ۻظ������2����------
	//private int _replyid;//���ظ��ĵ�������id
	//private int _authorid;//�����۵�ԭʼ����id
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_pub_activity);
		
		this.initView();
		
	}
	
	
	
    //��ʼ����ͼ�ؼ�
    private void initView()
    {

		_postId = getIntent().getIntExtra("postId", 0);
    	mBack = (ImageView)findViewById(R.id.comment_list_back);
    	mPublish = (Button)findViewById(R.id.comment_pub_publish);
    	mContent = (EditText)findViewById(R.id.comment_pub_content);
    	
    	mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CommentPubActivity.this.finish();
			}
    	});
    //	mPublish.setOnClickListener(publishClickListener);    	
    	
    	mQuote = (LinkView)findViewById(R.id.comment_pub_quote);
    //	mQuote.setText(UIHelper.parseQuoteSpan(getIntent().getStringExtra("author"),getIntent().getStringExtra("content")));
    	mQuote.parseLinkText();
    }
	/*
	private View.OnClickListener publishClickListener = new View.OnClickListener() {
		public void onClick(View v) {	
			_content = mContent.getText().toString();
			if(StringUtils.isEmpty(_content)){
				UIHelper.ToastMessage(v.getContext(), "��������������");
				return;
			}
				
			_uid = ac.getLoginUid();
			
	    	mProgress = ProgressDialog.show(v.getContext(), null, "�����С�����",true,true); 			
			
			final Handler handler = new Handler(){
				public void handleMessage(Message msg) {
					if(mProgress!=null)mProgress.dismiss();
					if(msg.what == 1){
						Result res = (Result)msg.obj;
						UIHelper.ToastMessage(CommentPubActivity.this, res.getErrorMessage());
						if(res.OK()){
							
							//���ظոշ��������
							Intent intent = new Intent();
							intent.putExtra("COMMENT_SERIALIZABLE", res.getComment());
							setResult(RESULT_OK, intent);
							//��ת����������
							finish();
						}
					}
					else {
						((AppException)msg.obj).makeToast(CommentPubActivity.this);
					}
				}
			};
			new Thread(){
				public void run() {
					Message msg = new Message();
					Result res = new Result();
					try {
						//��������
						if(_replyid == 0){
							res = ac.pubComment(_catalog, _id, _uid, _content, _isPostToMyZone);
						}
						//�����۽��лظ�
						else if(_replyid > 0){
							if(_catalog == CATALOG_BLOG)
								res = ac.replyBlogComment(_id, _uid, _content, _replyid, _authorid);
							else
								res = ac.replyComment(_id, _catalog, _replyid, _authorid, _uid, _content);
						}
						msg.what = 1;
						msg.obj = res;
		            } catch (AppException e) {
		            	e.printStackTrace();
						msg.what = -1;
						msg.obj = e;
		            }
					handler.sendMessage(msg);
				}
			}.start();
		}
	};*/
}
