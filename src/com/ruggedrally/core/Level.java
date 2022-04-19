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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.tiled.SimpleTileAtlas;
import com.badlogic.gdx.graphics.g2d.tiled.TileMapRenderer;
import com.badlogic.gdx.graphics.g2d.tiled.TiledLoader;
import com.badlogic.gdx.graphics.g2d.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ContactFilter;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.ruggedrally.ui.Controller.Cars;
import com.ruggedrally.ui.Controller.LevelMode;
import com.ruggedrally.ui.Controller.Stage;

public class Level {
	
	public Car mCa;
	public List<NpcCar> actors;
	private LevelContext levelContext;
	private Mesh pathMesh;
	private Mesh trackMesh;
	private TileMapRenderer tileRenderer;
	private float r,g,b;
	private List<Vector2> path1;
	private List<Vector2> path2;
	private Texture trackTexture;
	private Mesh textureMesh;
	private Texture levelTexture;
	private Body endpointBody;
	private Vector2 endpoint1;
	private Vector2 endpoint2;
	
	public static final class LevelContext {
		
		public final List<Vector2> path = new ArrayList<Vector2>();
		public final List<Integer> length = new ArrayList<Integer>();
		public final LevelDataProvider dataProvider = new LevelDataProvider(this);

		public Cars car;
		public Stage stage;
		public LevelMode mode;
		public float startAngle;
		public com.ruggedrally.ui.Controller.Level level;

		public World world;
		public Color particleColor;
		public OrthographicCamera camera;

	}
	
	public Level() {
	}
	
