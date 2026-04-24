## 🚗 Silicon Valley Trail 

 **A replayable survival game inspired by "Oregon Trail"** 

>Guide a scrappy startup team from the Silicon Prairie to the Silicon Valley to compete in the 🏆STARTUP WORLD CUP🏆.  
> >Manage your cash and other resources throughout the journey.
> 
> >Face unpredictable events and make strategic decisions.
> 
> >Game play is influenced by real-world data through public APIs.
---
## Game Overview

- Choose a departure date to balance pace, business risks, and resources.  
- Travel across 12 landmarks with branching paths.
- Travel costs increase as you move west.

### ⚙️ Features
- **Game Loop** - Each day you encounter events and choose actions.
- **Events** - Some events are landmark specific, while others are random.
- **Branching Routes** - Forks in the road enhance user decision-making and game results.
- **API Integration** 
  - The team's energy source/rations cost may change twice during the game due to live market rates.
  - Deck of Cards API (Reno casino event).
  - OSRM routing API affects miles traveled and miles remaining.
- **Offline Play** - Automatic fall back to mock data.
- **Win/Loss Conditions** 
  - Resource Depletion
  - Missed competition deadline
  - Arrival to final destination triggers resource calculation and determines the teams final placement and winnings.

### 📈 Metrics
```
RESOURCES
| 💰 Cash - $10,000
| 🎒 Rations - Scales with departure date
| 🤝 Connections - Open doors for product growth
| 📱 Followers - Campus prank video led to local social media fame 
| 🪙 AI Tokens - Technical firepower                         

STATUS
| ✨ Inspiration - Inspiration levels in check (too high = burnout, too low = lost steam)
| 📚 Learning Curve - STEADY, HIGH, or STEEP (affects AI token usage)   |
```


### 📐 Architecture
Uses a layered architecture with clear separation between game state, business logic, and static game data.

```
## 📁 Project Structure

silicon-valley-trail/
│
├── data/
│   └── LandmarkData.java        # Static attributes
│
├── model/
│   ├── GameSession.java        # Core game state
│   ├── GameEvent.java          # Base event model
│   ├── Landmark.java           # Location entity
│   ├── CompetitionResult.java  # Outcome tracking
│   ├── DepartureDate.java      # Game timing
│   ├── LandmarkType.java       # Enum
│   ├── LearningCurve.java      # Enum
│   └── Region.java             # Enum
│
├── service/
│   ├── GameService.java        # Main game loop logic
│   ├── TravelService.java      # Movement & progression
│   ├── ScoringService.java     # Score calculation
│   ├── PersistenceService.java # Save/load state
│   ├── RandomEventService.java # Random event generation
│   │
│   ├── api/                   # External API integrations
│   │
│   └── event/
│       └── anchored/
│           ├── AnchoredEvent.java
│           ├── DenverEvent.java
│           ├── PikesPeakEvent.java
│           ├── RenoEvent.java
│           ├── RiverEvent.java
│           └── SidneyEvent.java
│
├── util/
│   ├── GameConstants.java      # Config & constants
│   └── GameDisplay.java        # CLI display helpers
│
└── Main.java
```
---
## ▶️ Quick Start

**Prerequisites:**
- Java 17 (Amazon Corretto 17 recommended)
- Maven 3.6+

**Run the Game:**
```bash
git clone https://github.com/rgulbrandson27/silicon-valley-trail.git
cd silicon-valley-trail
mvn compile
mvn exec:java -Dexec.mainClass="com.raina.siliconvalleytrail.Main"
```

**Optional: API Key (Twelve Data)**  
Used for live Coca-Cola stock prices. The game runs without it using mock data.

```bash
cp .env.example .env
```

Add to `.env`:
```text
TWELVE_DATA_API_KEY=your_key_here
```
No API keys required for Deck of Cards or OSRM APIs.



## Design Notes

### GameSession as state container, services as rule enforcers
`GameSession` holds all mutable game state such as current location and resource levels.  A method belongs in `GameSession` only if it uses exclusively that class's own fields — for example, `getDaysRemaining()` is derived (`totalDays - daysElapsed`) rather than stored, eliminating the risk of values getting out of sync. Everything else is a service concern.

### Landmarks
`Landmark` fields are `final` to represent real travel.

### HashMap + embedded graph for landmark navigation
Landmarks are stored in a `HashMap<String, Landmark>` for O(1) name-based lookup. 
Storage order and game navigation order are decoupled, allowing for game model alterations (ex. new forks in the road) without significant restructuring. Each `Landmark` carries a `List<String> nextLandmarkName(s)` — the graph edges

