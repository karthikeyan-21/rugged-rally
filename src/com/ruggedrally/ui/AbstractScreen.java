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

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.graphics.g2d.stbtt.TrueTypeFontFactory;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.tablelayout.Table;

public abstract class AbstractScreen implements Screen {

	public static int WIDTH = 570;
	public static int HEIGHT = 320;

	protected static final float DURATION = 1.5f;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"�`'<>";

	protected RuggedRallyGame game;
	protected AbstractScreen next;
	protected AbstractScreen previous;

	
	protected  BitmapFont font;
	public static SpriteBatch batch;
	public static TweenManager tweenManager;
	public static OrthographicCamera camera;

	static {
//		font = new BitmapFont();
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		camera = new OrthographicCamera(WIDTH,HEIGHT);

		Tween.setWaypointsLimit(10);
		Tween.setCombinedAttributesLimit(3);
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(Actor.class, new LabelAccessor());
	}
	
	public AbstractScreen(RuggedRallyGame game) {
		this.game = game;
	}
	
	protected void exit() {
		game.activityHandler.finish();
		game.activityHandler.exit();
	}

	protected void createFont() {
//		BitmapFont tempfont = TrueTypeFontFactory.createBitmapFont(Gdx.files.internal("data/fonts/groundzero.ttf"), FONT_CHARACTERS, 10f, 6f, 1.0f, WIDTH, HEIGHT);
//		BitmapFontData data = tempfont.getData();
//		TextureRegion region = tempfont.getRegion();
//		data.flipped = false;
//		TextBounds bounds = tempfont.getBounds("test");
//		data.ascent = - bounds.height;
//		data.down = - bounds.height;
//		font = new BitmapFont(data, region, true);
//		font.setColor(139f/ 255f, 71f/ 255f, 38f/ 255f, 1f);
//		tempfont.dispose();
		font = new BitmapFont();
	}
	
	protected void resizeTable(Table table) {
//		table.debug("all");
		table.setClip(true);
		table.setFillParent(true);
		table.size(WIDTH / 3, HEIGHT);
		table.x = WIDTH / 2;
		table.y = - HEIGHT / 2;
	}

}
