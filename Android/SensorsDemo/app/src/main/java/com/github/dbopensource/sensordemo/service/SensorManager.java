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
 * @file		SensorManager.java
 *
 * @brief
 *
 * @date		Apr 22, 2016.
 *
 * @details		Service class used by the UI to update Gesture values from JNI..
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

package com.github.dbopensource.sensordemo.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.github.dbopensource.sensordemo.DemoUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SensorManager implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "SensorDemo";

    private static SensorManager instance;
    private SensorDataReceiver sensorDataReceiver;

    private ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> readingTask;

    private Context context;
    private SharedPreferences sharedPrefs;

    private SensorConfig colorConfig;
    private SensorConfig lightConfig;
    private SensorConfig joystickConfig;
    private SensorConfig gestureConfig;

    private ExecutorService sensorInitThread = Executors.newFixedThreadPool(1);


    static
    {
        System.loadLibrary("sensorlib");
    }

    public static synchronized SensorManager getInstance(Context context) {
        if (instance == null) {
            instance = new SensorManager(context);
        }
        return instance;
    }

    private SensorManager(Context context)
    {
        this.context = context;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPrefs.registerOnSharedPreferenceChangeListener(this);

        colorConfig = new SensorConfig(SensorConfig.SensorType.COLOR, sharedPrefs);
        lightConfig = new SensorConfig(SensorConfig.SensorType.LIGHT, sharedPrefs);
        joystickConfig = new SensorConfig(SensorConfig.SensorType.JOYSTICK, sharedPrefs, false);
        gestureConfig = new SensorConfig(SensorConfig.SensorType.GESTURE, sharedPrefs);

        wakeUpSensors();
    }

    public boolean wakeUpSensors() {
        int sensorStatus = newServiceJNI();

        colorConfig.setIsPresent(sensorStatus);
        lightConfig.setIsPresent(sensorStatus);
        joystickConfig.setIsPresent(sensorStatus);
        gestureConfig.setIsPresent(sensorStatus);

        if (sensorStatus != SensorConfig.SensorType.allPresent()) {
            sensorInitThread.execute(initSensorIfNecessary());
        }
        return sensorStatus == SensorConfig.SensorType.allPresent();
    }

    public synchronized void startService() {
        if (readingTask != null && !readingTask.isCancelled()) {
            readingTask.cancel(true);
        }
        readingTask = scheduledService.scheduleWithFixedDelay(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                if (sensorDataReceiver == null) {
                    return;
                }

                if (count % 4 == 0) {
                    readColorData();
                    readLightData();
                    readJoystickData();
                }

                count = (++count) % 4;
                readGestureData();
            }
        }, 0, 150, TimeUnit.MILLISECONDS);

    }


    private void readColorData() {
        if (colorConfig.readFromSensor()) {
            int data = readRGBJNI();
            if (data >= 0){
                sensorDataReceiver.onColorData(DemoUtils.cIERgbToColor(data));
            } else {
                Log.d(TAG, "Ignore Error");
            }

        }
    }

    private void readLightData() {
        if (lightConfig.readFromSensor()) {
            long data = readLightJNI();
            if (data >= 0) {
                sensorDataReceiver.onLightData(data);
            }
        }
    }

    private void readJoystickData() {
        if (joystickConfig.readFromSensor()) {
            int data = readJoystickJNI();
            if (data > 0) {
                sensorDataReceiver.onJoystickData(Joystick.Up);
            }
        }
    }

    private void readGestureData() {
        if (gestureConfig.readFromSensor()) {
            int data = readGestureJNI();
            Gesture gesture = Gesture.getGesture(data);
            if (gesture != null) {
                sensorDataReceiver.onGestureData(gesture);
            }
        }
    }

    private Runnable initSensorIfNecessary () {

        return new Runnable() {
            @Override
            public void run() {
                int maxRetry = 0;
                int combinedValue = 0;

                while (maxRetry < 5 && combinedValue != SensorConfig.SensorType.allPresent()) {
                    maxRetry ++;
                    combinedValue |= initSensor(colorConfig);
                    combinedValue |= initSensor(lightConfig);
                    combinedValue |= initSensor(joystickConfig);
                    combinedValue |= initSensor(gestureConfig);
                    try {
                        Thread.sleep(1000 * 5 * maxRetry);
                    } catch (Exception e) {

                    }
                }
            }
        };
    }

    private int initSensor(SensorConfig config) {
        boolean success = true;
        if (!config.isPresent()) {
            if (initSensorJNI(config.getType().code)) {
                config.setIsPresent(true);
                if (sensorDataReceiver != null) {
                    switch (config.getType()) {
                        case COLOR:
                            sensorDataReceiver.disableColorInput(config);
                            break;
                        case LIGHT:
                            sensorDataReceiver.disableLightInput(config);
                            break;
                        case JOYSTICK:
                            sensorDataReceiver.disableJoystickInput(config);
                            break;
                        case GESTURE:
                            sensorDataReceiver.disableGestureInput(config);
                            break;
                    }
                }
            } else {
                success = false;
            }
        }
        return success ? 1 << config.getType().code : 0;
    }

    public void registerReceiver(SensorDataReceiver receiver) {
        sensorDataReceiver = receiver;
        startService();
    }

    public void unregisterReceiver() {
        if (readingTask != null && !readingTask.isCancelled()) {
            readingTask.cancel(true);
        }

        sensorDataReceiver = null;
    }

    public SensorConfig getColorConfig() {
        return colorConfig;
    }

    public SensorConfig getLightConfig() {
        return lightConfig;
    }

    public SensorConfig getJoystickConfig() {
        return joystickConfig;
    }

    public SensorConfig getGestureConfig() {
        return gestureConfig;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sensorDataReceiver == null) {
            return;
        }

        if (colorConfig.containsKey(key)) {
            colorConfig.update(key, sharedPreferences.getBoolean(key, true));
            sensorDataReceiver.disableColorInput(colorConfig);
        } else if (lightConfig.containsKey(key)) {
            lightConfig.update(key, sharedPreferences.getBoolean(key, true));
            sensorDataReceiver.disableLightInput(lightConfig);
        } else if (joystickConfig.containsKey(key)) {
            joystickConfig.update(key, sharedPreferences.getBoolean(key, true));
            sensorDataReceiver.disableJoystickInput(joystickConfig);
        } else if (gestureConfig.containsKey(key)) {
            gestureConfig.update(key, sharedPreferences.getBoolean(key, true));
            sensorDataReceiver.disableGestureInput(gestureConfig);
        }
    }

    private static native int readJoystickJNI();
    private static native int readGestureJNI();
    private static native long readLightJNI();
    private static native int readRGBJNI();
    private static native int newServiceJNI();
    private static native boolean initSensorJNI(int sensorType);


    public interface SensorDataReceiver {
        public void onColorData(int color);
        public void onLightData(long lux);
        public void onGestureData(Gesture gesture);
        public void onJoystickData(Joystick joystick);
        public void disableColorInput(SensorConfig config);
        public void disableJoystickInput(SensorConfig config);
        public void disableLightInput(SensorConfig config);
        public void disableGestureInput(SensorConfig config);

    }

}
