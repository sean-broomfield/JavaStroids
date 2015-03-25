package edu.broomfield.lab3.gameobjects;

//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.*;

public class Ship extends GameObject implements Updatable{
	
	private Vector2 direction;
	private Vector2 targetDirection;
	private Vector2 velocity;
	private final float MIN_VELOCITY = 20;
	
	public Ship(Texture texture, int x, int y) {
		sprite = new Sprite(texture);
		sprite.setOrigin(texture.getWidth()/2, texture.getHeight()/2);
		sprite.setPosition(x, y);
		direction = new Vector2(0, -1);
		targetDirection = new Vector2(0, -1);
		velocity = new Vector2(0, MIN_VELOCITY);
		setIsDrawable(true);
	}
	
	public void face(Vector2 targetPos) {
		targetDirection = targetPos;
	}
	
	@Override
	public void update(float deltaTime) {
//		Student, your code goes here
		double cosTheta = (direction.dot(targetDirection))/targetDirection.len();
		if(cosTheta >1) {
			cosTheta = 1;
		}
		
		double deg = Math.acos(cosTheta);
		deg = Math.toDegrees(deg) * deltaTime;
		if(direction.crs(targetDirection) > 0) {
			deg = -deg;
		}
		
		sprite.rotate((float) deg);
		direction.rotate( -(float) deg);
				
		//Bounds the ship to viewable areas in the screen
	//	if(sprite.getX() <= Gdx.graphics.getWidth() && sprite.getX() >= 0 && sprite.getY() <= Gdx.graphics.getHeight() - 30 && sprite.getY() >= 0) {
			sprite.translate(velocity.x*deltaTime, velocity.y*deltaTime);
	//	}
		if(velocity.len() > MIN_VELOCITY) {
			velocity = velocity.scl(1-deltaTime);
		}

	}

	public void moveForward(float deltaTime) {
//		Student, your code goes here	
		velocity.x = velocity.x + direction.x * velocity.len()*deltaTime*2;
		velocity.y = velocity.y - direction.y * velocity.len()*deltaTime*2;
		
		velocity.clamp(10, 80);
	}
	
	public Vector2 getDirection() {
		return direction;
	}
	
	public Vector2 getPosition() {
		return new Vector2(sprite.getX(), sprite.getY());
	}

}
