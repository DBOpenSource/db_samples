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
 * @file		Sensor3DFragment.java
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

import android.content.Context;

import android.content.SharedPreferences;
import android.graphics.Color;

import android.preference.PreferenceManager;
import android.util.Log;

import android.view.animation.LinearInterpolator;



import org.rajawali3d.ATransformable3D;
import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.IAnimationListener;
import org.rajawali3d.animation.TranslateAnimation3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.lights.SpotLight;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.methods.SpecularMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;


import java.util.ArrayDeque;
import java.util.Queue;

import com.github.dbopensource.sensordemo.SensorManagerFragment.Shapes;
import com.github.dbopensource.sensordemo.service.Gesture;
import com.github.dbopensource.sensordemo.service.Joystick;


public class Sensor3DFragment extends Abstract3DFragment implements SensorManagerFragment.OnDataChangeListner {
    private static final String TAG = "Sensor3DFragment";

    float brightnessOffset = 0.0f;

    public void updateSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String valueString = sharedPrefs.getString("key_brightness_offset", "0.0");
        try {
            float value = Float.valueOf(valueString);
            if (value >= 0) {
                getSensor3DRenderer().increaseLightOffset(value - brightnessOffset);
                brightnessOffset = value;
            }
        } catch (Exception e) {
            Log.d(TAG, valueString + " " + e.getLocalizedMessage());
        }
    }

    @Override
    public AbstractRenderer createRenderer() {
        return new Sensor3DRenderer(getActivity());
	}

    @Override
    public void onColorChange(int color) {
        Sensor3DRenderer renderer = getSensor3DRenderer();
        if (renderer.initiated) {
            renderer.changeMovingObjectColor(color);
        }
    }

    @Override
    public void onLightsChange(int brightness) {
        Sensor3DRenderer renderer = getSensor3DRenderer();
        if (renderer.initiated) {
            renderer.adjustLightPower(brightness);
        }
    }

    @Override
    public void onGestureChange(Gesture gesture) {
        Sensor3DRenderer renderer = getSensor3DRenderer();
        if (renderer.initiated) {
            renderer.changeMovingObjectDirection(gesture);
        }
    }

    @Override
    public void onJoystickChange(Joystick joystick) {
        Sensor3DRenderer renderer = getSensor3DRenderer();
        if(renderer.initiated) {
            renderer.changePointLightDirection(joystick);
        }
    }

    @Override
    public void onModelChange(Shapes shape) {
        Sensor3DRenderer renderer = getSensor3DRenderer();
        if (renderer.initiated) {
            renderer.changeMovingObject(shape);
        }
    }

    private Sensor3DRenderer getSensor3DRenderer () {
        return (Sensor3DRenderer)mRenderer;
    }

    private final class Sensor3DRenderer extends AbstractRenderer {
        private static final String TAG = "SensorReactionRenderer";

        private static final int DURATION = 500;
        private static final float OBJECT_SIZE = 0.7f;

        private PointLight pointLight;
        private DirectionalLight directionalLight;
		private Object3D movingObject;
        private Plane background;

        private int sensorColor = SensorManagerFragment.SENSOR_DEFAULT_COLOR;
        private Shapes currentShape = SensorManagerFragment.DEFAULT_SHAPE;

        private double maxX = 1;
        private double maxY = 1;
        private double maxZ = 3;
        private double pointLightZ = 5;

        boolean initiated;

		public Sensor3DRenderer(Context context) {
			super(context);
		}

        @Override
		protected void initScene() {

            DirectionalLight lightBack = new DirectionalLight(.3f, -.3f, -1);
            lightBack.setPower(.2f);
            getCurrentScene().addLight(lightBack);

           /* PointLight lightUp = new PointLight();
            lightUp.setPower(2f);
            lightUp.enableLookAt();
            lightUp.setColor(Color.RED);
            lightUp.setPosition(maxX, -(maxY + 1), 1);
            lightUp.setLookAt(0, 0, 0);
            getCurrentScene().addLight(lightUp);*/

            directionalLight = new DirectionalLight(0, 0, -1);
            directionalLight.setPower(brightnessToPower(SensorManagerFragment.BRIGHTNESS_DEFAULT));
            getCurrentScene().addLight(directionalLight);

            pointLight = new PointLight();
            pointLight.setPosition(0, (maxY + 1), pointLightZ);
            pointLight.setPower(1.0f);
			getCurrentScene().addLight(pointLight);

			getCurrentCamera().setPosition(0, 0, 7);

            addMovingObject();
            Vector3 pos = calculatePos(getViewportWidth(), getViewportHeight(), movingObject);
            maxX = pos.x;
            maxY = -pos.y;

            addBackground();

            initiated = true;
		}

        public void changeMovingObject(Shapes shape) {
            if (!currentShape.equals(shape)) {
                currentShape = shape;
                addMovingObject();
            }
        }

        public void changePointLightDirection(Joystick direction) {
            Vector3 toPos = pointLight.getPosition();
            switch (direction) {
                case Up:
                    toPos = new Vector3(0, (maxY + 1), 0);
                    break;

                case Down:
                    toPos = new Vector3(0, -(maxY + 1), 0);
                    break;

                case Left:
                    toPos = new Vector3(-(maxX + 1), 0, 0);
                    break;

                case Right:
                    toPos = new Vector3((maxX + 1), 0, 0);
                    break;

                default:
                    break;
            }

            if (pointLight.getPosition().equals(toPos)) {
                return;
            }

            Queue<Vector3> toPosQueue = new ArrayDeque<Vector3>();
            Vector3 pos1 = new Vector3(pointLight.getX(), pointLight.getY(), 0);
            toPosQueue.add(pos1);
            toPosQueue.add(toPos);
            Vector3 pos3 = new Vector3(toPos.x, toPos.y, pointLightZ);
            toPosQueue.add(pos3);
            moveObject(toPosQueue, DURATION / toPosQueue.size(), pointLight);

         /*   TranslateAnimation3D lightAnim = new TranslateAnimation3D(toPos);
            lightAnim.setDurationMilliseconds(DURATION);
            lightAnim.setRepeatMode(Animation.RepeatMode.NONE);
            lightAnim.setTransformable3D(pointLight);
            lightAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            getCurrentScene().registerAnimation(lightAnim);
            lightAnim.play();*/
        }

        public void changeMovingObjectDirection(Gesture gesture) {

            switch (gesture) {
                case Circle_Clockwise:
                case Circle_Counter_Clockwise:
                    moveCircle(gesture.equals(Gesture.Circle_Clockwise));
                    break;

                case Down_to_Up:
                case Up_to_Down:
                case Left_to_Right:
                case Right_to_Left:
                    twoGestureMove(gesture);
                    break;

                default:
                    singleGestureMove(gesture);
            }
        }

        private void twoGestureMove(Gesture gesture) {
            Vector3 toPos1 = movingObject.getPosition().clone();
            Vector3 toPos2 = toPos1.clone();
            toPos2.z = 0;

            switch (gesture) {
                case Down_to_Up:
                    toPos1.y = -maxY + OBJECT_SIZE;
                    toPos2.y = maxY - OBJECT_SIZE;
                    break;
                case Up_to_Down:
                    toPos1.y = maxY - OBJECT_SIZE;
                    toPos2.y = -maxY + OBJECT_SIZE;
                    break;
                case Left_to_Right:
                    toPos1.x = -maxX + OBJECT_SIZE;
                    toPos2.x = maxX - OBJECT_SIZE;
                    break;
                case Right_to_Left:
                    toPos1.x = maxX - OBJECT_SIZE;
                    toPos2.x = -maxX + OBJECT_SIZE;
                    break;
                default:
                    break;
            }

            Queue<Vector3> toPosQueue = new ArrayDeque<Vector3>();
            if (!toPos1.equals(movingObject.getPosition())) {
                toPosQueue.add(toPos1);
            }
            toPosQueue.add(toPos2);
            moveObject(toPosQueue, DURATION / toPosQueue.size(), movingObject);
        }

        private void singleGestureMove(Gesture gesture) {
            Vector3 toPos = movingObject.getPosition().clone();
            toPos.z = 0;
            switch (gesture) {
                case Up:
                    toPos.y = maxY - OBJECT_SIZE;
                    break;
                case Down:
                    toPos.y = -maxY + OBJECT_SIZE;
                    break;
                case Backward:
                    double z = movingObject.getZ() - maxZ;
                    toPos.z = (Math.abs(z) > maxZ) ? -maxZ : z;
                    toPos.x = 0;
                    toPos.y = 0;
                    break;
                case Forward:
                    z = movingObject.getZ() + maxZ;
                    toPos.z = (Math.abs(z) > maxZ) ? maxZ : z;
                    toPos.x = 0;
                    toPos.y = 0;
                    break;
                case Left:
                    toPos.x = -maxX + OBJECT_SIZE;
                    break;

                case Right:
                    toPos.x = maxX - OBJECT_SIZE;
                    break;

                case Move:
                    toPos = new Vector3(0, 0, 0);
                    break;

                default:
                    break;

            }

            Queue<Vector3> toPosQueue = new ArrayDeque<Vector3>();
            if (!toPos.equals(movingObject.getPosition())) {
                toPosQueue.add(toPos);
            }
            moveObject(toPosQueue, DURATION, movingObject);
        }

        private void moveObject(final Queue<Vector3> toPosQueue, final int duration, final ATransformable3D aObject) {
            if (toPosQueue.isEmpty()) {
                return;
            }

            Vector3 toPos = toPosQueue.remove();
            TranslateAnimation3D objectAnim = new TranslateAnimation3D(toPos);
            objectAnim.setDurationMilliseconds(duration);
            objectAnim.setRepeatMode(Animation.RepeatMode.NONE);
            objectAnim.setTransformable3D(aObject);
            objectAnim.setInterpolator(new LinearInterpolator());
            if (!toPosQueue.isEmpty()) {
                objectAnim.registerListener(new IAnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        moveObject(toPosQueue, duration, aObject);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationUpdate(Animation animation, double v) {

                    }
                });
            }
            getCurrentScene().registerAnimation(objectAnim);
            objectAnim.play();
        }

        private void moveCircle(boolean clockwise)  {

            EllipticalOrbitAnimation3D anim = new EllipticalOrbitAnimation3D(new Vector3(), new Vector3(0,
                    maxY * .5f, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0, 360,
                    clockwise ? EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE
                                 : EllipticalOrbitAnimation3D.OrbitDirection.COUNTERCLOCKWISE);
            anim.setInterpolator(new LinearInterpolator());
            anim.setDurationMilliseconds(DURATION);
            anim.setRepeatMode(Animation.RepeatMode.NONE);
            anim.setTransformable3D(movingObject);
            getCurrentScene().registerAnimation(anim);
            anim.play();
        }

        public void adjustLightPower(int brightness) {
            directionalLight.setPower(brightnessToPower(brightness));
        }

        void increaseLightOffset(float diff) {
            float power = directionalLight.getPower() + diff;
            if (power >= 0) {
                directionalLight.setPower(power);
            }
        }

        private float brightnessToPower(int brightness) {
            return brightnessOffset + brightness / 100f;
        }

        public void changeMovingObjectColor(int color) {
            if (sensorColor == color) {
                return;
            }
            sensorColor = color;
            if (movingObject.getNumChildren() > 0) {

                for (int i = 0; i < movingObject.getNumChildren(); ++i) {
                    Object3D subObject = movingObject.getChildAt(i);
                    subObject.getMaterial().setColor(sensorColor);
                }
            } else {
                movingObject.getMaterial().setColor(sensorColor);
            }
        }


        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
            movingObject.rotate(Vector3.Axis.Y, -0.5);
            movingObject.rotate(Vector3.Axis.Z, -0.5);
		}

        private void addMovingObject() {
            if (movingObject != null) {
                getCurrentScene().removeChild(movingObject);
            }

            Material material = new Material();
            material.enableLighting(true);
            material.setDiffuseMethod(new DiffuseMethod.Lambert());

            SpecularMethod.Phong phongMethod = new SpecularMethod.Phong(Color.WHITE);
            phongMethod.setShininess(30);
            material.setSpecularMethod(phongMethod);
            material.setColor(sensorColor);

            switch (currentShape) {

                case Monkey:
                    try {
                        LoaderOBJ parser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.monkey);
                        parser.parse();
                        movingObject = parser.getParsedObject();
                        movingObject.setScale(OBJECT_SIZE);
                    } catch (Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                        movingObject = new Cube(OBJECT_SIZE + 0.2f);
                    }
                    break;

                case Teapot:
                    try {
                        LoaderOBJ parser = new LoaderOBJ(mContext.getResources(), mTextureManager, R.raw.teapot_obj);
                        parser.parse();
                        movingObject = parser.getParsedObject();
                        movingObject.setScale(OBJECT_SIZE);
                    } catch (Exception e) {
                        Log.d(TAG, e.getLocalizedMessage());
                        movingObject = new Cube(OBJECT_SIZE + 0.2f);
                    }
                    break;

                default:
                    movingObject = new Cube(OBJECT_SIZE + 0.2f);
                    break;
            }

            if (movingObject.getNumChildren() > 0) {
                for (int i = 0; i < movingObject.getNumChildren(); ++i) {
                    Object3D subObject = movingObject.getChildAt(i);
                    subObject.setMaterial(material);
                    subObject.setDoubleSided(true);
                }
            } else {
                movingObject.setMaterial(material);
                movingObject.setDoubleSided(true);
            }

            movingObject.setX(0);
           // movingObject.setRotY(45);
            getCurrentScene().addChild(movingObject);
        }

        private void addBackground() {
            try {
                background = new Plane(22, 12, 2, 2);
                Material material = new Material();
                material.setDiffuseMethod(new DiffuseMethod.Lambert());
                material.enableLighting(true);
                material.addTexture(new Texture("wallDiffuseTex", R.drawable.background));
                material.setColorInfluence(0);
                background.setMaterial(material);
                background.setZ(-5);
                getCurrentScene().addChild(background);

            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }
        }
    }
}
