package com.indeema.plot;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kostiantyn Bushko on 2/16/17.
 */

public class OrthoRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = OrthoRenderer.class.getSimpleName();
    private Context mContext;

    private int mWidth, mHeight;

    private int mShaderProgramId;
    private FloatBuffer vertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    float[] vertices;


    private GLCoordinateSystem mGlCoordinateSystem;

    public OrthoRenderer(Context context) {
        mContext = context;
        vertices = new float[2000];

        for (int i = 0; i < 2000; i+=2) {
            float x = (i - 1000.0f) / 100.0f;
            vertices[i] = x;
            float y = ((float)(Math.sin(x * 10.0f) / (1.0f + x * x)));
            vertices[i+1] = y;
            Log.d(TAG, i + " x = " + x + ", y = " + y);
        }

        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
        vertexData.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        int vertexShaderId = ShaderUtils.CreateShader(mContext, GLES20.GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.CreateShader(mContext, GLES20.GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        mShaderProgramId = ShaderUtils.CreateProgram(vertexShaderId, fragmentShaderId);
//        GLES20.glUseProgram(mShaderProgramId);
//        bindData();
        uColorLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Color");
        aPositionLocation = GLES20.glGetAttribLocation(mShaderProgramId, "a_Position");
        uMatrixLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Matrix");

        /**
         * Experimental
         */
        float[] abscice = new float[] {
                -10.0f, 0.0f, 10.0f, 0.0f,
                0.0f, -1.0f, 0.0f, 1.0f
        };
        mGlCoordinateSystem = new GLCoordinateSystem(mContext);
        mGlCoordinateSystem.setVertexData(abscice);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        GLES20.glViewport(0, 0, width, height);

//        float ratio = (float) width / height;
//        Matrix.frustumM(mProjectionMatrix, 0, -5, 5, -1, 1, 1, 1000);
//        Log.d(TAG, "SurfaceChanged RATIO -> " + ratio);
        Matrix.orthoM(mProjectionMatrix, 0, -10.1f, 10.1f, -1, 1, 1, 10);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, eyeZ, 0f, 0f, 0f, 0f, 1.0f, 1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

//    private void bindData() {
//        uColorLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Color");
//        aPositionLocation = GLES20.glGetAttribLocation(mShaderProgramId, "a_Position");
//        uMatrixLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Matrix");
//    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // Draw coordinate system
        mGlCoordinateSystem.draw(mMVPMatrix);

        // Draw wave
        GLES20.glUniformMatrix4fv(uMatrixLocation , 1, false, mMVPMatrix, 0);

        GLES20.glUseProgram(mShaderProgramId);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, vertexData);

        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINE_STRIP, 0, vertexData.capacity() / 2);
        GLES20.glDisableVertexAttribArray(aPositionLocation);
    }

    private float eyeZ = -1;
    public void incrementEyeZ(float eyeZ) {
        this.eyeZ += eyeZ;
        Log.d(TAG, "Eye Z = " + this.eyeZ);
    }

    public void decrementEyeZ(float eyeZ) {
        this.eyeZ -= eyeZ;
        Log.d(TAG, "Eye Z = " + this.eyeZ);
    }
}
