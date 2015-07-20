package org.example.myapp.client.model;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

/**
 * 鐠囧嫯顔戦崚妤勩�冪�圭偘缍嬬猾锟�
 * @author liux (http://my.oschina.net/liux)
 * @version 1.0
 * @created 2012-3-21
 */
public class CommentList {

	public final static int CATALOG_NEWS = 1;
	public final static int CATALOG_POST = 2;
	public final static int CATALOG_TWEET = 3;
	public final static int CATALOG_ACTIVE = 4;
	public final static int CATALOG_MESSAGE = 4;//閸斻劍锟戒椒绗岄悾娆掆枅闁棄鐫樻禍搴㈢Х閹垯鑵戣箛锟�
	
	private int pageSize;
	private int allCount;
	private List<Comment> commentlist = new ArrayList<Comment>();
	
	public int getPageSize() {
		return pageSize;
	}
	public int getAllCount() {
		return allCount;
	}
	public List<Comment> getCommentlist() {
		return commentlist;
	}

	public static CommentList parse(String str) throws IOException, Exception {
		CommentList commentList = new CommentList();
		Comment comment = null;
		try {
			JSONTokener jsonParser = new JSONTokener(str);
			JSONObject ret = (JSONObject)(jsonParser.nextValue());
			JSONArray doc_list = ret.getJSONArray("list");
			int length = doc_list.length();
			for(int i = 0; i < length; i++) {
				JSONObject oj_tmp = doc_list.getJSONObject(i);
				comment = Comment.jiexi(oj_tmp);

				if (comment != null) {
					commentList.getCommentlist().add(comment);
				}			
			}
		} catch (JSONException  e) {
			commentList.getCommentlist().clear();
		}
		return commentList;
	}
}
