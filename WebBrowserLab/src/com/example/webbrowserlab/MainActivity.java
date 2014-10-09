package com.example.webbrowserlab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	String URL;
	Button button;
	EditText searchField;
	
	Handler webPageHandler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			WebView wv = (WebView) findViewById(R.id.display);
			wv.loadData(String.valueOf(msg.obj), "text/html", "UTF-8");

			return false;
		}
	});
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		searchField = (EditText) findViewById(R.id.URL_Field);
		button = (Button) findViewById(R.id.btnSearch);
		
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				EditText searchField = (EditText) findViewById(R.id.URL_Field);
				URL = searchField.getText().toString();
				
				if (URL.substring(0,6) != "http://")
				{
					URL = "http://" + URL;
				}
				
				Thread t = new Thread(){
					@Override
					public void run (){

						URL url = null;
						String pageContent = "";
						
						try 
						{
							url = new URL(URL);
							
							BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
							
							String tmpString = br.readLine();
			
							while(tmpString != null)
							{
								pageContent += tmpString;
								tmpString = br.readLine();
							}
							
							Message msg = webPageHandler.obtainMessage();
							msg.obj = pageContent;
							webPageHandler.sendMessage(msg);
							//msg.setTarget(webPageHandler);
							//msg.sendToTarget();
						}
						
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				};
				
				t.start();
				
			}
		});
		
	}
	
}