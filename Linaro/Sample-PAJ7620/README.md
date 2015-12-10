PAJ7620 - Gesture sensor.
=======================
 
  Driver files and sample userspace application for the Groove Gesture sensor (PAJ7620).

## To build: 
 1. Download a recent Eclipse IDE with CDT (eg: https://eclipse.org/cdt/downloads.php) 
 2. Download the Linaro GCC tool chain for AARCH64 (https://releases.linaro.org/14.04/components/toolchain/binaries/) 
 3. Create a New C++ Project -> Executable -> Toolchain (Select Cross GCC) -> Provide a project name.
 4. In the window for 'Cross Compiler prefix' select prefix as : "aarch64-linux-gnu-"
    Provide the Cross compiler Path to be the root directory where you installed the tool chain (Step2)
 5. Add the sample files into the new project and compile.
 
 
## To Test:
 1. Connect the Grove sensor with I2C interface. See Interface diagram below.
 2. Get the IP address of the DB410c by following steps here : https://github.com/96boards/documentation/wiki/Sharing-Internet-connections-over-USB-on-96Boards#method-1-usb-ethernet-adapter 
 3. $scp push Debug/Samples-PAJ7620 linaro@<ipaddress>:/home/linaro 
 (use the default linaro password)
 5. $ ssh linaro@<ipaddress> 
 6. root@linaro-alip:~# cd /home/linaro 
 7. verify that the I2C hardware connections by running the following command:
 root@linaro-alip:~# i2cdetect -r 0 
    Continue? [Y/n] y
     
     - 0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f

     -20: -- -- -- -- -- -- -- -- -- 73 -- -- -- -- -- --

     - 60: -- -- -- -- -- -- -- -- -- -- -- -- -- -- -- --

 9. root@linaro-alip:~# chmod 755 /home/linaro/Sample-PAJ7620 
 10.root@linaro-alip:~# sudo Sample-PAJ7620 
 
## Observe: 
 - If successful you should see a series of prints with the Gesture that is detected.. 
 
## Sensor hardware setup:
 ![] (./../images/410c_I2C_interface.png)
  
