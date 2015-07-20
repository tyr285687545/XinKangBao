package com.example.myapp.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.example.myapp.client.model.VersionInfo;
import org.example.myapp.common.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SaxService extends DefaultHandler {

	private VersionInfo info;

	private List<VersionInfo> version_info;

	final int CODE = 1;

	final int NAME = 2;

	final int URL = 3;

	final int LOG = 4;

	int currentTag = 0;

	String current_Tag;

	public List<VersionInfo> getVersion(InputStream xmlStream) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser hanler = factory.newSAXParser();
		SaxService handler = new SaxService();
		hanler.parse(xmlStream, handler);
		return handler.getVersion();
	}

	public List<VersionInfo> getVersion() {
		return version_info;
	}

	@Override
	public void startDocument() throws SAXException {
		version_info = new ArrayList<VersionInfo>();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException 
			{
		
		if ("android".equals(qName)) {
			info = new VersionInfo();
		}
		current_Tag = qName;// 将正在解析的节点名称赋给preTag
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if ("android".equals(qName)) {
			version_info.add(info);
		}
		/**
		 * 当解析结束时置为空。
		 */
		current_Tag = null;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (current_Tag != null) {
			String content = new String(ch, start, length);
			if ("versionCode".equals(current_Tag)
					&& !StringUtils.isEmpty(content.trim())) {
				info.setVersionCode(content);
			} else if ("versionName".equals(current_Tag)
					&& !StringUtils.isEmpty(content.trim())) {
				info.setVersionName(content);
			} else if ("downloadUrl".equals(current_Tag)
					&& !StringUtils.isEmpty(content.trim())) {
				info.setDownloadUrl(content);
			} else if ("updateLog".equals(current_Tag)
					&& !StringUtils.isEmpty(content.trim())) {
				info.setUpdateLog(content);
			}
		}
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
}