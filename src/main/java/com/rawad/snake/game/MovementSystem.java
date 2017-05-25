package com.rawad.snake.game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.rawad.gamehelpers.game.GameSystem;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.EventManager;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.MovementComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.entity.UserControlComponent;
import com.rawad.snake.game.event.CollisionEvent;
import com.rawad.snake.game.event.OutOfBoundsEvent;

public class MovementSystem extends GameSystem {
	
	private static final int MIN_DELAY = 2;
	
	private final Board gameBoard;
	
	private final EventManager eventManager;
	
	private Queue<Velocity> requestedMovements = new ConcurrentLinkedQueue<Velocity>();
	
	/** Number of ticks to wait before updating the movement of the snake. */
	private int delay = 0;
	
	private int cumulativeTicks = 0;
	
	public MovementSystem(Board gameBoard, EventManager eventManager) {
		super();
		
		this.gameBoard = gameBoard;
		this.eventManager = eventManager;
		
		compatibleComponentTypes.add(MovementComponent.class);
		compatibleComponentTypes.add(PositionComponent.class);
		compatibleComponentTypes.add(HeadComponent.class);
		compatibleComponentTypes.add(UserControlComponent.class);
		
	}
	
	@Override
	public void tick(Entity snakeHead) {
		
		cumulativeTicks++;
		
		if(cumulativeTicks >= delay) {
			
			MovementComponent movementComp = snakeHead.getComponent(MovementComponent.class);
			PositionComponent positionComp = snakeHead.getComponent(PositionComponent.class);
			
			Velocity movement = movementComp.getVelocity();
			Position position = positionComp.getPosition();
			
			Velocity movementDirection = requestedMovements.poll();
			
			if(movementDirection != null) {
				// Don't move backwards into self.
				if(movement.getX() != -movementDirection.getX()) movement.setX(movementDirection.getX());
				if(movement.getY() != -movementDirection.getY()) movement.setY(movementDirection.getY());
			}
			
			int x = position.getX() + movement.getX();
			int y = position.getY() + movement.getY();
			
			position.setX(x);
			position.setY(y);
			
			HeadComponent headComp = snakeHead.getComponent(HeadComponent.class);
			
			for(Entity tail: headComp.getTailParts()) {
				
				this.moveTail(tail);
				
			}
			
			// Last tail, the most recently moved tail. For the first tail part, this will be the head.
			Entity lastTail = snakeHead;
			
			MovementComponent tailAheadMovementComp = new MovementComponent();
			movementComp.copyData(tailAheadMovementComp);
			
			MovementComponent temp = new MovementComponent();
			
			for(Entity tail: headComp.getTailParts()) {
				
				MovementComponent tailMovement = tail.getComponent(MovementComponent.class);
				
				tailAheadMovementComp.copyData(temp);
				
				tailMovement.copyData(tailAheadMovementComp);
				
				temp.copyData(tailMovement);
				
				lastTail = tail;
				
			}
			
			// If the snake is more than just the head, delete the position previously occupied by the last tail.
			if(lastTail != snakeHead) {
				
				Velocity tailAheadMovement = tailAheadMovementComp.getVelocity();
				
				PositionComponent tailPositionComp = lastTail.getComponent(PositionComponent.class);
				
				Position tailPosition = tailPositionComp.getPosition();
				
				gameBoard.set(null, tailPosition.getX() - tailAheadMovement.getX(), 
						tailPosition.getY() - tailAheadMovement.getY());
				
			}
			
			// Do at the very end so a snake with 3 tail parts can never hit itself.
			if(gameBoard.isInBounds(x, y)) {
				
				if(gameBoard.get(x, y) != null) {
					eventManager.queueEvent(new CollisionEvent(snakeHead, gameBoard.get(x, y)));
				}
				
				gameBoard.set(snakeHead, x, y);
				
			} else {
				// Snake head no longer in bounds.
				eventManager.queueEvent(new OutOfBoundsEvent(snakeHead));
			}
			
			cumulativeTicks = 0;
			
		}
		
	}
	
	private void moveTail(Entity tail) {
		
		PositionComponent tailPositionComp = tail.getComponent(PositionComponent.class);
		MovementComponent tailMovementComp = tail.getComponent(MovementComponent.class);
		
		Position tailPosition = tailPositionComp.getPosition();
		Velocity tailMovement = tailMovementComp.getVelocity();
		
		tailPosition.setX(tailPosition.getX() + tailMovement.getX());
		tailPosition.setY(tailPosition.getY() + tailMovement.getY());
		
		// Updates the tail's position to overwrite the new spot on the board.
		gameBoard.set(tail, tailPosition.getX(), tailPosition.getY());
		
	}
	
	public void setDelay(int delay) {
		
		if(delay < MIN_DELAY) delay = MIN_DELAY;
		
		this.delay = delay;
		
		cumulativeTicks = 0;
		
	}
	
	public int getDelay() {
		return delay;
	}
	
	public Queue<Velocity> getRequestedMovements() {
		return requestedMovements;
	}
	
}
