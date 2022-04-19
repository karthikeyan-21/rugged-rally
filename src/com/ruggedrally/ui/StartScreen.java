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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;
import com.ruggedrally.core.Level.LevelContext;
import com.ruggedrally.ui.Controller.Cars;
import com.ruggedrally.ui.Controller.Level;
import com.ruggedrally.ui.Controller.LevelMode;
import com.ruggedrally.ui.Controller.Mode;
import com.ruggedrally.ui.Controller.Screen;
import com.ruggedrally.ui.Controller.Stage;


public class StartScreen extends AbstractScreen {
	
	private Label back;

	private com.badlogic.gdx.scenes.scene2d.Stage stage;
	private Table countryTable;
	private Table modeTable;
	private Table stageTable;
	private Table carsTable;
	private Label.LabelStyle style;

	private Table startTable;
	private Table championShipContinueTable;
	private Table championShipDataTable;

	private Label cStage;
	private Label cCountry;

	private Label cStart;

	private Image image;

	private Label statusLabel;

	private Table championShipStandingTable;

	private Image image1;
	private Image image2;
	private Image image3;
	private Image image4;

	private Label label1;
	private Label label2;
	private Label label3;
	private Label label4;

	private List<Texture> themes;

	public StartScreen(RuggedRallyGame game) {
		super(game);
		game.controller.setSelectedScreen(Screen.Start);
	}
	
	@Override
	public void render(float delta) {
		AbstractScreen.tweenManager.update(Gdx.graphics.getDeltaTime());
		back.visible = !(game.controller.getSelectedScreen().equals(Screen.Start));
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
//		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(WIDTH, HEIGHT, true);
		camera.setToOrtho(false, WIDTH, HEIGHT);

		image.originX = stage.centerX();
		image.originY = stage.centerY();

		back.originX = stage.centerX();
		back.originY = stage.centerY();
		back.x = 10;
		back.y = stage.centerY();

		startTable.originX =  stage.centerX();
		startTable.originY = stage.centerY();
		startTable.x = stage.centerX() + stage.width() + startTable.width/2;
		startTable.y = 0;

		modeTable.originX =  stage.centerX();
		modeTable.originY = stage.centerY();
		modeTable.x = stage.centerX() + stage.width() + modeTable.width/2;
		modeTable.y = 0;

		countryTable.originX =  stage.centerX();
		countryTable.originY = stage.centerY();
		countryTable.x = stage.centerX() + stage.width() + countryTable.width/2;
		countryTable.y = 0;

		stageTable.originX =  stage.centerX();
		stageTable.originY = stage.centerY();
		stageTable.x = stage.centerX() + stage.width() + stageTable.width/2;
		stageTable.y = 0;

		carsTable.originX =  stage.centerX();
		carsTable.originY = stage.centerY();
		carsTable.x = stage.centerX() + stage.width() + carsTable.width/2;
		carsTable.y = 0;
		
		championShipContinueTable.originX =  stage.centerX();
		championShipContinueTable.originY = stage.centerY();
		championShipContinueTable.x = stage.centerX() + stage.width() + championShipContinueTable.width/2;
		championShipContinueTable.y = 0;

		championShipDataTable.originX =  stage.centerX();
		championShipDataTable.originY = stage.centerY();
		championShipDataTable.x = stage.centerX() + stage.width() + championShipDataTable.width/2;
		championShipDataTable.y = 0;

		championShipStandingTable.originX =  stage.centerX();
		championShipStandingTable.originY = stage.centerY();
		championShipStandingTable.x = stage.centerX() + stage.width() + championShipStandingTable.width/2;
		championShipStandingTable.y = 0;

		statusLabel.originX =  stage.centerX();
		statusLabel.originY = stage.centerY();
		
		statusLabel.x = stage.centerX() +stage.width() + statusLabel.width;
		statusLabel.y = statusLabel.height/3;

	}

