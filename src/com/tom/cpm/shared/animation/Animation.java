package com.tom.cpm.shared.animation;

import com.tom.cpm.shared.animation.AnimationEngine.AnimationMode;
import com.tom.cpm.shared.animation.interpolator.Interpolator;
import com.tom.cpm.shared.animation.interpolator.InterpolatorType;
import com.tom.cpm.shared.definition.ModelDefinition;

public class Animation implements IAnimation {
	private final IModelComponent[] componentIDs;
	private final Interpolator[][] psfs;
	private final Boolean[][] show;
	private boolean add;

	private final int duration;
	private final int frames;

	private final int priority;

	/**
	 *
	 * @param components2
	 * @param data
	 *            [component][attributeId][frame]
	 *
	 *            attributeIDs: 0: rotationPointX 1: rotationPointY 2:
	 *            rotationPointZ 3: rotationX 4: rotationY 5: rotationZ
	 */
	public Animation(IModelComponent[] components2, float[][][] data, Boolean[][] show, int duration, int priority, boolean add, InterpolatorType itType) {
		this.componentIDs = components2;
		this.duration = duration;
		this.show = show;
		this.add = add;
		this.priority = priority;

		if(components2.length == 0) {
			frames = 0;
			psfs = new Interpolator[0][];
			return;
		}

		int components = data.length;
		frames = data[0][0].length;

		psfs = new Interpolator[components2.length][InterpolatorChannel.VALUES.length];

		for (int component = 0; component < components; component++) {
			for (InterpolatorChannel channel : InterpolatorChannel.VALUES) {
				Interpolator i = itType.create();
				i.init(data[component][channel.channelID()], channel.createInterpolatorSetup());
				psfs[component][channel.channelID()] = i;
			}
		}
	}

	@Override
	public void animate(long millis, ModelDefinition def, AnimationMode mode) {
		if(frames == 0)return;
		float step = (float) millis % duration / duration * frames;
		for (int componentId = 0; componentId < componentIDs.length; componentId++) {
			IModelComponent component = componentIDs[componentId];
			component.setRotation(add,
					getValue(componentId, InterpolatorChannel.ROT_X, step),
					getValue(componentId, InterpolatorChannel.ROT_Y, step),
					getValue(componentId, InterpolatorChannel.ROT_Z, step));
			component.setPosition(add,
					getValue(componentId, InterpolatorChannel.POS_X, step),
					getValue(componentId, InterpolatorChannel.POS_Y, step),
					getValue(componentId, InterpolatorChannel.POS_Z, step));
			component.setColor(
					getValue(componentId, InterpolatorChannel.COLOR_R, step),
					getValue(componentId, InterpolatorChannel.COLOR_G, step),
					getValue(componentId, InterpolatorChannel.COLOR_B, step));
			component.setVisible(show[componentId][(int) step]);
			component.setRenderScale(add,
					getValue(componentId, InterpolatorChannel.SCALE_X, step),
					getValue(componentId, InterpolatorChannel.SCALE_Y, step),
					getValue(componentId, InterpolatorChannel.SCALE_Z, step));
		}
	}

	private float getValue(int component, InterpolatorChannel attribute, double time) {
		return (float) psfs[component][attribute.channelID()].applyAsDouble(time);
	}

	@Override
	public int getDuration(AnimationMode mode) {
		return duration;
	}

	@Override
	public int getPriority(AnimationMode mode) {
		return priority;
	}
}
