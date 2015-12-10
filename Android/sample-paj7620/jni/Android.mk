# A simple test for the minimal standard C++ library
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := samples-paj7620
LOCAL_SRC_FILES := \
	PAJ7620.cpp \
        main.cpp 
include $(BUILD_EXECUTABLE)

LOCAL_LDLIBS := -llog
