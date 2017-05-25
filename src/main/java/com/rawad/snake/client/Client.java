package com.rawad.snake.client;

import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.Game;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.jfxengine.client.AbstractClient;
import com.rawad.jfxengine.gui.Root;
import com.rawad.snake.client.input.InputAction;
import com.rawad.snake.client.state.GameState;
import com.rawad.snake.client.state.MenuState;
import com.rawad.snake.entity.EEntity;
import com.rawad.snake.entity.RenderingComponent;
import com.rawad.snake.loader.Loader;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;

public class Client extends AbstractClient {
	
	private static final int SCREEN_WIDTH = 640;
	private static final int SCREEN_HEIGHT = 480;
	
	private static final int TARGET_FPS = 60;
	
	private SimpleStringProperty gameTitle;
	
	private Loader loader;
	
	@Override
	public void preInit(Game game) {
		super.preInit(game);
		
		loader = new Loader();
		loaders.put(loader);
		
		EntityFileParser entityFileParser = new EntityFileParser();
		fileParsers.put(entityFileParser);
		
		loader.loadEntityBlueprints(entityFileParser);
		
		for(EEntity entityKey: EEntity.values()) {
			BlueprintManager.getBlueprint(entityKey).getEntityBase().addComponent(new RenderingComponent());
		}
		
		this.initInputBindings();
		
	}
	
	@Override
	public void init() {
		
		sm.addState(new MenuState());
		sm.addState(new GameState());
		
		sm.setState(StateChangeRequest.instance(MenuState.class));
		
		renderingTimer.start();
		
		BlueprintManager.getBlueprint(EEntity.SNAKE_HEAD).getEntityBase().getComponent(RenderingComponent.class)
		.setTexture(loader.loadEntityTexture("Snake Head"));
		BlueprintManager.getBlueprint(EEntity.SNAKE_TAIL).getEntityBase().getComponent(RenderingComponent.class)
		.setTexture(loader.loadEntityTexture("Snake Tail"));
		BlueprintManager.getBlueprint(EEntity.FOOD).getEntityBase().getComponent(RenderingComponent.class)
		.setTexture(loader.loadEntityTexture("Food"));
		
	}
	
	@Override
	protected void initGui() {
		
		Scene scene = new Scene(new Root(new StackPane(new Label("Loading...")), ""), SCREEN_WIDTH, SCREEN_HEIGHT);
		
		stage.setOnCloseRequest((WindowEvent e) -> {
			
			game.requestStop();
			
			e.consume();
			
		});
		
		gameTitle = new SimpleStringProperty(game.getName());
		
		stage.titleProperty().bind(gameTitle);
		
		Platform.runLater(() -> {
			
			stage.setScene(scene);
			stage.show();
			
			update = true;
			
		});
		
	}
	
	private void initInputBindings() {
		
		inputBindings.setDefaultAction(InputAction.DEFAULT);
		
		inputBindings.put(InputAction.MOVE_UP, KeyCode.UP);
		inputBindings.put(InputAction.MOVE_DOWN, KeyCode.DOWN);
		inputBindings.put(InputAction.MOVE_RIGHT, KeyCode.RIGHT);
		inputBindings.put(InputAction.MOVE_LEFT, KeyCode.LEFT);
		inputBindings.put(InputAction.PAUSE, KeyCode.ESCAPE);
		
	}
	
	@Override
	public void terminate() {
		super.terminate();
		
		Platform.runLater(() -> stage.close());
		
	}
	
	@Override
	protected int getTargetFps() {
		return TARGET_FPS;
	}
	
}