	public void create(LevelContext levelContext) {
		if(levelContext.mode == null || levelContext.level == null || levelContext.stage == null) {
			throw new IllegalArgumentException("level context is not properly initialized");
		}
		this.levelContext = levelContext;
		
		Properties props = new Properties();
		InputStream read = null;
		try {
			FileHandle internal = Gdx.files.internal(levelContext.dataProvider.getTileManifest());
			read = internal.read();
			props.load(read);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(read != null) {
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		levelContext.startAngle = Float.parseFloat(props.getProperty("start-angle")) * MathUtils.degreesToRadians;
		String property = props.getProperty("background-color");
		String[] rgb = null;
		if(property != null && (rgb = property.split(",")).length == 3) {
			r = Integer.parseInt(rgb[0].trim()) / 255f; 
			g = Integer.parseInt(rgb[1].trim()) / 255f;
			b = Integer.parseInt(rgb[2].trim()) / 255f;
		}
		levelContext.particleColor = new Color(r,g,b,.2f);

		createWorld();
		
		CatmullRomSpline2D spline1 = new CatmullRomSpline2D();
		String coords = String.valueOf(props.get("primitives.1"));
		String[] tokens = coords.split(",");
		for (int i = 0; i < tokens.length; i += 2) {
			spline1.add(new Vector2( Float.parseFloat(tokens[i]), Math.abs(Float.parseFloat(tokens[i + 1]))));
		}

		CatmullRomSpline2D spline2 = new CatmullRomSpline2D();
		coords = String.valueOf(props.get("primitives.3"));
		tokens = coords.split(",");
		for ( int i = 0; i < tokens.length; i += 2) {
			spline2.add(new Vector2( Float.parseFloat(tokens[i]), Math.abs(Float.parseFloat(tokens[i + 1]))));
		}

		CatmullRomSpline2D spline3 = new CatmullRomSpline2D();
		coords = String.valueOf(props.get("primitives.2"));
		tokens = coords.split(",");
		for (int i = 0; i < tokens.length; i += 2) {
			spline3.add(new Vector2( Float.parseFloat(tokens[i]), Math.abs(Float.parseFloat(tokens[i + 1]))));
		}
		levelContext.path.addAll(spline3.getPath(20));
		int vertexCount = levelContext.path.size();
		float[] vertices = new float[vertexCount * 2];
		int i = 0;
		for(Vector2 point : levelContext.path) {
			vertices[i++] = point.x;
			vertices[i++] = point.y;
		}
		for (int p = 0; p < levelContext.path.size(); p++) {
			Vector2 p1 = levelContext.path.get(p);
			Vector2 p2 = null;
			if(p == (levelContext.path.size() - 1)) {
				p2 = levelContext.path.get(0);
			} else {
				p2 = levelContext.path.get(p + 1);
			}
			int len = (int)p1.dst(p2);
			levelContext.length.add(Math.abs(len));
		}


		path1 = spline1.getPath(20);
		path2 = spline2.getPath(20);

		createEdgeChain(path1);
		createEdgeChain(path2);
		if(!path1.get(0).equals(path1.get(path1.size() - 1))) {
			createEdgeChainClosure(path1,path2);
		}
		if(endpointBody != null) {
			trackMesh = new Mesh(true,2,2,new VertexAttribute(Usage.Position,2,"position"));
			trackMesh.setAutoBind(true);
			ArrayList<Fixture> fixtureList = endpointBody.getFixtureList();
			EdgeShape edgeShape = (EdgeShape) fixtureList.get(0).getShape();
			endpoint1 = new Vector2();
			endpoint2 = new Vector2();
			edgeShape.getVertex1(endpoint1);
			edgeShape.getVertex2(endpoint2);
			trackMesh.setVertices(new float[] { endpoint1.x,endpoint1.y,endpoint2.x,endpoint2.y } );
		}
		
		vertexCount = path1.size() + path2.size();
		vertices = new float[vertexCount * 2 * 2];
		i = 0;
		int u = 0,v = 0;
		for(int j = 0; j < path1.size() ; j++) {
			Vector2 point1 = path1.get(j);
			vertices[i++] = point1.x;
			vertices[i++] = point1.y;
			vertices[i++] = u;
			vertices[i++] = v;
			if(u == 0 && v == 0) {
				u = 0;
				v = 1;
			} else if(u == 0 && v == 1) {
				u = 1;
				v = 0;
			} else if(u == 1 && v == 0) {
				u = 1;
				v = 1;
			} else if(u == 1 && v == 1) {
				u = 0;
				v = 0;
			}
			Vector2 point2 = path2.get(j);
			vertices[i++] = point2.x;
			vertices[i++] = point2.y;
			vertices[i++] = u;
			vertices[i++] = v;
			if(u == 0 && v == 0) {
				u = 0;
				v = 1;
			} else if(u == 0 && v == 1) {
				u = 1;
				v = 0;
			} else if(u == 1 && v == 0) {
				u = 1;
				v = 1;
			} else if(u == 1 && v == 1) {
				u = 0;
				v = 0;
			}
		}
		
		pathMesh = new Mesh(true,vertexCount,vertexCount,new VertexAttribute(Usage.Position,2,"position"),new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
		pathMesh.setAutoBind(true);
		pathMesh.setVertices(vertices);
		
		actors = new ArrayList<NpcCar>();
		int c = 1;
		for(Cars ca : Cars.values()) {
			if(ca != levelContext.car) {
				NpcCar ac = new NpcCar(false,ca);
				ac.create(levelContext,(c + 30) * c);
				actors.add(ac);
				c++;
			}
		}
		
		mCa = new Car(true,levelContext.car);
		mCa.create(levelContext, 30);
//		mCa.create(levelContext, levelContext.path.size() - 30);
//		((Ca)mCa).create(context, lContext,  2);
//		Gdx.input.setInputProcessor(mCa);
		TiledMap map = TiledLoader.createMap(Gdx.files.internal(levelContext.dataProvider.getTileMap()));
		SimpleTileAtlas atlas = new SimpleTileAtlas(map,Gdx.files.internal(levelContext.dataProvider.getTileAtlasDirectory())); 
		tileRenderer = new TileMapRenderer(map,atlas,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		
		trackTexture = new Texture(Gdx.files.internal(props.getProperty("track-texture")));
		trackTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		trackTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		textureMesh = new Mesh(true,8,8,new VertexAttribute(Usage.Position,2,"position"),new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoords"));
		levelTexture = new Texture(Gdx.files.internal(props.getProperty("level-texture")));
		levelTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		levelTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		
		vertices = new float[4 * 4];
		String[] split = props.getProperty("Grid-Dimension").split(",");
		int width = Integer.parseInt(split[0]) * 32 * 2;
		int height = Integer.parseInt(split[1]) * 32 * 2;
		int textureSize = 300/2;
		
		vertices[0] = -width;
		vertices[1] = -height;
		vertices[2] = 0;
		vertices[3] = 0;
		
		vertices[4] = -width;
		vertices[5] = height;
		vertices[6] = 0;
		vertices[7] = textureSize;
		
		vertices[8] = width;
		vertices[9] = -height;
		vertices[10] = textureSize;
		vertices[11] = 0;
		
		vertices[12] = width;
		vertices[13] = height;
		vertices[14] = textureSize;
		vertices[15] = textureSize;
		textureMesh.setVertices(vertices);

	}

	public void start() {
		for(NpcCar ca : actors) {
			ca.start();
		}
		mCa.start();
	}
	
	public void stop() {
		for(NpcCar ca : actors) {
			ca.stop();
		}
		mCa.stop();
	}
	
	private void createWorld() {
		levelContext.world = new World(new Vector2(0,0),false);
		levelContext.world.setContactFilter(new ContactFilter() {
			private int rankCounter = 1;
			@Override
			public boolean shouldCollide(Fixture fixtureA, Fixture fixtureB) {
				if(fixtureA.getBody() == endpointBody || fixtureB.getBody() == endpointBody) {
					if(fixtureA.getBody().getUserData() instanceof Car) {
						Car mCa2 = (Car) fixtureA.getBody().getUserData();
						if(mCa2.rank == 0) {
							mCa2.rank = rankCounter++;
						}
					} else if(fixtureB.getBody().getUserData() instanceof Car) {
						Car mCa2 = (Car) fixtureB.getBody().getUserData();
						if(mCa2.rank == 0) {
							mCa2.rank = rankCounter++;
						}
					}
					return false;
				}
				return true;
			}
		});
	}

	private void createEdgeChain(List<Vector2> path) {
		BodyDef cBodyDef = new BodyDef();
		cBodyDef.type = BodyDef.BodyType.KinematicBody;
		Body cBody = levelContext.world.createBody(cBodyDef);
		
		for(int e = 0;e < path.size() - 1;e++) {
			EdgeShape edgeShape = new EdgeShape();
			Vector2 vec1 = path.get(e);
			Vector2 vec2 = path.get(e + 1);
			edgeShape.set(vec1,vec2);
			Fixture edgeFixture = cBody.createFixture(edgeShape, 1);
			edgeFixture.setRestitution(.1f);
		}
		cBody.resetMassData();
	}

	private void createEdgeChainClosure(List<Vector2> path1,List<Vector2> path2) {
		BodyDef cBodyDef = new BodyDef();
		cBodyDef.type = BodyDef.BodyType.KinematicBody;
		Body cBody = levelContext.world.createBody(cBodyDef);
		EdgeShape edgeShape = new EdgeShape();
		Vector2 vec1 = path1.get(15);
		Vector2 vec2 = path2.get(15);
		edgeShape.set(vec1,vec2);
		Fixture edgeFixture = cBody.createFixture(edgeShape, 1);
		edgeFixture.setRestitution(1);
		cBody.resetMassData();

		cBodyDef = new BodyDef();
		cBodyDef.type = BodyDef.BodyType.KinematicBody;
		cBody = levelContext.world.createBody(cBodyDef);
		edgeShape = new EdgeShape();
		vec1 = path1.get(path1.size() - 15);
		vec2 = path2.get(path2.size() - 15);
		edgeShape.set(vec1,vec2);
		edgeFixture = cBody.createFixture(edgeShape, 1);
		edgeFixture.setRestitution(1);
		cBody.resetMassData();

		cBodyDef = new BodyDef();
		cBodyDef.type = BodyDef.BodyType.KinematicBody;
		endpointBody = levelContext.world.createBody(cBodyDef);
		edgeShape = new EdgeShape();
		vec1 = path1.get(path1.size() - 21);
		vec2 = path2.get(path2.size() - 21);
		edgeShape.set(vec1,vec2);
		edgeFixture = endpointBody.createFixture(edgeShape, 1);
		edgeFixture.setRestitution(1);
		endpointBody.resetMassData();
	}

	public void logic() {
		for(NpcCar a : actors) {
			a.logic();
		}
		mCa.logic();
		levelContext.world.step(Gdx.graphics.getDeltaTime(), 6, 4);
	}
	
	public void render() {
		GL10 gl = Gdx.graphics.getGL10();
		gl.glClearColor(r, g, b, 0);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		gl.glColor4f(1, 1, 1, .8f);

		Gdx.graphics.getGL10().glEnable(GL10.GL_TEXTURE_2D);
		levelTexture.bind(50);
		textureMesh.render(GL10.GL_TRIANGLE_STRIP,0,4);
		trackTexture.bind(50);
		pathMesh.render(GL10.GL_TRIANGLE_STRIP,0,levelContext.path.size() * 2);

		tileRenderer.render(levelContext.camera);

		gl.glColor4f(1, 0, 0, 0.1f);
		gl.glLineWidth(5);
		trackMesh.render(GL10.GL_LINES,0,4);

		gl.glColor4f(0, 0, 0, 0.5f);
		for(NpcCar a : actors) {
			a.renderWheels();
		}
		mCa.renderWheels();
		gl.glColor4f(1, 1, 1, 0.5f);
		for(NpcCar a : actors) {
			a.render();
		}
		mCa.render();
	}

	public InputAdapter getInputAdapter() {
		return mCa;
	}
	
	public void dispose() {
		if(mCa != null) {
			mCa.dispose();
		}
		for(NpcCar a : actors) {
			a.dispose();
		}
		if(levelTexture != null) {
			levelTexture.dispose();
		}
		if(trackTexture != null) {
			trackTexture.dispose();
		}
		if(tileRenderer != null) {
			tileRenderer.dispose();
		}
		if(textureMesh != null) {
			textureMesh.dispose();
		}
		if(pathMesh != null) {
			pathMesh.dispose();
		}
		if(levelContext.world != null) {
			levelContext.world.dispose();
		}
	}
	
}