package com.rawad.snake.entity;

import com.rawad.gamehelpers.game.entity.Component;

import javafx.scene.image.Image;

public class RenderingComponent extends Component {
	
	private Image texture;
	
	/**
	 * @return the texture
	 */
	public Image getTexture() {
		return texture;
	}
	
	/**
	 * @param texture the texture to set
	 */
	public void setTexture(Image texture) {
		this.texture = texture;
	}
	
	@Override
	public Component copyData(Component comp) {
		
		if(comp instanceof RenderingComponent) {
			
			RenderingComponent renderingComp = (RenderingComponent) comp;
			
			renderingComp.setTexture(getTexture());
			
		}
		
		return comp;
		
	}
	
}
