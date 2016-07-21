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
 * @file		SensorConfig.java
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

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.github.dbopensource.sensordemo.DemoUtils;
import com.github.dbopensource.sensordemo.R;

public class SensorConfig {
    enum SensorType {
        COLOR("key_color_show", "key_color_auto", 0),
        LIGHT("key_light_show", "key_light_auto", 1),
        JOYSTICK("key_joystick_show", "key_joystick_auto", 2),
        GESTURE("key_gesture_show", "key_gesture_auto", 3);

        String keyShow;
        String keyAuto;
        int code;

        private SensorType(String keyShow, String keyAuto, int code) {
            this.keyAuto = keyAuto;
            this.keyShow = keyShow;
            this.code = code;
        }

        public String getKeyAuto() {
            return keyAuto;
        }

        public String getKeyShow() {
            return keyShow;
        }

        public int code() {
            return code;
        }

        public static int allPresent () {
            return 15;
        }
    }

    private SensorType type;
    private boolean isPresent;
    private boolean show = true;
    private boolean auto = true;

    public SensorConfig(SensorType type, SharedPreferences sharedPrefs) {
         this(type, sharedPrefs, true);
    }

    public SensorConfig(SensorType type, SharedPreferences sharedPrefs, boolean defaultShowValue) {
        switch (type) {
            case COLOR:
                this.type = SensorType.COLOR;
                break;

            case LIGHT:
                this.type = SensorType.LIGHT;
                break;

            case JOYSTICK:
                this.type = SensorType.JOYSTICK;
                break;

            case GESTURE:
                this.type = SensorType.GESTURE;
                break;

            default:
                this.type = SensorType.COLOR;
                break;
        }

        show = sharedPrefs.getBoolean(type.keyShow, defaultShowValue);
        auto = sharedPrefs.getBoolean(type.keyAuto, true);
    }



    public boolean readFromSensor() {
        return show && isPresent && auto;
    }

    public boolean containsKey(String key) {
        return type.keyShow.equals(key) || type.keyAuto.equals(key);
    }

    public void update(String key, boolean value) {
        if (type.keyAuto.equals(key)) {
            auto = value;
        } else {
            show = value;
        }
    }

    public float alpha() {
        return readFromSensor() ? 0.0f : 1.0f;
    }


    public SensorType getType() {
        return type;
    }

    public void setType(SensorType type) {
        this.type = type;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setIsPresent(boolean isPresent) {
        this.isPresent = isPresent;
    }

    public void setIsPresent(int combineValue) {
        this.isPresent = DemoUtils.isBitSet(type.code, combineValue);
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isAuto() {
        return auto;
    }

    public void setAuto(boolean auto) {
        this.auto = auto;
    }


}
