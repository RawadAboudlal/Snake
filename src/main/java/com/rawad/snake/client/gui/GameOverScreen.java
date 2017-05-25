package com.rawad.snake.client.gui;

import com.rawad.gamehelpers.client.gui.Hideable;
import com.rawad.jfxengine.loader.GuiLoader;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameOverScreen extends GridPane implements Hideable {
	
	@FXML private Label scoreLabel;
	@FXML private Button playAgain;
	@FXML private Button mainMenu;
	
	public GameOverScreen() {
		super();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load(GuiLoader.streamLayoutFile(getClass()));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@Override
	public void show() {
		setVisible(true);
	}
	
	@Override
	public void hide() {
		Platform.runLater(() -> {
			setVisible(false);
			getParent().requestFocus();
		});
	}
	
	@Override
	public boolean isShowing() {
		return isVisible();
	}
	
	/**
	 * @return the scoreLabel
	 */
	public Label getScoreLabel() {
		return scoreLabel;
	}
	
	/**
	 * @return the playAgain
	 */
	public Button getPlayAgain() {
		return playAgain;
	}
	
	/**
	 * @return the mainMenu
	 */
	public Button getMainMenu() {
		return mainMenu;
	}
	
}
