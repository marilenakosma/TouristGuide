# Travel App

An interactive, modern **Java Swing-based desktop application** designed to help users plan transit routes, search for accommodation, and discover popular travel attractions in **Romania** (specifically focused around Brasov). Powered by a custom OpenStreetMap rendering engine, modern typography, and a polished flat-themed GUI.

---

## Key Features

-   **Interactive City Map Explorer** (`MapPnl.java`): Centered in Brasov, Romania. Renders critical points of interest (Airport, Train Station, Museum) and connects them dynamically with route path lines.
-   **Multi-City Route Planner** (`TransportationPnl.java`): Renders transit connections between major Romanian hubs (Brasov, Cluj-Napoca, Bucharest, Sibiu). Supports dynamic route paths depending on the transport mode chosen (Plane, Train, Bus).
-   **Hotel Browser & Booking Dialog** (`AccomodationPnl.java`): Lists popular lodging destinations in a grid-like view. Clicking "Book Now" launches a specialized date-picker modal dialog (`BookingDialog.java`) with field validation.
-  **Popular Sights Guide** (`AttractionPnl.java`): Explores historic landmarks (e.g., Bran Castle, Black Church, White Tower) with interactive markers. Selecting a landmark pops up an detail view featuring:
    *   An auto-advancing **image slideshow** (`SlideshowPanel`) powered by an internal Swing Timer.
    *   Attraction description, ticket prices, and opening hours.
    *   Online ticket purchase integration.
-   **UI styling**: Powered by the FlatLaf theme, custom rounded buttons, customized hover micro-animations, and Montserrat / Roboto typography.

---

## Technologies 

