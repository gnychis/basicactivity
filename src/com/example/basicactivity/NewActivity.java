package com.example.basicactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class NewActivity extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);
    }
    
    @Override
    public void onBackPressed() {	
		//Intent i = new Intent(NewActivity.this, MainActivity.class);
        //startActivity(i);
    	finish();
    }
    
}
