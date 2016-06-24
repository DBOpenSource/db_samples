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
 * @file		ColorComponent.java
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.dbopensource.sensordemo.service.SensorConfig;
import com.github.dbopensource.sensordemo.service.SensorManager;

public class ColorComponent extends AbstractSensorComponent {

    DemoSeekBar colorR;
    DemoSeekBar colorG;
    DemoSeekBar colorB;
    TextView textViewR;
    TextView textViewG;
    TextView textViewB;


    public ColorComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_color_component, this, true);

        colorR = (DemoSeekBar) findViewById(R.id.seekBarR);
        colorR.setOnSeekBarChangeListener(new ColorBarOnClickListner());

        colorG = (DemoSeekBar) findViewById(R.id.seekBarG);
        colorG.setOnSeekBarChangeListener(new ColorBarOnClickListner());

        colorB = (DemoSeekBar) findViewById(R.id.seekBarB);
        colorB.setOnSeekBarChangeListener(new ColorBarOnClickListner());

        textViewR = (TextView)findViewById(R.id.textViewR);
        textViewG = (TextView)findViewById(R.id.textViewG);
        textViewB = (TextView)findViewById(R.id.textViewB);
    }

    public void init(int color, SensorConfig config, SensorManager.SensorDataReceiver sensorDataReceiver) {
        super.init(config, sensorDataReceiver);
        disableInput(config);
        updateColor(color);
    }

    @Override
    public void disableInput (SensorConfig config) {
        super.disableInput(config);
        colorR.setBlocked(config.readFromSensor());
        colorG.setBlocked(config.readFromSensor());
        colorB.setBlocked(config.readFromSensor());
    }

    public void updateColor(int color) {
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        colorR.setProgress(red);
        colorG.setProgress(green);
        colorB.setProgress(blue);
        textViewR.setText(String.valueOf(red));
        textViewG.setText(String.valueOf(green));
        textViewB.setText(String.valueOf(blue));
    }

    private final class ColorBarOnClickListner implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int color = Color.rgb(colorR.getProgress(), colorG.getProgress(), colorB.getProgress());
            if (sensorDataReceiver != null) {
                sensorDataReceiver.onColorData(color);
            }
        }
    }
}
