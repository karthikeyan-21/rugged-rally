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

import java.util.EnumMap;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;


public class Controller {

	public enum Mode {
		Free,
//		Pro,
		Championship
	}
	
	public enum Screen {
		Start,
		Mode,
		Country,
		Stage,
		Car,
		Game,
		ChampionshipContinue,
		ChampionshipData,
		ChampionshipStanding,
		Pause
	};

	public enum LevelMode {
		TimeTrial,
		Championship
	}
	
	public enum Level {
		UK("United Kingdom"),
		GERMANY("Germany"),
		JAPAN("Japan"),
		AUSTRALIA("Australia"),
		FINLAND("Finland");
		private String name;
		private Level(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public String getDirectory() {
			if(name.equals(UK.name)) {
				return "uk";
			}
			return name.toLowerCase();
		}
	}

	public enum Stage {
		Stage1("1"),
		Stage2("2"),
		Stage3("3"),
		Stage4("4"),
		Stage5("5"),
		Stage6("6"),
		Stage7("7"),
		Stage8("8"),
		Stage9("9"),
		Stage10("10");
		private String name;
		private Stage(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	
	public enum Cars {
		one("data/cars/car1"),
		two("data/cars/car2"),
		three("data/cars/car3"),
		four("data/cars/car4");
		
		private String name;
		private Texture texture;
		private Texture wTexture;

		private Cars(String name) {
			this.name = name;
//			Texture.setEnforcePotImages(false);
			initialize();
		}
		public void initialize() {
			dispose();
			texture = new Texture(Gdx.files.internal(getImageLocation()+".gif"));
			texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			wTexture = new Texture(Gdx.files.internal(getImageLocation()+"w.gif"));
			wTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			wTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		}
		private String getImageLocation() {
			return name;
		}
		
		public Texture getTexture() {
			return texture;
		}

		public Texture getWTexture() {
			return wTexture;
		}

		public void dispose() {
			if(texture != null) {
				texture.dispose();
			}
			if(wTexture != null) {
				wTexture.dispose();
			}
		}
	}
	
	private Mode mode;
	private Cars selectedCar;
	private Level selectedLevel;
	private Screen selectedScreen;
	
	private LevelMode levelMode;
	private Stage selectedStage;
	private boolean championshipStatus;
	private EnumMap<Level,Stage> stagesMap;
	private EnumMap<Cars,AtomicInteger> scoreCard;
	private EnumMap<Cars,AtomicInteger> levelScoreCard;

	public Controller(Mode mode) {
		this.mode = mode;
		stagesMap = new EnumMap<Level,Stage>(Level.class);
		for(Level level :  Level.values()) {
			stagesMap.put(level, Stage.Stage1);
		}
		scoreCard = new EnumMap<Cars,AtomicInteger>(Cars.class);
		for(Cars car :  Cars.values()) {
			scoreCard.put(car, new AtomicInteger());
		}
		levelScoreCard = new EnumMap<Cars,AtomicInteger>(Cars.class);
		load();
	}
	
	public void setMode(Mode mode) {
		this.mode = mode;
	}
	
	public Cars getSelectedCar() {
		return selectedCar;
	}

	public void setSelectedCar(Cars selectedCar) {
		this.selectedCar = selectedCar;
	}

	public Level getSelectedLevel() {
		return selectedLevel;
	}

	public void setSelectedLevel(Level selectedLevel) {
		this.selectedLevel = selectedLevel;
	}

	public Screen getSelectedScreen() {
		return selectedScreen;
	}

	public void setSelectedScreen(Screen selectedScreen) {
		this.selectedScreen = selectedScreen;
	}

	public LevelMode getGameMode() {
		return levelMode;
	}

	public void setLevelMode(LevelMode gameMode) {
		this.levelMode = gameMode;
	}

	public Mode getMode() {
		return mode;
	}

	public void setSelectedStage(Stage stage) {
		this.selectedStage = stage;
	}
	
	public Stage getSelectedStage() {
		return selectedStage;
	}

	public void setChampionshipStatus(boolean status) {
		championshipStatus = status;
	}
	
	public boolean getChampionshipStatus() {
		return championshipStatus;
	}
	
	public EnumMap<Level,Stage> getStagesMap() {
		return stagesMap;
	}
	
	public EnumMap<Cars,AtomicInteger> getScoreCard() {
		return scoreCard;
	}
	
	public EnumMap<Cars,AtomicInteger> getLevelScoreCard() {
		return levelScoreCard;
	}
	
	public void load() {
		Preferences preferences = Gdx.app.getPreferences("savesettings");
		setChampionshipStatus(preferences.getBoolean("championship", getChampionshipStatus()));
		for(Level level : Level.values()) {
			String stageValue = preferences.getString(level.name());
			if(stageValue != null && stageValue.trim().length() > 0) {
				getStagesMap().put(level, Stage.valueOf(stageValue));
			}
		}
		if(getChampionshipStatus()) {
			String levelValue = preferences.getString("level");
			if(levelValue != null && levelValue.trim().length() > 0) {
				setSelectedLevel(Level.valueOf(levelValue));
				setSelectedStage(getStagesMap().get(getSelectedLevel()));
			}
			String userCar = preferences.getString("user-car");
			if(userCar != null && userCar.trim().length() > 0) {
				setSelectedCar(Cars.valueOf(userCar));
			}
			for(Cars car : Cars.values()) {
				getScoreCard().put(car, new AtomicInteger(preferences.getInteger(car.name())));
			}
		}
	}
	
	public void save() {
		Preferences preferences = Gdx.app.getPreferences("savesettings");
		preferences.putBoolean("championship", getChampionshipStatus());
		if(getChampionshipStatus()) {
			preferences.putString("user-car", getSelectedCar().name());
			preferences.putString("level", getSelectedLevel().name());
			for(Entry<Cars,AtomicInteger> entry : getScoreCard().entrySet()) {
				preferences.putInteger(entry.getKey().name(), entry.getValue().get());
			}
		}
		for(Entry<Level,Stage> entry : getStagesMap().entrySet()) {
			preferences.putString(entry.getKey().name(), entry.getValue().name());
		}
		preferences.flush();
	}

	public void clearLevelScoreCard() {
		for(Entry<Cars,AtomicInteger> entry : levelScoreCard.entrySet()) {
			entry.getValue().set(0);
		}
	}

	public void clearScoreCard() {
		for(Entry<Cars,AtomicInteger> entry : scoreCard.entrySet()) {
			entry.getValue().set(0);
		}
	}

}