### Anchored vs Random events
Initially, anchored and random events were separated, with location-based events implemented through inheritance from an abstract `AnchoredEvent` class.

Based on feedback during the interview process, this design was refactored to favor composition. Instead of an "is-a" relationship, each `Landmark` now has an optional `AnchoredEvent` property. When a player arrives, the game checks for and executes the event if present.

Random events remain managed by `RandomEventService`, which applies a 1-in-3 chance of occurrence and selects from a pool of possible events.

This refactor simplifies control flow, avoids unnecessary inheritance, and localizes behavior within each `Landmark`, making the system easier to extend.

### Jackson for persistence
Save files are serialized to JSON using Jackson's `ObjectMapper` with pretty-printing enabled for human readability. Each save is named by the player and stored in the `saves/` directory with the session UUID appended to prevent collisions. The no-arg constructor on `GameSession` enables Jackson deserialization without requiring additional configuration.

### Region-based travel cost multiplier
Each `Region` enum carries a `costMultiplier`. Travel costs scale automatically as the team moves west — Great Plains is cheapest (1.0x), West Coast most expensive (1.75x). This creates a natural difficulty ramp without hardcoding region-specific logic in services. The `Region` enum owns its own multiplier, keeping that knowledge encapsulated where it belongs.

### Days-based deadline, not landmark-count-based
The win/lose condition is time-based (reach San Francisco by November 18th), not landmark-count-based. This preserves extensibility — adding new landmarks between existing ones never breaks the core deadline mechanic. Starting rations scale with departure date (`totalDays / 2`), so difficulty is built into the departure choice itself.

### Miles remaining as display stat
`MilesRemaining` always reflects real driving distance from current landmark to San Francisco, regardless of which fork the player took. This ensures the stat is always meaningful and accurate — a player who took the Chimney Rock detour and a player who went through Sidney both see honest miles-to-SF figures once they converge at Denver.

---

## 🧪 Testing

```bash
mvn test
```
- API fallback behavior
- Event outcomes
- Distance calculations
- Core game logic
- Error Handling



## ⚖️ Trade-offs
- Gameplay resource state is prioritized over strict realism.
- Game has a CLI only interface to allow for focus on backend design concepts.
- Events are hardcoded (future: database-driven system).

---

## ⌛ If I Had More Time

- **Initial supply store**: Pre-departure store where players spend starting cash on rations, AI tokens, and supplies — meaningful tradeoffs before the journey begins, just like the original Oregon Trail.
- **Individual team member stats**: Team members differ in ways that could affect game play, such as caffeine dependency levels, unique quirks that become relevant throughout travel (ex. scared of heights, overly focused on cash flow).
- **Persistence upgrade with a relational database for events**: Events are currently hardcoded. A database with trigger conditions, resource impacts, and location filters would make the event pool extensible without touching service code.
- **Full path coverage testing**: Systematically testing every fork combination and its cumulative resource impact at San Francisco.
- **Web interface**: Spring Boot REST API wrapper to make the game browser-playable with a visual trail map.
- **Use of AI Agent/s**: Allowing team members to gather information or ask for recommendations prior decision-making.  Limitless ideas here!

---

## AI Usage

Claude (Anthropic) and OpenAi/ChatGPT were used as instructors thoughtout collaborative design and architecture partner throughout development.   Decisions were discussed and suggestions were challenged.  The developer made final decisions regarding game sturcture.  

Code was written by the developer with AI assistance for reviewing structure, catching inconsistencies, and generating boilerplate. The game design, route, landmark selection, event narratives, scoring system, and overall creative direction are original. AI helped implement decisions; the developer made them.

README.md was written by AI, with assistance/edits from the developer.

---

## Dependencies

| Dependency | Purpose |
|-----------|---------|
| Java 17 (Amazon Corretto) | Runtime |
| Maven | Build tool |
| Jackson (`jackson-databind` 2.15.2) | JSON serialization for save/load |
| JUnit Jupiter 5.10.0 | Testing framework |
| Twelve Data API | KO stock price (free tier, no credit card) |
| Deck of Cards API | Casino card draw (no key required) |
| OSRM / OpenStreetMap | Real driving distances (no key required) |

---

*Built for the LinkedIn REACH Backend Engineering Apprenticeship — 2026*
*From the Silicon Prairie to Silicon Valley 🏞️*