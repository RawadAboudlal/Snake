package com.rawad.snake.entity;

import com.rawad.gamehelpers.game.entity.Component;

public class PointsComponent extends Component {
	
	private int points = 0;
	
	/**
	 * @return the points
	 */
	public int getPoints() {
		return points;
	}
	
	/**
	 * @param points the points to set
	 */
	public void setPoints(int points) {
		this.points = points;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof PointsComponent) {
			
			PointsComponent pointsComp = (PointsComponent) comp;
			
			pointsComp.setPoints(getPoints());
			
		}
		
		return comp;
		
	}
	
}
