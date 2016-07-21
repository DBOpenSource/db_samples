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
 * @file		DemoUtils.java
 *
 * @brief
 *
 * @date		Apr 22, 2016.
 *
 * @details		Utility class for rendering the sensors data..
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

package com.github.dbopensource.sensordemo;


import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;


public class DemoUtils {

    public static final String TAG = "DemoUtils";
    public static final long MAX_LUX = 40000;
    public static final int CIE_R_RANGE = 65;
    public static final int CIE_G_RANGE = 111;
    public static final int CIE_B_RANGE = 71;

    public static <E extends Enum<E>>  String[] convertEnumToArray(E enumClass) {
        String valuesStr = Arrays.toString(enumClass.getClass().getEnumConstants());

        return valuesStr.substring(1, valuesStr.length()-1).replace(" ", "").replace("_", " ").split(",");
    }

    public static int opposeColor(int ColorToInvert)
    {
        int RGBMAX = 255;

        float[] hsv = new float[3];
        float H;


        Color.RGBToHSV( Color.red(ColorToInvert),  RGBMAX - Color.green( ColorToInvert), Color.blue(ColorToInvert), hsv);

        H = (float) (hsv[0] + 0.5);

        if (H > 1) H -= 1;

        return Color.HSVToColor(hsv);


    }

    public static int addBrightnessToColor(int colorR, int colorG, int colorB, int brightness) {
        colorR += brightness;
        colorR = Math.min(colorR, 255);
        colorR = Math.max(colorR, 0);

        colorG += brightness;
        colorG = Math.min(colorG, 255);
        colorG = Math.max(colorG, 0);

        colorB += brightness;
        colorB = Math.min(colorB, 255);
        colorB = Math.max(colorB, 0);

        return Color.rgb(colorR, colorG, colorB);
    }

    public static int convertLuxToBrightness(long lux) {
        return Math.min(255, (int) (lux * 255.0 / MAX_LUX));
    }

    public static long convertBrightnessToLux(int brightness) {
        return (long)( MAX_LUX * brightness / 255 );
    }

    public static boolean isBitSet(int pos, int value) {
        return (((1 << pos) & value) >> pos) == 1;
    }

  /*  public static int cIEXyzToColor(float[] data) {
        if (data.length != 3) {
            return 0;
        }

        float sRGB[] = new float[3];
        ColorXyz.xyzToSrgb(data[0], data[1], data[2], sRGB);

        int r = Math.round(sRGB[0] * 255);
        int g = Math.round(sRGB[1] * 255);
        int b = Math.round(sRGB[2] *255);
        return Color.rgb(r, g, b);
    }*/

    public static int cIERgbToColor(int data) {
        int r = colorConversion(Color.red(data), CIE_R_RANGE);
        int g = colorConversion(Color.green(data), CIE_G_RANGE);
        int b = colorConversion(Color.blue(data), CIE_B_RANGE);
        return Color.rgb(r, g, b);
    }

    private static int colorConversion (int cieColor, int range) {
        float input = Math.min(cieColor, range);
        return (int)Math.round((input / range) * 255);
    }

}

