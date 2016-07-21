/*
 * Paj7620.h
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
 * @file		PAJ7620.h
 *
 * @brief		I2C Gesture sensor driver
 *
 * @date		Jun 6, 2015, 7:57:28 AM
 *
 * @details		Identifies UP/DOWN/LEFT/RIGHT/FORWARD/BACKWARD/
 * 				CLOCKWISE/COUNTER_CLOCKWISE gestures
 */

#ifndef PAJ7260_H_
#define PAJ7260_H_

#ifdef	__cplusplus
extern "C" {
#endif

/* ---- System headers ------------------------------------------------------ */
#include <stdlib.h>
#include <stdint.h>

/* ---- Local headers ------------------------------------------------------- */

// DEVICE ID
#define GROVE_GESTURE_SENSOR  				0x73

#define BIT(x)  							1 << x

// REGISTER DESCRIPTION
#define PAJ7620_VAL(val, maskbit)			( val << maskbit )
#define PAJ7620_ADDR_BASE					0x00

// REGISTER BANK SELECT
#define PAJ7620_REGITER_BANK_SEL			(PAJ7620_ADDR_BASE + 0xEF)	//W

// REGISTER BANK 0
#define PAJ7620_ADDR_SUSPEND_CMD			(PAJ7620_ADDR_BASE + 0x3)	//W
#define PAJ7620_ADDR_GES_PS_DET_MASK_0		(PAJ7620_ADDR_BASE + 0x41)	//RW
#define PAJ7620_ADDR_GES_PS_DET_MASK_1		(PAJ7620_ADDR_BASE + 0x42)	//RW
#define PAJ7620_ADDR_GES_PS_DET_FLAG_0		(PAJ7620_ADDR_BASE + 0x43)	//R
#define PAJ7620_ADDR_GES_PS_DET_FLAG_1		(PAJ7620_ADDR_BASE + 0x44)	//R
#define PAJ7620_ADDR_STATE_INDICATOR		(PAJ7620_ADDR_BASE + 0x45)	//R
#define PAJ7620_ADDR_PS_HIGH_THRESHOLD		(PAJ7620_ADDR_BASE + 0x69)	//RW
#define PAJ7620_ADDR_PS_LOW_THRESHOLD		(PAJ7620_ADDR_BASE + 0x6A)	//RW
#define PAJ7620_ADDR_PS_APPROACH_STATE		(PAJ7620_ADDR_BASE + 0x6B)	//R
#define PAJ7620_ADDR_PS_RAW_DATA			(PAJ7620_ADDR_BASE + 0x6C)	//R

// REGISTER BANK 1
#define PAJ7620_ADDR_PS_GAIN				(PAJ7620_ADDR_BASE + 0x44)	//RW
#define PAJ7620_ADDR_IDLE_S1_STEP_0			(PAJ7620_ADDR_BASE + 0x67)	//RW
#define PAJ7620_ADDR_IDLE_S1_STEP_1			(PAJ7620_ADDR_BASE + 0x68)	//RW
#define PAJ7620_ADDR_IDLE_S2_STEP_0			(PAJ7620_ADDR_BASE + 0x69)	//RW
#define PAJ7620_ADDR_IDLE_S2_STEP_1			(PAJ7620_ADDR_BASE + 0x6A)	//RW
#define PAJ7620_ADDR_OP_TO_S1_STEP_0		(PAJ7620_ADDR_BASE + 0x6B)	//RW
#define PAJ7620_ADDR_OP_TO_S1_STEP_1		(PAJ7620_ADDR_BASE + 0x6C)	//RW
#define PAJ7620_ADDR_OP_TO_S2_STEP_0		(PAJ7620_ADDR_BASE + 0x6D)	//RW
#define PAJ7620_ADDR_OP_TO_S2_STEP_1		(PAJ7620_ADDR_BASE + 0x6E)	//RW
#define PAJ7620_ADDR_OPERATION_ENABLE		(PAJ7620_ADDR_BASE + 0x72)	//RW

// PAJ7620_REGITER_BANK_SEL
#define PAJ7620_BANK0						PAJ7620_VAL(0,0)
#define PAJ7620_BANK1						PAJ7620_VAL(1,0)

// PAJ7620_ADDR_SUSPEND_CMD
#define PAJ7620_I2C_WAKEUP					PAJ7620_VAL(1,0)
#define PAJ7620_I2C_SUSPEND					PAJ7620_VAL(0,0)

// PAJ7620_ADDR_OPERATION_ENABLE
#define PAJ7620_ENABLE						PAJ7620_VAL(1,0)
#define PAJ7620_DISABLE						PAJ7620_VAL(0,0)


enum {
	PAJ7620_SLEEP							= 0,
	PAJ7620_WAKEUP							= 1,
};


typedef enum {
	BANK0 = 0,
	BANK1,
} bank_e;


enum {
	// REGISTER 0
	GES_RIGHT_FLAG			 				= BIT(0),
	GES_LEFT_FLAG							= BIT(1),
	GES_UP_FLAG				 				= BIT(2),
	GES_DOWN_FLAG			 				= BIT(3),
	GES_FORWARD_FLAG						= BIT(4),
	GES_BACKWARD_FLAG		 				= BIT(5),
	GES_CLOCKWISE_FLAG		 				= BIT(6),
	GES_COUNT_CLOCKWISE_FLAG 				= BIT(7),
	//REGISTER 1
	GES_WAVE_FLAG							= BIT(0),
};

enum {
	GESTURE_RIGHT			 				= BIT(0),
	GESTURE_LEFT							= BIT(1),
	GESTURE_UP				 				= BIT(2),
	GESTURE_DOWN			 				= BIT(3),
	GESTURE_FORWARD						    = BIT(4),
	GESTURE_BACKWARD		 				= BIT(5),
	GESTURE_CLOCKWISE		 				= BIT(6),
	GESTURE_COUNTER_CLOCKWISE 				= BIT(7),
	GESTURE_WAVE 							= BIT(8),
	GESTURE_LEFT_RIGHT						= BIT(9),
	GESTURE_RIGHT_LEFT						= BIT(10),
	GESTURE_DOWN_UP							= BIT(11),
	GESTURE_UP_DOWN							= BIT(12),
};


/**
 * @defgroup DOXYGEN_GROUP_PAJ7620 PAJ7620
 * @ingroup DOXYGEN_GROUP_SENSORS
 * @{
 */

class Paj7620 {

public:

	uint8_t gesture_combined;


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Initialize the driver
	 *
	 * -------------------------------------------------------------------------
	 */
	Paj7620( const char* i2c_dev );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Initialize the sensor
	 *
	 * @return		0 if success, -1 if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int initSensor( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Wake up / sleep the sensor
	 *
	 * @return		0 if success, -1 if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int wakeupSensor( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief		Read Sensor
	 *
	 * @return		0 if success, -1 if not
	 *
	 * -------------------------------------------------------------------------
	 */
	int readSensor( void );


	/**
	 * -------------------------------------------------------------------------
	 *
	 * @brief	Destructor
	 *
	 * -------------------------------------------------------------------------
	 */
	~Paj7620( ) {}


private:

	/* I2C file descriptor */
	int _i2c_fd;
	int _write_reg( uint8_t address, uint8_t val );
	int _read_reg( uint8_t address, int num, uint8_t _buff[] );

	int _select_bank( uint8_t bank );

};

/**@}*/	/* DOXYGEN_GROUP_PAJ7620 */

#ifdef	__cplusplus
}
#endif

#endif /* PAJ7260_H_ */

/* ==== End of file ======================================================== */
