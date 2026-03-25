# 🏞️ Silicon Valley Trail
### A REACH Apprenticeship Take-Home Assessment Project

> *From the Silicon Prairie to Silicon Valley. Guide your scrappy startup team from Lincoln, Nebraska to the Startup World Cup Grand Finale in San Francisco — before November 18th.*

---

## Summary

Silicon Valley Trail is a CLI-based game inspired by the classic Oregon Trail, set in the world of scrappy tech startups. You play as a founder leading a small team of engineers on a cross-country road trip from Lincoln, Nebraska to compete in the **Startup World Cup Grand Finale** at the Hilton Union Square in San Francisco on November 18, 2026 — a real competition with a real $1,000,000 first-place investment prize.

Each day the team travels, consumes resources, encounters random events, and makes consequential choices. Three real-world public APIs influence gameplay: live KO (Coca-Cola) stock prices affect ration costs as a market confidence indicator, the Deck of Cards API powers a casino event in Reno, and the OSRM/OpenStreetMap routing API provides real driving distances between landmarks.

> *Note: The dollar-and-cents resource math was designed to feel directionally correct rather than financially precise. Some resource costs and event impacts prioritize gameplay tension over accounting accuracy — a deliberate tradeoff given time constraints.*

---

## Quick Start

### Prerequisites
- Java 17 (Amazon Corretto 17 recommended)
- Maven 3.6+

### Run the Game
```bash
git clone https://github.com/rgulbrandson27/silicon-valley-trail.git
cd silicon-valley-trail
mvn compile
mvn exec:java -Dexec.mainClass="com.raina.siliconvalleytrail.Main"
```



### API Keys

