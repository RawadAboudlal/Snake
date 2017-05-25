package com.rawad.snake.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.LayerRender;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.snake.entity.FoodComponent;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.PointsComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.entity.RenderingComponent;
import com.rawad.snake.game.Board;
import com.rawad.snake.game.Position;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class TileRender extends LayerRender {
	
	public static final int TILE_WIDTH = 16;
	public static final int TILE_HEIGHT = 16;
	
	private SnakeRender snakeRender = new SnakeRender();
	private FoodRender foodRender = new FoodRender();
	
	private Board gameBoard;
	
	public TileRender(Board gameBoard) {
		super();
		
		this.gameBoard = gameBoard;
		
	}
	
	@Override
	public void render() {
		
		Canvas canvas = GuiRegister.getRoot(masterRender.getState()).getCanvas();
		
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		Affine originalTransform = g.getTransform();
		
		double canvasWidth = canvas.getWidth();
		double canvasHeight = canvas.getHeight();
		
		g.setFill(Color.GREEN);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		
		double boardWidth = gameBoard.getWidth() * TILE_WIDTH;
		double boardHeight = (gameBoard.getHeight() + 1) * TILE_HEIGHT;// Reserve one tile height for text at bottom.
		
		double boardWidthInCanvas = canvasWidth / boardWidth;
		double boardHeightInCanvas = canvasHeight / boardHeight;
		
		// If the canvas is a good size, we'll scale the board to fit it, otherwise we just center it.
		if(		Math.abs(boardWidthInCanvas - (int) boardWidthInCanvas) < 0.1d || 
				Math.abs(boardHeightInCanvas - (int) boardHeightInCanvas) < 0.1d) {
			
			g.scale(boardWidthInCanvas, boardHeightInCanvas);
		} else {
			g.translate((canvasWidth - boardWidth) / 2d, (canvasHeight - boardHeight) / 2d);
		}
		
		g.setFill(Color.WHITE);
		g.fillRect(0, 0, gameBoard.getWidth() * TILE_WIDTH, gameBoard.getHeight() * TILE_HEIGHT);
		
		for(Entity entity: gameBoard.getEntities()) {
			
			PositionComponent positionComp = entity.getComponent(PositionComponent.class);
			
			if(positionComp == null) continue;
			
			Position position = positionComp.getPosition();
			
			double x = position.getX() * TILE_WIDTH;
			double y = position.getY() * TILE_HEIGHT;
			
			RenderingComponent renderingComp = entity.getComponent(RenderingComponent.class);
			
			Affine allEntitiesTransform = g.getTransform();
			
			g.translate(x, y);
			
			if(renderingComp != null) {
				
				if(entity.getComponent(FoodComponent.class) != null) {
					foodRender.render(g, entity, renderingComp);
				}
				
				if(entity.getComponent(HeadComponent.class) != null) {
					snakeRender.render(g, entity, renderingComp);
				}
				
			}
			
			g.setTransform(allEntitiesTransform);
			
			PointsComponent pointsComp = entity.getComponent(PointsComponent.class);
			
			if(pointsComp != null) {
				
				g.setStroke(Color.BLACK);
				g.strokeText(String.format("Points: %s.", pointsComp.getPoints()), 0, boardHeight);
				
			}
			
		}
		
		g.setTransform(originalTransform);
		
	}
	
}
