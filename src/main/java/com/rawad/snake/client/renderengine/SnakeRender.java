package com.rawad.snake.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.MovementComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.entity.RenderingComponent;
import com.rawad.snake.game.Position;
import com.rawad.snake.game.Velocity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Affine;

public class SnakeRender extends Render {
	
	public void render(GraphicsContext g, Entity snakeHead, RenderingComponent renderingComp) {
		
		HeadComponent headComp = snakeHead.getComponent(HeadComponent.class);
		PositionComponent headPositionComp = snakeHead.getComponent(PositionComponent.class);
		MovementComponent movementComp = snakeHead.getComponent(MovementComponent.class);
		
		Position headPosition = headPositionComp.getPosition();
		
		final Affine perTailTransform = g.getTransform();
		
		for(Entity tail: headComp.getTailParts()) {
			
			PositionComponent tailPositionComp = tail.getComponent(PositionComponent.class);
			
			Position tailPosition = tailPositionComp.getPosition();
			
			double dx = tailPosition.getX() - headPosition.getX();
			double dy = tailPosition.getY() - headPosition.getY();
			
			g.translate(dx * TileRender.TILE_WIDTH, dy  * TileRender.TILE_HEIGHT);
			
			renderSnakePart(g, tail.getComponent(RenderingComponent.class), tail.getComponent(MovementComponent.class));
			
			g.setTransform(perTailTransform);
			
		}
		
		renderSnakePart(g, renderingComp, movementComp);
		
		g.setTransform(perTailTransform);
		
	}
	
	private void renderSnakePart(GraphicsContext g, RenderingComponent renderingComp, MovementComponent movementComp) {
		
		Velocity movement = movementComp.getVelocity();
		
		final double halfTileWidth = (double) TileRender.TILE_WIDTH / 2d;
		final double halfTileHeight = (double) TileRender.TILE_HEIGHT / 2d;
		
		g.translate(halfTileWidth, halfTileHeight);
		
		if(movement != null) {
			
			int vx = movement.getX();
			int vy = movement.getY();
			
			/*
			 * Draw unit circle with 4 directions (N, S, E, W) and coordinates. Find nonzero coordinate, take negative,
			 * and perform the inverse and opposite function where the opposite of the function for x is cos(x) and the
			 * opposite of the function for y is sin(y).
			 */
			int theta = (int) (vy == 0? Math.toDegrees(Math.acos(-vx)):Math.toDegrees(Math.asin(-vy)));
			g.rotate(theta);
			
		}
		
		g.drawImage(renderingComp.getTexture(), -halfTileWidth, -halfTileHeight, TileRender.TILE_WIDTH, TileRender.TILE_HEIGHT);
		
	}
	
}
