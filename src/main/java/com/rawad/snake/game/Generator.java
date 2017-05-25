package com.rawad.snake.game;

import java.util.Random;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.snake.entity.EEntity;
import com.rawad.snake.entity.PositionComponent;

/**
 * Just a class to hold all the random generation logic.
 * 
 * @author Rawad
 *
 */
public final class Generator {
	
	private static Random random = new Random();
	
	/**
	 * 
	 * @param tile
	 * @param gameBoard
	 * @return {@code true} when the whole board is blocked and a tile location could not be generated, {@code false} otherwise.
	 */
	public static boolean generateFood(Board gameBoard) {
		
		boolean blocked = true;
		
		int maxIterations = gameBoard.getWidth() * gameBoard.getHeight();
		
		int iterationCount = 0;
		
		Entity food = Entity.createEntity(EEntity.FOOD);
		
		PositionComponent foodPositionComp = food.getComponent(PositionComponent.class);
		
		Position foodPosition = foodPositionComp.getPosition();
		
		do {
			
			int x = random.nextInt(gameBoard.getWidth());
			int y = random.nextInt(gameBoard.getHeight());
			
			if(gameBoard.get(x, y) == null) {
				
				foodPosition.setX(x);
				foodPosition.setY(y);
				
				gameBoard.set(food, x, y);
				
				gameBoard.addEntity(food);
				
				blocked = false;
				
			}
			
			iterationCount++;
			
		} while(blocked && iterationCount < maxIterations);// Whole board is blocked basically
		
		return blocked;
		
	}
	
}
