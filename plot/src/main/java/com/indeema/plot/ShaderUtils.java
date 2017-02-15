package com.indeema.plot;

import android.content.Context;
import android.content.res.Resources;
import android.opengl.GLES20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Kostiantyn Bushko on 2/13/17.
 */

public class ShaderUtils {

    /**
     * Read shader program from resource file as a string.</br>
     *
     * @param context an application {@link Context}
     * @param resourceId resource id represent file in raw folder.
     * @return a string contains shader program.
     */
    public static String readShaderProgramFromRawData(Context context, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = null;
        try {
            BufferedReader bufferedReader = null;
            try {
                inputStream = context.getResources().openRawResource(resourceId);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\r\n");
                }
            } finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Create and compile program with vertex and fragment shader.
     *
     * @param vertexShaderId vertex shader identifier.
     * @param fragmentShaderId fragment shader identifier.
     * @return an identifier of compiled program.
     */
    public static int CreateProgram(int vertexShaderId, int fragmentShaderId) {
        final int programId = GLES20.glCreateProgram();
        if (programId == 0) {
            return 0;
        }
        GLES20.glAttachShader(programId, vertexShaderId);
        GLES20.glAttachShader(programId, fragmentShaderId);
        GLES20.glLinkProgram(programId);
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programId);
            return 0;
        }
        return programId;
    }

    /**
     * Create and compile shader with specific type.</br>
     *
     * @param context an application {@link Context}
     * @param type shader type {@link GLES20#GL_VERTEX_SHADER}, {@link GLES20#GL_FRAGMENT_SHADER}.
     * @param shaderRawId an identifier of raw data in resources.
     *
     * @return an identifier of compiled shader.
     */
    static int CreateShader(Context context, int type, int shaderRawId) {
        String shaderText = readShaderProgramFromRawData(context, shaderRawId);
        return ShaderUtils.CreateShader(type, shaderText);
    }

    static int CreateShader(int type, String shaderText) {
        final int shaderId = GLES20.glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }
        GLES20.glShaderSource(shaderId, shaderText);
        GLES20.glCompileShader(shaderId);
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }
}
