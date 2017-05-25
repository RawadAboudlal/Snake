package com.rawad.snake.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class FoodEatenEvent extends Event {
	
	/** The snake head that ate the food. */
	private final Entity snake;

	public FoodEatenEvent(Entity snake) {
		super(EventType.FOOD_EATEN);
		
		this.snake = snake;
		
	}
	
	/**
	 * @return the snake
	 */
	public Entity getSnake() {
		return snake;
	}
	
}
