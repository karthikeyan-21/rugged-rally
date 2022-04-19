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

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.World;
import com.ruggedrally.core.Level.LevelContext;

public class Wheel {
	
    private static final int[] INDEXES = new int[]{ 2,1,3,0 };

	protected Vector2 m_Position = new Vector2();
    
	private LevelContext context;
	private World world;
	private Body body;
	private Mesh cMesh;
	private float[] vertices;

	public ParticleEmitter particleEmitter;

	private Vector2 tempPos; 
	private boolean updateCamera;
	
    public Wheel(Vector2 position, float radius) {
        m_Position = position;
        updateCamera = false;
    }

    public Wheel(Vector2 position, float radius,boolean updateCamera) {
        m_Position = position;
        this.updateCamera = updateCamera;
    }

	public void create(LevelContext lContext) {
		this.context = lContext;
		this.world = lContext.world;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.angularDamping = 1f;
		bodyDef.linearDamping = 1f;
		body = world.createBody(bodyDef);
		body.setTransform(m_Position, 0);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(2, 4);
		Fixture fixture = body.createFixture(shape, 1);
		fixture.setSensor(true);
		body.resetMassData();

		cMesh = new Mesh(false,4,4,new VertexAttribute(Usage.Position,2,"position"));
		vertices = new float[4 * 2];
		updateVertices(body,cMesh);
		
		tempPos = new Vector2();
	}

	private void updateVertices(Body body,Mesh cMesh) {
		Fixture fixture = body.getFixtureList().get(0);
		PolygonShape shape = (PolygonShape)fixture.getShape();
		Transform transform = body.getTransform();
		tempPos = new Vector2();
		int j = 0;
		for(int i : INDEXES) {
			shape.getVertex(i, tempPos);
			transform.mul(tempPos);
			vertices[j++] = tempPos.x;
			vertices[j++] = tempPos.y;
		}
		if(updateCamera) {
			context.camera.position.set(vertices[0], vertices[1], 0);
		}
		cMesh.setVertices(vertices);
	}

    public Vector2 getAttachPoint() {
        return body.getPosition();
    }

    public Vector2 pointVel(Vector2 worldOffset) {
        Vector2 tangent = new Vector2(-worldOffset.y, worldOffset.x);
        return tangent.mul(body.getAngularVelocity()).add(body.getLinearVelocity());
    }
	
	public void render() {
//		Fixture fixture = body.getFixtureList().get(0);
//		PolygonShape shape = (PolygonShape)fixture.getShape();
//		shape.getVertex(2, tempPos);
//		body.getTransform().mul(tempPos);
//		context.camera.position.set(tempPos.x, tempPos.y, 0);
		updateVertices(body,cMesh);
		cMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void updateVertices() {
		updateVertices(body,cMesh);
	}
	
	public Body getBody() {
		return body;
	}

	public void dispose() {
		if(cMesh != null) {
			cMesh.dispose();
		}
	}
	
}
