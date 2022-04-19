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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Transform;
import com.badlogic.gdx.physics.box2d.joints.PrismaticJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.ruggedrally.core.Level.LevelContext;
import com.ruggedrally.ui.Controller.Cars;

public class Car extends InputAdapter {

	private static final int SPEED = 250;

	private static final float MAX_DRIVE_FORCE = 100000;
	
	private static final int FORWARD = 1 << 1;
	private static final int BACKWARD = 1 << 2;
	private static final int LEFT = 1 << 3;
	private static final int RIGHT = 1 << 4;
	
	protected int state;

	protected Mesh cMesh;
	protected float[] vertices;

	protected Body body;
	protected Wheel[] wheels;
	protected LevelContext context;
	
	protected SpriteBatch spriteBatch;
	protected ParticleEffect particleEffect;

	private boolean updateCamera;

	private RevoluteJoint frontWheelJoint1;
	private RevoluteJoint frontWheelJoint2;
	
	public Cars car;
	protected boolean start;

//	private long soundId;
//	private Sound carSound;
	
	public Car(boolean updateCamera, Cars car) {
		this.updateCamera = updateCamera;
		this.car = car;
	}
	
	public void start() {
		start = true;
//		if(soundId == 0) {
//			soundId = carSound.loop(1.0f);
//		}
	}
	public void stop() {
		start = false;
//		disposeSound();
	}

//	private void disposeSound() {
//		if(soundId > 0) {
//			carSound.stop(soundId);
//			carSound.dispose();
//		}
//	}
	
	public void create(LevelContext lContext,int pivot) {
		this.context = lContext;
		this.wheels = new Wheel[4];
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.angularDamping = 5f;
		bodyDef.linearDamping = 1f;
		body = lContext.world.createBody(bodyDef);
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10, 20);
		Fixture fixture = body.createFixture(shape, 1);
		body.setUserData(this);
//		fixture.setRestitution(.5f);
		body.resetMassData();

