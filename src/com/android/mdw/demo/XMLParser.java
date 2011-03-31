package com.android.mdw.demo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLParser {
	private URL url;
	
	public XMLParser(String url) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public LinkedList<com.android.mdw.demo.Element> parse() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		LinkedList<com.android.mdw.demo.Element> elements = new LinkedList<com.android.mdw.demo.Element>();
		com.android.mdw.demo.Element e;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(this.url.openConnection().getInputStream());
			Element root = dom.getDocumentElement();
			NodeList items = root.getElementsByTagName("item");
			for (int i=0;i<items.getLength();i++){
				e = new com.android.mdw.demo.Element();				
				Node item = items.item(i);
				NodeList properties = item.getChildNodes();
				for (int j=0;j<properties.getLength();j++){
					Node property = properties.item(j);
					String name = property.getNodeName();
					if (name.equalsIgnoreCase("title")){
						e.setTitle(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("link")){
						e.setLink(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("pubDate")){
						e.setDate(property.getFirstChild().getNodeValue());
					} else if (name.equalsIgnoreCase("dc:creator")){
						e.setAuthor(property.getFirstChild().getNodeValue());						
					} else if (name.equalsIgnoreCase("description")){
						StringBuilder text = new StringBuilder();
						NodeList chars = property.getChildNodes();
						for (int k=0;k<chars.getLength();k++){
							text.append(chars.item(k).getNodeValue());
						}
						e.setDescription(text.toString());
					} else if (name.equalsIgnoreCase("content:encoded")){		
						String text = property.getFirstChild().getNodeValue();
						Pattern p = Pattern.compile(".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(text.toString());
						if (m.find()) {
							e.setImage(m.group(1));
						}						
					}
				}
				elements.add(e);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} 

		return elements;
	}		
}
