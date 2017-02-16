package com.indeema.plot;

import android.content.Context;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Kostiantyn Bushko on 2/16/17.
 */

public abstract class GLShape {

    protected Context mContext;

    protected FloatBuffer vertexData;

    protected int mShaderProgramId;

    protected int uColorLocation;
    protected int aPositionLocation;
    protected int uMatrixLocation;


    /**
     *
     * @param vertices
     */
    void setVertexData(float[] vertices) {
        vertexData = null;
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
        vertexData.position(0);
    }

    abstract void draw(float[] mvpMatrix);

}
