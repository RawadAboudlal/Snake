package com.rawad.snake.game.event;

import com.rawad.gamehelpers.game.event.Event;

public class GameOverEvent extends Event {
	
	public GameOverEvent() {
		super(EventType.GAME_OVER);
	}
	
}
