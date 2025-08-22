# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

"Skyward Dash" is a complete LibGDX-based Icy Tower-style jumping game developed by Rolling Cat Software. The game
features momentum-based physics, procedural platform generation, infinite vertical climbing gameplay, combo systems, and
multiple platform types.

## Technology Stack

- **Framework**: LibGDX 1.12.1
- **Build Tool**: Maven 3
- **Java Version**: 11
- **Physics**: Custom physics system with collision detection
- **Graphics**: LibGDX ShapeRenderer for simple geometric graphics

## Build Commands

```bash
# Compile the project
mvn compile

# Run the game
mvn exec:exec

# Run tests
mvn test

# Package the application
mvn package

# Clean build artifacts
mvn clean
```

## Project Architecture

### Package Structure

- `com.skywarddash`: Main game class and desktop launcher
- `com.skywarddash.entities`: Player, Platform, DangerFloor game objects
- `com.skywarddash.screens`: MenuScreen, GameScreen for state management
- `com.skywarddash.systems`: InputHandler, CollisionSystem, ScoreSystem, CameraController
- `com.skywarddash.utils`: Constants, AssetManager, PlatformGenerator utilities

### Core Game Systems

**Entity Management**:

- `Player`: Momentum-based movement with physics
- `Platform`: Multiple types (normal, bouncy, breakable, moving)
- `DangerFloor`: Rising threat that ends the game

**Game Systems**:

- `InputHandler`: Keyboard input processing
- `CollisionSystem`: Platform and floor collision detection
- `ScoreSystem`: Height-based scoring with combo multipliers
- `CameraController`: Smooth camera following
- `PlatformGenerator`: Procedural infinite platform creation

**Screen Management**:

- `MenuScreen`: Main menu with navigation
- `GameScreen`: Core gameplay loop with pause/restart

### Game Mechanics

- **Momentum System**: Horizontal speed determines jump height (120-300 pixels)
- **Platform Generation**: Procedural platforms with increasing difficulty
- **Combo System**: Chaining jumps multiplies score
- **Rising Floor**: Danger zone that accelerates with score
- **Multiple Platform Types**: Each with unique behaviors

## Key Features Implemented

- Complete momentum-based physics system
- Infinite procedural platform generation
- Combo scoring system with multipliers
- Smooth camera following
- High score persistence using LibGDX Preferences
- Multiple platform types with special behaviors
- Game state management (menu, playing, paused, game over)
- Input handling for all controls

## Development Notes

- Uses LibGDX ShapeRenderer for simple geometric graphics
- All game constants centralized in `Constants.java`
- Physics runs at 60 FPS with delta time calculations
- Camera follows player vertically with smooth interpolation
- Platform generation adjusts difficulty based on height
- Score system rewards both height and combo chains

## Game Controls

- Arrow Keys / WASD: Move left/right
- Space / Up / W: Jump
- P: Pause/Unpause
- ESC: Return to menu
- R: Restart when game over

The game is feature-complete and ready to play, with all core mechanics implemented according to the Icy Tower-style
gameplay specifications.