package com.rawad.snake.entity;

import java.util.ArrayList;

import com.rawad.gamehelpers.game.entity.Component;
import com.rawad.gamehelpers.game.entity.Entity;

public class HeadComponent extends Component {
	
	private ArrayList<Entity> tailParts = new ArrayList<Entity>();
	
	private boolean attachTail = false;
	
	/**
	 * @return the tailParts
	 */
	public ArrayList<Entity> getTailParts() {
		return tailParts;
	}
	
	/**
	 * @param tailParts the tailParts to set
	 */
	public void setTailParts(ArrayList<Entity> tailParts) {
		this.tailParts = tailParts;
	}
	
	/**
	 * @return the attachTail
	 */
	public boolean shouldAttachTail() {
		return attachTail;
	}
	
	/**
	 * @param attachTail the attachTail to set
	 */
	public void setAttachTail(boolean attachTail) {
		this.attachTail = attachTail;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof HeadComponent) {
			
			HeadComponent headComp = (HeadComponent) comp;
			
			headComp.setTailParts(this.getTailParts());
			headComp.setAttachTail(this.shouldAttachTail());
			
		}
		
		return comp;
		
	}
	
}
