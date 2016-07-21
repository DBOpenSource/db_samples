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
 * @file		sensor_iface.cpp
 *
 * @brief       Utility to create and read data from sensors.
 *
 * @date		Apr 22, 2016.
 *
 * @details		initialize the Color,Light and Gesture sensors an reads values
                from these sensor..
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

/* ---- System headers ------------------------------------------------------ */
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <common/i2c-dev.h>
#include "common/header.h"
#include "sensor_iface.h"
#include "PAJ7620.h"
#include "tcs3414cs.h"
#include "Digital_Light_TSL2561.h"

Paj7620 gesture_sensor( I2C0 );

int init_gesture()
{
	return gesture_sensor.initSensor();
}

int read_gesture()
{
	int value = gesture_sensor.readSensor();
	return value;

}

Tcs3414cs color_sensor( I2C0 );

int init_rgb()
{
	return color_sensor.initSensor();

}


int read_rgb(int *r, int *g, int *b)
{
	if (color_sensor.readSensor() == 0 ) {
		//color_sensor.calculateCoordinate();
		*r = color_sensor.red;
		*g = color_sensor.green;
		*b = color_sensor.blue;
		return 0;
	} else {
		return -1;
	}
}

TSL2561_CalculateLux light;

int init_light()
{
	return light.init();
}

long read_lux()
{
	light.readVisibleLux();
}