		cMesh = new Mesh(false,4 * 2 ,4 * 2,new VertexAttribute(Usage.Position,2,"position"),new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
		vertices = new float[4 * 4];
		updateVertices(body,cMesh);
//		state |= FORWARD;
		
		Vector2 position = lContext.path.get(pivot);
		
		body.setTransform(position, lContext.startAngle);
		lContext.camera.rotate(lContext.startAngle, 0, 0, 1);		
		
		Vector2 pt1 = body.getLocalCenter().cpy().add(new Vector2(8.0f,11));
		Vector2 pt2 = body.getLocalCenter().cpy().add(new Vector2(-8.0f,11));
		Vector2 pt3 = body.getLocalCenter().cpy().add(new Vector2(8.5f,-12));
		Vector2 pt4 = body.getLocalCenter().cpy().add(new Vector2(-8.5f,-12));

		wheels[0] = new Wheel(body.getWorldPoint(pt1),.5f,updateCamera);
		wheels[1] = new Wheel(body.getWorldPoint(pt2),.5f,updateCamera);
		wheels[2] = new Wheel(body.getWorldPoint(pt3),.5f,updateCamera);
		wheels[3] = new Wheel(body.getWorldPoint(pt4),.5f,updateCamera);
		
		wheels[0].create( lContext);
		wheels[1].create( lContext);
		wheels[2].create( lContext);
		wheels[3].create( lContext);
		
		RevoluteJointDef jointDef = new RevoluteJointDef();
		jointDef.bodyB = wheels[0].getBody();
		jointDef.bodyA = body;
		jointDef.localAnchorB.set(wheels[0].getBody().getLocalCenter());
		jointDef.localAnchorA.set(pt1);
		jointDef.enableLimit = true;
		jointDef.enableMotor = true;
		jointDef.maxMotorTorque = MAX_DRIVE_FORCE;
		frontWheelJoint1 = (RevoluteJoint)lContext.world.createJoint(jointDef);

		jointDef = new RevoluteJointDef();
		jointDef.bodyB = wheels[1].getBody();
		jointDef.bodyA = body;
		jointDef.localAnchorB.set(wheels[1].getBody().getLocalCenter());
		jointDef.localAnchorA.set(pt2);
		jointDef.enableLimit = true;
		jointDef.enableMotor = true;
		jointDef.maxMotorTorque = MAX_DRIVE_FORCE;
		frontWheelJoint2 = (RevoluteJoint)lContext.world.createJoint(jointDef);
		
		PrismaticJointDef pJointDef = new PrismaticJointDef();
		pJointDef.bodyA = wheels[2].getBody();
		pJointDef.bodyB = body;
		pJointDef.localAnchorA.set(wheels[2].getBody().getLocalCenter());
		pJointDef.localAnchorB.set(pt3);
		pJointDef.localAxisA.set(body.getLocalVector(new Vector2(1,0)));
		pJointDef.enableLimit = true;
		pJointDef.maxMotorForce = MAX_DRIVE_FORCE;
		pJointDef.lowerTranslation = pJointDef.upperTranslation = 0;
		lContext.world.createJoint(pJointDef);
		
		pJointDef = new PrismaticJointDef();
		pJointDef.bodyA = wheels[3].getBody();
		pJointDef.bodyB = body;
		pJointDef.localAnchorA.set(wheels[3].getBody().getLocalCenter());
		pJointDef.localAnchorB.set(pt4);
		pJointDef.localAxisA.set(body.getLocalVector(new Vector2(1,0)));
		pJointDef.enableLimit = true;
		pJointDef.maxMotorForce = MAX_DRIVE_FORCE;
		pJointDef.lowerTranslation = pJointDef.upperTranslation = 0;
		lContext.world.createJoint(pJointDef);

		spriteBatch = new SpriteBatch();
		spriteBatch.setColor(Color.BLUE);
		particleEffect = new ParticleEffect();
		particleEffect.load(Gdx.files.internal(lContext.dataProvider.getParticleConfiguration()), Gdx.files.internal(lContext.dataProvider.getParticleImageDirectory()));
		ParticleEmitter particleEmitter = particleEffect.getEmitters().get(0);
		particleEmitter.setAdditive(false);
		particleEmitter.getTint().setColors(new float[]{lContext.particleColor.r,lContext.particleColor.g,lContext.particleColor.b});
		particleEmitter.getTransparency().setAlwaysActive(true);
		particleEmitter.getTransparency().setHigh(.1f);
		particleEmitter.getTransparency().setLow(.1f);
		wheels[0].particleEmitter = new ParticleEmitter(particleEmitter);
		wheels[1].particleEmitter = new ParticleEmitter(particleEmitter);
//		wheels[2].particleEmitter = new ParticleEmitter(particleEmitter);
//		wheels[3].particleEmitter = new ParticleEmitter(particleEmitter);
		particleEffect.getEmitters().clear();
		particleEffect.getEmitters().add(wheels[0].particleEmitter);
		particleEffect.getEmitters().add(wheels[1].particleEmitter);
//		particleEffect.getEmitters().add(wheels[2].particleEmitter);
//		particleEffect.getEmitters().add(wheels[3].particleEmitter);
		
//		carSound = Gdx.audio.newSound(Gdx.files.internal("data/sound/csound.ogg"));
//		pitch = -1;
	}

	protected void updateVertices(Body body,Mesh cMesh) {
		Fixture fixture = body.getFixtureList().get(0);
		PolygonShape shape = (PolygonShape)fixture.getShape();
		Transform transform = body.getTransform();

		Vector2 pos = new Vector2();
		shape.getVertex(2, pos);
		transform.mul(pos);
		vertices[0] = pos.x;
		vertices[1] = pos.y;
		vertices[2] = 0;
		vertices[3] = 0;
		
		shape.getVertex(1, pos);
		transform.mul(pos);
		vertices[4] = pos.x;
		vertices[5] = pos.y;
		vertices[6] = 0;
		vertices[7] = 1;

		shape.getVertex(3, pos);
		transform.mul(pos);
		vertices[8] = pos.x;
		vertices[9] = pos.y;
		vertices[10] = 1;
		vertices[11] = 0;
		
		shape.getVertex(0, pos);
		transform.mul(pos);
		vertices[12] = pos.x;
		vertices[13] = pos.y;
		vertices[14] = 1;
		vertices[15] = 1;

		context.camera.position.set(vertices[0], vertices[1], 0);
		cMesh.setVertices(vertices);
	}
	
