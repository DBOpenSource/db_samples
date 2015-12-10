# A simple test for the minimal standard C++ library
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := samples-adxl345
LOCAL_SRC_FILES := \
	ADXL345.cpp \
        main.cpp 
include $(BUILD_EXECUTABLE)
