package com.rawad.snake.entity;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.snake.game.Velocity;

public class MovementComponent extends Component {
	
	private Velocity velocity = new Velocity(0, 0);
	
	/**
	 * @return the velocity
	 */
	public Velocity getVelocity() {
		return velocity;
	}
	
	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof MovementComponent) {
			
			MovementComponent movementComp = (MovementComponent) comp;
			
			getVelocity().copy(movementComp.getVelocity());
			
		}
		
		return comp;
		
	}
	
}
