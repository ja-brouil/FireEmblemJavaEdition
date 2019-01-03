# FireEmblemJavaEdition
Fire Emblem JavaEdition Practice Purposes only

<h1> Fire Emblem written in Java </h1>
<p> This is a recreation of Fire Emblem written entirely in Java using the LibGDX framework.</p>
<br />
<p> This is only for educational purposes and for the intent of practicing writing code </p>


# Live Version
https://ja-brouil.github.io/FireEmblemJavaEdition/index.html

# Additional Info
# User Interface ReWrite

## Table of Contents
1. UI States
2. Description and utility of each state
3. FlowChart between each state
4. System to control User Interface

## Introduction
The current userinterface is very clunckly code wise and not very usable. We will need to rewrite the entire thing to make it feel better.

## UI States
All of the UI States <br>
### GamePlay
1. MapCursor
2. ActionMenu
3. Item Selection Menu
4. Entity Selection Menu

### Data Windows
1. Map Objective
2. Terrain Info
3. Unit Info

### End Turn
1. End turn menu

### Additional Menus to be built later
1. Items Inventory Menu
2. Trade between units menu


## Description of Each State
    Game Play UI
    1. Map Cursor
    The map cursor moves around the map and enables the player to select units and move them around. The cursor will change the data on the Data Window Boxes as it moves around.
    
    2. Action Menu
    Once a unit has been selected and moved, the action menu shows up. It has 4 options: Action, Items, Trade, Wait
    Action opens up the Item Selection Menu, Items opens up the Item Selection Menu, Trade Opens up the Trade Selection Menu.

    3. Item Selection Menu
    This menu has all the weapons that the selected unit currently has. It allows the player to select which weapon they want to use to attack/heal an enemy/ally.

    4. Entity Selection Menu
    Once an item has been selected to use, the Entity selection menu will open. This will allow the player to select which unit to perform the action with the weapon that they chose.


    Data Windows
    1. Map Objective
    Displays the level's win condition.

    2. Terrain Info
    Displays the Terrain info where the map cursor currently is.

    3. Unit Info
    Displays the Unit info where the map cursor currently is if there is a unit.

    End Turn
    1. End turn menu has the game options, end your turn right away and other misc options. This will be built later

    Additional Menus to be built later
    1. Items Inventory Menu
    Allows you to sort through your inventory. This may not be needed after all.

    2. Trade Inventory Menu.
    Allows you to swap items between your units.

## Flow Between Each Menu
Each Menu State will have two major components -> <br>
- A wrapper that contains the UI
- An update function that contains the logic of the UI state
- A handler function that contains the keystroke/mouse events of the UI state

1. Game starts in **MapCursor State**.
    - Input Handler
        - Arrow keys: Moves the cursor around the map.
        - A button: On an **EMPTY TILE** will open the end turn menu.
        - A button: On an occupied tile that is an **ALLY** will open the movement selection screen.
        - A button: On an occupied tile that is a **NEUTRAL** or **ENEMY** it will show the movement possible that this unit can take.
        - Any button: While **NEUTRAL** or **ENEMY** is selected -> reset mapcursor selection and remove possible paths.
    - Update Function
        - Everytime the cursor moves, check if there is an ally unit on it. If there is update the unit info window.
        - Everytime the cursor moves, get the tile info and set it to the tile info.
        - Everytime the cursor moves, check in which quadrant of the screen it is and move the data info screens.
        - Everytime the cursor moves, check if it will go out of bounds of the map. If it does, do not allow it to move.
        - Everytime the cursor moves, check if the cursor is in bounds of the map but the camera needs to move. Move the camera as needed.

2. Action Menu
    - Input Handler
        - A Button: Proceed to the appropriate action menu.
        - Arrow Keys: Move the action menu cursor up and down.
        - B Button : Return to the MapCursor State
    - Update Function
        - When arrow keys are hit, update the selection option.
3.  Item Selection Window
    - Input Handler
        - A Button: Select current item and proceed to Entity Selection Window
        - B Button: Return to the Action Menu
        - Arrow Keys: Move the cursor between items to select option.
    - Update Function
        - Display info of the weapon in the item info window.
        - Update the selection item based on the input handler.
4.  - Entity Selection Menu
        - Input Function
            - Arrow Key: select between entity options.
            - A button: select entity and proceed to combat phase.
            - B Button: (for either) -> no enemies = return to item selection window | with enemies/allies = return to item selection window
        - Update Function
            - Update the combat text window.

## System to control all of this
**User interface system**<br>
1. Entity System so other systems can access it quickly
2. Contains all the UI States
3. Public access to all the states so that it can easily swap states
4. Has a reference to the unit and componentmappers so that information that the UI needs can be easily managed and swapped around.
