package tech.drainwalk.utils.math;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Project {

    private static final float[] IDENTITY_MATRIX = new float[]{1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F};
    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    public static boolean gluProject(float objx, float objy, float objz, FloatBuffer modelMatrix, FloatBuffer projMatrix, IntBuffer viewport, FloatBuffer win_pos) {
        float[] in = new float[4];
        float[] out = new float[4];
        in[0] = objx;
        in[1] = objy;
        in[2] = objz;
        in[3] = 1.0F;
        __gluMultMatrixVecf(modelMatrix, in, out);
        __gluMultMatrixVecf(projMatrix, out, in);
        if ((double)in[3] == 0.0) {
            return false;
        } else {
            in[3] = 1.0F / in[3] * 0.5F;
            in[0] = in[0] * in[3] + 0.5F;
            in[1] = in[1] * in[3] + 0.5F;
            in[2] = in[2] * in[3] + 0.5F;
            win_pos.put(0, in[0] * (float)viewport.get(viewport.position() + 2) + (float)viewport.get(viewport.position() + 0));
            win_pos.put(1, in[1] * (float)viewport.get(viewport.position() + 3) + (float)viewport.get(viewport.position() + 1));
            win_pos.put(2, in[2]);
            return true;
        }
    }

    public static void gluPerspective(float fovy, float aspect, float zNear, float zFar) {
        float radians = fovy / 2.0F * 3.1415927F / 180.0F;
        float deltaZ = zFar - zNear;
        float sine = (float)Math.sin((double)radians);
        if (deltaZ != 0.0F && sine != 0.0F && aspect != 0.0F) {
            float cotangent = (float)Math.cos((double)radians) / sine;
            __gluMakeIdentityf(matrix);
            matrix.put(0, cotangent / aspect);
            matrix.put(5, cotangent);
            matrix.put(10, -(zFar + zNear) / deltaZ);
            matrix.put(11, -1.0F);
            matrix.put(14, -2.0F * zNear * zFar / deltaZ);
            matrix.put(15, 0.0F);
            GL11.glMultMatrixf(matrix);
        }
    }

    private static void __gluMakeIdentityf(FloatBuffer m) {
        int oldPos = m.position();
        m.put(IDENTITY_MATRIX);
        m.position(oldPos);
    }


    public static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
        for(int i = 0; i < 4; ++i) {
            out[i] = in[0] * m.get(m.position() + 0 + i) + in[1] * m.get(m.position() + 4 + i) + in[2] * m.get(m.position() + 8 + i) + in[3] * m.get(m.position() + 12 + i);
        }

    }
}
