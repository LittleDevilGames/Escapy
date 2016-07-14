package com.game.update_loop;

import com.badlogic.gdx.Screen;

public class UpdatableScreen implements Updatable {

	private Screen screen;
	
	public UpdatableScreen(Screen screen) 
	{
		this.setScreen(screen);
	}

	@Override
	public void update() 
	{
		((Updatable) screen).update();
	}

	public Screen getScreen() {
		return screen;
	}

	public void setScreen(Screen screen) {
		this.screen = screen;
	}

}