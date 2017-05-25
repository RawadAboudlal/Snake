package com.rawad.snake.game;

public final class Velocity {
	
	/** Number of axes a direction can represent (x and y). */
	public static final int AXES = 2;
	
	public static final int UP = -1;
	public static final int DOWN = 1;
	public static final int RIGHT = 1;
	public static final int LEFT = -1;
	
	private int x;
	private int y;
	
	public Velocity(int x, int y) {
		super();
		
		this.x = x;
		this.y = y;
		
	}
	
	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	public Velocity copy(Velocity other) {
		
		other.setX(getX());
		other.setY(getY());
		
		return other;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj == null) return false;
		
		if(obj instanceof Velocity) {
			Velocity dir = (Velocity) obj;
			return (this.getX() == dir.getX()) && (this.getY() == dir.getY());
		}
		
		return false;
		
	}
	
}
