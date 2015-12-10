#ifndef _MF_HEADER_H
#define _MF_HEADER_H

#include <android/log.h>

#define LOGV(TAG,...) __android_log_print(ANDROID_LOG_VERBOSE, TAG,__VA_ARGS__)
#define LOGD(TAG,...) __android_log_print(ANDROID_LOG_DEBUG  , TAG,__VA_ARGS__)
#define LOGI(TAG,...) __android_log_print(ANDROID_LOG_INFO   , TAG,__VA_ARGS__)
#define LOGW(TAG,...) __android_log_print(ANDROID_LOG_WARN   , TAG,__VA_ARGS__)
#define LOGE(TAG,...) __android_log_print(ANDROID_LOG_ERROR  , TAG,__VA_ARGS__)

#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "makerfaire JNI IFACE", __VA_ARGS__);
#define I2C0    "/dev/i2c-0"

#define COLOR_TAG "MF_COLOR_SENSOR"
#define GESTURE_TAG "MF_GESTURE_SENSOR"
#define LIGHT_TAG "MF_LIGHT_SENSOR"
#define JOYSTICK_TAG "MF_ACCL_SENSOR"

#endif