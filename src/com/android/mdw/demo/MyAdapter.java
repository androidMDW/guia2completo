package com.android.mdw.demo;

import java.util.LinkedList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Element> {
	LayoutInflater inf;
	LinkedList<Element> objects;
	public MyAdapter(Context context, int resource, int textViewResourceId,
			LinkedList<Element> objects) {
		super(context, resource, textViewResourceId, objects);
		this.inf = LayoutInflater.from(context);
		this.objects = objects;
	}

	public View getView(int position, View convertView, ViewGroup parent){
		View row = convertView;
		/**
		 * obtenemos el elemento actual a renderizar
		 */
		Element currentElement = (Element)objects.get(position);

		/**
		 * Si la vista (convertView) recibida es nula, debemos instanciar desde el XML
		 */
		if (row == null) {
			row = inf.inflate(R.layout.row, null);
		}
		
		/**
		 * Obtenemos la imagen del art’culo y la asignamos al ImageView correspondiente
		 * dentro de su layout
		 */
		ImageView iv = (ImageView) row.findViewById(R.id.imgElement);
		iv.setImageBitmap(currentElement.getImage());
		iv.setScaleType(ImageView.ScaleType.FIT_XY);

		/**
		 * Obtenemos el t’tulo del art’culo y la asignamos al TextView correspondiente
		 * dentro de su layout
		 */		
		TextView tv = (TextView) row.findViewById(R.id.txtElement);
		tv.setText(currentElement.getTitle());			

		return row;    			
	}	
	
}
