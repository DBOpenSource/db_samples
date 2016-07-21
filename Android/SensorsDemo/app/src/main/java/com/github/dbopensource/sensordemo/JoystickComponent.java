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
 * @file		JoystickComponent.java
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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.github.dbopensource.sensordemo.service.Joystick;
import com.github.dbopensource.sensordemo.service.SensorConfig;
import com.github.dbopensource.sensordemo.service.SensorManager;

public class JoystickComponent extends AbstractSensorComponent {

    ImageButton keyLeftButton;
    ImageButton keyRightButton;
    ImageButton keyUpButton;
    ImageButton keyDownButton;
    ImageButton currentKeyButton;
    ImageButton centerButton;


    public JoystickComponent(Context context) {
        this(context, null);
    }

    public JoystickComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_joystick_component, this, true);
        keyLeftButton = (ImageButton) findViewById(R.id.buttonKeyLeft);
        keyLeftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorDataReceiver != null) {
                    sensorDataReceiver.onJoystickData(Joystick.Left);
                }
            }
        });

        keyRightButton = (ImageButton) findViewById(R.id.buttonKeyRight);
        keyRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorDataReceiver != null) {
                    sensorDataReceiver.onJoystickData(Joystick.Right);
                }
            }
        });

        keyUpButton = (ImageButton) findViewById(R.id.buttonKeyUp);
        keyUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorDataReceiver != null) {
                    sensorDataReceiver.onJoystickData(Joystick.Up);
                }
            }
        });

        keyDownButton = (ImageButton) findViewById(R.id.buttonKeyDown);
        keyDownButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorDataReceiver != null) {
                    sensorDataReceiver.onJoystickData(Joystick.Down);
                }
            }
        });

        centerButton = (ImageButton) findViewById(R.id.buttonCenter);
    }

    public void init(Joystick joystick, SensorConfig config, SensorManager.SensorDataReceiver sensorDataReceiver) {
        switch (joystick) {
            case Down:
                currentKeyButton = keyDownButton;
                break;
            case Up:
                currentKeyButton = keyUpButton;
                break;
            case Left:
                currentKeyButton = keyLeftButton;
                break;
            case Right:
                currentKeyButton = keyRightButton;
                break;
        }
        super.init(config, sensorDataReceiver);
        disableInput(config);
    }

    @Override
    public void disableInput (SensorConfig config) {
        super.disableInput(config);
        keyDownButton.setClickable(!config.readFromSensor());
        keyUpButton.setClickable(!config.readFromSensor());
        keyLeftButton.setClickable(!config.readFromSensor());
        keyRightButton.setClickable(!config.readFromSensor());
        updateJoystickImage(config.alpha());
    }

    public void updateJoystickImage(Joystick joystick, SensorConfig config)
    {
        switch (joystick) {
            case Left:
                currentKeyButton = keyLeftButton;
                centerButton.setImageResource(R.drawable.cube3);
                break;
            case Up:
                currentKeyButton = keyUpButton;
                centerButton.setImageResource(R.drawable.cube2);
                break;
            case Right:
                currentKeyButton = keyRightButton;
                centerButton.setImageResource(R.drawable.cube1);
                break;

            default:
                currentKeyButton = keyDownButton;
                centerButton.setImageResource(R.drawable.cube4);
                break;
        }
        updateJoystickImage(config.alpha());
    }

    private void updateJoystickImage(float alpha) {
        keyDownButton.setAlpha(keyDownButton.getId() == currentKeyButton.getId() ? 1.0f : alpha);
        keyUpButton.setAlpha(keyUpButton.getId() == currentKeyButton.getId() ? 1.0f : alpha);
        keyLeftButton.setAlpha(keyLeftButton.getId() == currentKeyButton.getId() ? 1.0f : alpha);
        keyRightButton.setAlpha(keyRightButton.getId() == currentKeyButton.getId() ? 1.0f : alpha);

        keyDownButton.setImageResource(keyDownButton.getId() == currentKeyButton.getId() ? R.drawable.lamp4 : R.drawable.lamp_off4);
        keyUpButton.setImageResource(keyUpButton.getId() == currentKeyButton.getId() ? R.drawable.lamp2 : R.drawable.lamp_off2);
        keyLeftButton.setImageResource(keyLeftButton.getId() == currentKeyButton.getId() ? R.drawable.lamp3 : R.drawable.lamp_off3);
        keyRightButton.setImageResource(keyRightButton.getId() == currentKeyButton.getId() ? R.drawable.lamp1 : R.drawable.lamp_off1);
    }


}
