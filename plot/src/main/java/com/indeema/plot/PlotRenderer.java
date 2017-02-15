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
 * Created by Kostiantyn Bushko on 2/13/17.
 */

public class PlotRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = PlotRenderer.class.getSimpleName();
    private Context mContext;

    private int mWidth, mHeight;

    private int mShaderProgramId;
    private FloatBuffer vertexData;
    private FloatBuffer coordinateSystemVertexData;
    private int uColorLocation;
    private int aPositionLocation;
    private int uMatrixLocation;

    float[] mProjectionMatrix = new float[16];

    float[]  coordinateSystem = new float[] {
            -0.9f, 0.0f,
            0.9f, 0.0f
    };

    float[] vertices;

    public PlotRenderer(Context context) {
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
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        int vertexShaderId = ShaderUtils.CreateShader(mContext, GLES20.GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.CreateShader(mContext, GLES20.GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        mShaderProgramId = ShaderUtils.CreateProgram(vertexShaderId, fragmentShaderId);
        if (mShaderProgramId > 0) {
            GLES20.glUseProgram(mShaderProgramId);
            bindData();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        bindMatrix(width, height);
    }

    private void bindData() {
        uColorLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Color");
        aPositionLocation = GLES20.glGetAttribLocation(mShaderProgramId, "a_Position");
        uMatrixLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Matrix");

        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, vertexData);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
    }

    private void bindMatrix(int width, int height) {
        float ratio = 1.0f;
        float left = -1.0f;
        float right = 1.0f;
        float bottom = -1.0f;
        float top = 1.0f;
        float near = 1.0f;
        float far = 8.0f;
        if (width > height) {
            ratio = (float) width / height;
            left *= ratio;
            right *= ratio;
        } else {
            ratio = (float) height / width;
            bottom *= ratio;
            top *= ratio;
        }

//        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
//        Matrix.orthoM(mProjectionMatrix, 0, -1, 1, -1, 1, 1, 1);
        Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, mProjectionMatrix, 0);
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertices.length / 2);
    }
}
