package com.rawad.snake.game.event;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;

public class CollisionEvent extends Event {
	
	private final Entity entity;
	private final Entity collidingWith;
	
	public CollisionEvent(Entity entity, Entity collidingWith) {
		super(EventType.COLLISION);
		
		this.entity = entity;
		this.collidingWith = collidingWith;
		
	}
	
	/**
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * @return the collidingWith
	 */
	public Entity getCollidingWith() {
		return collidingWith;
	}
	
}
