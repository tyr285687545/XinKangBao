package org.example.myapp.client.view;

import java.util.List;
import org.example.myapp.R;
import org.example.myapp.client.model.Post;
import org.example.myapp.client.model.PostList;
import org.example.myapp.common.AppException;
import org.example.myapp.common.ReturnObj;
import org.example.myapp.common.AppContext;
import org.example.myapp.common.StringUtils;
import org.example.myapp.common.UIDefine;
import java.util.ArrayList;
import java.util.Date;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @version 1.0
 * @created 2012-8-27
 */
public class QuestionNewActivity extends Activity {

	private ImageView mHome;

	private PullToRefreshListView lvQuestion;
	private ListViewQuestionAdapter lvQuestionAdapter;
	private List<Post> lvQuestionData = new ArrayList<Post>();
	private View lvQuestion_footer;
	private TextView lvQuestion_foot_more;
	private ProgressBar lvQuestion_foot_progress;
	private Handler lvQuestionHandler;
	private int lvQuestionSumData;

	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;

	private String curTag;
	private AppContext appContext;// 全局Context
	private ImageView mHeadLogo;

	private TextView mHeadTitle;

	private ProgressBar mHeadProgress;
	private ImageButton mHeadPub_post;

	private ScrollLayout mScrollLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_question);
		// 初始化视图控件
		// initView();

		// 初始化控件数据
		// initData();

		this.initHeadView();
		// this.initBadgeView();

		this.initView();

		/*
		 * lvQuestionHandler = this.getLvHandler(lvQuestion, lvQuestionAdapter,
		 * lvQuestion_foot_more, lvQuestion_foot_progress,
		 * AppContext.PAGE_SIZE);
		 */
	}

	/**
	 * 初始化头部视图
	 */
	private void initHeadView() {
		mHeadLogo = (ImageView) findViewById(R.id.main_head_logo);
		mHeadTitle = (TextView) findViewById(R.id.main_head_title);
		mHeadProgress = (ProgressBar) findViewById(R.id.main_head_progress);
		mHeadPub_post = (ImageButton) findViewById(R.id.main_head_pub_post);
		mHeadLogo.setImageResource(R.drawable.frame_logo_post);
		mHeadPub_post.setVisibility(View.VISIBLE);

		mHeadPub_post.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// UIDefine.showQuestionPub(v.getContext());
				Context context = v.getContext();
				Intent intent = new Intent(context, QuestionPubActivity.class);
				context.startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
		// 读取左右滑动配置
		// mScrollLayout.setIsScroll(appContext.isScroll());
	}

	// 初始化视图控件
	private void initView() {

		lvQuestionAdapter = new ListViewQuestionAdapter(this, lvQuestionData,
				R.layout.question_listitem);
		lvQuestion_footer = getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		lvQuestion_foot_more = (TextView) lvQuestion_footer
				.findViewById(R.id.listview_foot_more);
		lvQuestion_foot_progress = (ProgressBar) lvQuestion_footer
				.findViewById(R.id.listview_foot_progress);
		lvQuestion = (PullToRefreshListView) findViewById(R.id.frame_listview_question);
		// lvQuestion.addFooterView(lvQuestion_footer);// 添加底部视图 必须在setAdapter前
		lvQuestion.setAdapter(lvQuestionAdapter);
		lvQuestion
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// 点击头部、底部栏无效
						if (position == 0 || view == lvQuestion_footer)
							return;

						Post post = null;
						// 判断是否是TextView
						if (view instanceof TextView) {
							post = (Post) view.getTag();
						} else {
							TextView tv = (TextView) view
									.findViewById(R.id.question_listitem_title);
							post = (Post) tv.getTag();
						}
						if (post == null)
							return;

						// 跳转到问答详情
						Intent intent = new Intent(QuestionNewActivity.this,
								QuestionDetailActivity.class);
						intent.putExtra("post_id", post.getTopic_id());
						/*
						 * intent.putExtra("addtime", post.getAddtime());
						 * intent.putExtra("title", post.getTitle());
						 * intent.putExtra("content", post.getContent());
						 * intent.putExtra("createName", post.getCreateName());
						 * intent.putExtra("views", post.getViews());
						 * intent.putExtra("comments", post.getComments());
						 */
						startActivity(intent);
					}
				});
		lvQuestion.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvQuestion.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (lvQuestionData.size() == 0)
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvQuestion_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvQuestion.getTag());
				if (scrollEnd && lvDataState == UIDefine.LISTVIEW_DATA_MORE) {
					lvQuestion.setTag(UIDefine.LISTVIEW_DATA_LOADING);
					lvQuestion_foot_more.setText(R.string.load_ing);
					lvQuestion_foot_progress.setVisibility(View.VISIBLE);
					// 褰撳墠pageIndex
					int pageIndex = lvQuestionSumData / AppContext.PAGE_SIZE;
					loadLvQuestionData(curTag, pageIndex, lvQuestionHandler,
							UIDefine.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvQuestion.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvQuestion
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
					public void onRefresh() {
						loadLvQuestionData(curTag, 0, lvQuestionHandler,
								UIDefine.LISTVIEW_ACTION_REFRESH);
					}
				});
	}

	// 初始化控件数据
	private void initData() {
		lvQuestionHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {

					headButtonSwitch(DATA_LOAD_COMPLETE);

					PostList list = (PostList) msg.obj;
					// Notice notice = list.getNotice();
					// 处理listview数据
					switch (msg.arg1) {
					case UIDefine.LISTVIEW_ACTION_INIT:
					case UIDefine.LISTVIEW_ACTION_REFRESH:
						lvQuestionSumData = msg.what;
						lvQuestionData.clear();// 鍏堟竻闄ゅ師鏈夋暟鎹�
						lvQuestionData.addAll(list.getPostlist());
						break;
					case UIDefine.LISTVIEW_ACTION_SCROLL:
						lvQuestionSumData += msg.what;
						if (lvQuestionData.size() > 0) {
							for (Post p1 : list.getPostlist()) {
								boolean b = false;
								for (Post p2 : lvQuestionData) {
									if (p1.getTopic_id() == p2.getTopic_id()
											&& p1.getUid() == p2.getUid()) {
										b = true;
										break;
									}
								}
								if (!b)
									lvQuestionData.add(p1);
							}
						} else {
							lvQuestionData.addAll(list.getPostlist());
						}
						break;
					}

					if (msg.what < 20) {
						lvQuestion.setTag(UIDefine.LISTVIEW_DATA_FULL);
						lvQuestionAdapter.notifyDataSetChanged();
						lvQuestion_foot_more.setText(R.string.load_full);
					} else if (msg.what == 20) {
						lvQuestion.setTag(UIDefine.LISTVIEW_DATA_MORE);
						lvQuestionAdapter.notifyDataSetChanged();
						lvQuestion_foot_more.setText(R.string.load_more);
					}
				} else if (msg.what == -1) {

					headButtonSwitch(DATA_LOAD_FAIL);

					// 有异常--显示加载出错 & 弹出错误消息
					lvQuestion.setTag(UIDefine.LISTVIEW_DATA_MORE);
					lvQuestion_foot_more.setText(R.string.load_error);
					((AppException) msg.obj)
							.makeToast(QuestionNewActivity.this);
				}
				if (lvQuestionData.size() == 0) {
					lvQuestion.setTag(UIDefine.LISTVIEW_DATA_EMPTY);
					lvQuestion_foot_more.setText(R.string.load_empty);
				}
				lvQuestion_foot_progress.setVisibility(View.GONE);
				if (msg.arg1 == UIDefine.LISTVIEW_ACTION_REFRESH) {
					lvQuestion
							.onRefreshComplete(getString(R.string.pull_to_refresh_update)
									+ new Date().toLocaleString());
					lvQuestion.setSelection(0);
				}
			}
		};
		this.loadLvQuestionData(curTag, 0, lvQuestionHandler,
				UIDefine.LISTVIEW_ACTION_INIT);
	}

	/**
	 * 线程加载问答列表数据
	 * 
	 * @param tag
	 *            当前Tag
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 *            处理器
	 * @param action
	 *            动作标识
	 */
	private void loadLvQuestionData(final String tag, final int pageIndex,
			final Handler handler, final int action) {

		headButtonSwitch(DATA_LOAD_ING);

		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIDefine.LISTVIEW_ACTION_REFRESH
						|| action == UIDefine.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				ReturnObj ret = MainActivity.client_in_strict_mode
						.get_post_list(MainActivity.myUser.getId());
				if (ret.getRet_code() == 0) {
					PostList list = PostList.parse(ret.getOrg_str());
					msg.what = list.getPageSize();
					msg.obj = list;
				}

				msg.arg1 = action;// 告知handler当前action
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 澶撮儴鍔犺浇鍔ㄧ敾灞曠ず
	 * 
	 * @param type
	 */
	private void headButtonSwitch(int type) {
		switch (type) {
		case DATA_LOAD_ING:
			mHeadProgress.setVisibility(View.VISIBLE);
			break;
		case DATA_LOAD_COMPLETE:
			mHeadProgress.setVisibility(View.GONE);
			break;
		case DATA_LOAD_FAIL:
			mHeadProgress.setVisibility(View.GONE);
			break;
		}
	}

	private View.OnClickListener homeClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// UIDefine.showHome(QuestionTag.this);
		}
	};
}
