package com.rawad.snake.entity;

/**
 * Defines all entity blueprints available to the Snake game.
 * 
 * @author Rawad
 *
 */
public enum EEntity {
	
	FOOD("Food"),
	SNAKE_TAIL("Snake Tail"),
	SNAKE_HEAD("Snake Head");
	
	private final String name;
	
	private EEntity(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static EEntity getByName(String name) {
		
		EEntity[] entityBlueprints = EEntity.values();
		
		for(EEntity entityBlueprint: entityBlueprints) {
			if(entityBlueprint.getName().equals(name)) return entityBlueprint;
		}
		
		return null;
		
	}
	
}
