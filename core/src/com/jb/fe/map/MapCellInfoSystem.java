package com.jb.fe.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.jb.fe.UI.SquareSelectorFactory;
import com.jb.fe.level.Level;
import com.jb.fe.map.MapCell.AvoidanceTileBonus;
import com.jb.fe.map.MapCell.CellType;
import com.jb.fe.map.MapCell.DefenseTileBonus;
import com.jb.fe.map.MapCell.MovementTileValues;

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

			// Tile Type
			String prop = tiledObject.getProperties().get("TileType", String.class);

			// Regular Cells
			if (prop.equals("")) {
				mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
				mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
				mapCell.tileName = prop;
				mapCell.cellType = CellType.Grass;
				mapCell.movementCost = MovementTileValues.NORMAL;
				mapCell.defenceBonus = DefenseTileBonus.GRASS_DEF;
				mapCell.avoidanceBonus = AvoidanceTileBonus.GRASS_AVD;
			} else {
				if (prop.equals("Forest")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.tileName = prop;
					mapCell.cellType = CellType.Forest;
					mapCell.movementCost = MovementTileValues.FOREST;
					mapCell.defenceBonus = DefenseTileBonus.FOREST_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.FOREST_AVD;
				} else if (prop.equals("Fortress")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.tileName = prop;
					mapCell.cellType = CellType.Fortress;
					mapCell.movementCost = MovementTileValues.FORTRESS;
					mapCell.defenceBonus = DefenseTileBonus.FORTRESS_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.FORTRESS_AVD;
				} else if (prop.equals("Village")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.tileName = prop;
					mapCell.cellType = CellType.Village;
					mapCell.movementCost = MovementTileValues.NORMAL;
					mapCell.defenceBonus = DefenseTileBonus.VILLAGE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.VILLAGE_AVD;
				} else if (prop.equals("Impassable")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.tileName = prop;
					mapCell.cellType = CellType.Impassable;
					mapCell.movementCost = MovementTileValues.IMPASSABLE;
					mapCell.defenceBonus = DefenseTileBonus.IMPASSABLE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.IMPASSABLE_AVD;
				}  else if (prop.equals("Throne")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.tileName = prop;
					mapCell.cellType = CellType.Grass;
					mapCell.movementCost = MovementTileValues.NORMAL;
					mapCell.defenceBonus = DefenseTileBonus.THRONE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.THRONE_AVD;
				}
				
			}

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