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
 * @file		GestureComponent.java
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


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;


import com.github.dbopensource.sensordemo.service.Gesture;
import com.github.dbopensource.sensordemo.service.SensorConfig;
import com.github.dbopensource.sensordemo.service.SensorManager;


public class GestureComponent extends AbstractSensorComponent {

    ImageView gestureView;
    ViewGroup gestureButtonLaout;
    ImageButton currentMove;


    public GestureComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_gesture_component, this, true);
        gestureView = (ImageView) findViewById(R.id.imageView);

        gestureButtonLaout =  (ViewGroup)findViewById(R.id.gestureButtonLayout);
        for (int i=0; i<gestureButtonLaout.getChildCount(); i++) {
            ViewGroup row = (ViewGroup) gestureButtonLaout.getChildAt(i);
            for (int j=0; j<row.getChildCount(); j++) {
                final ImageButton button = (ImageButton) row.getChildAt(j);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (sensorDataReceiver != null) {
                            sensorDataReceiver.onGestureData(Gesture.getGesture(button));
                        }
                    }
                });
            }
        }

    }

    @Override
    public void init(SensorConfig config, SensorManager.SensorDataReceiver sensorDataReceiver) {
        super.init(config, sensorDataReceiver);
        disableInput(config);
    }

    @Override
    public void disableInput (SensorConfig config) {
        super.disableInput(config);
        gestureButtonLaout.setVisibility(config.readFromSensor() ? View.GONE : View.VISIBLE);
        gestureView.setVisibility(config.readFromSensor() ? View.VISIBLE : View.GONE);
    }

    public void updateGesture(Gesture gesture, final SensorConfig config) {
        updateManualView(gesture);

        if (!config.readFromSensor()) {
            return;
        }

        gestureView.setImageResource(gesture.getImageId());
        /*AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(400);
        anim.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {

            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (config.readFromSensor()) {
                    AlphaAnimation anim = new AlphaAnimation(1.0f, 0.2f);
                    anim.setDuration(1000);
                    anim.setFillAfter(true);
                    gestureView.startAnimation(anim);
                }
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {

            }
        });
        gestureView.startAnimation(anim);*/
    }

    private void updateManualView(Gesture gesture) {
        if (currentMove != null) {
            if (currentMove.getId() == gesture.getButtonId()) {
                return;
            }
            currentMove.setBackgroundColor(Color.TRANSPARENT);
        }

        for (int i=0; i<gestureButtonLaout.getChildCount(); i++) {
            ViewGroup row = (ViewGroup) gestureButtonLaout.getChildAt(i);
            for (int j=0; j<row.getChildCount(); j++) {
                final ImageButton button = (ImageButton) row.getChildAt(j);
                if (button.getId() == gesture.getButtonId()) {
                    button.setBackgroundColor(Color.rgb(26, 26, 26));
                    currentMove = button;
                    break;
                }
            }
        }
    }

}
