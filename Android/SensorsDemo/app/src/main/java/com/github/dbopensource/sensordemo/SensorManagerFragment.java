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
 * @file		SensorManagerFragment.java
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
import android.app.Fragment;
import android.graphics.Color;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


import com.github.dbopensource.sensordemo.service.Gesture;
import com.github.dbopensource.sensordemo.service.Joystick;
import com.github.dbopensource.sensordemo.service.SensorConfig;
import com.github.dbopensource.sensordemo.service.SensorManager;
import com.github.dbopensource.sensordemo.service.SensorManager.SensorDataReceiver;

public class SensorManagerFragment extends Fragment implements SensorDataReceiver {

    public static final int SENSOR_DEFAULT_COLOR = 255;
    public static final int BRIGHTNESS_DEFAULT = 30;

    public static final Shapes DEFAULT_SHAPE = Shapes.Monkey;
    public static final Joystick DEFAULT_JOYSTICK = Joystick.Down;

    OnDataChangeListner dataChangeListner;

    JoystickComponent joystickComponent;
    ColorComponent colorComponent;
    LightComponent lightComponent;
    GestureComponent gestureComponent;


    ImageButton teapotButton;
    ImageButton cubeButton;
    ImageButton monkeyButton;

    Handler handler = new Handler(Looper.getMainLooper());
    SensorManager sensorManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor_indicator, container, false);

        initColorBar(view);
        initLightBar(view);
        initJoystickButtons(view);
        initGestureButtons(view);

        final int highlightedColor = Color.rgb(26, 26, 26);
        teapotButton = (ImageButton) view.findViewById(R.id.buttonTeapot);
        teapotButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(highlightedColor);
                monkeyButton.setBackgroundColor(Color.TRANSPARENT);
                cubeButton.setBackgroundColor(Color.TRANSPARENT);
                dataChangeListner.onModelChange(Shapes.Teapot);
            }
        });

        cubeButton = (ImageButton) view.findViewById(R.id.buttonCube);
        cubeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                v.setBackgroundColor(highlightedColor);
                teapotButton.setBackgroundColor(Color.TRANSPARENT);
                monkeyButton.setBackgroundColor(Color.TRANSPARENT);
                dataChangeListner.onModelChange(Shapes.Cube);
            }
        });

        monkeyButton = (ImageButton) view.findViewById(R.id.buttonMonkey);
        monkeyButton.setBackgroundColor(highlightedColor);
        monkeyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               v.setBackgroundColor(highlightedColor);
               teapotButton.setBackgroundColor(Color.TRANSPARENT);
                cubeButton.setBackgroundColor(Color.TRANSPARENT);
                dataChangeListner.onModelChange(Shapes.Monkey);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dataChangeListner = (OnDataChangeListner) activity;
            sensorManager = MFApplication.sensorManager;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDataChangeListner");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerReceiver(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterReceiver();
    }



    @Override
    public void onColorData(final int color) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (dataChangeListner != null) {
                    dataChangeListner.onColorChange(color);
                }
                colorComponent.updateColor(color);
            }
        });
    }


    @Override
    public void onGestureData(final Gesture gesture) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dataChangeListner.onGestureChange(gesture);
                gestureComponent.updateGesture(gesture, sensorManager.getGestureConfig());
            }
        });

    }


    @Override
    public void onLightData(final long lux) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dataChangeListner.onLightsChange(DemoUtils.convertLuxToBrightness(lux));
                lightComponent.updateLight(lux);
            }
        });

    }


    @Override
    public void onJoystickData(final Joystick joystick) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                dataChangeListner.onJoystickChange(joystick);
                joystickComponent.updateJoystickImage(joystick, sensorManager.getJoystickConfig());
            }
        });

    }


    @Override
    public void disableColorInput(SensorConfig config) {
        colorComponent.disableInput(config);
    }

    @Override
    public void disableLightInput(SensorConfig config) {
        lightComponent.disableInput(config);
    }

    @Override
    public void disableJoystickInput(SensorConfig config) {
        joystickComponent.disableInput(config);
    }

    @Override
    public void disableGestureInput(SensorConfig config) {
        gestureComponent.disableInput(config);

    }


    private void initLightBar(View view) {
        lightComponent = (LightComponent) view.findViewById(R.id.lightComponent);
        lightComponent.init(BRIGHTNESS_DEFAULT, sensorManager.getLightConfig(), this);
    }

    private void initColorBar(View view) {
        colorComponent = (ColorComponent) view.findViewById(R.id.colorComponent);
        colorComponent.init(SENSOR_DEFAULT_COLOR, sensorManager.getColorConfig(), this);
    }


    private void initJoystickButtons(final View view) {
        joystickComponent = (JoystickComponent) view.findViewById(R.id.joystickComponent);
        joystickComponent.init(DEFAULT_JOYSTICK, sensorManager.getJoystickConfig(), this);

    }


    private void initGestureButtons(View view) {
        gestureComponent = (GestureComponent) view.findViewById(R.id.gestureComponent);
        gestureComponent.init(sensorManager.getGestureConfig(), this);
    }


    public interface OnDataChangeListner {
        public void onColorChange(int color);
        public void onLightsChange(int brightness);
        public void onGestureChange(Gesture gesture);
        public void onJoystickChange(Joystick joystick);
        public void onModelChange(Shapes shape);
    }

    public enum Shapes {
        Cube,
        Teapot,
        Monkey;
    }

}
