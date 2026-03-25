## 🏞️ Silicon Valley Trail

 **A CLI startup survival game inspired by "Oregon Trail"** 

>Guide your startup team from Lincoln, Nebraska to the 🏆STARTUP WORLD CUP🏆 in San Francisco while managing resources, making strategic decisions, and surviving unpredictable events.  Game play is influenced by real-world data through public APIs.
---
## Game Overview

- Travel across 14 landmarks with branching paths
- Travel costs increase as you move west
- Choose a departure date and balance pace, business risks, and resources

### ⚙️ Features
- **Game Loop** - Each day you encounter events and choose actions.
- **Events** - Some events are landmark specific, while others are random.  Events can have a positive or negative impact on team resources.
- **Branching Routes** - Forks in the road enhance user decision-making and game results.
- **API Integration** 
  - The team's energy source cost may change twice during the game due to live market rates.
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
Designed using a layered architecture with clear separation between game state, business logic, and static game data.

**Package Structure**
```
data/       — landmark and event data
model/      — game state, landmarks, enums
service/    — game logic, events, APIs
util/       — display, constants, helpers
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
`GameSession` holds all mutable game state. Services make decisions. A method belongs in `GameSession` only if it uses exclusively that class's own fields — for example, `getDaysRemaining()` is derived (`totalDays - daysElapsed`) rather than stored, eliminating the risk of values getting out of sync. Everything else is a service concern.

### Immutable Landmarks
All `Landmark` fields are `final`. Landmarks are game configuration, not game state — they never change during gameplay. Only `GameSession` is mutable. This mirrors the real world: Chimney Rock doesn't move, but your cash balance does.

### HashMap + embedded graph for landmark navigation
Landmarks are stored in a `HashMap<String, Landmark>` for O(1) name-based lookup. Each `Landmark` carries a `List<String> nextLandmarkNames` — the graph edges. This decouples storage order from navigation order, meaning new landmarks can be added anywhere without restructuring the route. At fork points, `nextLandmarkNames` contains two entries; `RouteService` detects this and presents a choice to the player. For most of the route, landmarks behave like a linked list (one next connection). Fork points are where the graph structure becomes essential — a linked list cannot model branching without significant restructuring.

### Anchored vs Random events — separated by design
Location-specific events (Denver restaurant choices, Reno casino, Pikes Peak Cog Railway miss) extend an abstract `AnchoredEvent` superclass and execute deterministically on arrival at a specific landmark. Random events (`RandomEventService`) fire with a 1-in-3 chance each turn from a pool of 10 events — flat tires, viral posts, team conflicts, spoiled food, and learning curve escalations. Separating these two concerns keeps `Main` clean and makes each type easy to extend independently: adding a new anchored event is one new class; adding a new random event is one new private method.

### Jackson for persistence
Save files are serialized to JSON using Jackson's `ObjectMapper` with pretty-printing enabled for human readability. Each save is named by the player and stored in the `saves/` directory with the session UUID appended to prevent collisions. The no-arg constructor on `GameSession` enables Jackson deserialization without requiring additional configuration.

### Region-based travel cost multiplier
Each `Region` enum carries a `costMultiplier`. Travel costs scale automatically as the team moves west — Great Plains is cheapest (1.0x), West Coast most expensive (1.75x). This creates a natural difficulty ramp without hardcoding region-specific logic in services. The `Region` enum owns its own multiplier, keeping that knowledge encapsulated where it belongs.

### Days-based deadline, not landmark-count-based
The win/lose condition is time-based (reach San Francisco by November 18th), not landmark-count-based. This preserves extensibility — adding new landmarks between existing ones never breaks the core deadline mechanic. Starting rations scale with departure date (`totalDays / 2`), so difficulty is built into the departure choice itself.

### Miles remaining as display stat
`milesRemaining` always reflects real driving distance from current landmark to San Francisco, regardless of which fork the player took. This ensures the stat is always meaningful and accurate — a player who took the Chimney Rock detour and a player who went through Sidney both see honest miles-to-SF figures once they converge at Denver.

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
- Gameplay balance prioritized over strict realism
- CLI interface to focus on backend design
- Events are hardcoded (future: database-driven system)

---

## ⌛ If I Had More Time

- **Initial supply store**: Pre-departure store where players spend starting cash on rations, AI tokens, and supplies — meaningful tradeoffs before the journey begins, just like the original Oregon Trail
- **Individual team member stats**: Engineers with their own morale, coffee dependency, and quirks affecting events differently and creating more personal storytelling
- **Per-person ration tracking**: Each team member consuming individually based on coffee dependency rather than one shared daily unit
- **Relational database for events**: Events are currently hardcoded. A database with trigger conditions, resource impacts, and location filters would make the event pool extensible without touching service code
- **Full path coverage testing**: Systematically testing every fork combination and its cumulative resource impact at San Francisco
- **Persistence upgrade**: Flat JSON via Jackson works but a relational database would enable leaderboards and session history
- **Web interface**: Spring Boot REST API wrapper to make the game browser-playable with a visual trail map

---

## AI Usage

Claude (Anthropic) was used as a collaborative design and architecture partner throughout development. All architectural and data structure decisions were reasoned through in conversation.   ed values, immutability decisions, and the separation of anchored vs random events were all discussed and decided collaboratively.

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