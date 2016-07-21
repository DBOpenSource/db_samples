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
 * @file		LightComponent.java
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
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.dbopensource.sensordemo.service.SensorConfig;
import com.github.dbopensource.sensordemo.service.SensorManager;



public class LightComponent extends AbstractSensorComponent {

    int[] brightnessImages = {R.drawable.brightness0, R.drawable.brightness1, R.drawable.brightness2, R.drawable.brightness3,
                                R.drawable.brightness4, R.drawable.brightness5, R.drawable.brightness6, R.drawable.brightness7,
                                R.drawable.brightness8, R.drawable.brightness9, R.drawable.brightness10, R.drawable.brightness11,
                                R.drawable.brightness12, R.drawable.brightness13, R.drawable.brightness14, R.drawable.brightness15,
                                R.drawable.brightness16, R.drawable.brightness17, R.drawable.brightness18, R.drawable.brightness19,
                                R.drawable.brightness20};
    DemoSeekBar light;
  //  ImageView lightImageView;
    TextView lightText;

    public LightComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_light_component, this, true);

        light = (DemoSeekBar) findViewById(R.id.seekBarLight);
        light.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (sensorDataReceiver != null) {
                    sensorDataReceiver.onLightData(DemoUtils.convertBrightnessToLux(seekBar.getProgress()));
                }
            }
        });

       // lightImageView = (ImageView) findViewById(R.id.lightView);
        lightText = (TextView) findViewById(R.id.textViewLight);

    }

    public void init(int brightness, SensorConfig config, SensorManager.SensorDataReceiver sensorDataReceiver) {
        super.init(config, sensorDataReceiver);
        disableInput(config);
        updateLight(DemoUtils.convertBrightnessToLux(brightness));

    }

    @Override
    public void disableInput (SensorConfig config) {
        super.disableInput(config);
        light.setBlocked(config.readFromSensor());
        light.setVisibility(config.readFromSensor() ? View.INVISIBLE : View.VISIBLE);
    }

    public void updateLight(long lux) {
        int brightness = DemoUtils.convertLuxToBrightness(lux);
        light.setProgress(brightness);
        lightText.setText(String.format("%d", lux));

        int pos = (brightness * brightnessImages.length / 256);
        lightText.setBackgroundResource(brightnessImages[pos]);
    }

}
