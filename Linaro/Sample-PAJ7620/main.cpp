#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>

#include "PAJ7620.h"

#define I2C0    "/dev/i2c-0"


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

/*
Utility to print the Gesture that was detected
*/ 
void print_gesture(int value)
{
  
        char* gesture = NULL; 
       	switch (value){
       	case GESTURE_RIGHT:
                printf ("Right \n");
       		break;

      	case GESTURE_LEFT:
                printf( "Left \n");
      		break;

       	case GESTURE_UP:
                printf( "Up \n");
 	        break;

      	case GESTURE_DOWN:
                printf( "Down \n");
       		break;

      	case GESTURE_FORWARD:
                printf( "Forward \n");
                break;

        case GESTURE_BACKWARD:
                printf( "Backward \n");
      		break;

      	case GESTURE_CLOCKWISE:
                printf( "Clockwise \n");
       		break;

        case GESTURE_COUNTER_CLOCKWISE:
                printf( "Counter Clockwise \n");
             	break;

        default:
                printf("waiting \n"); 
          	break;
    }
  
}

int main(void)
{
    int result = -1;
    result = init_gesture();
    if (result > 0)
    {
      while (1)
      {
      int value = -1;
      value = gesture_sensor.readSensor();
      print_gesture(value);
      usleep( 100 * 1000 ); 
      }
    }
    
}
