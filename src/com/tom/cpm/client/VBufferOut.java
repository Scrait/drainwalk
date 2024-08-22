package com.tom.cpm.client;

import net.minecraft.util.math.vector.Matrix3f;
import net.minecraft.util.math.vector.Matrix4f;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import com.tom.cpm.shared.model.render.BatchedBuffers.BufferOutput;

public class VBufferOut implements BufferOutput<IVertexBuilder> {
	private int light, overlay;
	private Matrix4f mat4;
	private Matrix3f mat3;

	public VBufferOut(int light, int overlay, MatrixStack matrixStackIn) {
		this.light = light;
		this.overlay = overlay;
		mat4 = new Matrix4f(matrixStackIn.getLast().getMatrix());
		mat3 = new Matrix3f(matrixStackIn.getLast().getNormal());
	}

	@Override
	public void push(IVertexBuilder buffer, float x, float y, float z, float red, float green,
			float blue, float alpha, float u, float v, float nx, float ny, float nz) {
		buffer.pos(mat4, x, y, z);
		buffer.color(red, green, blue, alpha);
		buffer.tex(u, v);
		buffer.overlay(overlay);
		buffer.lightmap(light);
		buffer.normal(mat3, nx, ny, nz);
		buffer.endVertex();
	}
}
