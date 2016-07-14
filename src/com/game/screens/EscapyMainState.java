package com.game.screens;

import com.badlogic.gdx.Screen;
import com.game.render.EscapyGdxCamera;

// TODO: Auto-generated Javadoc
/**
 * The Interface EscapyMainState.
 */
public interface EscapyMainState 
{

	/**
	 * Inits the state.
	 *
	 * @return the screen
	 */
	public Screen initState();
	
	/**
	 * Render game objects.
	 *
	 * @param escapyCamera
	 *            the escapy camera
	 */
	public void renderGameObjects(EscapyGdxCamera escapyCamera);
}
