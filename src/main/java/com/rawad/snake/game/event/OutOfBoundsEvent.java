package com.rawad.snake.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class OutOfBoundsEvent extends Event {
	
	/** {@code Entity} that is out of bounds. */
	private final Entity entity;
	
	public OutOfBoundsEvent(Entity entity) {
		super(EventType.OUT_OF_BOUNDS);
		
		this.entity = entity;
		
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
}
