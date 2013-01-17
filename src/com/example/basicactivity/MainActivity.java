package com.example.basicactivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	Context _context;
	
	public static final String BASIC_COMPUTATIONAL_RESULT = 
			"com.example.basicactivity.BASIC_COMPUTATIONAL_RESULT";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context=this;
        
        //someWork(10);  // This would block the main thread
        
		WorkThread workThread = new WorkThread();
		workThread.execute(10);
    }
    
    public void someWork(int waitCount) {
    	int i;
    	for(i=0;i<waitCount; i++) {
    		try { 
    			Thread.sleep(500); 
    		} catch(Exception e) {}
    	}
    	Toast.makeText(getApplicationContext(), "Finished Computation", Toast.LENGTH_LONG).show();
    }
    
	protected class WorkThread extends AsyncTask<Integer, Double, Boolean>
	{
		ProgressDialog _pd;
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        _pd = new ProgressDialog(MainActivity.this);
	        _pd.setMessage("Waiting for completion...");
	        _pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        _pd.setProgress(0);
	        _pd.setMax(100);
	        _pd.show();
	    }
	    
		@Override	// This is the only code that runs on new thread
		protected Boolean doInBackground( Integer ... params )
		{
	    	int i;
	    	int waitCount = params[0];
	    	
	    	for(i=0;i<waitCount; i++) {
	    		try { 
	    			Thread.sleep(500); 
	    		} catch(Exception e) {}
	    		publishProgress((double)(i+1) / (double)waitCount);
	    	}
	    	
			return true;
		}	
		
	     protected void onProgressUpdate(Double... progress) {
	         _pd.setProgress((int)(progress[0]*100));
	     }
		
	    @Override
	    protected void onPostExecute(Boolean wasSuccessful) {
	    	_pd.cancel();
	    	Toast.makeText(getApplicationContext(), "Finished Computation", Toast.LENGTH_LONG).show();
	    	
			// Broadcast the result
			Intent i = new Intent();
			i.setAction(BASIC_COMPUTATIONAL_RESULT);
			i.putExtra("result", 1001);
			_context.sendBroadcast(i);
	    }
	}
    
    @Override
    public void onResume() {
    	super.onResume(); 
    	registerReceiver(incomingEvent, new IntentFilter(BASIC_COMPUTATIONAL_RESULT));
    }
    
    @Override
    public void onPause() {
    	super.onPause();
        unregisterReceiver(incomingEvent);
    }
    
    private BroadcastReceiver incomingEvent = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
        	
        	// Check that the incoming Intent has the Action we are looking for
        	if(intent.getAction().equals(BASIC_COMPUTATIONAL_RESULT)) {
        		
        		int compResult = (Integer) intent.getExtras().get("result");
        		
        		Toast.makeText(getApplicationContext(), "Computational Result: " + 
        					Integer.toString(compResult), Toast.LENGTH_LONG).show();
        	}
        }
    };
    
    public void clickedButton1(View v) {
		Intent i = new Intent(MainActivity.this, NewActivity.class);
        startActivity(i);
    	finish();
    }
    
}
