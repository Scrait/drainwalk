//package com.tom.cpm.mixin.of;
//
//
//import net.minecraft.client.renderer.texture.Texture;
//import net.optifine.shaders.MultiTexID;
//
//import com.tom.cpm.client.optifine.proxy.TextureOF;
//
//public class TextureMixin_OF implements TextureOF {
//
//	@Shadow public MultiTexID multiTex;
//
//	@Override
//	public MultiTexID cpm$multiTex() {
//		return multiTex;
//	}
//
//	@Override
//	public void cpm$copyMultiTex(MultiTexID mt) {
//		if(multiTex != null && mt != null) {
//			multiTex.norm = mt.norm;
//			multiTex.spec = mt.spec;
//		}
//	}
//}
