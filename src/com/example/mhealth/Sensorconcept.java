package com.example.mhealth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import org.json.simple.parser.JSONParser;



import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Sensorconcept extends ActionBarActivity {

	EditText sensor;
	Button b1;
	String query;
	LinearLayout main1;
	String username;
	String password;
	String url;
	TextView S;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		main1=new LinearLayout(this);
        main1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,android.view.ViewGroup.LayoutParams.MATCH_PARENT));
  	 main1.setOrientation(LinearLayout.VERTICAL);
  	 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
  	 Bundle extras = getIntent().getExtras(); 
  	 username= extras.getString("uname");
	 password = extras.getString("pword");
	
	 url=extras.getString("url");
     S=new TextView(this);
      S.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      S.setText("Enter sensor Id");
      main1.addView(S);
  	 sensor =new EditText(this);
  	 sensor.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));     
      main1.addView(sensor);
      b1=new Button(this);
      b1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));   
      b1.setText("Get Concepts");
      b1.setOnClickListener(listener2);
      main1.addView(b1);
      setContentView(main1);
		
		
		
	
}
	public static StringBuilder inputStreamToString (InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();
		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) { 
				total.append(line); 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Return full string
		return total;
		}
	
	public static String Httpget (String username, String password, String url,String query) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url+"/ws/rest/v1/sensor/scm/"+query);
		httpGet.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(username, password),"UTF-8", false));
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity responseEntity = httpResponse.getEntity();
		String str = inputStreamToString(responseEntity.getContent()).toString();
		httpClient.getConnectionManager().shutdown();

		return str;  
		         
    	} 
	public static StringBuffer JsonParse(String str) throws JSONException
	{ 
		StringBuffer res=new StringBuffer();
		StringBuffer r=new StringBuffer();
		JSONObject jo = new JSONObject(str);
		// get n array from json object
		JSONArray concept=(JSONArray)jo.get("concepts");
		for(int i=0;i<concept.length();i++)
		{ ;
       JSONObject row=concept.getJSONObject(i);
		res=new StringBuffer(row.getString("display"));
		
		r=r.append(res);
		r=r.append("*");
		}

	return r;
	}
	
private  class MyAsyncTask extends AsyncTask<String, String, String>{
		
		StringBuffer res1;
		
		  @Override
		  protected String doInBackground(String... params) {
			  
			   try {
				   
				   res1 = JsonParse(Httpget(params[0],params[1], params[2],params[3]));
				  
				
				   
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		       
				return new String(res1);
		  }
		  
		  
		  protected void onPostExecute(String params){
			
			  if(params==null)
			  {System.out.println("params"+params);
				  Toast.makeText(getApplicationContext(),"No Concepts found.", Toast.LENGTH_LONG).show();
			  }
			  
			  else{
		
				  
				  String r=params;
	      String arrayString[] = r.split("\\*");
	
          int size=arrayString.length;

	 
	 
	 for(int i=0;i<size;i++)
	 {
		 final TextView rowTextView = new TextView(Sensorconcept.this);
		 final EditText rowedit = new EditText(Sensorconcept.this);
		 rowTextView.setText(arrayString[i]);
		 main1.addView(rowTextView);
		 main1.addView(rowedit);
		 
	 }
	  
	
		  
	
			 
		  }
		  }	  
		  
		  protected void onProgressUpdate(Integer... progress){
			    
		  }
		  }
		 
	
	
OnClickListener listener2 = new OnClickListener(){
	
	@Override
public void onClick(View v)
{
	query=sensor.getText().toString();	
	  new MyAsyncTask().execute(username,password,url,query);
	
	 
		
		
}};
	
}