**Twelve Data** (KO stock price) requires a free API key — no credit card needed. Sign up at [twelvedata.com](https://twelvedata.com).

1. Copy the example env file:
```bash
cp .env.example .env
```

2. Add your key to `.env`:
```
TWELVE_DATA_API_KEY=your_key_here
```

3. Point IntelliJ at the `.env` file via **Run → Edit Configurations → Environment Variables**

If no key is provided, the game falls back to mock data automatically — **no key is required to play**.

No API keys are required for the **Deck of Cards API** (Reno casino) or **OSRM routing API** (driving distances).

---

## Game Overview

### Your Team
- **Founder** — you
- **Engineer 1, 2, 3** — your scrappy crew

Name your game session, team members, and select a strategic departure date.

### Resources

| Resource | Starting Value | Description                                 |
|----------|---------------|---------------------------------------------|
| 💰 Cash | $10,000 | Your runway — don't run out                 |
| 🎒 Rations | Scales with departure date | Food and coffee for the road                |
| 🤝 Connections | 2 | People who open doors for you               |
| 📱 Followers | 450 | Campus prank video led to social media fame |
| 🪙 AI Tokens | 250,000 | Technical firepower                         |

### Team Status

| Status | Starting Value | Description                                |
|--------|---------------|--------------------------------------------|
| ✨ Inspiration | 60 / 100 | Keep it between 20–90 or face consequences |
| 📈 Learning Curve | STEADY | Escalates through random events            |


### Losing Conditions
- Cash hits zero
- Rations run out
- Inspiration stays below 20 for 2 days → team loses steam
- Inspiration stays above 90 for 2 days → team hits burnout
- Fail to reach San Francisco by November 18th

### Winning
Reach San Francisco, compete in the Startup World Cup, and receive a score and placement:

| Score | Result |
|-------|--------|
| 90–100 | 🥇 1st Place — $1,000,000 investment |
| 75–89 | 🥈 2nd Place — $250,000 investment |
| 60–74 | 🥉 3rd Place — $100,000 investment |
| Below 60 | 📋 Did Not Place |

---

## The Route

14 landmarks in the map, 12 visited per playthrough (two fork points):

```
Lincoln, NE → ... → [FORK 1] Chimney Rock OR Sidney ...
→ Denver → Pikes Peak → ... → Reno
→ [FORK 2] Pacific Beach OR LinkedIn HQ
→ Stanford GSB → Philz Coffee → San Francisco 🏁
```

Travel costs increase by region as you head west:

| Region | Daily Cost Multiplier |
|--------|----------------------|
| Great Plains | 1.0x |
| Rockies | 1.25x |
| Southwest | 1.4x |
| West Coast | 1.75x |

---

## API Integration

### 1. Twelve Data — KO Stock Price
Live Coca-Cola (KO) stock price is used as a market confidence indicator, checked at three points in the journey: Lincoln (baseline), Pikes Peak (comparison), and Santa Clara School of Law (final adjustment). Price movement affects ration cost multipliers.

- **Live mode**: Fetches real-time KO price from Twelve Data
- **Fallback**: Uses a hardcoded mock price so the game always runs

### 2. Deck of Cards API
At Reno, the player can try their luck at the casino. A real card is drawn from a shuffled deck via the Deck of Cards API, and the card value determines the cash outcome.

- **Live mode**: Calls `deckofcardsapi.com` — no API key required
- **Fallback**: Randomly selects from the same 13-card array

### 3. OSRM / OpenStreetMap Routing
Real driving distances between landmarks are calculated using the Open Source Routing Machine (OSRM), powered by OpenStreetMap road data.

- **Live mode**: Calls `router.project-osrm.org` — no API key required
- **Fallback**: Uses `distanceFromPrevious` stored on each Landmark object

---

## Architecture Overview

### Package Structure
```
data/       — landmark and event data
model/      — game state, landmarks, enums
service/    — game logic, events, APIs
util/       — display, constants, helpers
```
---

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

## Testing

Tests are located in `src/test/java/com/raina/siliconvalleytrail/service/`.

Run all tests:
```bash
mvn test
```

### CardApiServiceTest
- Live API draws return valid card values
- Mock fallback returns valid card values across 20 random draws
- Cash outcomes correct for all card tiers (ACE, face cards, mid, low)
- Outcome messages contain correct symbols for win/loss/break-even

### RouteApiServiceTest
- Lincoln to Ole's returns approximately correct real driving distance
- Lincoln to San Francisco returns approximately correct total distance
- Invalid coordinates return -1 (fallback signal)
- All US landmark longitudes are negative (western hemisphere sanity check)
- Same-location distance returns 0 or near-0

### Future testing goals
- Unit tests for every win/lose condition individually: cash zero, rations zero, deadline missed, low inspiration streak, high inspiration streak
- Integration tests for full playthrough paths — every route combination (Chimney Rock vs Sidney, Pacific Beach vs LinkedIn HQ) and the cumulative impact on resources at San Francisco
- Event impact tests ensuring no single random event can cause an instant game-over from a healthy starting resource state
- Scoring validation tests ensuring correct point calculation for all cash tiers, connection counts, and follower growth rates

---

## Error Handling

- API timeouts and failures fall back to mock data automatically — the game never crashes due to network issues
- Invalid player input is caught and re-prompted with clear messaging
- `listSaves()` null-checks `File.listFiles()` before iteration
- `mkdirs()` return value is checked and warns the player if the save directory cannot be created
- `PersistenceService` wraps all file operations in try-catch with user-facing error messages
- Inspiration changes are clamped between 0 and 100 using `Math.min` / `Math.max`

---

## If I Had More Time

- **Initial supply store**: Pre-departure store where players spend starting cash on rations, AI tokens, and supplies — meaningful tradeoffs before the journey begins, just like the original Oregon Trail
- **Individual team member stats**: Engineers with their own morale, coffee dependency, and quirks affecting events differently and creating more personal storytelling
- **Per-person ration tracking**: Each team member consuming individually based on coffee dependency rather than one shared daily unit
- **Learning curve token scaling**: `LearningCurve` (STEADY/HIGH/STEEP) escalates through random events but doesn't yet affect AI token costs — the next planned implementation step
- **Relational database for events**: Events are currently hardcoded. A database with trigger conditions, resource impacts, and location filters would make the event pool extensible without touching service code
- **Full path coverage testing**: Systematically testing every fork combination and its cumulative resource impact at San Francisco
- **Persistence upgrade**: Flat JSON via Jackson works but a relational database would enable leaderboards and session history
- **Web interface**: Spring Boot REST API wrapper to make the game browser-playable with a visual trail map

---

## AI Usage

Claude (Anthropic) was used extensively as a collaborative design and architecture partner throughout development. All architectural decisions were reasoned through in conversation — data structure choices (HashMap vs ArrayList, graph vs linked list), field naming conventions, derived vs stored values, immutability decisions, and the separation of anchored vs random events were all discussed and decided collaboratively.

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