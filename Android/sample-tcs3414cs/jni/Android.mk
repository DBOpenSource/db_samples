# A simple test for the minimal standard C++ library
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := samples-Tcs3414cs

LOCAL_SRC_FILES := \
	Tcs3414cs.cpp \
        main.cpp 
include $(BUILD_EXECUTABLE)

LOCAL_LDLIBS := -llog
