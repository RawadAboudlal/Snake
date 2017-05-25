package com.rawad.snake.client.state;

import java.util.ArrayList;
import java.util.Queue;

import com.rawad.gamehelpers.client.states.State;
import com.rawad.gamehelpers.client.states.StateChangeRequest;
import com.rawad.gamehelpers.game.GameManager;
import com.rawad.gamehelpers.game.entity.Entity;
import com.rawad.gamehelpers.game.event.Event;
import com.rawad.gamehelpers.game.event.Listener;
import com.rawad.gamehelpers.log.Logger;
import com.rawad.gamehelpers.utils.Util;
import com.rawad.jfxengine.gui.GuiRegister;
import com.rawad.jfxengine.gui.Root;
import com.rawad.snake.client.Client;
import com.rawad.snake.client.gui.GameOverScreen;
import com.rawad.snake.client.gui.PauseScreen;
import com.rawad.snake.client.input.InputAction;
import com.rawad.snake.client.renderengine.TileRender;
import com.rawad.snake.entity.EEntity;
import com.rawad.snake.entity.HeadComponent;
import com.rawad.snake.entity.MovementComponent;
import com.rawad.snake.entity.PointsComponent;
import com.rawad.snake.entity.PositionComponent;
import com.rawad.snake.game.Board;
import com.rawad.snake.game.Generator;
import com.rawad.snake.game.MovementSystem;
import com.rawad.snake.game.Position;
import com.rawad.snake.game.Velocity;
import com.rawad.snake.game.event.CollisionListener;
import com.rawad.snake.game.event.EventType;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;

public class GameState extends State implements Listener, EventHandler<KeyEvent> {
	
	private static final int BOARD_WIDTH = 20;
	private static final int BOARD_HEIGHT = 20;
	
	/** Number of tail parts snake head should start with. */
	private static final int TAIL_PARTS_AT_START = 2;
	
	/** Number of points/foods/tails a snake can get before the gae speeds up slightly. */
	private static final int POINTS_BEFORE_SPEEDUP = 20;
	/** Initial number of ticks for movement system to wait before moving snake. */
	private static final int INITIAL_GAME_DELAY = 5;
	
	private MovementSystem movementSystem;
	
	private Board gameBoard;
	
	private Entity snake;
	private PointsComponent snakePoints;
	
	@FXML private PauseScreen pauseScreen;
	@FXML private GameOverScreen gameOverScreen;
	
	@Override
	public void init() {
		
		gameBoard = new Board(gameEngine.getEntities(), BOARD_WIDTH, BOARD_HEIGHT);
		
		eventManager.registerListener(EventType.COLLISION, new CollisionListener(gameEngine, gameBoard));
		eventManager.registerListener(EventType.OUT_OF_BOUNDS, this);
		eventManager.registerListener(EventType.FOOD_EATEN, this);
		eventManager.registerListener(EventType.GAME_OVER, this);
		
		movementSystem = new MovementSystem(gameBoard, eventManager);
		
		gameEngine.addGameSystem(movementSystem);
		
		masterRender.getRenders().put(new TileRender(gameBoard));
		
		Root root = GuiRegister.loadGui(this);
		
		root.addEventHandler(KeyEvent.ANY, this);
		
		pauseScreen.visibleProperty().addListener(e -> game.setPaused(pauseScreen.isShowing()));
		gameOverScreen.visibleProperty().addListener(e -> game.setPaused(gameOverScreen.isShowing()));
		
		pauseScreen.getMainMenu().setOnAction(e -> sm.requestStateChange(StateChangeRequest.instance(MenuState.class)));
		
		gameOverScreen.getPlayAgain().setOnAction(e -> {
			
			GameManager.scheduleTask(() -> {
				resetGame();
				gameOverScreen.hide();
			});
			
		});
		
		gameOverScreen.getMainMenu().setOnAction(e -> {
			sm.requestStateChange(StateChangeRequest.instance(MenuState.class));
		});
		
	}
	
	@Override
	public void onEvent(Event ev) {
		
		/*
		 *  TODO: Could make a FoodSystem, compatible comps: foood + position, override tick() and tick(Entity). Have counter,
		 *  increment in tick(Entity) then in tick() if counter is still 0, generate a food. This should be the first system,
		 *  even before user control.
		 */
		
		switch((EventType) ev.getEventType()) {
		
		case GAME_OVER:
		case OUT_OF_BOUNDS:
			showGameOverScreen();
			break;
			
		case FOOD_EATEN:
			handleFoodEaten();
			break;
			
			default:
				break;
			
		}
		
	}
	
	private void handleFoodEaten() {
		
		snakePoints.setPoints(snakePoints.getPoints() + 1);
		
		// Game speeds up everytime POINTS_BEFORE_SPEEDUP pieces of food are collected.
		if(snakePoints.getPoints() % POINTS_BEFORE_SPEEDUP == 0) {
//			movementSystem.setDelay(movementSystem.getDelay() - 1);
			Logger.log(Logger.DEBUG, "I refuse to make the game faster. You're welcome.");
		}
		
		// Food has to be generated at the end so new tail doesn't over-write it.
		gameBoard.setFull(Generator.generateFood(gameBoard));
		
	}
	
