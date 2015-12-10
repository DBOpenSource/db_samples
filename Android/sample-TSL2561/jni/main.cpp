/******************************************************************************
 * Copyright (c) 2015 Sujai SyrilRaj .
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
 * @brief		TSL2561 (Grove Light sensor ) test code
 *
 * @date		Dec 07, 2015.
 *
 * @details		Detects , parses and Prints TSL2561 's Lux readings.

 
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
#include "Digital_Light_TSL2561.h"


TSL2561_CalculateLux light;

/* ---- Function prototypes ------------------------------------------------- */
int init_light();
long read_lux();


int init_light()
{
	return light.init();
}

long read_lux()
{
	light.readVisibleLux();
}

int main(void)
{
    int result = -1;
    unsigned long lux = 0;
    result = init_light();
    if (result > 0)
    {
      while (1)
      {
      lux = read_lux();
      printf("The lux value is %ul\n",lux);
      usleep( 100 * 1000 ); 
      }
    }
    
}

/* ==== End of file ========================================================= */
