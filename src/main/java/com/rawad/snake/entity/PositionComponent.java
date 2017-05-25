package com.rawad.snake.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.snake.game.Position;

public class PositionComponent extends Component {
	
	private Position position = new Position(0, 0);
	
	/**
	 * @return the position
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof PositionComponent) {
			
			PositionComponent positionComp = (PositionComponent) comp;
			
			getPosition().copy(positionComp.getPosition());
			
		}
		
		return comp;
		
	}
	
}
