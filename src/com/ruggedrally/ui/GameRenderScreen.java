/**
 * BSD Zero Clause License
 *
 * Copyright (c) 2012 Karthikeyan Natarajan (karthikeyan21@gmail.com)
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
 * REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
 * LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
 * OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
 * PERFORMANCE OF THIS SOFTWARE.
 */
package com.ruggedrally.ui;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.equations.Back;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.ruggedrally.core.Level;
import com.ruggedrally.core.NpcCar;
import com.ruggedrally.ui.Controller.Cars;
import com.ruggedrally.ui.Controller.Screen;


public class GameRenderScreen extends AbstractScreen {

	private Level level;
	private Stage stage;
	
	private Texture buttonRTexture;
	private Texture buttonLTexture;
	private Texture buttonATexture;
	private Texture buttonBTexture;

	private boolean pause;
	private Actor buttomRImage;
	private Actor buttomLImage;
	private Actor buttonAImage;
	private Actor buttonBImage;
	private Table pauseTable;
	private AtomicBoolean switchToStartScreen;
	
	public static IDebugHandler handler;
	
	public GameRenderScreen(RuggedRallyGame game) {
		super(game);
		switchToStartScreen = new AtomicBoolean();
	}
	
	public void setLevel(Level level) {
		camera = new OrthographicCamera(WIDTH,HEIGHT);
		this.level = level;
	}
	
	@Override
	public void render(float delta) {
//		if(handler != null) {
//			handler.start();
//		}
		GL10 gl = Gdx.graphics.getGL10();
		tweenManager.update(Gdx.graphics.getDeltaTime());
		if(!pause) {
			level.logic();
		}
		level.render();
		stage.act(delta);
		stage.draw();
        camera.update();
        camera.apply(gl);
        
        if(switchToStartScreen.get()) {
        	switchToStartScreen();
        }
        if(level.mCa.rank > 0) {
	        new Thread() {
	        	public void run() {
	        		try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    			game.controller.clearLevelScoreCard();
	    			game.controller.getLevelScoreCard().put(level.mCa.car, new AtomicInteger(4 - level.mCa.rank));
	    			level.stop();
	    			int len = 0;
	    			for(NpcCar ca : level.actors) {
	    				if(ca.rank > 0) {
	    					game.controller.getLevelScoreCard().put(ca.car, new AtomicInteger(4 - ca.rank));
	    				} else {
	    					game.controller.getLevelScoreCard().put(ca.car, new AtomicInteger(-ca.headPivot));
	    					len++;
	    				}
	    			}
	    			if(len > 0) {
	    				Entry<Cars,AtomicInteger>[] entries = new Entry[len];
	    				for(Entry<Cars,AtomicInteger> entry : game.controller.getLevelScoreCard().entrySet()) {
	    					if(entry.getValue().get() < 0) {
	    						entries[--len] = new AbstractMap.SimpleEntry(entry);
	    					}
	    				}
	    				Arrays.sort(entries, new Comparator<Entry<Cars,AtomicInteger>>() {
	    					@Override
	    					public int compare(Entry<Cars, AtomicInteger> o1,Entry<Cars, AtomicInteger> o2) {
	    						return o1.getValue().get() < o2.getValue().get() ? -1 : 1;
	    					}
	    				});
	    				int rank = 4;
	    				for(Entry<Cars,AtomicInteger> entry : entries) {
	    					game.controller.getLevelScoreCard().get(entry.getKey()).set(4 - rank--);
	    				}
	    			}
	    			switchToStartScreen.set(true);
	        	}
	        }.start();
        }
//		if(handler != null) {
//			handler.stop();
//		}
	}

