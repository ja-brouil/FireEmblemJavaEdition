package com.jb.fe.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.jb.fe.UI.squareSelectors.SquareSelectorFactory;
import com.jb.fe.level.Level;

public class MapCellInfoSystem {

	public static String cellLayerName = "CellInfo";
	
	// Color Selector
	private SquareSelectorFactory squareSelectorFactory;
	private Engine engine;

	public MapCellInfoSystem(AssetManager assetManager, Engine engine) {
		this.engine = engine;
		squareSelectorFactory = new SquareSelectorFactory(assetManager);
	}

	public void processTiles(Level level) {
		// Reset Tiles
		for (int outer = 0; outer < level.allLevelMapCells.length; outer++) {
			for (int inner = 0; inner < level.allLevelMapCells[outer].length; inner++) {
				level.allLevelMapCells[outer][inner] = null;
			}
		}

		// Get Tile Layer
		MapLayer mapTileLayer = level.levelMap.getLayers().get(cellLayerName);

		// Get Map Tile Info
		for (MapObject tiledObject : mapTileLayer.getObjects()) {

			// Create New MapCell
			MapCell mapCell = new MapCell();

			// Get Tile Properties
			MapProperties tileProperties = tiledObject.getProperties();

			// Set Tile Properties
			mapCell.position.x = tileProperties.get("x", Float.class);
			mapCell.position.y = tileProperties.get("y", Float.class);
			mapCell.tileName = tileProperties.get("TileType", String.class);
			mapCell.backgroundFilePathString = "background/" + tileProperties.get("TileType", String.class);
			mapCell.movementCost = tileProperties.get("MovementCost", Integer.class);
			mapCell.defenceBonus = (int) tileProperties.get("Def", Integer.class);
			mapCell.avoidanceBonus = (int) tileProperties.get("Avd", Integer.class);  

			// Add Cell to the Array
			int row = (int) mapCell.position.x / MapCell.CELL_SIZE;
			int col = (int) mapCell.position.y / MapCell.CELL_SIZE;
			level.allLevelMapCells[row][col] = mapCell;
		}
		
		// Calculate adj tiles
		getAdjTiles(level.allLevelMapCells);
		
		// Create Color Selectors
		createSelectors(level.allLevelMapCells);
	}

	/*
	 * Calculate all adj tiles to the current tile
	 */
	private void getAdjTiles(MapCell[][] allMapCells) {
		
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[outer].length; inner++) {		
				MapCell mapCell = allMapCells[outer][inner];
				
				int x = (int) mapCell.position.x / MapCell.CELL_SIZE;
				int y = (int) mapCell.position.y / MapCell.CELL_SIZE;
				
				// East
				if (x + 1 < allMapCells.length) {
					mapCell.adjTiles.add(allMapCells[x + 1][y]);
				}
				
				// West
				if (x - 1 >= 0) {
					mapCell.adjTiles.add(allMapCells[x - 1][y]);
				}
				
				// North
				if (y - 1 >= 0) {
					mapCell.adjTiles.add(allMapCells[x][y - 1]);
				}
				
				// South
				if (y + 1 < allMapCells[x].length) {
					mapCell.adjTiles.add(allMapCells[x][y + 1]);
				}
				
			}
		}
		
	}
	
	// Create Selector
	private void createSelectors(MapCell[][] allMapCells) {
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[outer].length; inner++) {
				allMapCells[outer][inner].blueSquare = squareSelectorFactory.createBlueSquare(allMapCells[outer][inner].position.x, allMapCells[outer][inner].position.y);
				allMapCells[outer][inner].redSquare = squareSelectorFactory.createRedSquare(allMapCells[outer][inner].position.x, allMapCells[outer][inner].position.y);
				engine.addEntity(allMapCells[outer][inner].blueSquare);
				engine.addEntity(allMapCells[outer][inner].redSquare);
			}
		}
	}
	
	// Clear Engine of all MapCell Square Selectors
	public void clearSquareSelectorsFromEngine(MapCell[][] allMapCells) {
		for (int outer = 0; outer < allMapCells.length; outer++) {
			for (int inner = 0; inner < allMapCells[outer].length; inner++) {
				engine.removeEntity(allMapCells[outer][inner].blueSquare);
				engine.removeEntity(allMapCells[outer][inner].redSquare);
			}
		}
	}
}