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
 * @file		tcs3414cs.h
 *
 * @brief		I2C Color sensor driver
 *
 * @date		Jun 1, 2015, 10:55:45 PM
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

#ifndef TCS3414CS_H_
#define TCS3414CS_H_

#ifdef	__cplusplus
extern "C" {
#endif

/* ---- System headers ------------------------------------------------------ */
#include <stdlib.h>
#include <stdint.h>

/* ---- Local headers ------------------------------------------------------- */

#define GROVE_COLOR_SENSOR	0x39

#define TCS3414_REG_COMMAND		0x80
#define TCS3414_REG_CONTROL		0x00
#define TCS3414_REG_TIMING		0x01
#define TCS3414_REG_INT_CTRL	0x02
#define TCS3414_REG_INT_SRC		0x03
#define TCS3414_REG_ID			0x04
#define TCS3414_REG_GAIN		0x07
#define TCS3414_REG_LTHRESH_LB	0x08
#define TCS3414_REG_LTHRESH_HB	0x09
#define TCS3414_REG_HTHRESH_LB	0x0A
#define TCS3414_REG_HTHRESH_HB	0x0B
#define TCS3414_REG_GREEN_LOW	0x10
#define TCS3414_REG_GREEN_HIGH	0x11
#define TCS3414_REG_RED_LOW		0x12
#define TCS3414_REG_RED_HIGH	0x13
#define TCS3414_REG_BLUE_LOW	0x14
#define TCS3414_REG_BLUE_HIGH	0x15
#define TCS3414_REG_CLEAR_LOW	0x16
#define TCS3414_REG_CLEAR_HIGH	0x17

#define TCS3414CS_TIM_INTEG_FREE	0x00
#define TCS3414CS_TIM_INTEG_MANUAL	0x10
#define TCS3414CS_TIM_INTEG_SINGLE	0x20
#define TCS3414CS_TIM_INTEG_MULTI	0x30

#define TCS3414CS_CMD_PROT_BYTE		0x00
#define TCS3414CS_CMD_PROT_WORD		0x20
#define TCS3414CS_CMD_PROT_BLOCK	0x40
#define TCS3414CS_CMD_INT_CLR		0x60

#define TCS3414CS_CTRL_ADC_VALID	0x10
#define TCS3414CS_CTRL_ADC_EN		0x02
#define TCS3414CS_CTRL_POWER_ON		0x01

#define TCS3414CS_GAIN_1X			0x00
#define TCS3414CS_GAIN_4X			0x10f
#define TCS3414CS_GAIN_16X			0x20
#define TCS3414CS_GAIN_64X			0x30
#define TCS3414CS_GAIN_DIV1			0x00
#define TCS3414CS_GAIN_DIV2			0x01
#define TCS3414CS_GAIN_DIV4			0x02
#define TCS3414CS_GAIN_DIV8			0x03
#define TCS3414CS_GAIN_DIV16		0x04
#define TCS3414CS_GAIN_DIV32		0x05
#define TCS3414CS_GAIN_DIV64		0x06


/**
 * @defgroup DOXYGEN_GROUP_TCS3414CS TCS3414CS
 * @ingroup DOXYGEN_GROUP_SENSORS
 * @{
 */

class Tcs3414cs {

public:


	float x, y, z;
	int red, green, blue, clear;


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Initialize the driver
	 *
	 * -------------------------------------------------------------------------
	 */
	Tcs3414cs( const char* i2c_dev );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Initialize the sensor
	 *
	 * @return		true if sensor is connected, false if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int initSensor( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Read RGB & CLear values
	 *
	 * @return		0 if success, -1 if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int readSensor( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Calculate coordinate
	 *
	 * -------------------------------------------------------------------------
	 */
	void calculateCoordinate( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Clear interrupt
	 *
	 * @return		0 if success, -1 if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int clearInterrupt( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief	Destructor
	 *
	 * -------------------------------------------------------------------------
	 */
	~Tcs3414cs( ) {}

private:

	/* I2C file descriptor */
	int _i2c_fd;
	int _write_reg( uint8_t address, uint8_t val );
	int _read_reg( uint8_t address, int num, uint8_t _buff[] );

};

/**@}*/	/* DOXYGEN_GROUP_TCS3414CS */

#ifdef	__cplusplus
}
#endif

#endif /* TCS3414CS_H_ */

/* ==== End of file ========================================================= */