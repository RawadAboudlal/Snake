package com.rawad.snake.client.state;

import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.jfxengine.gui.GuiRegister;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuState extends State {
	
	@FXML private Button playButton;
	@FXML private Button quitButton;
	
	@Override
	public void init() {
		
		GuiRegister.loadGui(this);
		
		playButton.setOnAction((e) -> sm.requestStateChange(StateChangeRequest.instance(GameState.class)));
		quitButton.setOnAction((e) -> game.requestStop());
		
	}
	
	@Override
	public void terminate() {
	}
	
	@Override
	protected void onActivate() {
	}
	
	@Override
	protected void onDeactivate() {
	}
	
}
