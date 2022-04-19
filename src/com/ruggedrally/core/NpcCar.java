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

import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.ruggedrally.core.Level.LevelContext;
import com.ruggedrally.ui.Controller.Cars;

public class NpcCar extends Car {

	public int headPivot;

	private Body headPointBody;

//	private Mesh kMesh;

	public NpcCar(Cars car) {
		super(false,car);
	}
	
	public NpcCar(boolean b,Cars car) {
		super(b,car);
	}

	public void create(LevelContext lContext,int pivot) {
		headPivot = pivot;
		this.context = lContext;
		super.create( lContext, headPivot);
		createnpc(lContext);
	}

	protected void createnpc(LevelContext lContext) {
		BodyDef headBodyDef = new BodyDef();
		headBodyDef.type = BodyDef.BodyType.KinematicBody;
		headPointBody = lContext.world.createBody(headBodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(1, 2);
		Fixture fixture = headPointBody.createFixture(shape, 1);
		fixture.setUserData(new Object());
		headPointBody.resetMassData();
		headPointBody.setTransform(lContext.path.get(headPivot), 0);
//		kMesh = new Mesh(false,5,5,new VertexAttribute(Usage.Position,2,"position"));
//		updateVerticesM(headPointBody, kMesh);
		
		RopeJointDef jointDef = new RopeJointDef();
		jointDef.bodyA = headPointBody;
		jointDef.bodyB = wheels[0].getBody();
		jointDef.localAnchorA.set(headPointBody.getLocalCenter());
		jointDef.localAnchorB.set(new Vector2(10,0));
		jointDef.maxLength = 10;
		lContext.world.createJoint(jointDef);

		jointDef = new RopeJointDef();
		jointDef.bodyA = headPointBody;
		jointDef.bodyB = wheels[1].getBody();
		jointDef.localAnchorA.set(headPointBody.getLocalCenter());
		jointDef.localAnchorB.set(new Vector2(-10,0));
		jointDef.maxLength = 10;
		lContext.world.createJoint(jointDef);
		
	}

	public void updateVerticesM(Body body,Mesh cMesh) {
		Fixture fixture = body.getFixtureList().get(0);
		PolygonShape shape = (PolygonShape)fixture.getShape();
		Transform transform = body.getTransform();
		Vector2 pos = new Vector2();
		float[] vertices = new float[shape.getVertexCount() * 2 + 2];
		for(int i = 0,j = 0;i < shape.getVertexCount(); i++) {
			shape.getVertex(i, pos);
			transform.mul(pos);
			vertices[j++] = pos.x;
			vertices[j++] = pos.y;
		}
		vertices[vertices.length - 2] = vertices[0];
		vertices[vertices.length - 1] = vertices[1];
		float y = body.getLinearVelocity().nor().y;
		y = Math.abs(y) == 0 ? 1 : y; 
		context.camera.position.set(vertices[0], vertices[1] , 0);
		cMesh.setVertices(vertices);
	}

	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void logic() {
		if(start) {
			calculateHeadPivot();
//		switch(rank) {
//			case 0:
//				calculateHeadPivot();
//				break;
//			default:
//				headPointBody.setLinearVelocity(0,0);
//				body.setLinearVelocity(0,0);
//				break;
//		}
			super.logic();
		}
	}

	private void calculateHeadPivot() {
		if(headPivot >= context.path.size()) {
			headPointBody.setLinearVelocity(0,0);
			start = false;
			return;
		}
		Vector2 currentPoint = context.path.get(headPivot).cpy();
		Vector2 nor = currentPoint.cpy().sub(headPointBody.getPosition())/*.nor()*/;
		Vector2 position = headPointBody.getPosition();
		int len = Math.abs((int)position.dst(currentPoint));
		if(len < 10) {
			headPivot++;
//			if(headPivot == context.path.size() - 1) {
//				headPivot = 0;
//			}
//			headPointBody.setLinearVelocity(0, 0);
		} else {
			Vector2 position2 = body.getPosition();
			len = Math.abs((int)position.dst(position2));
			if(len > 32) {
				headPointBody.setLinearVelocity(0,0);
			} else {
				headPointBody.setLinearVelocity(nor.mul(12));
			}
		}
		
	}
	
	public void render() {
		super.render();
//		updateVerticesM(headPointBody,kMesh);
//		kMesh.render(GL10.GL_LINE_STRIP, 0, 5);
	}

	public void dispose() {
		super.dispose();
//		lContext.
		//FIXME mem leaks.
	}
	
}