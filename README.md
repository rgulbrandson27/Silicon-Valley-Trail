# silicon-valley-trail
LinkedIn take-home project.  Build a modern day version of "Oregon Trail."  
# 🚀 Silicon Valley Trail
### A REACH Take-Home Assessment Project

> *"Oregon Trail" meets Silicon Valley startup culture. Guide your scrappy startup team from the Silicon Prairie to San Francisco — before November 18th.*

---

## Summary

Silicon Valley Trail is a CLI-based game where the player manages a startup team on a cross-country journey from Lincoln, Nebraska to the Startup World Cup Grand Finale in San Francisco. Each day the team travels, consumes resources, encounters events, and makes choices. A real-world commodities API influences gameplay, tying coffee futures prices to in-game resource costs.

---

## Quick Start

### Prerequisites
- Java 17 (Amazon Corretto 17 recommended)
- Maven

### Run the Game
```bash
git clone https://github.com/rgulbrandson27/silicon-valley-trail.git
cd silicon-valley-trail
mvn compile
mvn exec:java -Dexec.mainClass="com.raina.siliconvalleytrail.Main"
```

### Run with Mock Data (no API key required)
```bash
# Set environment variable to use mock API responses
export USE_MOCK_API=true
mvn exec:java -Dexec.mainClass="com.raina.siliconvalleytrail.Main"
```

### Run Tests
```bash
mvn test
```

---

## How to Set API Keys

