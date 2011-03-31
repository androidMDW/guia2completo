package com.android.mdw.demo;

import java.util.LinkedList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends ListActivity {
	/** 
	 * constantes para identificar si el usuario quiere visualizar los artítuclos 
	 * dentro de la aplicación o en el navegador
	 */
	final static int APP_VIEW = 1;
	final static int BROWSER_VIEW = 2;
	
	/**
	 * constante para identificar la llave con la que envío datos a través de intents
	 * para comunicar entre las dos actividades: Main y ShowElement
	 */
	final static String POSITION_KEY = "P";
	
	/**
	 * Guardamos la dirección del feed como otra variable de clase para poder modificarla sin
	 * complicaciones más adelante.
	 */
	static String feedUrl = "http://www.maestrosdelweb.com/index.xml";	

	/**
	 * Utilizamos una variable de instancia para la clase de aplicación, de esta forma se accesarán,
	 * para guardar y recuperar, la lista de artículos y la preferncia del usuario
	 */	
	private MyApp appState;
	
	/**
	 * para el diálogo de progreso es necesaria una variable global porque iniciamos el diálogo en
	 * una función y lo ocultamos en otra
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * Android nos presenta la restricciones que no podemos alterar los elementos de interfaz 
	 * gráfica en un hilo de ejecución que no sea el principal por lo que es necesario utilizar 
	 * un manejador(Handler) para enviar un mensaje de un hilo a otro cuando la carga de datos
	 * haya terminado.
	 */		
    private final Handler progressHandler = new Handler() {
		public void handleMessage(Message msg) {
			setData();
			progressDialog.dismiss();
        }
    };
	
    /** Este método es llamado cuando la actividad es creada */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /** 
         * Le ponemos nombre a la ventana 
         */
        setTitle("Lector de feed Maestros del Web");        
        
        /**
         * Inicializamos la variable para nuestra clase de aplicación
         */
        appState = ((MyApp)getApplication());
        
        /**
         * Validamos si el intent lo levantó alguna otra actividad y si viene un -1
         * en el mensaje mostramos un error
         */
		Intent it = getIntent();
		int fromShowElement = it.getIntExtra(POSITION_KEY, 0);
		if (fromShowElement == -1) {
			Toast.makeText(this, "Error, imposible visualizar el elemento", Toast.LENGTH_LONG);
		}

        Button btn = (Button) findViewById(R.id.btnLoad);
        
        /**
         * personalizamos el evento del click del botón de carga  
         */        
        btn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {				
				LinkedList<Element> data = appState.getData();
    			/**
    			 * Si ya hay datos que se fueron a traer y reconocer (parsear)
    			 * mostramos un diálogo preguntando al usuario si está seguro de
    			 * realizar la carga de nuevo
    			 */				
				if (data != null) {
					AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
					builder.setMessage("ya ha cargado datos, ¿Está seguro de hacerlo de nuevo?")
					       .setCancelable(false)
					       .setPositiveButton("Si", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					        	   loadData();
					           }
					       })
					       .setNegativeButton("No", new DialogInterface.OnClickListener() {
					           public void onClick(DialogInterface dialog, int id) {
					                dialog.cancel();					            
					           }
					       })
					       .create()
				       .show();
    			/**
    			 * Si no hay datos aún, cargamos con loadData()
    			 */    									
				} else {
					loadData();
				}
			}
		});

    }

    /**
     * Función que inicializa el menú para mostrarlo
     */    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();     
        inflater.inflate(R.layout.menu, menu);
        return true;
    }
    
    /**
     * Función que se dispara al elegir una función del menú y guarda lo que eligió el usuario
     * en la clase de aplicacion a través de una llamada a setSelectedOption
     */    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case R.id.mmElementApp:
        		appState.setSelectedOption(APP_VIEW);
        		break;
        	case R.id.mmElementBrw:
        		appState.setSelectedOption(BROWSER_VIEW);
        		break;
        }
        
        return true;
    }    
    
    /**
     * Función que se dispara cuando el usuario haga click en algún elemento de la lista,
     * dependiendo de la configuración lo llevaremos al enlace del elemento a través del navegador o 
     * bien se le mostrará una vista previa dentro de la aplicación
     */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent nextActivity = null;
		
		if (appState.getSelectedOption() == APP_VIEW) {
			nextActivity = new Intent(this, ShowElement.class);
			nextActivity.putExtra(POSITION_KEY, position);			
		} else { 
			LinkedList<Element> data = appState.getData();		
			nextActivity = new Intent(Intent.ACTION_VIEW,
					Uri.parse(data.get(position).getLink()));		
			
		}
				
		this.startActivity(nextActivity);
	}   

    /** 
     * Función auxiliar que recibe una lista de mapas, y utilizando esta data crea un adaptador
     * para poblar al ListView del diseño
     * */    
    private void setData(){
    	this.setListAdapter(new MyAdapter(this, R.layout.row, 0, appState.getData()));
    } 
    
    /**
     * Función auxiliar que inicia la carga de datos, muestra al usuario un diálogo de que
     * se están cargando los datos y levanta un thread para lograr la carga.
     */	
    private void loadData() {
		progressDialog = ProgressDialog.show(
				Main.this,
				"", 
				"Por favor espere mientras se cargan los datos...", 
				true);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				XMLParser parser = new XMLParser(feedUrl);
				appState.setData(parser.parse());
				progressHandler.sendEmptyMessage(0);
			}}).start();
    }	
}