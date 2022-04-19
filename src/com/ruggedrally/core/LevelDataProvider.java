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
package com.ruggedrally.core;

import java.text.MessageFormat;

import com.badlogic.gdx.Gdx;
import com.ruggedrally.core.Level.LevelContext;

public class LevelDataProvider {

	private static final String TRACK_PATH_TEMPLATE = "data/{0}/track{1}.tmf";
	private static final String TRACK_DIRECTORY_TEMPLATE = "data/{0}";
	
	private LevelContext context;
	
	public LevelDataProvider(LevelContext levelContext) {
		this.context = levelContext;
	}

	public String getTileManifest() {
		return MessageFormat.format(TRACK_PATH_TEMPLATE, context.level.getDirectory(),context.stage.getName());
	}
	
	public String getTileAtlasDirectory() {
		return getTileMap().substring(0, getTileMap().lastIndexOf("/"));
//		return MessageFormat.format(TRACK_DIRECTORY_TEMPLATE, context.level.getDirectory());
	}
	
	public String getParticleConfiguration() {
		return "data/particle/particle-effects.par";
	}
	
	public String getParticleImageDirectory() {
		return "data/particle";
	}
	
	public String getTileMap() {
		if(Gdx.files.internal(getTileManifest()+".tmx").exists()) {
			return getTileManifest()+".tmx";
		}
		return "data/track1.tmf.tmx";
	}
	
}
