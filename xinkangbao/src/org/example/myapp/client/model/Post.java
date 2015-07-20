package org.example.myapp.client.model;

import java.io.Serializable;

import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 帖子实体类
 * 
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class Post implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int CATALOG_ASK = 1;
	public final static int CATALOG_SHARE = 2;
	public final static int CATALOG_OTHER = 3;
	public final static int CATALOG_JOB = 4;
	public final static int CATALOG_SITE = 5;

	private int topic_id; // 主键ID
	private int node_id; // 板块ID
	private int uid; // 创建者ID
	private int ruid; // 最近回复者ID
	private String title; // 标题
	private String keywords; // 标题
	private String content;
	private String addtime;
	private String updatetime;
	private String lastreply; // 最后回复时间

	private int views; // 浏览次数
	private int comments; // 评论次数
	private int favorites; // 收藏次数

	private int closecomment;
	private int is_top; // 评论次数
	private int is_hidden; // 是否隐藏
	private int ord; // 排序字段

	private String rname; // 最新回复人
	private String cname; // 板块的名字
	private String createName; // 创建人的名字

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public int getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(int topic_id) {
		this.topic_id = topic_id;
	}

	public int getNode_id() {
		return node_id;
	}

	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getRuid() {
		return ruid;
	}

	public void setRuid(int ruid) {
		this.ruid = ruid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAddtime() {
		return addtime;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getLastreply() {
		return lastreply;
	}

	public void setLastreply(String lastreply) {
		this.lastreply = lastreply;
	}

	public int getViews() {
		return views;
	}

	public void setViews(int views) {
		this.views = views;
	}

	public int getComments() {
		return comments;
	}

	public void setComments(int comments) {
		this.comments = comments;
	}

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}

	public int getClosecomment() {
		return closecomment;
	}

	public void setClosecomment(int closecomment) {
		this.closecomment = closecomment;
	}

	public int getIs_top() {
		return is_top;
	}

	public void setIs_top(int is_top) {
		this.is_top = is_top;
	}

	public int getIs_hidden() {
		return is_hidden;
	}

	public void setIs_hidden(int is_hidden) {
		this.is_hidden = is_hidden;
	}

	public int getOrd() {
		return ord;
	}

	public void setOrd(int ord) {
		this.ord = ord;
	}

	public String getRname() {
		return rname;
	}

	public void setRname(String rname) {
		this.rname = rname;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public static Post jiexi(JSONObject oj_tmp) {

		Post ret_obj = new Post();
		try {
			ret_obj.setTopic_id(Integer.parseInt(oj_tmp.getString("topic_id")));
			ret_obj.setNode_id(Integer.parseInt(oj_tmp.getString("node_id")));
			ret_obj.setUid(Integer.parseInt(oj_tmp.getString("uid")));
			if (!oj_tmp.isNull("ruid")) {
				ret_obj.setRuid(Integer.parseInt(oj_tmp.getString("ruid")));
			} else {
				ret_obj.setRuid(-1);
			}

			ret_obj.setTitle(oj_tmp.getString("title"));
			if (!oj_tmp.isNull("keywords")) {
				ret_obj.setKeywords(oj_tmp.getString("keywords"));
			} else {
				ret_obj.setKeywords("");
			}

			ret_obj.setContent(oj_tmp.getString("content"));

			ret_obj.setAddtime(oj_tmp.getString("addtime"));
			ret_obj.setUpdatetime(oj_tmp.getString("updatetime"));
			ret_obj.setLastreply(oj_tmp.getString("lastreply"));

			ret_obj.setViews(Integer.parseInt(oj_tmp.getString("views")));
			ret_obj.setComments(Integer.parseInt(oj_tmp.getString("comments")));
			ret_obj.setFavorites(Integer.parseInt(oj_tmp.getString("favorites")));

			ret_obj.setIs_top(Integer.parseInt(oj_tmp.getString("is_top")));
			ret_obj.setIs_hidden(Integer.parseInt(oj_tmp.getString("is_hidden")));
			ret_obj.setOrd(Integer.parseInt(oj_tmp.getString("ord")));

			if (!oj_tmp.isNull("rname")) {
				ret_obj.setRname(oj_tmp.getString("rname"));
			} else {
				ret_obj.setRname("");
			}
			// todo
			ret_obj.setCname(oj_tmp.getString("cname"));
			ret_obj.setCreateName(oj_tmp.getString("createname"));

		} catch (Exception e) {
			ret_obj = null;
		}
		return ret_obj;
	}

	public static Post jiexi_by_id(String detail) {
		int pos = detail.indexOf("<div style");
		if (pos != -1) {
			detail = detail.substring(0, pos);
		}

		JSONTokener jsonParser = new JSONTokener(detail);
		try {
			JSONObject ret = (JSONObject) (jsonParser.nextValue());
			JSONObject post_json_obj = ret.getJSONObject("content");
			Post ret_obj = Post.jiexi(post_json_obj);
			return ret_obj;
		} catch (Exception e) {
			return null;
		}
	}
}
