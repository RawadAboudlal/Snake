package com.rawad.snake.entity;

import com.rawad.gamehelpers.game.entity.Component;

/**
 * Indentifies an {@code Entity} as a tail.
 * 
 * @author Rawad
 *
 */
public class TailComponent extends Component {
	
	@Override
	public Component copyData(Component comp) {
		return comp;
	}
	
}
