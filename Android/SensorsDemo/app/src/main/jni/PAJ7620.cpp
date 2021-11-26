/*
 * Paj7620.cpp
 * A library for Grove-Guesture 1.0
 *
 * Copyright (c) 2015 seeed technology inc.
 * Website    : www.seeed.cc
 * Author     : Xiangnan
 *
 * Copyright (c) Kyle Lee & Ashwin Vijayakumar<ashwinvijayakumar@gmail.com>
 * Modified to run on Linux machines, tested on DragonBoard 410c
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * @file		Paj7620.cpp
 *
 * @brief		I2C Gesture sensor driver
 *
 * @date		Jun 6, 2015, 7:57:10 AM
 *
 * @details		Identifies UP/DOWN/LEFT/RIGHT/FORWARD/BACKWARD/
 * 				CLOCKWISE/COUNTER_CLOCKWISE gestures
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

/* ---- Local headers ------------------------------------------------------- */
#include "PAJ7620.h"
#include "common/i2c-dev.h"
#include "common/header.h"

static unsigned const char init_register_array[][2] = {
		{0xEF,0x00},
		{0x32,0x29},
		{0x33,0x01},
		{0x34,0x00},
		{0x35,0x01},
		{0x36,0x00},
		{0x37,0x07},
		{0x38,0x17},
		{0x39,0x06},
		{0x3A,0x12},
		{0x3F,0x00},
		{0x40,0x02},
		{0x41,0xFF},
		{0x42,0x01},
		{0x46,0x2D},
		{0x47,0x0F},
		{0x48,0x3C},
		{0x49,0x00},
		{0x4A,0x1E},
		{0x4B,0x00},
		{0x4C,0x20},
		{0x4D,0x00},
		{0x4E,0x1A},
		{0x4F,0x14},
		{0x50,0x00},
		{0x51,0x10},
		{0x52,0x00},
		{0x5C,0x02},
		{0x5D,0x00},
		{0x5E,0x10},
		{0x5F,0x3F},
		{0x60,0x27},
		{0x61,0x28},
		{0x62,0x00},
		{0x63,0x03},
		{0x64,0xF7},
		{0x65,0x03},
		{0x66,0xD9},
		{0x67,0x03},
		{0x68,0x01},
		{0x69,0xC8},
		{0x6A,0x40},
		{0x6D,0x04},
		{0x6E,0x00},
		{0x6F,0x00},
		{0x70,0x80},
		{0x71,0x00},
		{0x72,0x00},
		{0x73,0x00},
		{0x74,0xF0},
		{0x75,0x00},
		{0x80,0x42},
		{0x81,0x44},
		{0x82,0x04},
		{0x83,0x20},
		{0x84,0x20},
		{0x85,0x00},
		{0x86,0x10},
		{0x87,0x00},
		{0x88,0x05},
		{0x89,0x18},
		{0x8A,0x10},
		{0x8B,0x01},
		{0x8C,0x37},
		{0x8D,0x00},
		{0x8E,0xF0},
		{0x8F,0x81},
		{0x90,0x06},
		{0x91,0x06},
		{0x92,0x1E},
		{0x93,0x0D},
		{0x94,0x0A},
		{0x95,0x0A},
		{0x96,0x0C},
		{0x97,0x05},
		{0x98,0x0A},
		{0x99,0x41},
		{0x9A,0x14},
		{0x9B,0x0A},
		{0x9C,0x3F},
		{0x9D,0x33},
		{0x9E,0xAE},
		{0x9F,0xF9},
		{0xA0,0x48},
		{0xA1,0x13},
		{0xA2,0x10},
		{0xA3,0x08},
		{0xA4,0x30},
		{0xA5,0x19},
		{0xA6,0x10},
		{0xA7,0x08},
		{0xA8,0x24},
		{0xA9,0x04},
		{0xAA,0x1E},
		{0xAB,0x1E},
		{0xCC,0x19},
		{0xCD,0x0B},
		{0xCE,0x13},
		{0xCF,0x64},
		{0xD0,0x21},
		{0xD1,0x0F},
		{0xD2,0x88},
		{0xE0,0x01},
		{0xE1,0x04},
		{0xE2,0x41},
		{0xE3,0xD6},
		{0xE4,0x00},
		{0xE5,0x0C},
		{0xE6,0x0A},
		{0xE7,0x00},
		{0xE8,0x00},
		{0xE9,0x00},
		{0xEE,0x07},
		{0xEF,0x01},
		{0x00,0x1E},
		{0x01,0x1E},
		{0x02,0x0F},
		{0x03,0x10},
		{0x04,0x02},
		{0x05,0x00},
		{0x06,0xB0},
		{0x07,0x04},
		{0x08,0x0D},
		{0x09,0x0E},
		{0x0A,0x9C},
		{0x0B,0x04},
		{0x0C,0x05},
		{0x0D,0x0F},
		{0x0E,0x02},
		{0x0F,0x12},
		{0x10,0x02},
		{0x11,0x02},
		{0x12,0x00},
		{0x13,0x01},
		{0x14,0x05},
		{0x15,0x07},
		{0x16,0x05},
		{0x17,0x07},
		{0x18,0x01},
		{0x19,0x04},
		{0x1A,0x05},
		{0x1B,0x0C},
		{0x1C,0x2A},
		{0x1D,0x01},
		{0x1E,0x00},
		{0x21,0x00},
		{0x22,0x00},
		{0x23,0x00},
		{0x25,0x01},
		{0x26,0x00},
		{0x27,0x39},
		{0x28,0x7F},
		{0x29,0x08},
		{0x30,0x03},
		{0x31,0x00},
		{0x32,0x1A},
		{0x33,0x1A},
		{0x34,0x07},
		{0x35,0x07},
		{0x36,0x01},
		{0x37,0xFF},
		{0x38,0x36},
		{0x39,0x07},
		{0x3A,0x00},
		{0x3E,0xFF},
		{0x3F,0x00},
		{0x40,0x77},
		{0x41,0x40},
		{0x42,0x00},
		{0x43,0x30},
		{0x44,0xA0},
		{0x45,0x5C},
		{0x46,0x00},
		{0x47,0x00},
		{0x48,0x58},
		{0x4A,0x1E},
		{0x4B,0x1E},
		{0x4C,0x00},
		{0x4D,0x00},
		{0x4E,0xA0},
		{0x4F,0x80},
		{0x50,0x00},
		{0x51,0x00},
		{0x52,0x00},
		{0x53,0x00},
		{0x54,0x00},
		{0x57,0x80},
		{0x59,0x10},
		{0x5A,0x08},
		{0x5B,0x94},
		{0x5C,0xE8},
		{0x5D,0x08},
		{0x5E,0x3D},
		{0x5F,0x99},
		{0x60,0x45},
		{0x61,0x40},
		{0x63,0x2D},
		{0x64,0x02},
		{0x65,0x96},
		{0x66,0x00},
		{0x67,0x97},
		{0x68,0x01},
		{0x69,0xCD},
		{0x6A,0x01},
		{0x6B,0xB0},
		{0x6C,0x04},
		{0x6D,0x2C},
		{0x6E,0x01},
		{0x6F,0x32},
		{0x71,0x00},
		{0x72,0x01},
		{0x73,0x35},
		{0x74,0x00},
		{0x75,0x33},
		{0x76,0x31},
		{0x77,0x01},
		{0x7C,0x84},
		{0x7D,0x03},
		{0x7E,0x01},
};

