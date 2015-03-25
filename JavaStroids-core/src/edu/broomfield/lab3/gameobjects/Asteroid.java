package edu.broomfield.lab3.gameobjects;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.*;
import edu.broomfield.lab3.game.Constants;

public class Asteroid extends GameObject implements Updatable{

	private float rotationalVel;
	private Vector2 dirAndVel;
	Random rand = new Random();
	int screenW;
	int screenH;
	
	public Asteroid(Texture tex){
		sprite = new Sprite(tex);
		sprite.setSize(Constants.ASTEROIDS_SIZE, Constants.ASTEROIDS_SIZE); 
		sprite.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
		dirAndVel = new Vector2 (rand.nextInt(24) - 12, rand.nextInt(30) - 15);
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
		setIsDrawable(true);
	}
	
	@Override
	public void update(float deltaTime) {
		sprite.rotate(getRotVel()); 
		

		if(sprite.getX() > screenW || sprite.getX() < 0 || sprite.getY() > screenH || sprite.getY() < 0) {
			//Generate a vector with x value from -20 to 20 and a y value of -15 to 15
			dirAndVel = new Vector2 (rand.nextInt(40) - 20, rand.nextInt(30) - 15);
		}
	
	
		sprite.translate(dirAndVel.x*deltaTime, dirAndVel.y*deltaTime);
	}
		
	public void setRotVel(float vel){
		rotationalVel = vel;
	}
	public float getRotVel(){
		return rotationalVel;
	}

}
