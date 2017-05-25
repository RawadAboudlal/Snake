package com.rawad.snake.client;

import com.rawad.gamehelpers.game.GameManager;
import com.rawad.snake.game.Snake;

import javafx.application.Application;
import javafx.stage.Stage;

public class SnakeStart extends Application {
	
	private static final Client client = new Client();
	
	private static final Snake game = new Snake();
	
	public static void main(String[] args) {
		
		game.getProxies().put(client);
		
		Application.launch(args);
		
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		
		client.setStage(stage);
		
		GameManager.launchGame(game);
		
	}
	
}