	private void switchToStartScreen() {
		getModeMenuCancelSequence().start(tweenManager);
		game.controller.setSelectedScreen(Screen.ChampionshipStanding);
		game.setScreen(game.startScreen);
	};

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, WIDTH,HEIGHT);
		stage.setViewport(WIDTH,HEIGHT, true);
		
		buttomRImage.originX = WIDTH/2;
		buttomRImage.originY = HEIGHT/2;
		buttomRImage.x = WIDTH - (buttomRImage.width + buttomRImage.width/2);
		buttomRImage.y =  10;

		buttomLImage.originX = WIDTH/2;
		buttomLImage.originY = HEIGHT/2;
		buttomLImage.x = WIDTH/2 + buttomLImage.width;
		buttomLImage.y =  10;

		buttonAImage.originX = WIDTH/2;
		buttonAImage.originY = HEIGHT/2;
		buttonAImage.x =  10;
		buttonAImage.y =  HEIGHT/2;

		buttonBImage.originX = WIDTH/2;
		buttonBImage.originY = HEIGHT/2;
		buttonBImage.x =  10;
		buttonBImage.y =  HEIGHT/8;
		
		pauseTable.x = stage.centerX() + stage.width() + pauseTable.width/2;
		pauseTable.y = 0;
		pauseTable.x = WIDTH + pauseTable.width/2;
		pauseTable.y = 0;

	}

	@Override
	public void show() {
		switchToStartScreen.set(false);
		pause = false;
		createFont();
		stage = new com.badlogic.gdx.scenes.scene2d.Stage(WIDTH,HEIGHT,true,batch) {
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode) {
					case Input.Keys.BACK:
					case Input.Keys.HOME:	
					case Input.Keys.ESCAPE:
						buttonAImage.visible = false;
						buttonBImage.visible = false;
						buttomRImage.visible = false;
						buttomLImage.visible = false;
						pause = true;
						level.mCa.pause();
						getModeMenuStartSequence().start(tweenManager);
						break;
				}
				return level.getInputAdapter().keyUp(keycode) ? true : super.keyUp(keycode);
			}
			@Override
			public boolean keyDown(int keycode) {
				return level.getInputAdapter().keyDown(keycode) ? true : super.keyDown(keycode);
			}
			@Override
			public void draw () {
				camera.update();
				if (!root.visible) return;
				batch.setProjectionMatrix(camera.combined);
				batch.begin();
				if(pause) {
					root.draw(batch, 1f);
 				} else {
 					root.draw(batch, .5f);
 				}
				batch.end();
			}
		};
		LabelStyle style = new Label.LabelStyle(font, font.getColor());

		buttonRTexture = new Texture(Gdx.files.internal("data/ui/arrow-right.png"));
		buttomRImage = new Image(buttonRTexture) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				stage.keyDown(Input.Keys.RIGHT);
				return true;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				stage.keyUp(Input.Keys.RIGHT);
			}
		};
		stage.addActor(buttomRImage);

		buttonLTexture = new Texture(Gdx.files.internal("data/ui/arrow-left.png"));
		buttomLImage = new Image(buttonLTexture) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				stage.keyDown(Input.Keys.LEFT);
				return true;
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				stage.keyUp(Input.Keys.LEFT);
			}
		};
		stage.addActor(buttomLImage);

		buttonATexture = new Texture(Gdx.files.internal("data/ui/arrow-up.png"));
		buttonAImage = new Image(buttonATexture) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				return stage.keyDown(Input.Keys.UP);
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				stage.keyUp(Input.Keys.UP);
			}
		}; 
		stage.addActor(buttonAImage);

		buttonBTexture = new Texture(Gdx.files.internal("data/ui/arrow-down.png"));
		buttonBImage = new Image(buttonBTexture) {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				return stage.keyDown(Input.Keys.DOWN);
			}
			@Override
			public void touchUp(float x, float y, int pointer) {
				stage.keyUp(Input.Keys.DOWN);
			}
		}; 
		stage.addActor(buttonBImage);
		
		pauseTable = new Table("paused");
		resizeTable(pauseTable);
		stage.addActor(pauseTable);
		
		Label title = new Label("Resume",style, "Resume") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				getModeMenuCancelSequence().start(tweenManager);
				resume();
				return true;
			}
		};
		Label description = new Label("Return to main menu",style, "Return to main menu") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				getModeMenuCancelSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Mode);
				game.setScreen(game.startScreen);
				return true;
			}
		};

		pauseTable.defaults().space(10,0,0,0).pad(HEIGHT/3,20,0,0).center();
		pauseTable.add(title).bottom();
		pauseTable.addActor(title);
		pauseTable.row();
		pauseTable.defaults().space(10).pad(5).center();
		pauseTable.add(description).bottom();
		pauseTable.addActor(description);
		pauseTable.pack();
		resize(0, 0);
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		level.start();
	}

	@Override
	public void hide() {
		dispose();
	}

	private Timeline getModeMenuCancelSequence() {
		return Timeline.createSequence().beginParallel()
			.push(Tween.to(pauseTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + pauseTable.width/2,0).ease(Back.INOUT))
			.end();
	}

	private Timeline getModeMenuStartSequence() {
		return Timeline.createSequence().beginParallel()
			.push(Tween.to(pauseTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - pauseTable.width/2,0).ease(Back.INOUT))
			.end();
	}

	@Override
	public void pause() {
		exit();
	}

	@Override
	public void resume() {
		buttonAImage.visible = true;
		buttonBImage.visible = true;
		buttomRImage.visible = true;
		buttomLImage.visible = true;
		pause = false;
	}

	@Override
	public void dispose() {
		if(level != null) {
			level.dispose();
		}
		if(buttonATexture != null) {
			buttonATexture.dispose();
		}
		if(buttonBTexture != null) {
			buttonBTexture.dispose();
		}
		if(buttonRTexture != null) {
			buttonRTexture.dispose();
		}
		if(buttonLTexture != null) {
			buttonLTexture.dispose();
		}
	}

}