	@Override
	public void show() {
		createFont();
		for(Cars car : Cars.values()) {
			car.initialize();
		}
		style = new Label.LabelStyle(font, font.getColor());
		stage = new com.badlogic.gdx.scenes.scene2d.Stage(WIDTH,HEIGHT,true,batch){
			@Override
			public boolean keyUp(int keycode) {
				switch(keycode) {
					case Input.Keys.BACK:
						back();
						break;
					case Input.Keys.HOME:	
					case Input.Keys.ESCAPE:
						exit();
						break;
				}
				return super.keyUp(keycode);
			}
		};

		themes = new ArrayList<Texture>();
		
		Texture themeTexture1 = new Texture("data/theme/image1.jpg");
		Texture themeTexture2 = new Texture("data/theme/image2.jpg");
		Texture themeTexture3 = new Texture("data/theme/image3.jpg");
		Texture themeTexture4 = new Texture("data/theme/image4.jpg");
		themes.add(themeTexture1);
		themes.add(themeTexture2);
		themes.add(themeTexture3);
		themes.add(themeTexture4);

		image = new Image(themeTexture1);
		image.setFillParent(true);
		stage.addActor(image);
		
		back = new Label("<<",style,"<<") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				back();
				return true;
			}
		};
		back.touchable = true;
		stage.addActor(back);
		
		startTable = new Table("start");
		resizeTable(startTable);
		stage.addActor(startTable);
		
		Label title = new Label("Rugged Rally ",style, " Rugged Rally");
		Label description = new Label("start",style, "start") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				if(game.controller.getSelectedScreen().equals(Screen.Start)) {
					getStartMenuEndSequence().start(tweenManager);
					getModeMenuStartSequence().start(tweenManager);
					game.controller.setSelectedScreen(Screen.Mode);
				}
				return true;
			}
		};

		startTable.setFillParent(true);
		startTable.defaults().space("50","0","0","0").pad(WIDTH/6,0,0,0).center();
		startTable.add(title).bottom();
		startTable.addActor(title);
		startTable.row();
		startTable.defaults().space(10).pad(5).center();
		startTable.add(description).bottom();
		startTable.addActor(description);
		startTable.pack();
		
		modeTable = new Table("Mode");
		resizeTable(modeTable);
		stage.addActor(modeTable);
		
		LevelModeLabel timeTrial = new LevelModeLabel(LevelMode.TimeTrial);
		LevelModeLabel championship = new LevelModeLabel(LevelMode.Championship);
		
		modeTable.defaults().space(50,0,0,0).pad(WIDTH/6,0,0,0).center();
		modeTable.add(timeTrial).bottom();
		modeTable.addActor(timeTrial);
		modeTable.row();
		modeTable.defaults().space(10).pad(5).center();
		modeTable.add(championship).bottom();
		modeTable.addActor(championship);
		modeTable.pack();
		
		countryTable = new Table("Country");
		resizeTable(countryTable);
		stage.addActor(countryTable);
		
		countryTable.defaults().space(10,0,0,0).pad(30,0,0,0).center();
		for(Level level : Level.values()) {
			CountryLabel countryLabel = new CountryLabel(level);
			countryTable.add(countryLabel);
			countryTable.addActor(countryLabel);
			countryTable.row();
			countryTable.defaults().space(10).pad(5).center();
		}
		countryTable.pack();

		stageTable = new Table();
		resizeTable(stageTable);
		stage.addActor(stageTable);
		
		stageTable.defaults().space(20,0,0,0).pad(40,0,0,0).center();
		for(int i = 0; i < Stage.values().length ; i++) {
			Label _stage = new StageLabel(Stage.values()[i]);
			stageTable.add(_stage);
			stageTable.addActor(_stage);
			if( i == 3 || i == 7) {
				stageTable.row();
				stageTable.defaults().space(30).pad(10).center();
			}
		}
		stageTable.pack();
		
		carsTable = new Table();
		resizeTable(carsTable);
		stage.addActor(carsTable);

		carsTable.defaults().space(40).pad(WIDTH/6,0,0,0).center();
		for(int i = 0; i < Cars.values().length ; i++) {
			Image _stage = new CarImage(Cars.values()[i]);
			carsTable.add(_stage);
			carsTable.addActor(_stage);
		}
		carsTable.pack();

		championShipContinueTable = new Table();
		resizeTable(championShipContinueTable);
		stage.addActor(championShipContinueTable);
		
		Label continueChampionship = new Label("continue",style,"continue") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				game.controller.setSelectedScreen(Screen.ChampionshipData);
				getChampionshipContinueEndSequence().start(tweenManager);
				getChampionshipDataStartSequence().start(tweenManager);
				return true;
			}
		};
		continueChampionship.touchable = true;
		Label startNewChampionship = new Label("start new",style,"start new") {
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				game.controller.setChampionshipStatus(false);
				game.controller.setSelectedScreen(Screen.Car);
				game.controller.setSelectedCar(null);
				game.controller.setSelectedLevel(null);
				game.controller.setSelectedStage(null);
				game.controller.clearScoreCard();
				getChampionshipContinueEndSequence().start(tweenManager);
				getCarMenuStartSequence().start(tweenManager);
				return true;
			}
		};
		startNewChampionship.touchable = true;

		championShipContinueTable.defaults().space(20).pad(HEIGHT/4,0,0,0).center();
		championShipContinueTable.add(continueChampionship).bottom();
		championShipContinueTable.addActor(continueChampionship);
		championShipContinueTable.row();
		championShipContinueTable.defaults().space(10).pad(5).center();
		championShipContinueTable.add(startNewChampionship).bottom();
		championShipContinueTable.addActor(startNewChampionship);
		championShipContinueTable.row();
		championShipContinueTable.pack();

		championShipDataTable = new Table();
		resizeTable(championShipDataTable);
		stage.addActor(championShipDataTable);

		cCountry = new Label("Country",style, "Country");
		cStage = new Label("Stage",style,"stage");
		cStart = new Label("start",style,"start"){
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				game.controller.setChampionshipStatus(true);
				getChampionshipDataEndSequence().start(tweenManager);
				getLoadingLabelStartSequence().start(tweenManager);
				return true;
			}
		};
		championShipDataTable.defaults().space(20).pad(HEIGHT/4,0,0,0).center();
		championShipDataTable.add(cCountry).bottom();
		championShipDataTable.addActor(cCountry);
		championShipDataTable.row();
		championShipDataTable.defaults().space(10).pad(5).center();
		championShipDataTable.add(cStage).bottom();
		championShipDataTable.addActor(cStage);
		championShipDataTable.row();
		championShipDataTable.defaults().space(10).pad(5).center();
		championShipDataTable.add(cStart).bottom();
		championShipDataTable.addActor(cStart);
		championShipDataTable.pack();
		
		championShipStandingTable = new Table();
		resizeTable(championShipStandingTable);
		stage.addActor(championShipStandingTable);
		
		championShipStandingTable.defaults().space(0).pad(0,20,0,0).center();
		image1 = new Image(Cars.values()[3].getTexture());
		image1.originX = image1.width/2;
		image1.originY = image1.height/2;
		image1.rotation = 90;
		championShipStandingTable.add(image1);
		championShipStandingTable.addActor(image1);
		championShipStandingTable.defaults().space(0).pad(0,100,0,0).center();
		label1 = new Label("1",style,"");
		championShipStandingTable.add(label1);
		championShipStandingTable.addActor(label1);
		championShipStandingTable.row();
		
		championShipStandingTable.defaults().space(0).pad(0,20,0,0).center();
		image2 = new Image(Cars.values()[0].getTexture());
		image2.originX = image2.width/2;
		image2.originY = image2.height/2;
		image2.rotation = 90;
		championShipStandingTable.add(image2);
		championShipStandingTable.addActor(image2);
		championShipStandingTable.defaults().space(0).pad(0,100,0,0).center();
		label2 = new Label("2",style,"");
		championShipStandingTable.add(label2);
		championShipStandingTable.addActor(label2);
		championShipStandingTable.row();
		
		championShipStandingTable.defaults().space(0).pad(0,20,0,0).center();
		image3 = new Image(Cars.values()[1].getTexture());
		image3.originX = image3.width/2;
		image3.originY = image3.height/2;
		image3.rotation = 90;
		championShipStandingTable.add(image3);
		championShipStandingTable.addActor(image3);
		championShipStandingTable.defaults().space(0).pad(0,100,0,0).center();
		label3 = new Label("3",style,"");
		championShipStandingTable.add(label3);
		championShipStandingTable.addActor(label3);
		championShipStandingTable.row();
		
		championShipStandingTable.defaults().space(0).pad(0,20,0,0).center();
		image4 = new Image(Cars.values()[2].getTexture());
		image4.originX = image4.width/2;
		image4.originY = image4.height/2;
		image4.rotation = 90;
		championShipStandingTable.add(image4);
		championShipStandingTable.addActor(image4);
		championShipStandingTable.defaults().space(0).pad(0,100,0,0).center();
		label4 = new Label("4",style,"");
		championShipStandingTable.add(label4);
		championShipStandingTable.addActor(label4);
		championShipStandingTable.row();
		
		championShipStandingTable.defaults().space(0).pad(0,20,0,0).center();
		Label retryLabel = new Label("retry",style,"retry"){
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				getChampionshipStandingEndSequence().start(tweenManager);
				getLoadingLabelStartSequence().start(tweenManager);
				return true;
			}
		};
		retryLabel.touchable = true;
		championShipStandingTable.addActor(retryLabel);
		championShipStandingTable.add(retryLabel);

		championShipStandingTable.defaults().space(0).pad(0,50,0,0).center();
		Label continueLabel = new Label("continue",style,"continue"){
			@Override
			public boolean touchDown(float x, float y, int pointer) {
				getChampionshipStandingEndSequence().start(tweenManager);
				if(game.controller.getGameMode().equals(LevelMode.TimeTrial)) {
					game.controller.clearLevelScoreCard();
					getModeMenuStartSequence().start(tweenManager);
				} else if(game.controller.getGameMode().equals(LevelMode.Championship)) {
					for(Entry<Cars,AtomicInteger> entry : game.controller.getLevelScoreCard().entrySet()) {
						game.controller.getScoreCard().get(entry.getKey()).addAndGet(entry.getValue().get());
					}
					game.controller.clearLevelScoreCard();
					int ordinal = game.controller.getSelectedStage().ordinal();
					if(ordinal < 9) {
						game.controller.setSelectedStage(Stage.values()[++ordinal]);
						game.controller.getStagesMap().put(game.controller.getSelectedLevel(),
								Stage.values()[
								        Math.max(game.controller.getSelectedStage().ordinal(),game.controller.getStagesMap().get(game.controller.getSelectedLevel()).ordinal())
									]);
					} else if(ordinal == 9) {
						game.controller.setSelectedStage(Stage.Stage1);
						int levelOrdinal = game.controller.getSelectedLevel().ordinal();
						if(levelOrdinal < 4) {
							game.controller.setSelectedLevel(Level.values()[++levelOrdinal]);
							game.controller.getStagesMap().put(game.controller.getSelectedLevel(),
									Stage.values()[
									        Math.max(game.controller.getSelectedStage().ordinal(),game.controller.getStagesMap().get(game.controller.getSelectedLevel()).ordinal())
										]);
						} else if(levelOrdinal == 4) {
							getModeMenuStartSequence().start(tweenManager);
						}
					}
					game.controller.save();
					getChampionshipDataStartSequence().start(tweenManager);
				}
				return true;
			}
		};
		continueLabel.touchable = true;
		championShipStandingTable.addActor(continueLabel);
		championShipStandingTable.add(continueLabel);

		championShipStandingTable.pack();
		
		statusLabel = new Label("Loading...",style,"loading");
		stage.addActor(statusLabel);
		
		resize(0, 0);
		
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		
		switch(game.controller.getSelectedScreen()) {
			case Start:
				getStartMenuSequence().start(tweenManager);
				break;
			case Car:
				getCarMenuStartSequence().start(tweenManager);
				break;
			case Country:
				getCountryMenuStartSequence().start(tweenManager);
				break;
			case Mode:
				getModeMenuStartSequence().start(tweenManager);
				break;
			case Stage:
				getStageMenuStartSequence().start(tweenManager);
				break;
			case ChampionshipData:
				getChampionshipDataStartSequence().start(tweenManager);
				break;
			case ChampionshipStanding:
				getChampionshipStandingStartSequence().start(tweenManager);
				break;
		}
		getThemeSequence().start(tweenManager);
		game.activityHandler.updateGameMode();
	}

	private Timeline getThemeSequence() {
		Texture texture = themes.remove(0);
		themes.add(texture);
		image.setRegion(new TextureRegion(texture));
		return Timeline.createSequence().beginSequence()
		.push(Tween.to(image, SpriteAccessor.SCALE_XY, 100f).target(.95f ,.95f).ease(Elastic.IN))
		.push(Tween.to(image, SpriteAccessor.SCALE_XY, 100f).target(1f ,1f).ease(Elastic.OUT))
		.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				image.scaleX = 1;
				image.scaleY = 1;
				tweenManager.killTarget(image);
				getThemeSequence().start(tweenManager);
			}
		})
		.end();
	}

	private Timeline getChampionshipStandingEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipStandingTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + championShipStandingTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipStandingCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipStandingTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + championShipStandingTable.width/2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipStandingStartSequence() {
		final LevelMode gameMode = game.controller.getGameMode();
		Entry<Cars,AtomicInteger>[] entries = game.controller.getLevelScoreCard().entrySet().toArray(new Entry[4]);
    	Arrays.sort(entries, new Comparator<Entry<Cars,AtomicInteger>>() {
			@Override
			public int compare(Entry<Cars, AtomicInteger> o1,Entry<Cars, AtomicInteger> o2) {
				if(gameMode.equals(LevelMode.Championship)) {
					return (o1.getValue().get() + game.controller.getScoreCard().get(o1.getKey()).get()) 
							< (o2.getValue().get() + game.controller.getScoreCard().get(o2.getKey()).get()) ? -1 : 1;
					
				} else {
					return o1.getValue().get() < o2.getValue().get() ? -1 : 1;
				}
			}
		});
    	if(gameMode.equals(LevelMode.TimeTrial)) {
    		image1.setRegion(new TextureRegion(entries[3].getKey().getTexture()));
    		label1.setText(String.valueOf(4 - entries[3].getValue().get()));

    		image2.setRegion(new TextureRegion(entries[2].getKey().getTexture()));
    		label2.setText(String.valueOf(4 - entries[2].getValue().get()));

    		image3.setRegion(new TextureRegion(entries[1].getKey().getTexture()));
    		label3.setText(String.valueOf(4 - entries[1].getValue().get()));

    		image4.setRegion(new TextureRegion(entries[0].getKey().getTexture()));
    		label4.setText(String.valueOf(4 - entries[0].getValue().get()));

    	} else if(gameMode.equals(LevelMode.Championship)) {
    		image1.setRegion(new TextureRegion(entries[3].getKey().getTexture()));
    		label1.setText(String.valueOf(entries[3].getValue().get() + game.controller.getScoreCard().get(entries[3].getKey()).get()));

    		image2.setRegion(new TextureRegion(entries[2].getKey().getTexture()));
    		label2.setText(String.valueOf(entries[2].getValue().get() + game.controller.getScoreCard().get(entries[2].getKey()).get()));

    		image3.setRegion(new TextureRegion(entries[1].getKey().getTexture()));
    		label3.setText(String.valueOf(entries[1].getValue().get() + game.controller.getScoreCard().get(entries[1].getKey()).get()));

    		image4.setRegion(new TextureRegion(entries[0].getKey().getTexture()));
    		label4.setText(String.valueOf(entries[0].getValue().get() + game.controller.getScoreCard().get(entries[0].getKey()).get()));
    	}

		return Timeline.createSequence().beginParallel()
		.push(Tween.to(back, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH/2 + back.width),  HEIGHT/2).ease(Back.OUT))
		.push(Tween.to(championShipStandingTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - championShipStandingTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getStatusUpdateSequence(String text) {
		statusLabel.setText(text);
		statusLabel.x = WIDTH + WIDTH/2;
		statusLabel.y = statusLabel.height/2;
		tweenManager.killTarget(statusLabel);
		return Timeline.createSequence().beginSequence()
				.push(Tween.to(statusLabel, SpriteAccessor.POS_XY, 15f).target(-(WIDTH + WIDTH/2), statusLabel.height/2).ease(Back.OUT))
				.end();
	}
	
	private Timeline getLoadingLabelStartSequence() {
		statusLabel.setText("loading...");
		statusLabel.x = WIDTH + WIDTH/2;
		statusLabel.y = statusLabel.height/2;
		tweenManager.killTarget(statusLabel);
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(statusLabel, SpriteAccessor.POS_XY, DURATION).target(WIDTH - statusLabel.width, statusLabel.height).ease(Back.INOUT))
		.push(Tween.to(back, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH/2 + back.width),  HEIGHT/2).ease(Back.OUT))
		.end()
		.beginSequence()
		.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				createLevel();
				getLoadingLabelEndSequence().start(tweenManager);
			}
		}).end();
	}

	private Timeline getLoadingLabelEndSequence() {
		return Timeline.createSequence().beginSequence()
		.push(Tween.to(statusLabel, SpriteAccessor.POS_XY, DURATION).target(WIDTH + statusLabel.width/2, statusLabel.height).ease(Back.INOUT))
		.setCallback(new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				game.setScreen(game.renderScreen);
			}
		}).end();
	}
	
	private Timeline getChampionshipDataEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipDataTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + championShipDataTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipDataCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipDataTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + championShipDataTable.width/2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipDataStartSequence() {
		if(game.controller.getSelectedLevel() == null || game.controller.getSelectedStage() == null) {
			game.controller.setSelectedLevel(Level.values()[0]);
			game.controller.setSelectedStage(Stage.values()[0]);
		}
		cCountry.setText(game.controller.getSelectedLevel().getName());
		cStage.setText("Stage "+game.controller.getSelectedStage().getName());
		championShipDataTable.pack();
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipDataTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - championShipDataTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipContinueEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipContinueTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + championShipContinueTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipContinueCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipContinueTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + championShipContinueTable.width/2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getChampionshipContinueStartSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(championShipContinueTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - championShipContinueTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCarMenuEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(carsTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + carsTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCarMenuCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(carsTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + carsTable.width/2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCarMenuStartSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(carsTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - carsTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getStageMenuEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(stageTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + stageTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getStageMenuCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(stageTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + stageTable.width / 2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getStageMenuStartSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(stageTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - stageTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCountryMenuEndSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(countryTable, SpriteAccessor.POS_XY, DURATION).target(-(WIDTH / 2 + countryTable.width/2), 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCountryMenuCancelSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(countryTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + countryTable.width/2,0).ease(Back.INOUT))
		.end();
	}

	private Timeline getCountryMenuStartSequence() {
		return Timeline.createSequence().beginParallel()
		.push(Tween.to(countryTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - countryTable.width/2, 0).ease(Back.INOUT))
		.end();
	}

	private Timeline getModeMenuEndSequence() {
		return Timeline.createSequence().beginParallel()
			.push(Tween.to(modeTable, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH / 2 + modeTable.width/2), 0).ease(Back.INOUT))
			.end();
	}

	private Timeline getModeMenuCancelSequence() {
		return Timeline.createSequence().beginParallel()
			.push(Tween.to(modeTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + modeTable.width/2,0).ease(Back.INOUT))
			.end();
	}

	private Timeline getModeMenuStartSequence() {
		return Timeline.createSequence().beginParallel()
			.push(Tween.to(modeTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - modeTable.width/2,0).ease(Back.INOUT))
			.push(Tween.to(back, SpriteAccessor.POS_XY, DURATION).target(20,  HEIGHT/2).ease(Back.OUT))
			.end();
	}

	private Timeline getStartMenuEndSequence() {
		return Timeline.createSequence()
			.beginParallel()
			.push(Tween.to(startTable, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH / 2 + modeTable.width/2), 0).ease(Back.INOUT))
			.pushPause(0.3f)
			.end();
	}

	private Timeline getStartMenuCancelSequence() {
		return Timeline.createSequence()
			.beginParallel()
			.push(Tween.to(startTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH + startTable.width/2,0).ease(Back.INOUT))
			.push(Tween.to(back, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH/2 + back.width),  HEIGHT/2).ease(Back.INOUT))
			.end();
	}

	private Timeline getStartMenuSequence() {
		return Timeline.createSequence()
			.beginParallel()
			.push(Tween.to(startTable, SpriteAccessor.POS_XY, DURATION).target(WIDTH / 2 - startTable.width/2,0).ease(Back.INOUT))
			.push(Tween.to(back, SpriteAccessor.POS_XY, DURATION).target(- (WIDTH/2 + back.width),  HEIGHT/2).ease(Back.INOUT))
			.end();
	}

	private final class LevelModeLabel extends Label {
		private LevelMode mode;
		public LevelModeLabel(LevelMode mode) {
			super(mode.name(), style,mode.name());
			this.mode = mode;
			touchable = true;
		}
		@Override
		public boolean touchDown(float x, float y, int pointer) {
			game.controller.setLevelMode(mode);
			getModeMenuEndSequence().start(tweenManager);
			if(LevelMode.TimeTrial.equals(mode)) {
				game.controller.setSelectedScreen(Screen.Country);
				getCountryMenuStartSequence().start(tweenManager);
			} else if(LevelMode.Championship.equals(mode)) {
				if(game.controller.getMode().equals(Mode.Free)) {
					game.activityHandler.requestPurchase("championship");
				} else {
					if(game.controller.getChampionshipStatus()) {
						game.controller.load();
						game.controller.setSelectedScreen(Screen.ChampionshipContinue);
						getChampionshipContinueStartSequence().start(tweenManager);
					} else {
						game.controller.setSelectedLevel(null);
						game.controller.setSelectedStage(null);
						game.controller.setSelectedScreen(Screen.Car);
						getCarMenuStartSequence().start(tweenManager);
					}
				}
			}
			return true;
		}
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			if(LevelMode.Championship.equals(mode) && Mode.Free.equals(game.controller.getMode())){
				super.draw(batch, .6f);
			} else {
				super.draw(batch, parentAlpha);
			}
		}
	}

	private final class StageLabel extends Label {
		private Stage stage;
		public StageLabel(Stage stage) {
			super(stage.getName(),style,stage.getName());
			this.stage = stage;
			touchable = true;
		}
		@Override
		public boolean touchDown(float x, float y, int pointer) {
			if(game.controller.getSelectedLevel() != null && game.controller.getStagesMap().get(game.controller.getSelectedLevel()).ordinal() < stage.ordinal()) {
				getStatusUpdateSequence("play championship to unlock").start(tweenManager);
			} else {
				game.controller.setSelectedScreen(Screen.Car);
				game.controller.setSelectedStage(stage);
				getStageMenuEndSequence().start(tweenManager);
				getCarMenuStartSequence().start(tweenManager);
			}
			return true;
		}
		@Override
		public void draw(SpriteBatch batch, float parentAlpha) {
			if(game.controller.getSelectedLevel() != null && game.controller.getStagesMap().get(game.controller.getSelectedLevel()).ordinal() < stage.ordinal()) {
				super.draw(batch, .6f);
			} else {
				super.draw(batch, parentAlpha);
			}
		}
	}
	
	private final class CarImage extends Image {
		private Cars car;
		public CarImage(Cars car) {
			super(car.getTexture());
			this.car = car;
			touchable = true;
		}
		@Override
		public boolean touchDown(float x, float y, int pointer) {
			game.controller.setSelectedCar(car);
			getCarMenuEndSequence().start(tweenManager);
			if(game.controller.getGameMode().equals(LevelMode.Championship)) {
				game.controller.setSelectedScreen(Screen.ChampionshipData);
				getChampionshipDataStartSequence().start(tweenManager);
			} else {
				game.controller.setSelectedScreen(Screen.Game);
				getLoadingLabelStartSequence().start(tweenManager);
			}
			return true;
		}
	}

	private final class CountryLabel extends Label {
		private Level level;
		public CountryLabel(Level level) {
			super(level.getName().toUpperCase(),style,level.getName().toUpperCase());
			this.level = level;
			touchable = true;
		}
		@Override
		public boolean touchDown(float x, float y, int pointer) {
			game.controller.setSelectedScreen(Screen.Stage);
			game.controller.setSelectedLevel(level);
			getCountryMenuEndSequence().start(tweenManager);
			getStageMenuStartSequence().start(tweenManager);
			return true;
		}
	}

	@Override
	public void hide() {

		if(font != null) {
			font.dispose();
		}
		
		if(themes != null) {
			for(Texture texture : themes) {
				texture.dispose();
			}
		}
	}

	@Override
	public void pause() {
		hide();
	}

	@Override
	public void resume() {
		show();
//		tweenManager.resume();
	}

	@Override
	public void dispose() {
		hide();
	}

	private void createLevel() {
		com.ruggedrally.core.Level level = new com.ruggedrally.core.Level();
		game.renderScreen.setLevel(level);
		LevelContext context = new LevelContext();
		context.camera = camera;
		context.stage = game.controller.getSelectedStage();
		context.mode = game.controller.getGameMode();
		context.level = game.controller.getSelectedLevel();
		context.car = game.controller.getSelectedCar();
		level.create(context);
	}

	private void back() {
		Screen selectedScreen = game.controller.getSelectedScreen();
		if(selectedScreen.equals(Screen.Stage)) {
			getStageMenuCancelSequence().start(tweenManager);
			if(game.controller.getGameMode().equals(LevelMode.TimeTrial)) {
				getCountryMenuStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Country);
			} else {
				getModeMenuStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Mode);
			}
		} else if(selectedScreen.equals(Screen.Car)) {
			getCarMenuCancelSequence().start(tweenManager);
			if(game.controller.getGameMode().equals(LevelMode.TimeTrial)) {
				getStageMenuStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Stage);
			} else {
				getModeMenuStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Mode);
			}
		} else if(selectedScreen.equals(Screen.Country)) {
			getCountryMenuCancelSequence().start(tweenManager);
			getModeMenuStartSequence().start(tweenManager);
			game.controller.setSelectedScreen(Screen.Mode);
		} else if(selectedScreen.equals(Screen.Mode)) {
			getModeMenuCancelSequence().start(tweenManager);
			getStartMenuSequence().start(tweenManager);
			game.controller.setSelectedScreen(Screen.Start);
		} else if(selectedScreen.equals(Screen.ChampionshipContinue)) {
			getChampionshipContinueCancelSequence().start(tweenManager);
			getModeMenuStartSequence().start(tweenManager);
			game.controller.setSelectedScreen(Screen.Mode);
		} else if(selectedScreen.equals(Screen.ChampionshipData)) {
			getChampionshipDataCancelSequence().start(tweenManager);
			if(game.controller.getChampionshipStatus()) {
				getChampionshipContinueStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.ChampionshipContinue);
			} else {
				getCarMenuStartSequence().start(tweenManager);
				game.controller.setSelectedScreen(Screen.Car);
			}
//			game.controller.setSelectedScreen(Screen.Mode);
		} else if(selectedScreen.equals(Screen.Start)) {
			exit();
		}
	}

}
