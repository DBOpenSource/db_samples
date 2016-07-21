/******************************************************************************
 * Copyright (c) 2016 Sujai SyrilRaj .
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 * - Neither the name of [Owner Organization] nor the names of its
 *   contributors may be used to endorse or promote products derived from this
 *   software without specific prior written permission.
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
 * FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,  INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/
/**
 * @file		SensorDemoActivity.java
 *
 * @brief
 *
 * @date		Apr 22, 2016.
 *
 * @details		Utility class for rendering the sensors data..
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

package com.github.dbopensource.sensordemo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
 import android.content.Context;
 import android.os.Bundle;

 import android.os.PowerManager;
 import android.view.MotionEvent;
import android.view.View;
 import android.view.WindowManager;


import com.github.dbopensource.sensordemo.SensorManagerFragment.OnDataChangeListner;
import com.github.dbopensource.sensordemo.service.Gesture;
import com.github.dbopensource.sensordemo.service.Joystick;

public class SensorDemoActivity extends Activity implements OnDataChangeListner {

	private static final String FRAGMENT_TAG = SensorDemoActivity.class.getSimpleName() + ".FRAGMENT_TAG";
    private static final String TAG = "SensorDemoActivity";

    private Sensor3DFragment fragment;

    float x1,x2;
    float y1, y2;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
            launchFragment();
		} else {
            fragment = (Sensor3DFragment) getFragmentManager().findFragmentByTag(FRAGMENT_TAG);
		}

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

    @Override
    protected void onResume() {
      //  getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

        super.onResume();
    }

    @Override
	protected void onDestroy() {
		try {
			super.onDestroy();
		} catch (Exception e) {
		}
	}

	private void launchFragment() {
		final FragmentManager fragmentManager = getFragmentManager();

		final FragmentTransaction transaction = fragmentManager.beginTransaction();
        try {
            fragment = new Sensor3DFragment();

            if (fragmentManager.findFragmentByTag(FRAGMENT_TAG) != null)
                transaction.addToBackStack(null);

            transaction.replace(R.id.content_frame, fragment, FRAGMENT_TAG);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onModelChange(SensorManagerFragment.Shapes shape) {
        fragment.onModelChange(shape);
    }

    @Override
    public void onGestureChange(Gesture gesture) {
         fragment.onGestureChange(gesture);
    }

    @Override
    public void onColorChange(int color) {
        if (fragment != null) {
            fragment.onColorChange(color);
        }
    }


    @Override
    public void onJoystickChange(Joystick joystick) {
        fragment.onJoystickChange(joystick);
    }

    @Override
    public void onLightsChange(int brightness) {
        fragment.onLightsChange(brightness);
    }

    @Override
    public boolean onTouchEvent(MotionEvent touchEvent)
    {
        switch (touchEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();

                if (x2 - x1 > 300)
                {
                    findViewById(R.id.settingsLayout).setVisibility(View.VISIBLE);
                }

                if (x1 - x2 > 10)
                {
                    findViewById(R.id.settingsLayout).setVisibility(View.INVISIBLE);
                    fragment.updateSettings();
                }

                break;
            }
        }
        return false;
    }



}
