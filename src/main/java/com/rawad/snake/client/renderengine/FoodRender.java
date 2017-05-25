package com.rawad.snake.client.renderengine;

import com.rawad.gamehelpers.client.renderengine.Render;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.snake.entity.RenderingComponent;

import javafx.scene.canvas.GraphicsContext;

public class FoodRender extends Render {
	
	public void render(GraphicsContext g, Entity food, RenderingComponent renderingComp) {
		g.drawImage(renderingComp.getTexture(), 0, 0, TileRender.TILE_WIDTH, TileRender.TILE_HEIGHT);
	}
	
}
