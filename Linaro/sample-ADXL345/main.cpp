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
 * @file		main.cpp
 *
 * @brief		ADXL345 (Accelerometer) test code
 *
 * @date		May 29, 2015, 1:45:06 AM
 *
 * @details		Prints ADXL's raw and acceleration values
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

#include "ADXL345.h"

#define LED1    "/sys/class/leds/led1/brightness"
#define LED2    "/sys/class/leds/led2/brightness"
#define LED3    "/sys/class/leds/led3/brightness"
#define BOOT    "/sys/class/leds/boot/brightness"

#define I2C0    "/dev/i2c-0"

/* ---- Function prototypes ------------------------------------------------- */
int adxl_init( void );

ADXL345 adxl( I2C0 );

int main( void )
{
	int x = 0, y = 0, z = 0;
	double xyz[3] = {0};
	double ax = 0, ay = 0, az = 0;

	int led1_fd = open( LED1, O_WRONLY);
	int led2_fd = open( LED2, O_WRONLY);
	int led3_fd = open( LED3, O_WRONLY);
	int boot_fd = open( BOOT, O_WRONLY);

	if( adxl_init() < 0 )
	{
		perror( "ADXL initialization failed." );
		exit( 1 );
	}

	while( 1 )
	{
		write( led1_fd, "1", 2 );
		write( led2_fd, "0", 2 );
		write( led3_fd, "1", 2 );
		write( boot_fd, "0", 2 );

		usleep( 100 * 1000 );

		write( led1_fd, "0", 2 );
		write( led2_fd, "1", 2 );
		write( led3_fd, "0", 2 );
		write( boot_fd, "1", 2 );

		usleep( 100 * 1000 );

		/* Read & print RAW values from ADXL */
		adxl.readXYZ(&x, &y, &z);
		printf( "Raw values of X = %d, Y = %d, Z = %d\n\r", x, y, z );

		/* Calculate & print acceleration */
		adxl.getAcceleration(xyz);

		ax = xyz[0];
		ay = xyz[1];
		az = xyz[2];

		printf( "Acceleration of X = %lg, Y = %lg, Z = %lg\n\r", ax, ay, az );

		puts( "------------------------------------------------------\n\r" );
	}
}

int adxl_init( void )
{
	if( adxl.powerOn() < 0 )
	{
		printf( "ADXL initialization failed" );
		return( -1 );
	}

	//set activity/ inactivity thresholds (0-255)
	adxl.setActivityThreshold(75); //62.5mg per increment
	adxl.setInactivityThreshold(75); //62.5mg per increment
	adxl.setTimeInactivity(10); // how many seconds of no activity is inactive?

	//look of activity movement on this axes - 1 == on; 0 == off
	adxl.setActivityX(1);
	adxl.setActivityY(1);
	adxl.setActivityZ(1);

	//look of inactivity movement on this axes - 1 == on; 0 == off
	adxl.setInactivityX(1);
	adxl.setInactivityY(1);
	adxl.setInactivityZ(1);

	//look of tap movement on this axes - 1 == on; 0 == off
	adxl.setTapDetectionOnX(0);
	adxl.setTapDetectionOnY(0);
	adxl.setTapDetectionOnZ(1);

	//set values for what is a tap, and what is a double tap (0-255)
	adxl.setTapThreshold(50); //62.5mg per increment
	adxl.setTapDuration(15); //625us per increment
	adxl.setDoubleTapLatency(80); //1.25ms per increment
	adxl.setDoubleTapWindow(200); //1.25ms per increment

	//set values for what is considered freefall (0-255)
	adxl.setFreeFallThreshold(7); //(5 - 9) recommended - 62.5mg per increment
	adxl.setFreeFallDuration(45); //(20 - 70) recommended - 5ms per increment

	//setting all interrupts to take place on int pin 1
	//I had issues with int pin 2, was unable to reset it
	adxl.setInterruptMapping( ADXL345_INT_SINGLE_TAP_BIT,   ADXL345_INT1_PIN );
	adxl.setInterruptMapping( ADXL345_INT_DOUBLE_TAP_BIT,   ADXL345_INT1_PIN );
	adxl.setInterruptMapping( ADXL345_INT_FREE_FALL_BIT,    ADXL345_INT1_PIN );
	adxl.setInterruptMapping( ADXL345_INT_ACTIVITY_BIT,     ADXL345_INT1_PIN );
	adxl.setInterruptMapping( ADXL345_INT_INACTIVITY_BIT,   ADXL345_INT1_PIN );

	//register interrupt actions - 1 == on; 0 == off
	adxl.setInterrupt( ADXL345_INT_SINGLE_TAP_BIT, 1);
	adxl.setInterrupt( ADXL345_INT_DOUBLE_TAP_BIT, 1);
	adxl.setInterrupt( ADXL345_INT_FREE_FALL_BIT,  1);
	adxl.setInterrupt( ADXL345_INT_ACTIVITY_BIT,   1);
	adxl.setInterrupt( ADXL345_INT_INACTIVITY_BIT, 1);

	return( 0 );
}

/* ==== End of file ========================================================= */
