package edu.broomfield.lab3.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;

import edu.broomfield.lab3.gameobjects.Asteroid;
import edu.broomfield.lab3.gameobjects.GameObject;
import edu.broomfield.lab3.gameobjects.Ship;
import edu.broomfield.lab3.gameobjects.Missile;


public class Controller {
	
	private float explosionX;
	private float explosionY;
	private float screenHeight;
	private Sound thrustersSound;
	private Sound missileSound;
	private Music backgroundNoise;
	private boolean shipCrashed;
	private boolean asteroidExplosion;
	private Sound explosionSound;
	ArrayList<GameObject> drawableObjects; 
	int score;
	Ship ship;
	long time;
	
	public Controller(){
		score = 0;
		drawableObjects = new ArrayList<GameObject>(); 
		initShip();
		initAsteroids(10);
		initSound();
		screenHeight = Gdx.graphics.getHeight();
		//Set CurrentTime
		time = System.currentTimeMillis();
	}
	
	private void initSound() {
		thrustersSound = Gdx.audio.newSound(Gdx.files.internal("rocket-start.wav"));
		backgroundNoise = Gdx.audio.newMusic(Gdx.files.internal("interior-spaceship.mp3"));
		missileSound = Gdx.audio.newSound(Gdx.files.internal("rocket-deploy.wav"));				
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion-17.wav"));
		backgroundNoise.setLooping(true);
		backgroundNoise.play();
		backgroundNoise.setVolume(0.5f);
	}
	
	private void processMouseInput(){
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			ship.face(new Vector2(Gdx.input.getX()-ship.sprite.getX(), -(screenHeight-Gdx.input.getY()-ship.sprite.getY())));
		}
	}
	
	private void initShip(){
		int w = Constants.SHIP_WIDTH; 
		int h = Constants.SHIP_HEIGHT; 
		Pixmap pmap = new Pixmap(w, h, Format.RGBA8888); // TODO: Check Image Format
		pmap.setColor(1, 1, 1, 1);
		pmap.drawLine(0, h, w/2, 0);
		pmap.drawLine(w, h, w/2, 0);
		pmap.drawLine(1, h-1, w, h-1);
		ship = new Ship(new Texture(pmap), 100, 100);
		drawableObjects.add(ship);
	}
	
	private void initAsteroids(int num){
		Random rand = new Random();
		for(int i = 0; i<num; i++){
			Asteroid asteroid = new Asteroid(new Texture("Asteroid_tex.png"));
			asteroid.sprite.setPosition(rand.nextInt(Gdx.graphics.getWidth()), rand.nextInt(Gdx.graphics.getHeight()));
			asteroid.sprite.setOrigin(asteroid.sprite.getWidth() / 2, asteroid.sprite.getHeight() / 2);
			asteroid.setRotVel(rand.nextFloat()*8-4);
			drawableObjects.add(asteroid);
		}
	}
	
	private void initMissile() {
		int w = Constants.SHIP_WIDTH/3;
		int h = Constants.SHIP_HEIGHT/3;
		Pixmap pmap = new Pixmap(w, h, Format.RGB565);
		pmap.setColor(1, 1, 1, 1);
		pmap.drawLine(w/2,  0,  w/2,  h);
		drawableObjects.add(new Missile(new Texture(pmap), ship.getDirection(), ship.getPosition()));
	}

	
	public void update(){
		processKeyboardInput();
		
		if(!Constants.PAUSED) {
			processMouseInput();
			float deltaT = Gdx.graphics.getDeltaTime();
		
			boolean[] removal = new boolean[drawableObjects.size()];
				
			for(int i = 0; i < drawableObjects.size(); i++) {
				GameObject gObj = drawableObjects.get(i);
				//Update Asteroids
				if(gObj instanceof Asteroid){
					((Asteroid) gObj).update(deltaT); 
					if(ship.sprite.getBoundingRectangle().overlaps(((Asteroid) gObj).sprite.getBoundingRectangle()) && !shipCrashed) {
						shipCrashed = true;
						explosionSound.play();
						thrustersSound.stop();
						explosionX = ship.sprite.getX();
						explosionY = ship.sprite.getY();
					}
				
					for(GameObject g : drawableObjects) {
						if(((g instanceof Missile)) && (((Asteroid) gObj).sprite.getBoundingRectangle().overlaps(((Missile)g).sprite.getBoundingRectangle()))) {
							explosionSound.play();
							score++;
							((Missile)g).remove = true;
							removal[i] = true;			
							asteroidExplosion = true;
							explosionX = ((Asteroid) gObj).sprite.getX();
							explosionY = ((Asteroid) gObj).sprite.getY();
							//drawableObjects.remove(g);
						}
					}
				}
			
				if(gObj instanceof Missile) {
					((Missile) gObj).update(deltaT);
					if(((Missile)gObj).remove)
						removal[i] = true;
				}
			}
		
			for(int i = removal.length - 1; i >= 0; i--) {
				if(removal[i]) {
				drawableObjects.remove(i);
				}
			}
		
	/*	for(int i = 0; i < removal.length; i++) {
	  		if(removal[i]) {
	  			drawableObjects.remove(i);
	  		}
	 */
		
	/*	for(GameObject o: drawableObjects) {
			if((o instanceof Missile)) {
				if (((Missile)o).remove)
					drawableObjects.remove(o);
			}
		}
	*/
			if(!shipCrashed)
				ship.update(deltaT);
			else
				drawableObjects.remove(ship);
			}	
	}
	
	private void processKeyboardInput(){
	
		if (Gdx.app.getType() != ApplicationType.Desktop) return; // Just in case :)
		if(Gdx.input.isKeyJustPressed(Keys.P) && !Constants.GAME_OVER){
			if(!Constants.PAUSED) 
				Constants.PAUSED = true;
			else 
				Constants.PAUSED = false;
		}
		if(!Constants.PAUSED) {	
			if (Gdx.input.isKeyPressed(Keys.UP) && !shipCrashed) 
				ship.moveForward(Gdx.graphics.getDeltaTime());
			if(Gdx.input.isKeyJustPressed(Keys.UP) && !shipCrashed) {
				thrustersSound.play(0.5f);
			}
			// If one second has passed then update time and shoot a missile
			if(System.currentTimeMillis() >= time + 1000) {
				time = System.currentTimeMillis();
				if (Gdx.input.isKeyPressed(Keys.SPACE) && !shipCrashed) {
					initMissile();
				}
				if(Gdx.input.isKeyPressed(Keys.SPACE) && !shipCrashed) {
					missileSound.play(0.5f);
				}
			}
		}
	}
	
	public boolean isShipCrashed() {
		return shipCrashed;
	}
	
	public boolean isAsteroidEx() {
		return asteroidExplosion;
	}
	
	public void setAsteroidExplosion(boolean b) {
		asteroidExplosion =  b;
	}
	
	public float getExplosionX() {
		return explosionX;
	}
	
	public float getExplosionY() {
		return explosionY;
	}
	
	public void dispose() {
		
		if(thrustersSound != null)
			thrustersSound.dispose();
		
		if(backgroundNoise != null)
			backgroundNoise.dispose();
		
		if(explosionSound != null) 
			explosionSound.dispose();
		
		if(missileSound != null)
			missileSound.dispose();
	}
	
	public ArrayList<GameObject> getDrawableObjects(){
		return drawableObjects;
	}
	
	public int getScore() {
		return score;
	}
}
