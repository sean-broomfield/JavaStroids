package edu.broomfield.lab3.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.broomfield.lab3.gameobjects.GameObject;

public class Renderer {
	
	Animation explosionAnim;
	Texture explosionSheet;
	TextureRegion [] explosionFrames;
	TextureRegion currentFrameExplosion;
	float shipExplosionStateTime;
	float asteroidTime;
	Texture bg1, bg2;
	float bg1XPos, bg2XPos;
	private SpriteBatch spriteBatch;
	private Controller control;
	BitmapFont font;
	
	public Renderer(Controller c){
		explosionSheet = new Texture(Gdx.files.internal("explosion17.png"));
		TextureRegion [][] tmp = TextureRegion.split(explosionSheet, explosionSheet.getWidth()/5, explosionSheet.getHeight()/5);
		explosionFrames = new TextureRegion[25];
		int index = 0;
		for (int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				explosionFrames[index++] = tmp[i][j];
			}
		}
		explosionAnim = new Animation (0.040f, explosionFrames);
		shipExplosionStateTime = 0f;
		asteroidTime = 0f;
		control = c;
		spriteBatch = new SpriteBatch(); 
		font = new BitmapFont();
		bg1 = new Texture("orion_background.jpg");
		bg2 = new Texture("orion_background.jpg");
		bg1XPos = 0;
		bg2XPos = bg1.getWidth();
	}
	
	public void renderBackground() {
		spriteBatch.draw(bg1, bg1XPos, 0);
		spriteBatch.draw(bg2, bg2XPos, 0);
		
		/*
		System.out.println("bg1XPos: " + bg1XPos);
		System.out.println("bg1.getWidth: " + bg1.getWidth());
		System.out.println("Gdx.graphics.width: " + Gdx.graphics.getWidth());
		System.out.println(" ");
		*/

		if (bg1XPos + bg1.getWidth() < Gdx.graphics.getWidth()) {
			bg2XPos = (bg1XPos + bg1.getWidth());
		}
		if (bg2XPos + bg2.getWidth() < Gdx.graphics.getWidth()) {
			bg1XPos = (bg2XPos + bg2.getWidth());
	    }
		
		if(!Constants.PAUSED) {
			bg1XPos -= 0.3;
			bg2XPos -= 0.3;
		}
	}
	
	public void render(){
		spriteBatch.begin();
		renderBackground();
		//font.draw(spriteBatch, "Drawn Objects: " + control.getDrawableObjects().size(), 0, Gdx.graphics.getHeight() - 20);
		font.draw(spriteBatch, "Score: " + control.getScore(), 0, Gdx.graphics.getHeight() - 30);
		for(GameObject gObj : control.getDrawableObjects()){
			gObj.sprite.draw(spriteBatch);
		}
		
		if(Constants.PAUSED) {
			font.draw(spriteBatch,  "PAUSED",  (Gdx.graphics.getWidth()/2) - 30, (Gdx.graphics.getHeight()/2));
		} else {
			if(control.isShipCrashed() && !explosionAnim.isAnimationFinished(shipExplosionStateTime)) {
				shipExplosionStateTime += Gdx.graphics.getDeltaTime();
				currentFrameExplosion = explosionAnim.getKeyFrame(shipExplosionStateTime, false);
				spriteBatch.draw(currentFrameExplosion, control.getExplosionX() - Constants.SHIP_WIDTH, control.getExplosionY()-Constants.SHIP_HEIGHT);
			}
		
			if(control.isAsteroidEx()) {
				currentFrameExplosion = explosionAnim.getKeyFrame(asteroidTime);
				asteroidTime += Gdx.graphics.getDeltaTime();
				spriteBatch.draw(currentFrameExplosion, control.getExplosionX(), control.getExplosionY());
			}
		
			//If asteroid is finished exploding then setAsteroidExplosion(false) so if another
			//asteroid gets hit then it explodes as well.
			if(explosionAnim.isAnimationFinished(asteroidTime) && control.isAsteroidEx()) {
				asteroidTime = 0;
				control.setAsteroidExplosion(false);
			}
			
			if(control.isShipCrashed()) {
				font.draw(spriteBatch,  "Game Over",  (Gdx.graphics.getWidth()/2) - 30, (Gdx.graphics.getHeight()/2));
				font.draw(spriteBatch, "Press 'R' to restart.",  (Gdx.graphics.getWidth()/2) - 55,  Gdx.graphics.getHeight()/2 - 20);
				Constants.GAME_OVER = true;
			}
		}
		
		spriteBatch.end();
	}

}