#define INIT_REG_ARRAY_SIZE \
		( sizeof( init_register_array ) / sizeof( init_register_array[0] ) )

/* Constructor */
Paj7620::Paj7620( const char* i2c_dev ):
		gesture_combined( 0 ), _i2c_fd( 0 )
{

	_i2c_fd = open( i2c_dev, O_RDWR );

	if( _i2c_fd < 0 )
		exit( 1 );

}


/* Initialize sensor */
int Paj7620::initSensor( void )
{
	unsigned int i = 0;
	uint8_t _buff[2] = {};

    LOGD(GESTURE_TAG, "+%s", __func__ );

	/* Acquire I2C bus and probe the slave device */
	if( ioctl( _i2c_fd, I2C_SLAVE, GROVE_GESTURE_SENSOR ) < 0 )
	{
		LOGD(GESTURE_TAG, "Failed to acquire bus access or talk to slave." );
		return 0;
	}

	/* Allow sensor to properly power-up */
	usleep( 1 * 1000 );

	wakeupSensor();

	if( wakeupSensor() < 0 )
	{
		LOGD(GESTURE_TAG, "Failed to wake up the sensor." );
		return 0;
	}

	if( _select_bank( BANK0 ) < 0 )
	{
		LOGD(GESTURE_TAG, "Failed to select back." );
		return 0;
	}

	if( _read_reg( 0, 2, _buff ) < 0 )
	{
		LOGD(GESTURE_TAG, "Failed _read_reg( 0, 2, _buff )." );
		return 0;
	}
	else if( ( _buff[0] != 0x20 ) || ( _buff[1] != 0x76 ) )
	{
	    LOGD(GESTURE_TAG, "Invalid address."  );
		return 0;
	}

	for( i = 0; i < INIT_REG_ARRAY_SIZE; i++ )
	{
		_write_reg( init_register_array[i][0], init_register_array[i][1] );
	}

	if( _select_bank( BANK0 ) < 0 )
    {
    	LOGD(GESTURE_TAG, "Failed to wake up the sensor." );
    	return 0;
    }

    LOGD(GESTURE_TAG, "-%s", __func__ );

	return 1;
}


