package tech.drainwalk.utils.shader;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.ARBShaderObjects;
import tech.drainwalk.api.impl.models.DrainwalkResource;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glUniform1f;

public class Shader {
    int shaderProgram;

    public String readInputStream(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append('\n');

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public Shader(String fragmentSource, boolean isFile) {
        int shaderProgram = glCreateProgram();
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        try {
            glShaderSource(fragmentShader, (isFile ? readInputStream(Minecraft.getInstance().getResourceManager().getResource(new DrainwalkResource(fragmentSource)).getInputStream()) : fragmentSource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glAttachShader(shaderProgram, fragmentShader);
        glCompileShader(fragmentShader);
        checkCompileErrors(fragmentShader, "FRAGMENT");
        glLinkProgram(shaderProgram);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, """
                #version 120
                varying vec2 fragCoord;
                void main() {
                    fragCoord = gl_MultiTexCoord0.xy;
                    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
                }""");
        glCompileShader(vertexShader);
        glAttachShader(shaderProgram, vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteShader(vertexShader);

        glLinkProgram(shaderProgram);
        this.shaderProgram = shaderProgram;
    }

    public Shader(String fragmentSource, String vertexSource, boolean isFile) {
        int shaderProgram = glCreateProgram();
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        try {
            glShaderSource(fragmentShader, (isFile ? readInputStream(Minecraft.getInstance().getResourceManager().getResource(new DrainwalkResource(fragmentSource)).getInputStream()) : fragmentSource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glAttachShader(shaderProgram, fragmentShader);
        glCompileShader(fragmentShader);

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        try {
            glShaderSource(vertexShader, (isFile ? readInputStream(Minecraft.getInstance().getResourceManager().getResource(new DrainwalkResource(vertexSource)).getInputStream()) : vertexSource));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        glCompileShader(vertexShader);
        glAttachShader(shaderProgram, vertexShader);
        glDeleteShader(fragmentShader);
        glDeleteShader(vertexShader);

        glLinkProgram(shaderProgram);
        this.shaderProgram = shaderProgram;
    }

    public void useProgram() {
       glUseProgram(shaderProgram);
    }

    public void unloadProgram() {
       glUseProgram(0);
    }

    public void setupUniformf(String name, float... args) {
        int loc = ARBShaderObjects.glGetUniformLocationARB(shaderProgram, name);
        switch (args.length) {
            case 1 -> glUniform1f(loc, args[0]);
            case 2 -> glUniform2f(loc, args[0], args[1]);
            case 3 -> glUniform3f(loc, args[0], args[1], args[2]);
            case 4 -> glUniform4f(loc, args[0], args[1], args[2], args[3]);
            default ->
                    throw new IllegalArgumentException("Недопустимое количество аргументов для uniform '" + name + "'");
        }
    }

    public void setupUniformi(String name, int... args) {
        int loc = glGetUniformLocation(shaderProgram, name);
        if (args.length > 1) glUniform2i(loc, args[0], args[1]);
        else glUniform1i(loc, args[0]);
    }

    public void setupUniform1f(String uniform, float x) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform1f(vertexColorLocation, x);
    }

    public void setupUniform2f(String uniform, float x, float y) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform2f(vertexColorLocation, x, y);
    }

    public void setupUniform3f(String uniform, float x, float y, float z) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform3f(vertexColorLocation, x, y, z);
    }

    public void setupUniform4f(String uniform, float x, float y, float z, float w) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform4f(vertexColorLocation, x, y, z, w);
    }

    public void setupUniformFM(String uniform, float[] fm) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform4f(vertexColorLocation, fm[0], fm[1], fm[2], fm[3]);
    }

    public void setupUniform1i(String uniform, int x) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform1i(vertexColorLocation, x);
    }

    public void setupUniform2i(String uniform, int x, int y) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform2i(vertexColorLocation, x, y);
    }

    public void setupUniform3i(String uniform, int x, int y, int z) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform3i(vertexColorLocation, x, y, z);
    }

    public void setupUniform4i(String uniform, int x, int y, int z, int w) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        glUniform4i(vertexColorLocation, x, y, z, w);
    }

    public void setupUniformBF(String uniform, final FloatBuffer floatBuffer) {
        int vertexColorLocation = glGetUniformLocation(shaderProgram, uniform);
        RenderSystem.glUniform1(vertexColorLocation, floatBuffer);
    }

    public static void drawQuads(float x, float y, float width, float height) {
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(x, y);
        glTexCoord2f(0, 1);
        glVertex2f(x, y + height);
        glTexCoord2f(1, 1);
        glVertex2f(x + width, y + height);
        glTexCoord2f(1, 0);
        glVertex2f(x + width, y);
        glEnd();
    }

    public static void drawQuads() {
        final MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
        final float width = (float) mainWindow.getScaledWidth();
        final float height = (float) mainWindow.getScaledHeight();
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(0, 0);
        glTexCoord2f(0, 0);
        glVertex2f(0, height);
        glTexCoord2f(1, 0);
        glVertex2f(width, height);
        glTexCoord2f(1, 1);
        glVertex2f(width, 0);
        glEnd();
    }

    public int getUniform(String name) {
        return glGetUniformLocation(shaderProgram, name);
    }

    private static void checkCompileErrors(int shader, String type) {
        int success;
        if (type.equals("VERTEX") || type.equals("FRAGMENT")) {
            success = glGetShaderi(shader, GL_COMPILE_STATUS);
            if (success == GL_FALSE) {
                String infoLog = glGetShaderInfoLog(shader);
                System.out.println(type + " SHADER COMPILATION ERROR: " + infoLog);
            }
        }
    }

    private static void checkLinkErrors(int program) {
        int success = glGetProgrami(program, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            String infoLog = glGetProgramInfoLog(program);
            System.out.println("SHADER PROGRAM LINKING ERROR: " + infoLog);
        }
    }

}
