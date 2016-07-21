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
 * @file		Abstract3DFragment.java
 *
 * @brief
 *
 * @date		Dec 07, 2015.
 *
 * @details		This class renders the 3D screen of the sample application.
 */

 /* =============================================================================
 * EDIT HISTORY FOR MODULE
 *
 * When		Who			What, where, why
 * -------- -------    ---------------------------------------------------------
 *
 * ========================================================================== */

package com.github.dbopensource.sensordemo;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.rajawali3d.IRajawaliDisplay;
import org.rajawali3d.Object3D;
import org.rajawali3d.math.Matrix4;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.renderer.RajawaliRenderer;
import org.rajawali3d.surface.IRajawaliSurface;
import org.rajawali3d.surface.IRajawaliSurfaceRenderer;
import org.rajawali3d.util.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Abstract3DFragment extends Fragment implements IRajawaliDisplay {

	protected ProgressBar mProgressBarLoader;
    protected FrameLayout mLayout;
    protected IRajawaliSurface mRajawaliSurface;
    protected IRajawaliSurfaceRenderer mRenderer;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the view
        mLayout = (FrameLayout) inflater.inflate(getLayoutID(), container, false);

		mLayout.findViewById(R.id.relative_layout_loader_container).bringToFront();

        mLayout.findViewById(R.id.infoButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new InfoDialogFragment();
                dialog.show(getFragmentManager(), "InfoDialogFragment");
            }
        });

        // Find the TextureView
        mRajawaliSurface = (IRajawaliSurface) mLayout.findViewById(R.id.rajwali_surface);

		// Create the loader
		mProgressBarLoader = (ProgressBar) mLayout.findViewById(R.id.progress_bar_loader);
		mProgressBarLoader.setVisibility(View.GONE);

        // Create the renderer
        mRenderer = createRenderer();
        onBeforeApplyRenderer();
        applyRenderer();
		return mLayout;
	}

    protected void onBeforeApplyRenderer() {

    }

    protected void applyRenderer() {
        mRajawaliSurface.setSurfaceRenderer(mRenderer);
    }


	@Override
	public void onDestroyView() {
		super.onDestroyView();

		if (mLayout != null)
			mLayout.removeView((View) mRajawaliSurface);
	}

    @Override
    public int getLayoutID() {
        return R.layout.rajawali_textureview_fragment;
    }

	protected void hideLoader() {
		mProgressBarLoader.post(new Runnable() {
			@Override
			public void run() {
				mProgressBarLoader.setVisibility(View.GONE);
			}
		});
	}

	protected void showLoader() {
		mProgressBarLoader.post(new Runnable() {
			@Override
			public void run() {
				mProgressBarLoader.setVisibility(View.VISIBLE);
			}
		});
	}

	protected abstract class AbstractRenderer extends RajawaliRenderer {

		public AbstractRenderer(Context context) {
			super(context);
		}

        @Override
        public void onOffsetsChanged(float v, float v2, float v3, float v4, int i, int i2) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
		public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
			showLoader();
			super.onRenderSurfaceCreated(config, gl, width, height);
			hideLoader();
		}

        @Override
        protected void onRender(long ellapsedRealtime, double deltaTime) {
            super.onRender(ellapsedRealtime, deltaTime);
        }

        public Vector3 calculatePos(int x, int y, Object3D object3D) {
            Matrix4 mViewMatrix = getCurrentCamera().getViewMatrix();
            Matrix4 mProjectionMatrix = getCurrentCamera().getProjectionMatrix();
            double[] mNearPos4 = new double[4];
            double[] mFarPos4 = new double[4];
            Vector3 mNearPos = new Vector3();
            Vector3 mFarPos = new Vector3();
            Vector3 mNewObjPos = new Vector3();

            int[] mViewport = new int[] { 0, 0, getViewportWidth(), getViewportHeight() };

            GLU.gluUnProject(x, getViewportHeight() - y, 0, mViewMatrix.getDoubleValues(), 0,
                    mProjectionMatrix.getDoubleValues(), 0, mViewport, 0, mNearPos4, 0);

            GLU.gluUnProject(x, getViewportHeight() - y, 1.f, mViewMatrix.getDoubleValues(), 0,
                    mProjectionMatrix.getDoubleValues(), 0, mViewport, 0, mFarPos4, 0);

            mNearPos.setAll(mNearPos4[0] / mNearPos4[3], mNearPos4[1]
                    / mNearPos4[3], mNearPos4[2] / mNearPos4[3]);
            mFarPos.setAll(mFarPos4[0] / mFarPos4[3],
                    mFarPos4[1] / mFarPos4[3], mFarPos4[2] / mFarPos4[3]);

            double factor = (Math.abs(object3D.getZ()) + mNearPos.z)
                    / (getCurrentCamera().getFarPlane() - getCurrentCamera()
                    .getNearPlane());

            mNewObjPos.setAll(mFarPos);
            mNewObjPos.subtract(mNearPos);
            mNewObjPos.multiply(factor);
            mNewObjPos.add(mNearPos);

            return mNewObjPos;
        }
    }
}
