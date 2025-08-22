# 🚀 Skyward Dash

<div align="center">
  <h3>🎮 Momentum-based Endless Platformer Game</h3>
  <p>An Icy Tower-inspired vertical climbing game built with LibGDX</p>
  
  [![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
  [![LibGDX](https://img.shields.io/badge/LibGDX-1.12.1-blue.svg)](https://libgdx.com/)
  [![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
  [![Status](https://img.shields.io/badge/Status-Active-brightgreen.svg)]()
</div>

## 📖 Table of Contents

- [🎯 About](#-about)
- [✨ Features](#-features)
- [🕹️ Controls](#️-controls)
- [🎮 How to Play](#-how-to-play)
- [🔧 Installation & Setup](#-installation--setup)
- [🏗️ Building from Source](#️-building-from-source)
- [🎨 Platform Types](#-platform-types)
- [⚡ Game Mechanics](#-game-mechanics)
- [🏆 Scoring System](#-scoring-system)
- [🔊 Audio & Assets](#-audio--assets)
- [📁 Project Structure](#-project-structure)
- [🛠️ Technical Details](#️-technical-details)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

## 🎯 About

**Skyward Dash** is a fast-paced, momentum-based platformer inspired by the classic game "Icy Tower". Players control a character that must climb an endless tower by jumping from platform to platform while avoiding a rising danger floor. The game emphasizes momentum physics - the faster you move horizontally, the higher you can jump!

### 🎪 Key Highlights

- **Physics-Based Movement**: Realistic momentum and gravity system
- **Endless Vertical Challenge**: Procedurally generated platforms that get more challenging
- **Multiple Platform Types**: Each with unique mechanics and behaviors
- **Rising Danger Floor**: Constant pressure to keep climbing
- **Combo System**: Chain jumps for massive score multipliers
- **High Score Tracking**: Persistent leaderboard system

## ✨ Features

### 🏃‍♂️ Core Gameplay
- **Momentum-Based Jumping**: Build horizontal speed to achieve greater jump heights
- **Infinite Procedural Generation**: Endless tower with increasing difficulty
- **Rising Danger Floor**: Dynamic threat that follows the player
- **Smooth Camera System**: Intelligent camera that follows player movement
- **Combo Chain System**: Consecutive jumps multiply your score

### 🎨 Visual & Audio
- **Multiple Platform Types**: 6 different platform types with unique behaviors
- **Visual Effects**: Platform-specific animations and state changes
- **Sound Effects**: Jump sounds, combo notifications, and UI feedback
- **Parallax Backgrounds**: Multi-layer scrolling backgrounds
- **Asset Integration**: Professional game assets from Kenney

### 🎯 Game Mechanics
- **Variable Platform Widths**: Dynamic platform sizing for increased challenge
- **Progressive Difficulty**: Platforms get smaller and further apart as you climb
- **Air Control**: Limited but responsive mid-air movement
- **Coyote Time**: Brief window to jump after leaving a platform
- **Double Jump**: One air jump available when not on ground

## 🕹️ Controls

| Control | Action | Alternative |
|---------|--------|-------------|
| **⬅️ ➡️ Arrow Keys** | Move Left/Right | **A/D Keys** |
| **⬆️ Arrow Key** | Jump | **Space Bar** or **W Key** |
| **P** | Pause/Unpause | - |
| **ESC** | Return to Menu | - |
| **R** | Restart Game | *(When Game Over)* |

### 🎮 Movement Tips
- **Hold direction keys** to build momentum
- **Release and repress** jump for precise timing
- **Use air control** to adjust trajectory mid-jump
- **Chain movements** for maximum momentum

## 🎮 How to Play

### 🚀 Getting Started
1. **Launch the game** and press any key to start
2. **Move horizontally** using arrow keys or WASD
3. **Build momentum** by moving in one direction
4. **Jump** when you have enough speed to reach the next platform
5. **Avoid the red danger floor** that rises from below

### 🎯 Advanced Strategies
- **Momentum Management**: Higher horizontal speed = higher jumps
- **Platform Selection**: Choose your landing spots carefully
- **Combo Building**: Chain jumps without touching the ground
- **Risk vs Reward**: Farther platforms give more points but are riskier

### 📊 Progression System
- **Floors**: Track your vertical progress
- **Score**: Points based on platforms reached and combos
- **High Score**: Personal best tracking
- **Difficulty Scaling**: Game gets harder as you climb higher

## 🔧 Installation & Setup

### 📋 Prerequisites
- **Java Development Kit (JDK) 11 or higher**
- **Maven 3.6 or higher**
- **Git** (for cloning the repository)

### 💾 Quick Start

1. **Clone the Repository**
   ```bash
   git clone https://github.com/yourusername/skyward-dash.git
   cd skyward-dash
   ```

2. **Run the Game**
   ```bash
   # Compile and run in one command
   mvn compile exec:java -Dexec.mainClass="com.skywarddash.DesktopLauncher"
   
   # Or use the wrapper (Windows)
   ./mvnw.cmd compile exec:java -Dexec.mainClass="com.skywarddash.DesktopLauncher"
   
   # Or use the wrapper (Linux/Mac)
   ./mvnw compile exec:java -Dexec.mainClass="com.skywarddash.DesktopLauncher"
   ```

3. **Enjoy the Game!** 🎉

## 🏗️ Building from Source

### 📦 Development Commands

```bash
# Clean the project
mvn clean

# Compile source code
mvn compile

# Run tests (if available)
mvn test

# Package as executable JAR
mvn package

# Install to local Maven repository
mvn install

# Generate project documentation
mvn javadoc:javadoc
```

### 🎯 Creating Distributable

```bash
# Create a fat JAR with all dependencies
mvn clean package

# The executable JAR will be in target/
java -jar target/skyward-dash-1.0-SNAPSHOT.jar
```

## 🎨 Platform Types

| Platform | Description | Visual | Behavior |
|----------|-------------|--------|----------|
| **🟢 Normal** | Standard platform | Green | Basic landing platform |
| **🟠 Bouncy** | Extra jump boost | Orange | 50% higher jump when landed on |
| **🟤 Breakable** | Disappears after use | Brown | Breaks 1 second after landing |
| **⚪ Moving** | Horizontal movement | White/Gray | Slides left and right smoothly |
| **🔵 Icy** | Slippery surface | Light Blue | 30% speed boost + random sliding |
| **🔴 Falling** | Falls when stepped on | Red | Falls after 0.5 second delay |

### 🎪 Platform Distribution
- **Normal**: 60% (base platforms)
- **Special Platforms**: 40% total
  - Bouncy: 25% of special (10% overall)
  - Moving: 20% of special (8% overall)
  - Breakable: 20% of special (8% overall)
  - Icy: 20% of special (8% overall)
  - Falling: 15% of special (6% overall)

## ⚡ Game Mechanics

### 🏃‍♂️ Movement Physics
- **Horizontal Acceleration**: 1800 units/sec²
- **Maximum Speed**: 750 units/sec
- **Friction** (Ground): 5% per frame
- **Friction** (Air): 2% per frame
- **Air Control**: 80% of ground control

### 🦘 Jumping System
- **Base Jump Height**: 400 units
- **Maximum Jump Height**: 600 units (with full momentum)
- **Momentum Formula**: Based on horizontal speed and combo count
- **Coyote Time**: 0.1 seconds after leaving platform
- **Double Jump**: Available when airborne

### 📏 Platform Generation
- **Width Variation**: ±20% random variance from base width
- **Minimum Width**: 90 units
- **Vertical Spacing**: 140-220 units (increases with difficulty)
- **Horizontal Spacing**: Up to 2 platform widths apart

### ⚡ Danger Floor
- **Grace Period**: 2 seconds before activation
- **Base Speed**: 1.5x multiplier
- **Score Acceleration**: +10% per 200 points
- **Time Acceleration**: +50% per 30 seconds

## 🏆 Scoring System

### 📊 Points Calculation
- **Platform Landing**: 10 base points
- **Floor Progression**: Points increase with height
- **Combo Multiplier**: 2x, 3x, 4x... up to 10x
- **Platform Type Bonus**: Special platforms give extra points

### 🔥 Combo System
- **Combo Start**: 3 consecutive jumps without touching ground
- **Speed Boost**: 25% per combo level
- **Maximum Combo**: 10 levels
- **Combo Reset**: Touching ground or missing platform

### 🎯 Difficulty Progression
- **Floor 0-10**: Large platforms (450 units wide)
- **Floor 10-50**: Medium platforms (300 units wide)
- **Floor 50-100**: Small platforms (180 units wide)
- **Floor 100+**: Tiny platforms (120 units wide)

## 🔊 Audio & Assets

### 🎵 Sound Effects
- **Jump Sound**: Satisfying boing effect
- **Landing Sound**: Platform contact feedback
- **Combo Sound**: Achievement notification
- **Game Over Sound**: Failure feedback
- **UI Sounds**: Button clicks and interactions

### 🎨 Visual Assets
- **Kenney Platformer Pack**: Professional platform sprites
- **Kenney UI Pack**: Interface elements
- **Kenney Background Elements**: Environmental graphics
- **Custom Graphics**: Procedurally generated fallbacks

### 📁 Asset Sources
- [Kenney Platformer Pack Redux](https://kenney.nl/assets/platformer-pack-redux)
- [Kenney UI Pack](https://kenney.nl/assets/ui-pack)
- [Kenney Interface Sounds](https://kenney.nl/assets/interface-sounds)
- [Freesound Jump Effect](https://freesound.org/)

## 📁 Project Structure

```
src/main/java/com/skywarddash/
├── 🚀 DesktopLauncher.java          # Application entry point
├── 🎮 SkywardDashGame.java          # Main game class
├── 📦 entities/                     # Game objects
│   ├── 🏃 Player.java               # Player character logic
│   ├── 🟫 Platform.java             # Platform behavior & types
│   └── 🔴 DangerFloor.java          # Rising danger mechanics
├── 🖥️ screens/                      # Game screens
│   ├── 📋 MenuScreen.java           # Main menu interface
│   └── 🎯 GameScreen.java           # Core gameplay screen
├── ⚙️ systems/                      # Game systems
│   ├── 🎮 InputHandler.java         # Input processing
│   ├── 💥 CollisionSystem.java      # Physics & collisions
│   ├── 📊 ScoreSystem.java          # Scoring & progression
│   └── 📹 CameraController.java     # Camera management
└── 🔧 utils/                        # Utilities
    ├── 📋 Constants.java            # Game configuration
    ├── 🏗️ PlatformGenerator.java    # Procedural generation
    └── 📦 AssetManager.java         # Resource management
```

## 🛠️ Technical Details

### 🏗️ Architecture Patterns
- **Entity-Component System**: Modular game object design
- **State Management**: Clean separation of game states
- **Observer Pattern**: Event-driven score and combo tracking
- **Strategy Pattern**: Different platform behaviors
- **Factory Pattern**: Platform and asset creation

### 🎯 Design Principles
- **SOLID Principles**: Clean, maintainable code structure
- **Separation of Concerns**: Clear responsibility boundaries
- **Error Handling**: Comprehensive exception management
- **Logging**: Detailed debugging and monitoring
- **Resource Management**: Proper asset loading and disposal

### 📊 Performance Optimizations
- **Object Pooling**: Efficient platform reuse
- **Viewport Culling**: Only render visible elements
- **Asset Caching**: Preloaded textures and sounds
- **Delta Time**: Frame-rate independent movement
- **Memory Management**: Proper resource cleanup

### 🔧 Key Technologies
- **LibGDX**: Cross-platform game framework
- **Java**: Primary programming language
- **Maven**: Dependency management and build system
- **Box2D**: Physics simulation (if needed)
- **OpenGL**: Hardware-accelerated rendering

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### 🐛 Bug Reports
1. Check existing issues first
2. Provide detailed reproduction steps
3. Include system information
4. Attach screenshots if relevant

### 💡 Feature Requests
1. Describe the feature clearly
2. Explain the use case
3. Consider implementation complexity
4. Discuss with maintainers first

### 🔧 Code Contributions
1. Fork the repository
2. Create a feature branch
3. Follow coding standards
4. Add tests if applicable
5. Submit a pull request

### 📝 Documentation
- Improve README sections
- Add code comments
- Write tutorials
- Create video guides

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### 🎨 Asset Licenses
- **Kenney Assets**: [CC0 1.0 Universal](https://creativecommons.org/publicdomain/zero/1.0/)
- **Sound Effects**: Various Creative Commons licenses
- **Custom Code**: MIT License

---

<div align="center">
  <h3>🎮 Ready to Climb? Start Your Journey Now! 🚀</h3>
  <p>Built with ❤️ using LibGDX</p>
  
  ⭐ **Star this repository if you enjoyed the game!** ⭐
</div>