This game uses the [Commodities API](https://commodities-api.com/) to pull live Arabica Coffee futures prices (ticker: KC), which influence in-game ration costs.

1. Copy the example env file:
```bash
cp .env.example .env
```

2. Add your API key to `.env`:
```
COMMODITIES_API_KEY=your_key_here
```

3. If no key is provided, the game runs automatically with mock data. No secrets are required to play.

---

## Game Overview

### Your Team
- **Founder** — you
- **Engineer 1, 2, 3** — your scrappy crew

### Resources to Manage
| Resource | Description |
|----------|-------------|
| 💰 Cash | Your runway — don't run out |
| 🎒 Rations | Days of food and coffee — replenish or game over |
| 🔗 Connections | People who open doors for you |
| 📱 Followers | Your startup's social media presence |
| 🤖 AI Tokens | Useful for solving technical challenges |

### Team Status
| Status | Description |
|--------|-------------|
| Inspiration | Must stay between 20-80 or streaks trigger consequences |
| Learning Curve | STEADY / HIGH / STEEP — affects resource costs |

### The Deadline
You must reach San Francisco **by November 18th** for the Startup World Cup Grand Finale. Choose your departure date wisely — earlier departures give more time but the same distance to cover.

| Departure Date | Days Available |
|----------------|---------------|
| October 28 | 21 days |
| November 1 | 17 days |
| November 5 | 13 days |
| November 10 | 8 days |

### Losing Conditions
- Cash hits zero
- Rations run out
- Inspiration streak outside 20-80 range too long
- Fail to reach San Francisco by November 18th

---

## The Route

14 landmarks, 12 visited per playthrough (two fork points):

```
Lincoln, NE → Ole's Big Game Steakhouse (Paxton, NE)
    → [FORK] Chimney Rock OR Sidney/Cabela's HQ
        → Denver, CO → Pikes Peak → Mystery Stop
            → Reno, NV → Santa Clara School of Law
                → [FORK] Pacific Beach OR LinkedIn HQ
                    → Stanford GSB → Philz Coffee → San Francisco 🏁
```

Travel costs increase by region — California is the most expensive:

| Region | Cost Multiplier |
|--------|----------------|
| Great Plains | 1.0x |
| Rockies | 1.25x |
| Southwest | 1.4x |
| West Coast | 1.75x |

---

## API Integration

### Commodities API — Arabica Coffee Futures (KC)
Live coffee futures prices influence the cost of rations. When coffee prices spike on the commodities market, your team feels it in the field.

- **Live mode**: Fetches real-time KC futures price
- **Fallback/mock mode**: Uses a hardcoded default price so the game always runs

---

## Architecture Overview

### Package Structure
```
com.raina.siliconvalleytrail
├── data/
│   ├── EventData.java       — event pool definitions
│   ├── LandmarkData.java    — HashMap of all 14 landmarks
│   └── RouteData.java       — route configuration
├── model/
│   ├── GameSession.java     — mutable game state container
│   ├── Landmark.java        — immutable landmark with graph edges
│   ├── GameEvent.java       — abstract event superclass
│   ├── CompetitionResult.java
│   ├── DepartureDate.java
│   ├── LandmarkType.java
│   ├── LearningCurve.java
│   └── Region.java
├── service/
│   ├── EventService.java    — event triggering and resolution
│   ├── GameService.java     — game loop, win/lose logic
│   ├── RouteService.java    — travel, mileage, detours
│   └── ScoringService.java  — final score calculation
└── util/
    ├── GameConstants.java   — constants used across the game
    ├── GameDisplay.java     — all CLI display logic
    └── RandomUtil.java      — random event helpers
```

### Key Design Decisions

**GameSession as state container, services as rule enforcers**
`GameSession` holds all mutable game state. Services make decisions. A method belongs in `GameSession` only if it uses exclusively that class's own fields (e.g. `getDaysRemaining()`). Everything else is a service concern.

**Immutable Landmarks**
All `Landmark` fields are `final`. Landmarks are game configuration, not game state — they never change during gameplay. Only `GameSession` is mutable.

**HashMap + embedded graph for landmark navigation**
Landmarks are stored in a `HashMap<String, Landmark>` for O(1) name-based lookup. Each `Landmark` carries a `List<String> nextLandmarkNames` — the graph edges. This decouples storage order from navigation order, meaning new landmarks can be added anywhere without restructuring the route.

**Days-based deadline, not landmark-count-based**
The win/lose condition is time-based (reach San Francisco by November 18th), not landmark-count-based. This preserves extensibility — adding new landmarks never breaks the core deadline mechanic.

**Derived vs stored fields**
`daysRemaining` is derived (`totalDays - daysElapsed`) rather than stored, eliminating the risk of those values getting out of sync. Same principle applied throughout.

**Region-based travel cost multiplier**
Each `Region` enum carries a `costMultiplier`. Travel costs scale automatically as the team moves west — Great Plains is cheapest, West Coast most expensive. This creates a natural difficulty ramp without hardcoding region-specific logic in services.

**Fork points handled by graph, not conditionals**
`RouteService` checks `nextLandmarkNames.size()` — if 1, travel automatically; if 2, present a choice. No hardcoded fork logic, no index-skipping. Adding a new fork point just means updating one landmark's `nextLandmarkNames`.

---

## Error Handling

- API timeouts and failures fall back to mock data automatically
- Invalid player input is caught and re-prompted
- Save file corruption is caught with a clear error message

---

## Tradeoffs & If I Had More Time

- **Database persistence**: Currently saves to a flat JSON file via Jackson. A proper database would support leaderboards and session history
- **More events**: The event pool could be much larger — currently covers critical path scenarios
- **Individual team member stats**: Engineers could have individual morale, coffee dependency, and skill levels affecting events differently
- **Real mileage API**: Distances between landmarks are currently hardcoded approximations. A routing API (OpenStreetMap/Nominatim or Mapbox) would provide real distances
- **Web interface**: The CLI could be wrapped in a simple REST API to make the game browser-playable

---

## AI Usage

Claude (Anthropic) was used as a collaborative design and architecture partner during development. All architectural decisions, field names, class designs, and tradeoffs were reasoned through in conversation. Code was written by the developer with AI assistance for reviewing structure and catching inconsistencies. The game design, route, landmarks, and narrative are original.

---

## Dependencies

- Java 17
- Maven
- Jackson (JSON serialization for save/load)
- [Commodities API](https://commodities-api.com/) (free tier)

---

*Built for the LinkedIn REACH Backend Engineering Apprenticeship — 2026*