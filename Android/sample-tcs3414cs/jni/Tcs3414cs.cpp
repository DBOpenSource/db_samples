/******************************************************************************
 * Copyright (c) 2015 Ashwin Vijayakumar
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
 * @file		tcs3414cs.cpp
 *
 * @brief		I2C Color sensor driver
 *
 * @date		Jun 1, 2015, 10:48:56 PM
 *
 * @details		Returns 16-bit Red, Green, Blue and Clear values
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
#include "tcs3414cs.h"
#include "i2c-dev.h"
//#include "common/header.h"

/* Constructor */
Tcs3414cs::Tcs3414cs( const char* i2c_dev ):
	x( 0 ), y( 0 ), z(0), red( 0 ), green( 0 ), blue( 0 ), clear( 0 ), _i2c_fd( 0 )
{
	_i2c_fd = open( i2c_dev, O_RDWR );

	if( _i2c_fd < 0 )
		exit( 1 );

}

/* Initialize sensor */
int Tcs3414cs::initSensor( void )
{
	/* Acquire I2C bus and probe the slave device */
	if( ioctl( _i2c_fd, I2C_SLAVE, GROVE_COLOR_SENSOR ) < 0 )
	{
		perror( "Failed to acquire bus access and/or talk to slave." );
		return 0;
	}
	/* Enable ADC and power ON the sensor */
	int result = _write_reg( TCS3414_REG_CONTROL, ( TCS3414CS_CTRL_ADC_EN | TCS3414CS_CTRL_POWER_ON ) );
	if (result == -1) {
		return 0;
	}

	/* Set ADC gain and prescalar */
	_write_reg( TCS3414_REG_GAIN, ( TCS3414CS_GAIN_1X | TCS3414CS_GAIN_DIV4 ) );

	return 1;
}


/* Read sensor values */
int Tcs3414cs::readSensor( void )
{
	uint8_t buff8[9];

	if( _read_reg( TCS3414_REG_CONTROL, 1, buff8 ) != 0 ) {
    	return -1;
    }

    printf( "register control  0x%X", buff8[0]);
    if (buff8[0] != 0x13) {
        printf( "ADC not ready");
    	return -1;
    }

	if( _read_reg( 0x0F, 9, buff8 ) <0 ) {
		printf( "read color failed " );
		return -1;
	}

	green	= ( buff8[2] * 256 ) + buff8[1];
	red 	= ( buff8[4] * 256 ) + buff8[3];
	blue	= ( buff8[6] * 256 ) + buff8[5];
	clear	= ( buff8[8] * 256 ) + buff8[7];

  //  printf( " %d %d %d %d ", buff8[0], red, green, blue );


	/*for (int i=0; i<8; i++) {
		printf( "0x%X  ", buff8[i] );
	}*/

	if (red > 200 || blue > 200 || green > 200) {
		printf( "Ignore bad data %d %d %d ", red, green, blue );
		return -1;
	}

//	printf( "%d %d %d ", red, green, blue );
	return 0;
}


/* Calculate Coordinate */
void Tcs3414cs::calculateCoordinate( void )
{
	float X, Y, Z;

	X = (-0.14282) * red + (1.54924) * green + (-0.95641) * blue;
	Y = (-0.32466) * red + (1.57837) * green + (-0.73191) * blue;
	Z = (-0.68202) * red + (0.77073) * green + (0.56332) * blue;
	printf( "%0.6f %0.6f %0.6f ", X, Y, Z );


	x = X / ( X + Y + Z );
	y = Y / ( X + Y + Z );
	z = Z / ( X + Y + Z );

	printf( "%0.6f %0.6f %0.6f ", x, y, z );
}


int Tcs3414cs::clearInterrupt( void )
{
    if( i2c_smbus_write_byte( _i2c_fd, ( TCS3414_REG_COMMAND | TCS3414CS_CMD_INT_CLR ) ) < 0 )
    {
		perror("Failed i2c_smbus_write_byte( _i2c_fd, CLR_INT ).\n");
    	return( -1 );
    }
    return( 0 );
}


/* Write to register over I2C bus */
int Tcs3414cs::_write_reg( uint8_t address, uint8_t val )
{
    if( i2c_smbus_write_byte_data( _i2c_fd, ( TCS3414_REG_COMMAND | address ), val ) < 0 )
    {
		perror("Failed i2c_smbus_write_byte_data( _i2c_fd, address, val ).\n");
    	return( -1 );
    }
    else
    	return( 0 );
}


/* Read register over I2C bus */
int Tcs3414cs::_read_reg( uint8_t address, int num, uint8_t _buff[] )
{
	struct i2c_rdwr_ioctl_data i2c_data;
    struct i2c_msg msg[2] = {};
    char temp_buff[2] = {0, 0};

    i2c_data.msgs = msg;
    i2c_data.nmsgs = 2;

    temp_buff[0] = TCS3414_REG_COMMAND  | address;
    i2c_data.msgs[0].addr = GROVE_COLOR_SENSOR;
    i2c_data.msgs[0].flags = 0;
    i2c_data.msgs[0].len = 1;
    i2c_data.msgs[0].buf = temp_buff;

    i2c_data.msgs[1].addr = GROVE_COLOR_SENSOR;
    i2c_data.msgs[1].flags = I2C_M_RD;
    i2c_data.msgs[1].len = num;
    i2c_data.msgs[1].buf = (char*)_buff;

	if( ioctl( _i2c_fd, I2C_RDWR, &i2c_data ) < 0 )
	{
		perror( "I2C_RDWR Failed" );
		return( -1 );
	}
	else
		return( 0 );
}

/* ==== End of file ========================================================= */
