package edu.broomfield.lab3.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class Game extends ApplicationAdapter {
	private Controller control; 
	private Renderer render;
	
	@Override
	public void create () {
		control = new Controller();
		render = new Renderer(control);
	}
	
	@Override
	public void dispose() {
		control.dispose();
	}
	
	@Override
	public void render () {
		if(Gdx.input.isKeyJustPressed(Keys.R) && !Constants.PAUSED){
			Constants.GAME_OVER = false;
			dispose();
			create();
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		control.update(); // Process inputs and update game world.
		render.render();
	}
	
}
