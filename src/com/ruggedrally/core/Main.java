package com.ruggedrally.core;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.ruggedrally.core.Level.LevelContext;
import com.ruggedrally.ui.ActivityHandler;
import com.ruggedrally.ui.Controller.Cars;
import com.ruggedrally.ui.Controller.LevelMode;
import com.ruggedrally.ui.Controller.Stage;
import com.ruggedrally.ui.RuggedRallyGame;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		RuggedRallyGame listener = new RuggedRallyGame();
//		listener.activityHandler = new ActivityHandler(){
//			@Override
//			public void finish() {
//			}
//			@Override
//			public void exit() {
//			}
//			@Override
//			public void requestPurchase(String itemId) {
//			}
//			@Override
//			public void updateGameMode() {
//			}
//		};
//		new LwjglApplication(listener, "Rugged Rally", 800,400, false);
		new LwjglApplication(new RuggedRallyGame() {
			@Override
			public void create() {
				activityHandler = new ActivityHandler(){
					@Override
					public void finish() {
					}
					@Override
					public void exit() {
					}
					@Override
					public void requestPurchase(String itemId) {
					}
					@Override
					public void updateGameMode() {
					}
				};
				super.create();
				com.ruggedrally.core.Level level = new com.ruggedrally.core.Level();
				renderScreen.setLevel(level);
				LevelContext context = new LevelContext();
				context.camera = renderScreen.camera;
				context.stage = Stage.Stage9;
				context.mode = LevelMode.TimeTrial;
				context.level = com.ruggedrally.ui.Controller.Level.AUSTRALIA;
				context.car = Cars.four;
				level.create(context);
				setScreen(renderScreen);
			}
		}, "test", 800,400, false);
	}
/*
 * australia: stage 5
 * finland: stage 9
 * 
 */
}