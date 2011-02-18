/*
 * Copyright 2010 akquinet
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.akquinet.android.marvintest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ActivityB extends Activity
{
	public static final int CONTENT_VIEW_ID = 98765;
	public static final int BUTTON_ID = 56789;
	public static final int EDIT_TEXT_ID = 75689;
	public static final int TEXT_VIEW_ID = 98567;
	
	public static final int LONG_CLICK = 2;
	public static final int CLICK = 1;
	
	public ViewGroup viewGroup;
	public int clickIdentifier = -1;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	viewGroup = new LinearLayout(this);
    	viewGroup.setPadding(0, 100, 0, 0);
    	viewGroup.setOnLongClickListener(new OnLongClickListener(){

			@Override
			public boolean onLongClick(View arg0) {
				clickIdentifier = LONG_CLICK;
				return true;
			}
    		
    	});
    	
    	viewGroup.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				clickIdentifier = CLICK;
			}		
    	});
    	
    	viewGroup.setId(CONTENT_VIEW_ID);
    	
    	View view=new Button(this);
    	view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	view.setId(BUTTON_ID);
    	view.setFocusable(true);
    	viewGroup.addView(view);
    	
    	EditText editText = new EditText(this);
    	editText.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	editText.setId(EDIT_TEXT_ID);
    	editText.setFocusable(true);
    	viewGroup.addView(editText);
    	
    	TextView textView = new TextView(this);
    	textView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	editText.setId(TEXT_VIEW_ID);
    	editText.setText("42");
    	editText.setVisibility(View.GONE);
    	editText.setEnabled(false);
    	
    	viewGroup.addView(textView);
    	
    	setContentView(viewGroup);
		super.onCreate(savedInstanceState);
	} 
}