/* Wake up / sleep the sensor */
int Paj7620::wakeupSensor( void )
{

	LOGD(GESTURE_TAG, "+%s", __func__ );

	/* Wakeup sensor by writing I2C slave ID */

    if( i2c_smbus_write_byte( _i2c_fd, GROVE_GESTURE_SENSOR ) < 0 )
    {
    	LOGD(GESTURE_TAG, "Failed i2c_smbus_write_byte( _i2c_fd, %d )", GROVE_GESTURE_SENSOR );
    	return( -1 );
    }

	LOGD(GESTURE_TAG, "-%s", __func__);
    return( 0 );
}


/* Read Sensor */
int Paj7620::readSensor( void )
{
    uint8_t data[2];
	int value = 0;

    if (!_read_reg( PAJ7620_ADDR_GES_PS_DET_FLAG_0, 1, data)) {
        if (data[0]) {
          	switch (data[0]) {
            	case GES_RIGHT_FLAG:
            		 value = GESTURE_RIGHT;
             		 break;

            	case GES_LEFT_FLAG:
  					value = GESTURE_LEFT;
              		break;

            	case GES_UP_FLAG:
            		value = GESTURE_UP;
             		 break;

            	case GES_DOWN_FLAG:
 					value = GESTURE_DOWN;
             		break;

            	case GES_FORWARD_FLAG:
            	     value = GESTURE_FORWARD;
              		 break;

            	case GES_BACKWARD_FLAG:
 	             	value = GESTURE_BACKWARD;
              		break;

            	case GES_CLOCKWISE_FLAG:
              		value = GESTURE_CLOCKWISE;
              		break;

            	case GES_COUNT_CLOCKWISE_FLAG:
              		value = GESTURE_COUNTER_CLOCKWISE;
             		break;

            	default:
              		break;
           }
        }
    }


    if (!_read_reg( PAJ7620_ADDR_GES_PS_DET_FLAG_1, 1, data)) {
        if (data[0] == GES_WAVE_FLAG) {
          	value = GESTURE_WAVE;
       	}
    }


	//LOGD(GESTURE_TAG, "read gesture is %d ", value );

	return value;
}


/* Select register bank */
int Paj7620::_select_bank( uint8_t bank )
{
	int ret;

	ret = _write_reg( PAJ7620_REGITER_BANK_SEL, bank );

	return( ret );
}


/* Write to register over I2C bus */
int Paj7620::_write_reg( uint8_t address, uint8_t val )
{

	if( i2c_smbus_write_byte_data( _i2c_fd, address, val ) < 0 )
	{
		LOGD(GESTURE_TAG, "Failed i2c_smbus_write_byte_data( _i2c_fd, address, val ).");
		return( -1 );
	}


	return( 0 );
}


/* Read register over I2C bus */
int Paj7620::_read_reg( uint8_t address, int num, uint8_t _buff[] )
{
	int i = 0;

	//LOGD(GESTURE_TAG, "+%s", __func__);

	for( i = 0; i < num; i++ )
	{
		if( i2c_smbus_write_byte( _i2c_fd, ( address + i ) ) < 0 )
		{
			LOGD(GESTURE_TAG, "Failed i2c_smbus_write_byte( _i2c_fd, address ).");
			return( -1 );
		}

		if( ( _buff[i] = i2c_smbus_read_byte( _i2c_fd ) ) < 0 )
		{
			LOGD(GESTURE_TAG, "Failed i2c_smbus_read_byte( _i2c_fd )");
			return( -1 );
		}
	}

	//LOGD(GESTURE_TAG, "-%s", __func__);

    return( 0 );
}


/* ==== End of file ======================================================== */