[![Java Version](https://img.shields.io/badge/Java-8%2B-orange.svg)](https://www.oracle.com/java/)
[![L&F FlatLaf](https://img.shields.io/badge/Look%20%26%20Feel-FlatLaf-blue.svg)](https://github.com/DevChute/FlatLaf)
[![Map engine](https://img.shields.io/badge/Map-JXMapViewer2-green.svg)](https://github.com/msteiger/jxmapviewer2)

- **Java Swing and Flatlaf** – UI components
- **JXMapViewer2** – Map rendering and interaction
- **Maven**  – Dependency management
- **Java 8+**

---

## Visual Gallery

| Home Dashboard | Route Planner (Train/Bus/Plane) |
|:---:|:---:|
| ![Home Screen](TouristGuide/src/Images/Home.png) | ![Route Planner](TouristGuide/src/Images/TransportationPnl.png) |
| *Welcome screen with customized navigation options and Montserrat typography.* | *Interactive map routes, terminal location pins, and search parameters.* |

| Accommodation Finder | Attraction Sights Guide |
|:---:|:---:|
| ![Accommodation Panel](TouristGuide/src/Images/AccomodationPnl.png) | ![Attractions Map Panel](TouristGuide/src/Images/AttractionPnl.png) |
| *Browse handpicked stays with description cards and booking CTAs.* | *Explore points of interest on the interactive map with custom markers.* |

| Booking Calendar Modal | Ticket Purchase Slideshow |
|:---:|:---:|
| ![Booking dialog](TouristGuide/src/Images/AccomodationBook.png) | ![Attraction ticket modal](TouristGuide/src/Images/AttractionTicket.png) |
| *Date-picking modal calendar powered by JDatePicker.* | *Rotating image slideshow panel and instant ticket checkout.* |

##  Architecture & Component Flow

The application implements a clean **Mediator & CardLayout Architecture**, where [TravelApp.java](TouristGuide/src/TravelApp.java) acts as the central coordinator swapping sub-panels dynamically.

###  Class Diagram

```mermaid
classDiagram
    direction TB
    class TravelApp {
        -JFrame frame
        -JPanel contentPnl
        -CardLayout cardLayout
        +TravelApp()
        +showPanel(String panelName)
        +main(String[] args)
    }
    class HomePanel {
        -Image backgroundImage
        +HomePanel(TravelApp app)
        -createButton(String text, String iconPath)
    }
    class TransportationPnl {
        -JXMapViewer mapViewer
        -MapOverlayPainter painter
        -Map routes
        +TransportationPnl(TravelApp app)
        -handleSearch()
        -updateRoute(List route)
    }
    class AccomodationPnl {
        +AccomodationPnl(TravelApp app)
        -createHotelCard(String name, String imagePath, String description)
    }
    class AttractionPnl {
        -JXMapViewer mapViewer
        -AttractionPainter painter
        +AttractionPnl(TravelApp app)
        -showAttractionDetails(Attraction attr)
    }
    class MapPnl {
        +MapPnl(TravelApp app)
        +createMapViewer()
    }
    class BookingDialog {
        +BookingDialog(JFrame parent, String hotelName)
    }
    class SlideshowPanel {
        -JLabel imageLabel
        -List images
        -int currentIndex
        -nextImage()
    }
    
    TravelApp --> HomePanel : manages
    TravelApp --> TransportationPnl : manages
    TravelApp --> AccomodationPnl : manages
    TravelApp --> AttractionPnl : manages
    TravelApp --> MapPnl : manages
    
    HomePanel ..> TravelApp : switches panels
    TransportationPnl ..> TravelApp : returns home
    AccomodationPnl ..> TravelApp : returns home
    AttractionPnl ..> TravelApp : returns home
    MapPnl ..> TravelApp : returns home
    
    AccomodationPnl --> BookingDialog : launches
    AttractionPnl --> SlideshowPanel : displays in details dialog
```

### Navigation Flow

```mermaid
graph TD
    Start([Launch TravelApp]) --> Home[Home Dashboard]
    Home -->|Click Transportation| TransPanel[Transportation Route Planner]
    Home -->|Click Accommodation| AccPanel[Accommodation Finder]
    Home -->|Click Attractions| AttrPanel[Interactive Attraction Map]
    Home -->|Click City Map| MapPanel[Brasov POI Map]
    
    TransPanel -->|Click Back| Home
    AccPanel -->|Click Back| Home
    AttrPanel -->|Click Back| Home
    MapPanel -->|Click Back| Home
    
    AccPanel -->|Click Book Now| BookDialog[Booking Form JDatePicker]
    BookDialog -->|Submit / Cancel| AccPanel
    
    AttrPanel -->|Click Marker| AttrDialog[Attraction Details Dialog]
    AttrDialog -->|View Slideshow| Slideshow[SlideshowPanel Auto Timer]
    AttrDialog -->|Click Buy Ticket| TicketConfirm[Ticket Confirmation]
```

---

## Project Structure

```directory
TouristGuide/
├── .vscode/                 # VS Code-specific configurations and classpaths
├── bin/                     # Compiled JVM .class files output directory
├── lib/                     # External library archives (.jar dependencies)
└── src/                     # Java source files
    ├── Fonts/               # TrueType Font assets (Montserrat, Roboto)
    ├── Images/              # UI Icons, backgrounds, and landmark images
    ├── AccomodationPnl.java # Accommodation display panel
    ├── AttractionPnl.java   # Map panel showing landmarks & details dialog
    ├── BookingDialog.java   # Booking form using date pickers
    ├── HomePanel.java       # Main entry dashboard view
    ├── MapPnl.java          # Basic city map with route vectors
    ├── TransportationControls.java # Panel controls for input search
    ├── TransportationPnl.java # Main route planner interface
    └── TravelApp.java       # Central coordinator & Application main entry
```

---

## Dependencies

All required JAR libraries are preloaded in the `TouristGuide/lib/` folder:

| Library | Version | Description |
|:---|:---:|:---|
| **FlatLaf** | `3.2` | Flat Look & Feel library providing standard modern designs. |
| **FlatLaf Extras** | `3.6` | Auxiliary styling utilities for custom Swing components. |
| **JXMapViewer2** | `2.8` | OpenStreetMap layout visualizer for rendering interactive tile maps. |
| **JDatePicker** | `1.3.4` | Interactive Calendar Date Picker component used for bookings. |
| **JCalendar** | `1.4` | Helper calendar date picker suite. |
| **JGoodies Common** | `1.2.0` | Helper foundation for JGoodies Swing UI components. |
| **JGoodies Looks** | `2.4.1` | Swing Look & Feel rendering extensions. |
| **Commons Logging** | `1.3.5` | Logging interface utilized by visual libraries. |
| **JUnit** | `4.6` | Unit testing utility suite. |

---

## Installation & Setup

### Prerequisites
*   **Java SE Development Kit (JDK)**: Version 8 or newer installed on your machine.
*   **Visual Studio Code** (with the *Extension Pack for Java*) **OR** any standard IDE (IntelliJ IDEA, Eclipse).

---

### Run in Visual Studio Code (Recommended)
1.  Clone the repository and open the workspace root folder in VS Code.
2.  VS Code will automatically detect the java project structure using configurations inside `.vscode/settings.json`.
3.  Ensure the libraries inside `TouristGuide/lib/` are fully referenced by checking the **Java Projects** view sidebar.
4.  Navigate to [TravelApp.java](TouristGuide/src/TravelApp.java), locate the `main` method, and click **Run** or press `F5` to compile and launch.

---

### Run from Command Line Interface (CLI)

Since the resources (Fonts, Images) are embedded within the `src` directory, you must include `src` in your classpath when executing to ensure images and fonts load properly.

#### 1. Navigate to the project directory:
```bash
cd TouristGuide
```

#### 2. Create compile destination folder:
```bash
mkdir bin
```

#### 3. Compile the Java files:
*   **Windows (Command Prompt / PowerShell)**:
    ```cmd
    javac -d bin -cp "lib/*" src/*.java
    ```
*   **Linux / macOS**:
    ```bash
    javac -d bin -cp "lib/*" src/*.java
    ```

#### 4. Run the application:
*   **Windows (Command Prompt / PowerShell)**:
    ```cmd
    java -cp "bin;src;lib/*" TravelApp
    ```
*   **Linux / macOS**:
    ```bash
    java -cp "bin:src:lib/*" TravelApp
    ```

---

## Cross-Platform Compatibility Notice

> [!WARNING]
> The source code references assets dynamically (e.g. `"images/marker-icon.png"` vs. `"Images/Metro.png"`).
> *   On **Windows**, filenames and paths are case-insensitive.
> *   On **Linux & macOS**, the filesystem is strictly case-sensitive. If you experience resource load failures, ensure the filesystem directory name matches the source paths (`Images` folder vs. `images/` loader string).

---
## License & Attributions

*   This project is licensed under the MIT License.
*   Maps rendering powered by [OpenStreetMap](https://www.openstreetmap.org/) contributors under ODbL, via JXMapViewer2.
*   Theme styling utilizes [FormDev FlatLaf](https://www.formdev.com/flatlaf/).




