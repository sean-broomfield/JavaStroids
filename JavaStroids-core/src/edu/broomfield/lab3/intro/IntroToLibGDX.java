package edu.broomfield.lab3.intro;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import java.lang.Math;


public class IntroToLibGDX extends ApplicationAdapter{

	private SpriteBatch spriteBatch;
	private Sprite bug;
	private Sprite chest;
	private float rotDeg;
	boolean direc;

	
	@Override
	public void create() {
		// Game Initialization  
		spriteBatch = new SpriteBatch(); 
		bug = new Sprite(new Texture("EnemyBug.png"));
		bug.setSize(50, 85);
		bug.setOrigin(bug.getWidth() / 2, bug.getHeight() / 2);
		bug.setPosition(0, 0);
		chest = new Sprite(new Texture("ChestClosed.png"));
		chest.setSize(50, 85);
		chest.setOrigin(chest.getWidth()/2, chest.getHeight()/2);
		chest.setPosition(270, 240);
		rotDeg = 1;
		
	}

	@Override
	public void render() {
		// Game Loop
		float deltaTime = Gdx.graphics.getDeltaTime();
		
		Vector2 topRightVect = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Vector2 bottomLeftVect = new Vector2(0 - topRightVect.x, 0 - topRightVect.y);
		
		//Normalizes the vectors since only direction matters not magnitude
		topRightVect.nor();
		bottomLeftVect.nor();

		topRightVect.scl(10);
		bottomLeftVect.scl(10);
		
		//Check to see if the bug is at the origin or below the origin
		if(bug.getX() <= 0) {
			//If the bug is not facing the top right of the screen. Rotate it until it does. 
			if(bug.getRotation() != topRightVect.angle()) {
				bug.rotate(rotDeg);
				if((double)bug.getRotation() % Math.toDegrees(2 * Math.PI) <= topRightVect.angle()) {
					bug.setRotation(topRightVect.angle());
				}	
			}
		//If the bug is near the top right of the screen, Rotate it until it is facing the bottom left corner.
		} else if(bug.getX() > Gdx.graphics.getWidth() - 85) {
			if(bug.getRotation() != bottomLeftVect.angle()) {
				bug.rotate(rotDeg);
				if((double) bug.getRotation() % Math.toDegrees(2 * Math.PI) >= bottomLeftVect.angle()) {
					bug.setRotation(bottomLeftVect.angle());
				}
			}
		}
		
		//If the bug is facing the top right move it in that direction.
		if(bug.getRotation() == topRightVect.angle()) {
			bug.translate(topRightVect.x * deltaTime, topRightVect.y * deltaTime);
		//If the bug is facing the bottom left move it in that direction.
		} else if (bug.getRotation() == bottomLeftVect.angle()) {
			bug.translate(bottomLeftVect.x * deltaTime,  bottomLeftVect.y * deltaTime);
		}
		
	
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.2f, 1);
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		bug.draw(spriteBatch);
		chest.draw(spriteBatch);
		spriteBatch.end();
	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
		super.dispose();
	}
}
