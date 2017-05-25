package com.rawad.snake.loader;

import java.io.BufferedReader;

import com.rawad.gamehelpers.fileparser.xml.EntityFileParser;
import com.rawad.gamehelpers.game.entity.Blueprint;
import com.rawad.gamehelpers.game.entity.BlueprintManager;
import com.rawad.gamehelpers.resources.AbstractLoader;
import com.rawad.snake.entity.EEntity;

import javafx.scene.image.Image;

public class Loader extends AbstractLoader {
	
	private static final String FOLDER_RES = "res";
	private static final String FOLDER_ENTITY = "entity";
	private static final String FOLDER_TEXTURES = "textures";
	
	private static final String EXTENSION_ENTITY_BLUEPRINT_FILE = "xml";
	private static final String EXTENSION_ENTITY_TEXTURE = "png";
	
	private static final String PROTOCOL_FILE = "file:";
	
	public Loader() {
		super(FOLDER_RES);
	}
	
	public Image loadEntityTexture(String textureName) {
		return new Image(PROTOCOL_FILE + getFilePathFromParts(EXTENSION_ENTITY_TEXTURE, FOLDER_TEXTURES, textureName));
	}
	
	public Blueprint loadEntityBlueprint(EntityFileParser parser, String entityName, String... contextPaths) {
		
		BufferedReader reader = readFile(getEntityBlueprintPath(entityName));
		
		parser.setContextPaths(contextPaths);
		
		parser.parseFile(reader);
		
		Blueprint blueprint = new Blueprint(parser.getEntity());
		
		return blueprint;
		
	}
	
	public String getEntityBlueprintPath(String entityName) {
		return getFilePathFromParts(EXTENSION_ENTITY_BLUEPRINT_FILE, FOLDER_ENTITY, entityName);
	}
	
	public void loadEntityBlueprints(EntityFileParser parser) {
		

		final String[] contextPaths = {
				EEntity.class.getPackage().getName(),
		};
		
		for(EEntity entityKey: EEntity.values()) {
			
			Blueprint blueprint = this.loadEntityBlueprint(parser, entityKey.getName(), contextPaths);
			
			BlueprintManager.addBlueprint(entityKey, blueprint);
			
		}
		
	}
	
}
