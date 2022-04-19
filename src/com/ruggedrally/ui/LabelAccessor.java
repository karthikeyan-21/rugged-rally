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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import aurelienribon.tweenengine.TweenAccessor;

public class LabelAccessor implements TweenAccessor<Actor> {
	public static final int POS_XY = 1;
	public static final int CPOS_XY = 2;
	public static final int SCALE_XY = 3;
	public static final int ROTATION = 4;
	public static final int OPACITY = 5;
	public static final int TINT = 6;

	@Override
	public int getValues(Actor target, int tweenType, float[] returnValues) {
			switch (tweenType) {
			case POS_XY:
				returnValues[0] = target.x;
				returnValues[1] = target.y;
				return 2;
	
			case CPOS_XY:
				returnValues[0] = target.x + target.width/2;
				returnValues[1] = target.y + target.height/2;
				return 2;
	
			case SCALE_XY:
				returnValues[0] = target.scaleX;
				returnValues[1] = target.scaleY;
				return 2;
	
			case ROTATION: returnValues[0] = target.rotation; return 1;
//			case OPACITY: returnValues[0] = target.getColor().a; return 1;
	
//			case TINT:
//				returnValues[0] = target.getColor().r;
//				returnValues[1] = target.getColor().g;
//				returnValues[2] = target.getColor().b;
//				return 3;
	
			default: assert false; return -1;
		}
	}

	@Override
	public void setValues(Actor target, int tweenType, float[] newValues) {
			switch (tweenType) {
			case POS_XY: target.x = newValues[0];target.y = newValues[1]; break;
			case CPOS_XY: target.x = newValues[0] - target.width/2; target.y = newValues[1] - target.height/2; break;
			case SCALE_XY: target.scaleX = newValues[0]; target.scaleY = newValues[1]; break;
			case ROTATION: target.rotation = newValues[0]; break;
	
//			case OPACITY:
//				Color c = target.getColor();
//				c.set(c.r, c.g, c.b, newValues[0]);
//				target.setColor(c);
//				break;
	
//			case TINT:
//				c = target.getColor();
//				c.set(newValues[0], newValues[1], newValues[2], c.a);
//				target.setColor(c);
//				break;
	
			default: assert false;
		}
	}

}
