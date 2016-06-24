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
 * @file		Gesture.java
 *
 * @brief
 *
 * @date		Apr 22, 2016.
 *
 * @details		Service class used by the UI to update Gesture values from JNI..
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

package com.github.dbopensource.sensordemo.service;

import android.widget.Button;
import android.widget.ImageButton;

import com.github.dbopensource.sensordemo.DemoUtils;
import com.github.dbopensource.sensordemo.R;

public enum Gesture {
    Right(0),
    Left(1),
    Up(2),
    Down(3),
    Forward(4),
    Backward(5),
    Circle_Clockwise(6),
    Circle_Counter_Clockwise(7),
    Move(8),
    Left_to_Right(9),
    Right_to_Left(10),
    Down_to_Up(11),
    Up_to_Down(12);

    int bit;

    private Gesture(int bit) {
        this.bit = bit;
    }

    public int getIntValue() {
        return 1<<bit;
    }

    public int getBit() {
        return bit;
    }

    public static Gesture getGesture(int value) {
        for (Gesture gesture : Gesture.values()) {
            if (gesture.getIntValue() == value) {
                return gesture;
            }
        }
        return null;
    }

    public int getImageId() {
        switch (this) {
            case Down:
                return R.drawable.down;

            case Up:
                return R.drawable.up;

            case Left:
                return R.drawable.left;

            case Right:
                return R.drawable.right;

            case Circle_Clockwise:
                return R.drawable.clockwise;

            case Circle_Counter_Clockwise:
                return R.drawable.counterclockwise;

            case Left_to_Right:
                return R.drawable.left_right;

            case Right_to_Left:
                return R.drawable.right_left;

            case Down_to_Up:
                return R.drawable.down_up;

            case Up_to_Down:
                return R.drawable.up_down;

            case Forward:
                return R.drawable.forward;

            case Backward:
                return R.drawable.backward;

            default:
                return R.drawable.move;
        }
    }

    public static Gesture getGesture(ImageButton button) {
        switch (button.getId()) {
            case R.id.buttonDown:
                return Down;

            case R.id.buttonUp:
                return Up;

            case R.id.buttonLeft:
                return Left;

            case R.id.buttonRight:
                return Right;

            case R.id.buttonForward:
                return Forward;

            case R.id.buttonBackward:
                return Backward;

            case R.id.buttonLeftRight:
                return Left_to_Right;

            case R.id.buttonRightLeft:
                return Right_to_Left;

            case R.id.buttonUpDown:
                return Up_to_Down;

            case R.id.buttonDownUp:
                return Down_to_Up;

            case R.id.buttonClockwise:
                return Circle_Clockwise;

            case R.id.buttonCounterClockwise:
                return Circle_Counter_Clockwise;

            default:
                return Move;
        }
    }

    public int getButtonId() {
        switch (this) {
            case Down:
                return R.id.buttonDown;

            case Up:
                return R.id.buttonUp;

            case Left:
                return R.id.buttonLeft;

            case Right:
                return R.id.buttonRight;

            case Circle_Clockwise:
                return R.id.buttonClockwise;

            case Circle_Counter_Clockwise:
                return R.id.buttonCounterClockwise;

            case Left_to_Right:
                return R.id.buttonLeftRight;

            case Right_to_Left:
                return R.id.buttonRightLeft;

            case Down_to_Up:
                return R.id.buttonDownUp;

            case Up_to_Down:
                return R.id.buttonUpDown;

            case Forward:
                return R.id.buttonForward;

            case Backward:
                return R.id.buttonBackward;

            default:
                return R.id.buttonMove;
        }
    }
}
