package com.android.mdw.demo;

import java.util.LinkedList;
import android.app.Application;

public class MyApp extends Application {
	  private LinkedList<Element> data = null; 	  
	  private int selectedOption = Main.APP_VIEW;
	  
	  public LinkedList<Element> getData(){
		  return this.data;
	  }
	  public void setData(LinkedList<Element> d){
		  this.data = d;
	  }
	  
	  public int getSelectedOption(){
		  return this.selectedOption;
	  }
	  
	  public void setSelectedOption(int selectedOption) {
		  this.selectedOption = selectedOption;
	  }
}
