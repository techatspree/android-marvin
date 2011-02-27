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
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;


public class ActivityC extends Activity {
    public static final int CONTENT_VIEW_ID = 54321;
    public int keyIdentifier = -1;
    public boolean actionIdentifierUp = false;
    public boolean actionIdentifierDown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View layout = new LinearLayout(this);
        layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        layout.setId(CONTENT_VIEW_ID);

        layout.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                keyIdentifier = keyCode;

                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    actionIdentifierDown = true;
                else if (event.getAction() == KeyEvent.ACTION_UP)
                    actionIdentifierUp = true;
                return true;
            }

        });

        setContentView(layout);

        layout.setFocusable(true);
        layout.setFocusableInTouchMode(true);
        layout.requestFocus();

        super.onCreate(savedInstanceState);
    }
}
