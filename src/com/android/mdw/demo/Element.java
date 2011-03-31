package com.android.mdw.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Element {
	static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
	private String title;
	private String author;
	private String link;
	private Bitmap image;
	private String description;
	private Date date;

	public String getTitle() {
		return this.title;
	}

	public String getLink() {
		return this.link;
	}

	public String getDescription() {
		return this.description;
	}
	
	public String getDate() {
		return FORMATTER.format(this.date);
	}
	
	public Bitmap getImage(){
		return this.image;
	}
	
	public String getAuthor(){
		return this.author;
	}
	
	public void setTitle(String title) {
		this.title = title.trim();
	}
	
	public void setLink(String link) {
		this.link = link;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

	public void setDate(String date) {
		try {
			this.date = FORMATTER.parse(date);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
	}
	
	public void setImage(String image){
		if (image.contains("autor")) {
			image = "http://a1.twimg.com/profile_images/82885809/mdw_hr_reasonably_small.png";
		}
		try {			
			this.image = loadFromUrl(new URL(image));
		} catch (Exception e) {
			try {
				this.image = loadFromUrl(new URL("http://a1.twimg.com/profile_images/82885809/mdw_hr_reasonably_small.png"));
			} catch (MalformedURLException e1) {}
		}
	}
	
	public void setAuthor(String author){
		this.author = author;
	}
	
	public String toString(){
		return this.title;
	}

	private Bitmap loadFromUrl(URL link) {		
		Bitmap bitmap = null;
		InputStream in = null;       
		try {
			in = link.openConnection().getInputStream();
		    bitmap = BitmapFactory.decodeStream(in, null, null);
		    in.close();
		} catch (IOException e) {}
		return bitmap; 
	}	
}
