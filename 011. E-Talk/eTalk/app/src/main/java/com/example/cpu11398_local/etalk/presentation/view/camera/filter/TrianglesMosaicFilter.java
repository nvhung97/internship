package com.example.cpu11398_local.etalk.presentation.view.camera.filter;

import android.content.Context;
import android.opengl.GLES20;
import com.example.cpu11398_local.etalk.R;
import com.example.cpu11398_local.etalk.presentation.view.camera.Utils.MyGLUtils;

public class TrianglesMosaicFilter extends BaseFilter {
    private int program;

    public TrianglesMosaicFilter(Context context) {
        super(context);

        // Build shaders
        program = MyGLUtils.buildProgram(context, R.raw.vertext, R.raw.triangles_mosaic);
    }

    @Override
    public void onDraw(int cameraTexId, int canvasWidth, int canvasHeight) {
        setupShaderInputs(program,
                new int[]{canvasWidth, canvasHeight},
                new int[]{cameraTexId},
                new int[][]{});
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}