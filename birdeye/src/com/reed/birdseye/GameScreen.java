package com.reed.birdseye;

import box2dLight.RayHandler;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameScreen implements Screen {
	Game game;
	//static for getting position
	static OrthographicCamera camera;
	SpriteBatch batch;
	ShapeRenderer shapeRenderer;
	BitmapFont currentFont;
	Farm farm;
	Level level;
	Player player;
	// TEMP use arrayList for random or static assigned rocks
	Resource resource;
	ArrayListsz arrays;
	// Crafting craft = new Crafting();
	TopMenu topMenu;
	Iron iron;
	Inventory inv;
	Copper copper;
	CollisionDetection collision;
	Android android;
	Points points;
	Messages message;
	SwordShop swordShop;
	Fishing fishing;
	TradeShop trade;
	World world;
	RayHandler rayHandler;

	public GameScreen(Game game) {
		this.game = game;
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();

		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();
		level = new Level();
		player = new Player();
		resource = new Resource();
		arrays = new ArrayListsz();
		topMenu = new TopMenu();
		iron = new Iron();
		inv = new Inventory();
		// collision detection problem?
		fps = new FPSLogger();
		android = new Android();
		points = new Points();
		collision = new CollisionDetection();

		message = new Messages();
		swordShop = new SwordShop();
		fishing = new Fishing();
		trade = new TradeShop();
		arrays.treeArrayEstablisher();

		if (Gdx.app.getType() == ApplicationType.Android) {
			currentFont = Assets.cgfFont;
		} else
			currentFont = new BitmapFont();

		world = new World(new Vector2(0, 0), true);

		rayHandler = new RayHandler(world);

		Time.createLights(rayHandler);
		/*
		 * int[] maxTextureSize = new int[1]; IntBuffer buf =
		 * BufferUtils.newIntBuffer(16);
		 * Gdx.gl.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, buf); int result =
		 * buf.get(); System.out.println(result); int result2 = buf.get();
		 * System.out.println(result);
		 */
	}

	int lightX = 400, lightY = 400;
	FPSLogger fps;

	public void update(float deltaTime) {
		// can be changed anytime in time class
		player.setSprites();
		player.move();
		player.input();
		topMenu.input();
		inv.input();
		points.updateLevel();
		level.update();
		Messages.update();
		message.removeOldMessages();
		swordShop.textSetter();
		swordShop.update();
		swordShop.handleInput();
		collision.doCollision();
		inv.input();
		fishing.update();
		fishing.fishCaught();
		trade.textSetter();
		trade.update();
		trade.handleInput();
		// fps.log();
		Time.update(rayHandler);
	}

	public static float xRate = 0;
	public static float yRate = 0;

	public void draw(float deltaTime) {
		Gdx.gl.glClearColor(255f, 255f, 255f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		lightX = 402;
		lightY = 406;
		camera.translate(xRate, yRate);
		camera.update();

		batch.begin();
		// sets camera for drawing static items
		batch.setProjectionMatrix(camera.projection.cpy().translate(
				-(Gdx.graphics.getWidth() / 2),
				-(Gdx.graphics.getHeight() / 2), 0));

		level.draw(batch);

		// set camera for drawing moving items.
		batch.setProjectionMatrix(camera.combined);
		batch.draw(Assets.map, -958, -3316);
		swordShop.draw(batch);
		trade.draw(batch);
		arrays.drawTreeTrunk(batch);

		player.draw(batch, currentFont);
		arrays.drawBrush(batch, currentFont);

		batch.end();
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		batch.begin();

		// more static items
		batch.setProjectionMatrix(camera.projection.cpy().translate(
				-(Gdx.graphics.getWidth() / 2),
				-(Gdx.graphics.getHeight() / 2), 0));
		player.drawTools(batch);
		topMenu.draw(batch, currentFont);
		player.drawTools(batch);
		points.draw(batch);
		message.drawText(currentFont, batch);
		inv.draw(batch, currentFont);
		swordShop.drawInputText(batch, currentFont);
		trade.drawInputText(batch, currentFont);

		batch.end();

		points.drawBars(shapeRenderer);

	}

	@Override
	public void render(float delta) {
		update(delta);
		draw(delta);
		handleInput();
	}

	private void handleInput() {
		if (Gdx.input.isKeyPressed(Input.Keys.O)) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.I)) {
			camera.zoom -= 0.02;
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}
}
