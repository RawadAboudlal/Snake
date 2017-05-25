package com.rawad.snake.game.event;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.GameEngine;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.Listener;
import com.rawad.snake.entity.EEntity;
import com.rawad.snake.entity.FoodComponent;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.MovementComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.entity.TailComponent;
import com.rawad.snake.game.Board;
import com.rawad.snake.game.Position;
import com.rawad.snake.game.Velocity;

public class CollisionListener implements Listener {
	
	private GameEngine gameEngine;
	
	private Board gameBoard;
	
	public CollisionListener(GameEngine gameEngine, Board gameBoard) {
		super();
		
		this.gameEngine = gameEngine;
		
		this.gameBoard = gameBoard;
		
	}
	
	@Override
	public void onEvent(Event ev) {
		
		CollisionEvent collisionEvent = (CollisionEvent) ev;
		
		Entity snake = collisionEvent.getEntity();
		Entity collidingWith = collisionEvent.getCollidingWith();
		
		if(collidingWith.getComponent(FoodComponent.class) != null) {
			
			gameBoard.removeEntity(collidingWith);
			
			HeadComponent headComp = snake.getComponent(HeadComponent.class);
			
			Entity newTail = Entity.createEntity(EEntity.SNAKE_TAIL);
			
			ArrayList<Entity> tail = headComp.getTailParts();
			
			Entity lastTail = tail.get(tail.size() - 1);
			
			tail.add(newTail);
			
			PositionComponent tailPositionComp = newTail.getComponent(PositionComponent.class);
			MovementComponent tailMovementComp = newTail.getComponent(MovementComponent.class);
			
			PositionComponent lastTailPositionComp = lastTail.getComponent(PositionComponent.class);
			MovementComponent lastTailMovementComp = lastTail.getComponent(MovementComponent.class);
			
			Position tailPosition = tailPositionComp.getPosition();
			Position lastTailPosition = lastTailPositionComp.getPosition();
			
			Velocity tailMovement = tailMovementComp.getVelocity();
			Velocity lastTailMovement = lastTailMovementComp.getVelocity();
			
			int x = lastTailPosition.getX() - lastTailMovement.getX();
			int y = lastTailPosition.getY() - lastTailMovement.getY();
			
			Position newTailPosition = addTail(newTail, x, y);
			
			tailPosition.setX(newTailPosition.getX());
			tailPosition.setY(newTailPosition.getY());
			
			tailMovement.setX(lastTailPosition.getX() - newTailPosition.getX());
			tailMovement.setY(lastTailPosition.getY() - newTailPosition.getY());
			
			gameEngine.getEventManager().submitEvent(new FoodEatenEvent(snake));
			
		}
		
		if(collidingWith.getComponent(TailComponent.class) != null) {
			gameEngine.getEventManager().submitEvent(new GameOverEvent());
		}
		
	}
	
	/**
	 * 
	 * @param tail
	 * @param x
	 * @param y
	 * @return {@code Position} object of where the {@code tail} was actually added onto the game board.
	 */
	private Position addTail(Entity tail, int x, int y) {
		
		/*
		 * Combinations are as follows:
		 *  -------------- ---------- --------------
		 * | x - 1, y - 1 | x, y - 1 | x + 1, y - 1 |
		 *  -------------- ---------- --------------
		 * | x - 1, y     | x, y     | x + 1, y     |
		 *  -------------- ---------- --------------
		 * | x - 1, y + 1 | x, y + 1 | x + 1, y + 1 |
		 *  -------------- ---------  --------------
		 * If the tail cannot go in tile x, y then we go through, checking every other tile to see if it will work. There is at
		 * least one redundant check (the tail preceding this one), but that is also checked for.
		 */
		int[] xPositions = {x, x - 1, x + 1};
		int[] yPositions = {y, y - 1, y + 1};
		
		
		for(int newX: xPositions) {
			for(int newY: yPositions) {
				
				if(gameBoard.isInBounds(newX, newY)) {
					
					Entity newTile = gameBoard.get(newX, newY);
					
					// Can't over-write new tile AND has to be successfully set by game board.
					if(newTile == null && gameBoard.set(tail, newX, newY)) {
						return new Position(newX, newY);
					}
					
				}
				
			}
		}
		
		
		return new Position(0, 0);
	}
	
}
