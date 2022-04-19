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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.ruggedrally.ui.Controller.Cars;
import com.ruggedrally.ui.Controller.Mode;

public class RuggedRallyGame extends Game {

	public ActivityHandler activityHandler;
	public StartScreen startScreen;
	public GameRenderScreen renderScreen;
	public Controller controller;

	@Override
	public void create() {
		controller = new Controller(Mode.Championship);
		startScreen = new StartScreen(this);
		renderScreen = new GameRenderScreen(this);
		setScreen(startScreen);
	}

	@Override
	public void dispose() {
		AbstractScreen.tweenManager.killAll();
		controller.save();
		super.dispose();
		for(Cars car : Cars.values()) {
			car.dispose();
		}
	}
	
	@Override
	public void render () {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(0, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		super.render();
	}
	
}