	private void showGameOverScreen() {
		Platform.runLater(() -> {
			
			String message = "Score: " + snakePoints.getPoints();
			
			if(gameBoard.isFull()) message += Util.NL + "You just beat the game...";
			
			gameOverScreen.getScoreLabel().setText(message);
			gameOverScreen.show();
		});
	}
	
	@Override
	public void handle(KeyEvent event) {
		
		Object actionObject = game.getProxies().get(Client.class).getInputBindings().get(event.getCode());
		
		if(!(actionObject instanceof InputAction)) return;
		
		InputAction action = (InputAction) actionObject;
		
		// Future-proofing. GameState registered as a KeyEvent.ANY event listener.
		if(event.getEventType() == KeyEvent.KEY_PRESSED) {
			handleKeyPress(action);
		}
		
	}
	
	private void handleKeyPress(InputAction action) {
		
		if(gameOverScreen.isShowing()) return;
		
		if(pauseScreen.isShowing()) {
			
			switch(action) {
			
			case PAUSE:
				pauseScreen.hide();
				break;
				
				default:
					break;
			
			}
			
			return;
			
		}
		
		switch(action) {
		
		case MOVE_UP:
			addRequestedMovement(new Velocity(0, Velocity.UP));
			break;
			
		case MOVE_DOWN:
			addRequestedMovement(new Velocity(0, Velocity.DOWN));
			break;
			
		case MOVE_RIGHT:
			addRequestedMovement(new Velocity(Velocity.RIGHT, 0));
			break;
			
		case MOVE_LEFT:
			addRequestedMovement(new Velocity(Velocity.LEFT, 0));
			break;
		
		case PAUSE:
			pauseScreen.show();
			break;
			
			default:
				break;
		}
		
	}
	
	private void addRequestedMovement(Velocity dir) {
		
		Queue<Velocity> requestedMovements = movementSystem.getRequestedMovements();
		
		// This is so that the same movement can't be requested twice, causing for some laggy control.
		Velocity previousMovement = requestedMovements.peek();
		
		// Make sure we don't move in the same direction we are already moving in.
		Velocity currentMovement = snake.getComponent(MovementComponent.class).getVelocity();
		
		/* Size max is capped at exactly 2 so movements don't feel old. The most movements a player can make consecutively is
		 * 2 because there are two axes, x and y. Keeping movements after that makes game feel weird. i.e. backlogging any
		 * additional movement requests doesn't feel right. There is some skill associated, however, with doing a zig-zag now.
		 * Can't simply spam, must be timed in such a way that the 3rd key press isn't skipped over and only the 4th key press
		 * appearing.
		 */
		if(requestedMovements.size() < Velocity.AXES && !dir.equals(previousMovement) && !dir.equals(currentMovement)) {
			requestedMovements.offer(dir);
		}
		
	}
	
	@Override
	public void terminate() {}
	
	@Override
	protected void onActivate() {
		
		pauseScreen.hide();
		gameOverScreen.hide();
		
		resetGame();
		
	}
	
	@Override
	protected void onDeactivate() {}
	
	private void resetGame() {
		
		movementSystem.setDelay(INITIAL_GAME_DELAY);
		movementSystem.getRequestedMovements().clear();
		
		gameBoard.reset();
		
		snake = Entity.createEntity(EEntity.SNAKE_HEAD);
		snakePoints = snake.getComponent(PointsComponent.class);
		
		gameBoard.addEntity(snake);
		
		MovementComponent snakeMovementComp = snake.getComponent(MovementComponent.class);
		
		Velocity snakeMovement = snakeMovementComp.getVelocity();
		
		// Move to left by default, tail is on right.
		snakeMovement.setX(Velocity.LEFT);
		snakeMovement.setY(0);
		
		PositionComponent snakePositionComp = snake.getComponent(PositionComponent.class);
		
		Position snakePosition = snakePositionComp.getPosition();
		
		snakePosition.setX(BOARD_WIDTH / 2);
		snakePosition.setY(BOARD_HEIGHT / 2);
		
		gameBoard.set(snake, snakePosition.getX(), snakePosition.getY());
		
		HeadComponent headComp = snake.getComponent(HeadComponent.class);
		
		ArrayList<Entity> tailParts = headComp.getTailParts();
		tailParts.clear();
		
		for(int x = snakePosition.getX() + 1; x <= snakePosition.getX() + TAIL_PARTS_AT_START && x < BOARD_WIDTH; x++) {
			
			Entity tail = Entity.createEntity(EEntity.SNAKE_TAIL);
			
			tailParts.add(tail);
			
			MovementComponent tailMovementComp = tail.getComponent(MovementComponent.class);
			
			Velocity tailMovement = tailMovementComp.getVelocity();
			
			tailMovement.setX(snakeMovement.getX());
			tailMovement.setY(snakeMovement.getY());
			
			PositionComponent tailPositionComp = tail.getComponent(PositionComponent.class);
			
			Position tailPosition = tailPositionComp.getPosition();
			
			tailPosition.setX(x);
			tailPosition.setY(snakePosition.getY());
			
			gameBoard.set(tail, tailPosition.getX(), tailPosition.getY());
			
		}
		
		Generator.generateFood(gameBoard);
		
	}
	
}
