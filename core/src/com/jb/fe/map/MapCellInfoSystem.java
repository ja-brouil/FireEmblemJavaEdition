package com.jb.fe.map;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.utils.Array;
import com.jb.fe.components.SquareSelectorFactory;
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
		level.allLevelMapCells.clear();

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
				mapCell.cellType = CellType.Grass;
				mapCell.movementCost = MovementTileValues.NORMAL;
				mapCell.defenceBonus = DefenseTileBonus.GRASS_DEF;
				mapCell.avoidanceBonus = AvoidanceTileBonus.GRASS_AVD;
			} else {
				if (prop.equals("Forest")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.cellType = CellType.Forest;
					mapCell.movementCost = MovementTileValues.FOREST;
					mapCell.defenceBonus = DefenseTileBonus.FOREST_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.FOREST_AVD;
				} else if (prop.equals("Fortress")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.cellType = CellType.Fortress;
					mapCell.movementCost = MovementTileValues.NORMAL;
					mapCell.defenceBonus = DefenseTileBonus.FORTRESS_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.FORTRESS_AVD;
				} else if (prop.equals("Village")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.cellType = CellType.Village;
					mapCell.movementCost = MovementTileValues.NORMAL;
					mapCell.defenceBonus = DefenseTileBonus.VILLAGE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.VILLAGE_AVD;
				} else if (prop.equals("Impassable")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.cellType = CellType.Impassable;
					mapCell.movementCost = MovementTileValues.IMPASSABLE;
					mapCell.defenceBonus = DefenseTileBonus.IMPASSABLE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.IMPASSABLE_AVD;
				}  else if (prop.equals("Throne")) {
					mapCell.position.x = tiledObject.getProperties().get("x", Float.class);
					mapCell.position.y = tiledObject.getProperties().get("y", Float.class);
					mapCell.cellType = CellType.Grass;
					mapCell.movementCost = MovementTileValues.NORMAL;
					mapCell.defenceBonus = DefenseTileBonus.THRONE_DEF;
					mapCell.avoidanceBonus = AvoidanceTileBonus.THRONE_AVD;
				}
				
			}

			// Add Cell to the Array
			level.allLevelMapCells.add(mapCell);
		}

		// Calculate adj tiles
		getAdjTiles(level.allLevelMapCells);
		
		// Create Color Selectors
		createSelectors(level.allLevelMapCells);
	}

	/*
	 * Calculate all adj tiles to the current tile
	 */
	private void getAdjTiles(Array<MapCell> allMapCells) {
		for (int i = 0; i < allMapCells.size; i++) {
			MapCell mapCelltoCheck = allMapCells.get(i);

			for (int j = 0; j < allMapCells.size; j++) {
				MapCell adjPotentialCell = allMapCells.get(j);

				if (adjPotentialCell.position.x / MapCell.CELL_SIZE == (mapCelltoCheck.position.x / MapCell.CELL_SIZE) + 1
						&& adjPotentialCell.position.y / MapCell.CELL_SIZE == mapCelltoCheck.position.y / MapCell.CELL_SIZE) {
					mapCelltoCheck.adjTiles.add(adjPotentialCell);
				} else if (adjPotentialCell.position.x / MapCell.CELL_SIZE == (mapCelltoCheck.position.x / MapCell.CELL_SIZE) - 1
						&& adjPotentialCell.position.y / MapCell.CELL_SIZE == mapCelltoCheck.position.y / MapCell.CELL_SIZE) {
					mapCelltoCheck.adjTiles.add(adjPotentialCell);
				} else if (adjPotentialCell.position.x / MapCell.CELL_SIZE == mapCelltoCheck.position.x / MapCell.CELL_SIZE
						&& adjPotentialCell.position.y / MapCell.CELL_SIZE == (mapCelltoCheck.position.y / MapCell.CELL_SIZE) + 1) {
					mapCelltoCheck.adjTiles.add(adjPotentialCell);
				} else if (adjPotentialCell.position.x / MapCell.CELL_SIZE == mapCelltoCheck.position.x / MapCell.CELL_SIZE
						&& adjPotentialCell.position.y / MapCell.CELL_SIZE == (mapCelltoCheck.position.y / MapCell.CELL_SIZE) - 1) {
					mapCelltoCheck.adjTiles.add(adjPotentialCell);
				}
			}
		}
	}
	
	// Create Selector
	private void createSelectors(Array<MapCell> allMapCells) {
		for (MapCell mapCell : allMapCells) {
			mapCell.blueSquare = squareSelectorFactory.createBlueSquare(mapCell.position.x, mapCell.position.y);
			mapCell.redSquare = squareSelectorFactory.createRedSquare(mapCell.position.x, mapCell.position.y);
			engine.addEntity(mapCell.blueSquare);
			engine.addEntity(mapCell.redSquare);
		}
	}
	
	// Clear Engine of all MapCell Square Selectors
	public void clearSquareSelectorsFromEngine(Array<MapCell> allMapCells) {
		for (MapCell mapCell : allMapCells) {
			engine.removeEntity(mapCell.blueSquare);
			engine.removeEntity(mapCell.redSquare);
		}
	}
}