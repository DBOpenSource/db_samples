# A simple test for the minimal standard C++ library
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := samples-TSL2561

LOCAL_SRC_FILES := \
	Digital_Light_TSL2561.cpp \
        main.cpp 
include $(BUILD_EXECUTABLE)

LOCAL_LDLIBS := -llog
