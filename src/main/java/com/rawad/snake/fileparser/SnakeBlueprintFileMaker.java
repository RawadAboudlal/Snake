package com.rawad.snake.fileparser;

import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.snake.entity.EEntity;
import com.rawad.snake.entity.FoodComponent;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.MovementComponent;
import com.rawad.snake.entity.PointsComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.entity.TailComponent;
import com.rawad.snake.entity.UserControlComponent;
import com.rawad.snake.loader.Loader;

public class SnakeBlueprintFileMaker {
	
	public static void main(String[] args) {
		
		Entity snake = Entity.createEntity();
		snake.addComponent(new PositionComponent());
		snake.addComponent(new MovementComponent());
		snake.addComponent(new HeadComponent());
		snake.addComponent(new UserControlComponent());
		snake.addComponent(new PointsComponent());
		
		Entity tail = Entity.createEntity();
		tail.addComponent(new PositionComponent());
		tail.addComponent(new MovementComponent());
		tail.addComponent(new TailComponent());
		
		Entity food = Entity.createEntity();
		food.addComponent(new PositionComponent());
		food.addComponent(new FoodComponent());
		
		final String[] contextPaths = {
				EEntity.class.getPackage().getName()
		};
		
		Loader loader = new Loader();
		EntityFileParser entityFileParser = new EntityFileParser();
		
		entityFileParser.setContextPaths(contextPaths);
		
		entityFileParser.saveEntityBlueprint(snake, loader.getEntityBlueprintPath(EEntity.SNAKE_HEAD.getName()));
		entityFileParser.saveEntityBlueprint(tail, loader.getEntityBlueprintPath(EEntity.SNAKE_TAIL.getName()));
		entityFileParser.saveEntityBlueprint(food, loader.getEntityBlueprintPath(EEntity.FOOD.getName()));
		
	}
	
}
