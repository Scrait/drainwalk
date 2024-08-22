package com.tom.cpm.api;

import java.util.function.BiConsumer;

import com.tom.cpl.nbt.NBTTagCompound;
import com.tom.cpm.shared.io.ModelFile;

/**
 * Access common api for Customizable Player Models.
 *
 * The referenced minecraft class names are for official mappings, check the wiki for other mappings.
 * */
public interface ICommonAPI extends ISharedAPI {
	/**
	 * Set the player model, has the same effect as the /cpm setskin command
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * @param b64 Base64 encoded model, from the Base64 export option.
	 * @param forced Force the model, players can't change models when the server forces it.
	 * @param persistent Keep the model after re-logging
	 * */
	<P> void setPlayerModel(Class<P> playerClass, P player, String b64, boolean forced, boolean persistent);

	/**
	 * Set the player model, has the same effect as the /cpm setskin command
	 *
	 * <p>Note: the loaded model won't be kept between re-logging,
	 * use the Base64 variant {@link ICommonAPI#setPlayerModel(Class, Object, String, boolean, boolean)}
	 * for persistence.</p>
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * @param model The model to use. Load a {@link ModelFile} using {@link ModelFile#load(java.io.File)} or {@link ModelFile#load(java.io.InputStream)}
	 * @param forced Force the model, players can't change models when the server forces it.
	 * */
	<P> void setPlayerModel(Class<P> playerClass, P player, ModelFile model, boolean forced);

	/**
	 * Reset the player model, has the same effect as the /cpm setskin -r command
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * */
	<P> void resetPlayerModel(Class<P> playerClass, P player);

	/**
	 * Send player jump packet to play the jumping animation
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * */
	<P> void playerJumped(Class<P> playerClass, P player);

	/**
	 * Play the given command animation for a player.
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * @param name animation name
	 * @param value for layers (value: 0-255, toggle: 0-1) or -1 to switch state, 0: reset pose/gesture
	 * */
	<P> void playAnimation(Class<P> playerClass, P player, String name, int value);

	/**
	 * Play the given command animation for a player. For custom poses and gestures only.
	 * For more control use {@link ICommonAPI#playAnimation(Class, Object, String, int)}
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * @param name animation name
	 * */
	<P> void playAnimation(Class<P> playerClass, P player, String name);

	/**
	 * Register a plugin message handler
	 *
	 * @param clazz the player entity class (Player.class) for your minecraft version
	 * @param messageId the id of your message
	 * @param handler Message handler
	 * @return message sender
	 * */
	<P> MessageSender<P> registerPluginMessage(Class<P> clazz, String messageId, BiConsumer<P, NBTTagCompound> handler);

	/**
	 * Broadcast a message to all nearby players on a server
	 * */
	public static interface MessageSender<P> {

		/**
		 * Sends a message to a specific player
		 *
		 * @param player The player to send the message to
		 * @param tag Message written as a {@link NBTTagCompound}
		 * @return send success (client has mod installed)
		 * */
		boolean sendMessageTo(P player, NBTTagCompound tag);

		/**
		 * Broadcast a message to all of the tracking players
		 *
		 * @param player The player
		 * @param tag Message written as a {@link NBTTagCompound}
		 * @param sendToSelf Send message to the selected player
		 * */
		void sendMessageToTracking(P player, NBTTagCompound tag, boolean sendToSelf);
	}

	/**
	 * Get the value for the given animation (value or toggle)
	 *
	 * @param playerClass The player entity class (Player.class)
	 * @param player the player object
	 * @param name animation name
	 * @return The animation value (value layer: 0-255, other animations: 0-1), -1 if animation doesn't exist
	 * */
	<P> int getAnimationPlaying(Class<P> playerClass, P player, String name);
}
