package com.tom.cpm.shared.parts;

import com.tom.cpm.shared.parts.anim.ModelPartAnimationNew;

public enum ModelPartType {
	END(ModelPartEnd::new),
	@Deprecated PLAYER(ModelPartPlayer::new),
	TEMPLATE(ModelPartTemplate::new),
	@Deprecated DEFINITION(ModelPartDefinition::new),
	@Deprecated DEFINITION_LINK(ModelPartDefinitionLink::new),
	@Deprecated SKIN(ModelPartSkin::new),
	@Deprecated SKIN_LINK(ModelPartSkinLink::new),
	@Deprecated PLAYER_PARTPOS(ModelPartPlayerPos::new),
	RENDER_EFFECT(ModelPartRenderEffect::new),
	UUID_LOCK(ModelPartUUIDLockout::new),
	@Deprecated ANIMATION_DATA(ModelPartAnimation::new),
	SKIN_TYPE(ModelPartSkinType::new),
	@Deprecated MODEL_ROOT(ModelPartRoot::new),
	@Deprecated LIST_ICON(ModelPartListIcon::new),
	@Deprecated DUP_ROOT(ModelPartDupRoot::new),
	CLONEABLE(ModelPartCloneable::new),
	@Deprecated SCALE(ModelPartScale::new),
	TEXTURE(ModelPartTexture::new),
	ANIMATED_TEX(ModelPartAnimatedTexture::new),
	TAGS(ModelPartTags::new),
	PACKAGE_LINK(ModelPartCollection.PackageLink::new),
	CUBES(ModelPartCubes::new),
	ROOT_INFO(ModelPartRootInfo::new),
	ANIMATION_NEW(ModelPartAnimationNew::new),
	;
	public static final ModelPartType[] VALUES = values();
	private final IModelPart.Factory factory;

	private ModelPartType(IModelPart.Factory factory) {
		this.factory = factory;
	}

	public IModelPart.Factory getFactory() {
		return factory;
	}
}