	public void dispose() {
		if(cMesh != null) {
			cMesh.dispose();
		}
		if(spriteBatch != null) {
			spriteBatch.dispose();
		}
		if(particleEffect != null) {
			particleEffect.dispose();
		}
//		disposeSound();
		for(Wheel wheel : wheels) {
			wheel.dispose();
		}
	}
	
	public Vector2 getPosition() {
		return body.getPosition();
	}
	
	public void logic() {
		if(start) {
			updateTurn();
			updateDrive();
		}
		particleEffect.update(Gdx.graphics.getDeltaTime());
		if(body.getLinearVelocity().len() > 5) {
			particleEffect.start();
			for(Wheel wheel : wheels) {
				if(wheel.particleEmitter != null) {
					Vector2 position = wheel.getBody().getPosition();
					wheel.particleEmitter.setPosition(position.x, position.y);
				}
			}
		} else {
			particleEffect.allowCompletion();
		}
	}

	public int rank;
	private int lastAngle = 0;

	public void render() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_SRC_COLOR);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		car.getWTexture().bind(50);
		cMesh.render(GL10.GL_TRIANGLE_STRIP, 0, 4);
	}

	protected void updateDrive() {
        //find desired speed
        float desiredSpeed = 0;
        switch ( state & (FORWARD | BACKWARD)) {
            case FORWARD:
            	desiredSpeed = SPEED;
            	break;
            case BACKWARD: 
            	desiredSpeed = -SPEED;
            	break;
            default:
            	return; 
        }
    	applyWheelForce(desiredSpeed);

//        Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0,1) ).cpy();
//        Vector2 forwardVelocity = getForwardVelocity(body);
//        float currentSpeed = forwardVelocity.dot(currentForwardNormal);
//        carSound.setPitch(soundId, Math.abs((currentSpeed/SPEED)));
//        carSound.setVolume(soundId, Math.abs((currentSpeed/SPEED)));
	}

	protected final void applyWheelForce(float desiredSpeed) {
		applyWheelForce(desiredSpeed,wheels[0].getBody());
        applyWheelForce(desiredSpeed,wheels[1].getBody());
        applyWheelForce(desiredSpeed,wheels[2].getBody());
        applyWheelForce(desiredSpeed,wheels[3].getBody());
	}

	private void applyWheelForce(float desiredSpeed,Body wheel) {
		float force = 0;
        //find current speed in forward direction
        Vector2 currentForwardNormal = wheel.getWorldVector(new Vector2(0,1) ).cpy();
        Vector2 forwardVelocity = getForwardVelocity(wheel);
        float currentSpeed = forwardVelocity.dot(currentForwardNormal);
        //apply necessary force
    	if ( desiredSpeed > currentSpeed )
    		force = MAX_DRIVE_FORCE;
    	else if ( desiredSpeed < currentSpeed )
    		force = -MAX_DRIVE_FORCE/2;
    	else
    		return;
    	
        currentForwardNormal.mul(force);
//      body.applyForce(currentForwardNormal, body.getWorldCenter());
		wheel.applyForce( currentForwardNormal, wheel.getWorldCenter() );
//      wheel.applyLinearImpulse( currentForwardNormal, wheel.getWorldCenter() );
	}

	private void updateFriction(Body body) {
	      Vector2 impulse = getLateralVelocity(body).cpy().mul(-body.getMass());
	      body.applyLinearImpulse( impulse, body.getWorldCenter() );
	}
	
	private Vector2 getLateralVelocity(Body body) {
		Vector2 currentRightNormal = body.getWorldVector(new Vector2(0,0) );
	    return  currentRightNormal.mul(currentRightNormal.dot(body.getLinearVelocity()));
	}

	protected Vector2 getForwardVelocity(Body body) {
		Vector2 currentForwardNormal = body.getWorldVector(new Vector2(0,1) ).cpy();
        return currentForwardNormal.mul(currentForwardNormal.dot(body.getLinearVelocity())).cpy();
    }

	protected void updateTurn() {
		float limitLower = 0;
		float limitHigher = 0;
        float maxSteerSpeed = 0;
        switch ( state & (LEFT | RIGHT) ) {
            	case LEFT:  maxSteerSpeed = (float) Math.PI/2.5f;
            	limitHigher = (frontWheelJoint1.getUpperLimit() + maxSteerSpeed) % (float)Math.PI/2.5f;
            break;
            	case RIGHT: maxSteerSpeed = -(float) Math.PI/2.5f;
            	limitLower = (frontWheelJoint1.getLowerLimit() + maxSteerSpeed) % -(float)Math.PI/2.5f;
            break;
//            default: body.setAngularVelocity(0); break ;//nothing
        }

        frontWheelJoint1.setLimits(limitLower, limitHigher);
        frontWheelJoint2.setLimits(limitLower, limitHigher);
        if(maxSteerSpeed != 0) {
        	frontWheelJoint1.setMotorSpeed(maxSteerSpeed * 5);
        	frontWheelJoint2.setMotorSpeed(maxSteerSpeed * 5);
        	if(body.getLinearVelocity().len() > 32 && (state & FORWARD) != FORWARD && (state & BACKWARD) != BACKWARD) {
        		float desiredSpeed = MAX_DRIVE_FORCE;
        		applyWheelForce(desiredSpeed,wheels[0].getBody());
        		applyWheelForce(desiredSpeed,wheels[1].getBody());
        	}
        } else {
        	frontWheelJoint1.setMotorSpeed(0f);
        	frontWheelJoint2.setMotorSpeed(0f);
        }
        
    }
		
	@Override
	public boolean keyDown(int keycode) {
		boolean processed = false;
		switch(keycode) {
			case Input.Keys.UP:
			case Input.Keys.CENTER:	
			case Input.Keys.BUTTON_R1:
			case Input.Keys.BUTTON_X:
				processed = true;
				state |= FORWARD;
				break;
			case Input.Keys.DOWN:
			case Input.Keys.BUTTON_CIRCLE:
			case Input.Keys.BUTTON_L1:
				processed = true;
				state |= BACKWARD;
				break;
			case Input.Keys.RIGHT:
				processed = true;
				state |= RIGHT;
				break;
			case Input.Keys.LEFT:
				processed = true;
				state |= LEFT;
				break;
		}
		return processed;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		boolean processed = false;
		switch(keycode) {
			case Input.Keys.UP:
			case Input.Keys.CENTER:	
			case Input.Keys.BUTTON_R1:
			case Input.Keys.BUTTON_X:
				processed = true;
				state &= ~FORWARD;
				break;
			case Input.Keys.DOWN:
			case Input.Keys.BUTTON_CIRCLE:
			case Input.Keys.BUTTON_L1:
				processed = true;
				state &= ~BACKWARD;
				break;
			case Input.Keys.RIGHT:
				processed = true;
				state &= ~RIGHT;
				break;
			case Input.Keys.LEFT:
				processed = true;
				state &= ~LEFT;
				break;
		}
		return processed;
	}

	public void renderWheels() {
		spriteBatch.setProjectionMatrix(context.camera.combined);
		updateVertices(body,cMesh);
		Vector2 position = body.getPosition();
		int angle = (int)Math.toDegrees(body.getAngle());
		if(Math.abs(Math.abs(lastAngle) - Math.abs(angle)) > 0) {
			int newAngle = -(lastAngle - angle);
			if(updateCamera) {
				context.camera.position.set(position.x, position.y , 0);
				context.camera.rotate(newAngle, 0, 0, 1);
			}
			wheels[0].particleEmitter.getAngle().setHigh(lastAngle + 270, angle + 270);
			wheels[1].particleEmitter.getAngle().setHigh(lastAngle + 270, angle + 270);
			lastAngle = angle;
		}
		GLCommon gl = Gdx.gl;
		gl.glEnable(GL10.GL_BLEND);
		
		spriteBatch.begin();
		particleEffect.draw(spriteBatch);
		spriteBatch.end();

		wheels[0].render();
		wheels[1].render();
		if(updateCamera) {
			wheels[2].updateVertices();
			wheels[3].updateVertices();
		}
//		wheels[2].render();
//		wheels[3].render();
	}

	public void pause() {
//		carSound.setVolume(soundId, 0);
	}
	
}