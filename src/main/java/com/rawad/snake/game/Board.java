package com.rawad.snake.game;

import java.util.ArrayList;
import java.util.List;

import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.log.Logger;

public class Board {
	
	private final ArrayList<Entity> entities;
	
	/** First element is x coordinate, second is y coordinate. Top left is (0, 0). */
	private Entity[][] tiles;
	
	private int width;
	private int height;
	
	private boolean full = false;
	
	public Board(ArrayList<Entity> entities, int width, int height) {
		super();
		
		this.entities = entities;
		
		tiles = new Entity[width][height];
		
		this.width = width;
		this.height = height;
		
	}
	
	public void reset() {
		
		tiles = new Entity[width][height];
		
		entities.clear();
		
		setFull(false);
		
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
	
	public void addEntity(Entity e) {
		entities.add(e);
	}
	
	public void removeEntity(Entity e) {
		entities.remove(e);
	}
	
	/**
	 * 
	 * @param tile
	 * @param x
	 * @param y
	 * @return {@code true} if {@code tile} was successfullyset on the board and {@code false} otherwise
	 */
	public boolean set(Entity tile, int x, int y) {
		
		try {
			
			tiles[x][y] = tile;
			
			return true;
			
		} catch(ArrayIndexOutOfBoundsException ex) {
			Logger.log(Logger.WARNING, "Position: %s, %s is out of bounds.", x, y);
			return false;
		}
		
	}
	
	public Entity get(int x, int y) {
		
		try {
			return tiles[x][y];
		} catch(ArrayIndexOutOfBoundsException ex) {
			return null;
		}
		
	}
	
	public boolean isInBounds(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	/**
	 * @return the full
	 */
	public boolean isFull() {
		return full;
	}
	
	/**
	 * @param full the full to set
	 */
	public void setFull(boolean full) {
		this.full = full;
	}
	
}
