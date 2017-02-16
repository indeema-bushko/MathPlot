package com.indeema.plot;

import android.content.Context;
import android.opengl.GLES20;

/**
 * Created by Kostiantyn Bushko on 2/16/17.
 */

public class GLCoordinateSystem extends GLShape {

    public GLCoordinateSystem(Context context) {
        mContext = context;
        int vertexShader = ShaderUtils.CreateShader(context, GLES20.GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShader = ShaderUtils.CreateShader(context, GLES20.GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        mShaderProgramId = ShaderUtils.CreateProgram(vertexShader, fragmentShader);

        uColorLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Color");
        aPositionLocation = GLES20.glGetAttribLocation(mShaderProgramId, "a_Position");
        uMatrixLocation = GLES20.glGetUniformLocation(mShaderProgramId, "u_Matrix");
    }

    @Override
    public void draw(float[] mvpMatrix) {

        GLES20.glUniformMatrix4fv(uMatrixLocation , 1, false, mvpMatrix, 0);

        GLES20.glUseProgram(mShaderProgramId);
        GLES20.glEnableVertexAttribArray(aPositionLocation);
        GLES20.glVertexAttribPointer(aPositionLocation, 2, GLES20.GL_FLOAT, false, 0, vertexData);

        GLES20.glUniform4f(uColorLocation, 0.0f, 0.0f, 0.0f, 1.0f);

        GLES20.glDrawArrays(GLES20.GL_LINES, 0, vertexData.capacity() / 2);

        GLES20.glDisableVertexAttribArray(aPositionLocation);
    }
}
