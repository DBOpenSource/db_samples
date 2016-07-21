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
 * @file		makerfair-jni.cpp
 *
 * @brief       This file implements the JNI calls made from JAVA.
 *
 * @date		Apr 22, 2016.
 *
 * @details		This is the JNI implementation file. It uses the utilities exposed by
                sensor_iface.cpp to create the Color(RGB) , Gesture and Light sensors.
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

#define JNI_TAG "MF_JNI"

#include <jni.h>
#include <stddef.h>
#include <stdlib.h>
#include "sensorlib-jni.h"

extern "C"
{
    #include "sensor_iface.h"
}

enum {
    COLOR_POS = 0,
    LIGHT_POS = 1,
    JOYSTICK_POS = 2,
    GESTURE_POS = 3
};

JNIEXPORT jint JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_newServiceJNI(JNIEnv * env, jobject obj)
{
    int result = 0;

    int colorPresent = init_rgb();
    LOGD(JNI_TAG, "color present %d", colorPresent);
    result += (1 & colorPresent) << COLOR_POS;

    int lightPresent = init_light();
    LOGD(JNI_TAG, "light present %d", lightPresent);
    result += (1 & lightPresent) << LIGHT_POS;

    int gesturePresent = init_gesture();
    LOGD(JNI_TAG, "gesture present %d", gesturePresent);
    result += (1 & gesturePresent) << GESTURE_POS;

	return result;
}

JNIEXPORT jboolean JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_initSensorJNI(JNIEnv * env, jobject obj, jint sensorType) {
    int isPresent = 0;
    switch(sensorType) {
        case COLOR_POS:
           isPresent = init_rgb();
           LOGD(JNI_TAG, "color present %d", isPresent);
           break;

        case LIGHT_POS:
           isPresent = init_light();
           LOGD(JNI_TAG, "light present %d", isPresent);
           break;

        /*case JOYSTICK_POS:
           //isPresent = init_rgb();
           LOGD(JNI_TAG, "joystick present %d", isPresent);
           break;*/

        case GESTURE_POS:
           isPresent = init_gesture();
           LOGD(JNI_TAG, "gesture present %d", isPresent);
           break;
    }

    return (isPresent == 0) ? false : true;
}

JNIEXPORT jint JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_readRGBJNI(JNIEnv * env,
      jobject obj)
{

    int r, g, b;
    int error = read_rgb(&r, &g, &b);
    return (error == 0) ? ((r & 0xff) << 16) + ((g & 0xff) << 8) + (b & 0xff) : -1;
}

/*
JNIEXPORT jint JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_readJoystickJNI(JNIEnv * env,
      jobject obj)
{
    int x, y, z;
    return rand() % 4;
}*/

JNIEXPORT jint JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_readGestureJNI(JNIEnv * env,
      jobject obj)
{
    int gesture = read_gesture();
    return gesture;
}

JNIEXPORT jlong JNICALL
Java_com_github_dbopensource_sensordemo_service_SensorManager_readLightJNI(JNIEnv * env,
      jobject obj)
{
    long lux = read_lux();
  //  LOGD(JNI_TAG, "read light is %d ", lux );
    return lux;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	LOGD(JNI_TAG, "JNI_OnLoad." );
    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION_1_6) != JNI_OK) {
    	LOGE(JNI_TAG, "Error getting JNIEnv." );
        return -1;
    }
	return JNI_VERSION_1_6;
}






