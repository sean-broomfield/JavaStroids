package edu.broomfield.lab3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import edu.broomfield.lab3.intro.IntroToLibGDX;

public class LaunchIntro {
	public static void main(String [] args){
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Wandering Bug";
		config.width = 800;
		config.height = 480; 
		new LwjglApplication(new IntroToLibGDX(), config);
	}
}
