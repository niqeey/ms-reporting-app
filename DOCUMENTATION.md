# MyPaceTracker Backend (Spring Boot Microservice)

## 📋 Table of Contents

1. [Overview](#overview)
2. [Architecture](#architecture)
3. [Technology Stack](#technology-stack)
4. [Project Structure](#project-structure)
5. [Database Schema](#database-schema)
6. [API Endpoints](#api-endpoints)
7. [Services](#services)
8. [Security & Authentication](#security--authentication)
9. [Race Modes](#race-modes)
10. [Archiving System](#archiving-system)
11. [Setup & Configuration](#setup--configuration)
12. [Building & Running](#building--running)
13. [Testing](#testing)
14. [Error Handling](#error-handling)
15. [Performance & Optimization](#performance--optimization)

---

## 🎯 Overview

The MyPaceTracker backend is a **Spring Boot microservice** that provides REST API endpoints for race management, participant tracking, and result processing. It handles authentication, business logic, data persistence, and report generation.

### Key Characteristics
- **Type**: RESTful Microservice
- **Framework**: Spring Boot 3.2.5
- **Java Version**: 17
- **Port**: 8080
- **Database**: MySQL 8.x
- **API Docs**: Swagger/OpenAPI

### Core Responsibilities
1. **Authentication & Session Management**: User login, session validation (2-hour timeout)
2. **Event Management**: CRUD operations for events, archiving
3. **Race Configuration**: Category setup, race mode configuration (NET/OFFICIAL/LAP)
4. **Result Processing**: Participant tracking, ranking, status management
5. **Reporting**: Excel export, statistics, leaderboards
6. **Public API**: Unauthenticated leaderboard access

---

## 🏗️ Architecture

### Layered Architecture

```
┌─────────────────────────────────────────────┐
│          PRESENTATION LAYER                 │
│  @RestController                            │
│  - LoginController                          │
│  - OrgEventController                       │
│  - RaceController                           │
│  - ParticipantController                    │
│  - ReportController                         │
│  - StatisticController                      │
│  - PublicLeaderboardController              │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│          APPLICATION LAYER                  │
│  @Service                                   │
│  - LoginService                             │
│  - OrgEventService                          │
│  - RaceService                              │
│  - RaceResultService                        │
│  - ReportService                            │
│  - ReportExportService                      │
│  - StatisticReportService                   │
│  - LeaderboardService                       │
│  - EventArchiveService                      │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│          DATA ACCESS LAYER                  │
│  @Repository (Spring Data JPA)              │
│  - TOrgRepository                           │
│  - TOrgUserRepository                       │
│  - TEventRepository                         │
│  - TEventCatRepository                      │
│  - TResultsRepository                       │
│  - TResultsArchiveRepository                │
└────────────────┬────────────────────────────┘
                 │
┌────────────────▼────────────────────────────┐
│          PERSISTENCE LAYER                  │
│  MySQL Database - mptbasedev                │
│  - Tables (t_org, t_event, results, etc.)  │
│  - Stored Procedures                        │
└─────────────────────────────────────────────┘
```

### Cross-Cutting Concerns

- **Interceptor**: `SessionValidationInterceptor` - Validates session on all protected endpoints
- **CORS**: `WebConfig` - Configures cross-origin requests
- **Exception Handling**: Controller-level try-catch, service-level error propagation
- **Logging**: SLF4J with Logback

---

## 🛠️ Technology Stack

### Core Framework
- **Spring Boot**: 3.2.5
- **Spring Web**: REST API support
- **Spring Data JPA**: ORM and database access
- **Hibernate**: JPA implementation

### Database
- **MySQL Connector/J**: JDBC driver (runtime)
- **HikariCP**: Connection pooling (bundled with Spring Boot)

### API Documentation
- **Springdoc OpenAPI**: 2.2.0
- **Swagger UI**: Interactive API documentation

### Utilities
- **Lombok**: 1.18.32 - Reduce boilerplate code
- **Apache POI**: 5.2.5 - Excel file generation

### Testing
- **Spring Boot Test**: Testing framework
- **JUnit 5**: Unit testing
- **Mockito**: Mocking framework

### Build Tool
- **Maven**: 3.x (specified in pom.xml)

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/smart/reporting/
│   │   ├── ReportingAppApplication.java     # Spring Boot entry point
│   │   ├── WebConfig.java                   # CORS & interceptor config
│   │   │
│   │   ├── controller/                      # REST Controllers
│   │   │   ├── LoginController.java             # POST /login
│   │   │   ├── OrgEventController.java          # /org/event/*
│   │   │   ├── RaceController.java              # /race/*
│   │   │   ├── ParticipantController.java       # /participant/*
│   │   │   ├── ReportController.java            # /report/*
│   │   │   ├── StatisticController.java         # /statistic/*
│   │   │   └── PublicLeaderboardController.java # /public/leaderboard/*
│   │   │
│   │   ├── service/                         # Business Logic
│   │   │   ├── LoginService.java                # Authentication
│   │   │   ├── OrgService.java                  # Organization logic
│   │   │   ├── OrgEventService.java             # Event CRUD
│   │   │   ├── RaceService.java                 # Category config
│   │   │   ├── RaceSetupService.java            # Race setup
│   │   │   ├── RaceResultService.java           # Result queries
│   │   │   ├── ReportService.java               # Result formatting
│   │   │   ├── ReportExportService.java         # Excel export
│   │   │   ├── StatisticReportService.java      # Statistics
│   │   │   ├── LeaderboardService.java          # Public leaderboard
│   │   │   ├── EventService.java                # Event utilities
│   │   │   ├── EventCatService.java             # Category utilities
│   │   │   └── EventArchiveService.java         # Archiving
│   │   │
│   │   ├── repository/                      # Data Access (JPA)
│   │   │   ├── TOrgRepository.java              # Organizations
│   │   │   ├── TOrgUserRepository.java          # Users
│   │   │   ├── TOrgEventRepository.java         # Org-Event mapping
│   │   │   ├── TEventRepository.java            # Events
│   │   │   ├── TEventCatRepository.java         # Categories
│   │   │   ├── TResultsRepository.java          # Active results
│   │   │   ├── TResultsArchiveRepository.java   # Archived results
│   │   │   └── ... (bibs, chips, markers, etc.)
│   │   │
│   │   ├── entity/                          # JPA Entities
│   │   │   ├── TOrg.java                        # @Table(t_org)
│   │   │   ├── TOrgUser.java                    # @Table(t_org_user)
│   │   │   ├── TOrgEvent.java                   # @Table(t_org_event)
│   │   │   ├── TEvent.java                      # @Table(t_event)
│   │   │   ├── TEventCat.java                   # @Table(t_event_cat)
│   │   │   ├── TResults.java                    # @Table(results)
│   │   │   ├── TResultsArchive.java             # @Table(results_archive)
│   │   │   └── TOrgContract.java                # @Table(t_org_contract)
│   │   │
│   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── LoginRequest.java
│   │   │   ├── LoginResponse.java
│   │   │   ├── EventRequest.java
│   │   │   ├── EventResponse.java
│   │   │   ├── RaceCategoryRequest.java
│   │   │   ├── RaceCategoryResponse.java
│   │   │   ├── LeaderboardRequest.java
│   │   │   ├── LeaderboardResponse.java
│   │   │   ├── ParticipantRequest.java
│   │   │   ├── ParticipantResponse.java
│   │   │   ├── CategoryResultListWrapper.java   # Wraps results with mode
│   │   │   └── ... (various request/response DTOs)
│   │   │
│   │   ├── model/                           # Domain Models
│   │   │   ├── CategoryResult.java
│   │   │   ├── ParticipantDetail.java
│   │   │   └── ... (business models)
│   │   │
│   │   ├── interceptor/                     # Request Interceptors
│   │   │   └── SessionValidationInterceptor.java
│   │   │
│   │   └── util/                            # Utility Classes
│   │       ├── TimeFormatter.java
│   │       └── ... (helper utilities)
│   │
│   └── resources/
│       ├── application.properties           # Main configuration
│       ├── application-dev.properties       # Dev profile (optional)
│       └── db/migration/                    # SQL scripts (optional)
│           ├── P_LEADERBOARD_REPORT.sql
│           └── P_ASSIGN_RANK1CAT.sql
│
└── test/
    └── java/com/smart/reporting/
        ├── controller/
        │   └── LoginControllerTest.java
        ├── service/
        │   └── LoginServiceTest.java
        └── interceptor/
            └── SessionValidationInterceptorTest.java
```

---

## 🗄️ Database Schema

### Key Tables

#### t_org
**Purpose**: Organizations/Race organizers
```sql
CREATE TABLE t_org (
    id VARCHAR(36) PRIMARY KEY,
    org_name VARCHAR(200) NOT NULL,
    contact_person VARCHAR(100),
    email VARCHAR(100),
    phone VARCHAR(20)
);
```

#### t_org_user
**Purpose**: Users and authentication
```sql
CREATE TABLE t_org_user (
    id VARCHAR(36) PRIMARY KEY,
    org_id VARCHAR(36) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20), -- 'admin', 'user'
    session_id VARCHAR(100),
    session_expiry_time DATETIME,
    FOREIGN KEY (org_id) REFERENCES t_org(id)
);
```

#### t_event
**Purpose**: Racing events
```sql
CREATE TABLE t_event (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    event_dt DATE,
    location VARCHAR(100),
    country VARCHAR(100) NOT NULL,
    weather VARCHAR(100),
    archived TINYINT DEFAULT 0, -- 0 = active, 1 = archived
    INDEX idx_archived (archived)
);
```

#### t_event_cat
**Purpose**: Race categories within events
```sql
CREATE TABLE t_event_cat (
    cat_id INT PRIMARY KEY AUTO_INCREMENT,
    event_id VARCHAR(36) NOT NULL,
    cat VARCHAR(100) NOT NULL,         -- Code (A, B, C)
    category VARCHAR(100) NOT NULL,    -- Full name
    distance DECIMAL(10,0) NOT NULL,   -- Distance in meters
    race VARCHAR(100) NOT NULL,        -- Description
    gender VARCHAR(100) NOT NULL,      -- All/Male/Female
    racemode VARCHAR(100),             -- NET/OFFICIAL/LAP
    is_Lap TINYINT,                    -- 0 or 1
    CPLIST VARCHAR(200),               -- Checkpoint list (CP1,CP2,CP3)
    Top INT,                           -- Number of top prizes
    top_prize INT,                     -- Prize amount
    Timegun INT,                       -- Gun time (ms)
    Timegun1 INT, Timegun2 INT, ...   -- Multiple gun times
    is_Live INT,                       -- Live tracking enabled
    is_result INT,                     -- Results published
    FOREIGN KEY (event_id) REFERENCES t_event(id)
);
```

#### results
**Purpose**: Active race results (100+ columns)
```sql
CREATE TABLE results (
    Pid INT PRIMARY KEY AUTO_INCREMENT,
    EventId VARCHAR(36) NOT NULL,
    ChipCode VARCHAR(50),
    Bib VARCHAR(20),
    FirstName VARCHAR(100),
    LastName VARCHAR(100),
    Gender VARCHAR(10),
    
    -- Categories
    Cat VARCHAR(10),
    Category VARCHAR(100),
    Distance DECIMAL(10,2),
    
    -- Rankings
    Rank1cat INT,     -- Category rank
    Rank1mix INT,     -- Gender rank
    Rank1tot INT,     -- Overall rank
    
    -- Times
    Time1 BIGINT,     -- Start time (ms since epoch)
    Time100 BIGINT,   -- Finish time (ms since epoch)
    Timenet BIGINT,   -- Net time (Time100 - Time1)
    Timegun BIGINT,   -- Gun time
    Timeofcl BIGINT,  -- Official time (Time100 - Timegun)
    
    -- Checkpoints (CP1-CP10)
    Time2 BIGINT, Time3 BIGINT, Time4 BIGINT, ...
    
    -- LAP mode
    Lap INT,          -- Number of laps completed
    bestlaptime BIGINT, -- Best lap time
    
    -- Status
    status VARCHAR(20), -- FINISH, DNF, DNS, DQ, etc.
    
    -- ... 100+ more columns
    
    INDEX idx_event (EventId),
    INDEX idx_bib (Bib),
    INDEX idx_category (EventId, Cat),
    INDEX idx_status (status)
);
```

#### results_archive
**Purpose**: Archived race results (identical structure to `results`)
```sql
CREATE TABLE results_archive (
    -- Exact same structure as 'results' table
    -- Used for long-term storage of completed events
);
```

#### t_org_event
**Purpose**: Many-to-many mapping between orgs and events
```sql
CREATE TABLE t_org_event (
    org_id VARCHAR(36),
    event_id VARCHAR(36),
    PRIMARY KEY (org_id, event_id),
    FOREIGN KEY (org_id) REFERENCES t_org(id),
    FOREIGN KEY (event_id) REFERENCES t_event(id)
);
```

### Stored Procedures

#### P_LEADERBOARD_REPORT
**Purpose**: Generate public leaderboard data
**Parameters**:
- `p_event_id VARCHAR(36)` - Event ID
- `p_category VARCHAR(10)` - Category code

**Returns**: Result set with:
- Bib, Name, Category
- Ranks (category, gender, overall)
- Times (start, finish, net, official)
- Checkpoint times (CP1-CP10)

**Usage**:
```sql
CALL P_LEADERBOARD_REPORT('event-uuid', 'A');
```

#### P_ASSIGN_RANK1CAT
**Purpose**: Calculate and assign category ranks
**Parameters**:
- `p_event_id VARCHAR(36)` - Event ID
- `p_category VARCHAR(10)` - Category code

**Logic**:
1. Query all finished participants in category
2. Order by time (net or official, depending on race mode)
3. Assign sequential rank
4. Update `Rank1cat` column in results table

---

## 🔌 API Endpoints

### Authentication

#### POST /login
**Description**: Authenticate user and create session  
**Request Body**:
```json
{
  "username": "admin",
  "password": "password"
}
```
**Response**: `200 OK`
```json
{
  "sessionId": "uuid",
  "sessionExpiryTime": "2026-02-15T14:00:00",
  "username": "admin",
  "orgId": "org-uuid",
  "role": "admin"
}
```
**Response Headers**:
- `SessionId`
- `SessionExpiryTime`
- `UserName`
- `OrgId`
- `Role`

**Errors**:
- `401 Unauthorized` - Invalid credentials

---

### Event Management (`/org/event`)

#### POST /org/event/list/upcoming
**Description**: Get active (non-archived) events for organization  
**Auth**: Required  
**Request**:
```json
{
  "orgId": "org-uuid"
}
```
**Response**: `200 OK`
```json
[
  {
    "eventId": "event-uuid",
    "name": "Marathon 2026",
    "eventDt": "2026-03-15",
    "location": "Kuala Lumpur",
    "country": "Malaysia",
    "archived": false
  }
]
```

#### POST /org/event/list/archived
**Description**: Get archived events  
**Auth**: Required  
**Request**: Same as above  
**Response**: Same format, `archived: true`

#### POST /org/event/create
**Description**: Create new event (Admin only)  
**Auth**: Required (Admin)  
**Request**:
```json
{
  "orgId": "org-uuid",
  "name": "Marathon 2026",
  "eventDt": "2026-03-15",
  "location": "Kuala Lumpur",
  "country": "Malaysia"
}
```
**Response**: `201 Created`
```json
{
  "eventId": "new-event-uuid",
  "message": "Event created successfully"
}
```

#### POST /org/event/delete
**Description**: Delete event  
**Auth**: Required (Admin)  
**Request**:
```json
{
  "eventId": "event-uuid"
}
```
**Response**: `200 OK`

#### POST /org/event/archive
**Description**: Archive event (moves results to archive table)  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid"
}
```
**Response**: `200 OK`
```json
{
  "message": "Event archived successfully",
  "resultsArchived": 1523
}
```

#### POST /org/event/unarchive
**Description**: Unarchive event (restores results from archive)  
**Auth**: Required  
**Request**: Same as archive  
**Response**: `200 OK`
```json
{
  "message": "Event unarchived successfully",
  "resultsRestored": 1523
}
```

#### POST /org/event/upload-csv
**Description**: Upload CSV file for bulk participant registration  
**Auth**: Required  
**Content-Type**: `multipart/form-data`  
**Request**:
- `file`: CSV file
- `eventId`: Event UUID

**Response**: `200 OK`

---

### Race Configuration (`/race`)

#### POST /race/categories
**Description**: Get all categories for an event  
**Auth**: Required (can be public for leaderboards)  
**Request**:
```json
{
  "eventId": "event-uuid"
}
```
**Response**: `200 OK`
```json
[
  {
    "catId": 1,
    "eventId": "event-uuid",
    "eventName": "Marathon 2026",
    "cat": "A",
    "category": "10KM Men",
    "distance": 10000,
    "race": "10KM",
    "gender": "Male",
    "raceMode": "OFFICIAL",
    "isLap": 0,
    "cplist": "CP1,CP2,CP3",
    "top": 3,
    "topPrize": 1000,
    "timegun": 1700000000000
  }
]
```

#### POST /race/category/create
**Description**: Create new category  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "cat": "A",
  "category": "10KM Men",
  "distance": 10000,
  "race": "10KM",
  "gender": "Male",
  "raceMode": "OFFICIAL",
  "cplist": "CP1,CP2,CP3"
}
```
**Response**: `201 Created`

#### POST /race/category/update
**Description**: Update existing category  
**Auth**: Required  
**Request**: Same as create, plus `catId`  
**Response**: `200 OK`

#### POST /race/category/delete
**Description**: Delete category  
**Auth**: Required  
**Request**:
```json
{
  "catId": 1
}
```
**Response**: `200 OK`

---

### Results & Reports (`/report`)

#### POST /report/event/category
**Description**: Get results for a specific category  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "category": "A"
}
```
**Response**: `200 OK`
```json
{
  "mode": "TIME",
  "data": [
    {
      "bib": "101",
      "firstName": "John",
      "lastName": "Doe",
      "category": "10KM Men",
      "gender": "M",
      "rankCat": 1,
      "rankMix": 1,
      "rankTot": 1,
      "netTime": "00:45:30",
      "officialTime": "00:45:32",
      "timeCP1": "00:10:15",
      "timeCP2": "00:20:30",
      "timeCP3": "00:35:45"
    }
  ]
}
```

**For LAP Mode**:
```json
{
  "mode": "LAP",
  "data": [
    {
      "bib": "101",
      "firstName": "John",
      "lastName": "Doe",
      "lap": 5,
      "bestLapTime": "00:09:30",
      "totalTime": "00:47:30"
    }
  ]
}
```

#### POST /report/event/distance
**Description**: Get overall results by distance (all categories with same distance)  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "distance": 10000
}
```
**Response**: Similar to category, sorted by `rankTot`

#### POST /report/event/gender
**Description**: Get gender-specific results  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "distance": 10000,
  "gender": "M"
}
```
**Response**: Similar to category, sorted by `rankMix`

#### POST /report/export
**Description**: Export results to Excel (.xlsx)  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "category": "A"
}
```
**Response**: `200 OK`  
**Content-Type**: `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`  
**Content-Disposition**: `attachment; filename="results.xlsx"`

---

### Participants (`/participant`)

#### POST /participant/details
**Description**: Get detailed information for a specific participant  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid",
  "bib": "101"
}
```
**Response**: `200 OK`
```json
{
  "pid": 12345,
  "bib": "101",
  "firstName": "John",
  "lastName": "Doe",
  "gender": "M",
  "category": "10KM Men",
  "status": "FINISH",
  "time1": 1700000000000,
  "time100": 1700002730000,
  "timenet": 2730000,
  "timeofcl": 2732000,
  "lap": null,
  "rankCat": 1,
  "rankMix": 1,
  "rankTot": 1
}
```

#### POST /participant/update
**Description**: Update participant information (status, times, etc.)  
**Auth**: Required  
**Request**:
```json
{
  "pid": 12345,
  "status": "FINISH",
  "time1": 1700000000000,
  "time100": 1700002730000,
  "lap": 0
}
```
**Response**: `200 OK`

#### POST /participant/list/registered
**Description**: Get all registered participants  
**Auth**: Required  
**Request**:
```json
{
  "eventId": "event-uuid"
}
```
**Response**: Array of participants

**Similar endpoints**:
- `/participant/list/started`
- `/participant/list/finished`
- `/participant/list/dns` (Did Not Start)
- `/participant/list/dnf` (Did Not Finish)
- `/participant/list/dq` (Disqualified)
- ...etc

---

### Public Leaderboard (`/public/leaderboard`)

#### POST /public/leaderboard/{eventId}
**Description**: Get public leaderboard (NET/OFFICIAL modes)  
**Auth**: **Not Required**  
**Request**:
```json
{
  "category": "A"
}
```
**Response**: `200 OK`
```json
[
  {
    "bib": "101",
    "name": "John Doe",
    "category": "10KM Men",
    "gender": "M",
    "rankCat": 1,
    "rankMix": 1,
    "rankTot": 1,
    "timeStart": "08:00:00",
    "timeFinish": "08:45:30",
    "netTime": "00:45:30",
    "officialTime": "00:45:32",
    "timeCP1": "00:10:15",
    "timeCP2": "00:20:30",
    "cplist": "CP1,CP2,CP3"
  }
]
```

#### POST /public/leaderboard/lap/{eventId}
**Description**: Get public LAP leaderboard  
**Auth**: **Not Required**  
**Request**:
```json
{
  "eventId": "event-uuid",
  "category": "A"
}
```
**Response**: `200 OK` (LAP mode format)

---

### Statistics (`/statistic`)

#### POST /statistic/event/{eventId}
**Description**: Get event statistics  
**Auth**: Required  
**Response**: `200 OK`
```json
{
  "totalRegistered": 2500,
  "totalStarted": 2480,
  "totalFinished": 2400,
  "totalDNS": 20,
  "totalDNF": 80,
  "totalDQ": 0,
  "completionRate": 96.0,
  "categories": [
    {
      "category": "10KM Men",
      "registered": 1000,
      "finished": 980,
      "avgTime": "00:52:30"
    }
  ]
}
```

---

## 🧠 Services

### LoginService
**Purpose**: Handle user authentication and session management  
**Methods**:
- `authenticate(username, password)` → LoginResponse
- `generateSessionId()` → String (UUID)
- `saveSession(user, sessionId, expiryTime)` → void

**Logic**:
1. Query user by username
2. Verify password (plain text comparison - should be hashed in production!)
3. Generate session ID (UUID)
4. Calculate expiry time (now + 2 hours)
5. Update user record with session info
6. Return LoginResponse with session data

---

### OrgEventService
**Purpose**: Manage events for organizations  
**Methods**:
- `getEventsByOrgId(orgId)` → List<EventResponse>
- `getUpcomingEventsByOrgId(orgId)` → List<EventResponse> (archived=false)
- `getArchivedEventsByOrgId(orgId)` → List<EventResponse> (archived=true)
- `createEvent(request)` → String (eventId)
- `updateEvent(eventId, request)` → void
- `deleteEvent(eventId)` → void

**Archive Handling**:
- Filters by `archived` flag
- Does NOT handle result archiving (delegated to EventArchiveService)

---

### RaceService
**Purpose**: Manage race categories  
**Methods**:
- `getCategoriesByEventId(eventId)` → List<RaceCategoryResponse>
- `createCategory(request)` → RaceCategoryResponse
- `updateCategory(catId, request)` → RaceCategoryResponse
- `deleteCategory(catId)` → void

**Category Data Mapping**:
- DTO → Entity conversion
- Handle LAP mode fields (`isLap`, `racemode`)
- Calculate gun times
- Parse checkpoint list

---

### RaceResultService
**Purpose**: Query and format race results  
**Key Feature**: **Automatic Archive Detection**  

**Methods**:
- `getResults(eventId, category)` → List<CategoryResult>
- `getResultsByEventAndCat(eventId, category)` → List (auto-detects archive)
- `getTopResultsByEventAndCat(eventId, category, top)` → List
- `getResultsByEventAndDistanceOrderByRank1tot(eventId, distance)` → List
- `getResultsByEventAndDistanceAndGenderOrderByRank1mix(eventId, distance, gender)` → List
- `getParticipantDetails(eventId, bib)` → ParticipantDetail

**Archive Detection Logic**:
```java
public List<TResults> getResultsByEventAndCat(String eventId, String category) {
    TEvent event = eventRepository.findById(eventId).orElseThrow();
    
    if (event.getArchived() != null && event.getArchived()) {
        // Query from results_archive
        return convertArchiveToResults(
            archiveRepository.findByEventIdAndCatOrderByRank1cat(eventId, category)
        );
    } else {
        // Query from results
        return resultsRepository.findByEventIdAndCatOrderByRank1cat(eventId, category);
    }
}
```

**Time Formatting**:
- Converts milliseconds to HH:MM:SS format
- Handles null values
- Calculates net time (finish - start)
- Calculates official time (finish - gun)

---

### ReportService
**Purpose**: Format results for display  
**Methods**:
- `getCategoryResults(eventId, category)` → CategoryResultListWrapper
- `getOverallResults(eventId, distance)` → List
- `getGenderResults(eventId, distance, gender)` → List

**CategoryResultListWrapper**:
```java
{
  "mode": "TIME" | "LAP",
  "data": [ ...results... ]
}
```

**Mode Detection**:
- Checks `TEventCat.racemode`
- If "LAP" → Include lap data, different sorting
- If "NET" or "OFFICIAL" → Include time data, standard sorting

---

### ReportExportService
**Purpose**: Generate Excel reports  
**Methods**:
- `exportCategoryResults(eventId, category)` → byte[]
- `exportAllResults(eventId)` → byte[]

**Excel Generation** (Apache POI):
```java
Workbook workbook = new XSSFWorkbook();
Sheet sheet = workbook.createSheet("Results");

// Header row
Row headerRow = sheet.createRow(0);
headerRow.createCell(0).setCellValue("Rank");
headerRow.createCell(1).setCellValue("Bib");
headerRow.createCell(2).setCellValue("Name");
// ...

// Data rows
int rowNum = 1;
for (CategoryResult result : results) {
    Row row = sheet.createRow(rowNum++);
    row.createCell(0).setCellValue(result.getRankCat());
    row.createCell(1).setCellValue(result.getBib());
    row.createCell(2).setCellValue(result.getFullName());
    // ...
}

// Auto-size columns
for (int i = 0; i < 20; i++) {
    sheet.autoSizeColumn(i);
}

// Write to byte array
ByteArrayOutputStream baos = new ByteArrayOutputStream();
workbook.write(baos);
return baos.toByteArray();
```

---

### StatisticReportService
**Purpose**: Calculate event statistics  
**Methods**:
- `getEventStatistics(eventId)` → StatisticResponse

**Calculations**:
- Count by status (`status` field)
- Group by category
- Calculate averages (time)
- Calculate completion rate
- Gender distribution

**SQL Aggregation**:
```sql
SELECT 
    COUNT(*) as total,
    SUM(CASE WHEN status = 'FINISH' THEN 1 ELSE 0 END) as finished,
    AVG(CASE WHEN status = 'FINISH' THEN Timenet END) as avgTime
FROM results
WHERE EventId = ?
GROUP BY Cat;
```

---

### LeaderboardService
**Purpose**: Generate public leaderboard data  
**Methods**:
- `getLeaderboardData(eventId, category)` → List<LeaderboardResponse>
- `getLapLeaderboardData(eventId, category)` → List (LAP format)

**Uses Stored Procedure**:
```java
@Query(value = "CALL P_LEADERBOARD_REPORT(:eventId, :category)", 
       nativeQuery = true)
List<Object[]> getLeaderboardData(@Param("eventId") String eventId,
                                   @Param("category") String category);
```

**Post-Processing**:
- Convert Object[] to LeaderboardResponse
- Format times (ms → HH:MM:SS)
- Remove milliseconds for cleaner display
- Calculate checkpoint times relative to gun time

---

### EventArchiveService
**Purpose**: Archive/unarchive events with results  
**Critical Service** for data management  

**Methods**:
- `archiveEvent(eventId)` → ArchiveResponse
- `unarchiveEvent(eventId)` → ArchiveResponse

**Archive Process**:
1. Validate event exists and is not already archived
2. Query all results for event
3. Copy each result to `results_archive` (field-by-field, 100+ fields)
4. Delete from `results` table
5. Update `t_event.archived = 1`
6. Return count of archived results

**Field-by-Field Copy** (ensures 100% data integrity):
```java
private TResultsArchive copyToArchive(TResults result) {
    TResultsArchive archive = new TResultsArchive();
    
    archive.setPid(result.getPid());
    archive.setEventId(result.getEventId());
    archive.setChipCode(result.getChipCode());
    archive.setBib(result.getBib());
    archive.setFirstName(result.getFirstName());
    archive.setLastName(result.getLastName());
    // ... 100+ more fields
    archive.setTime1(result.getTime1());
    archive.setTime100(result.getTime100());
    archive.setRank1cat(result.getRank1cat());
    // ... all fields copied
    
    return archive;
}
```

**Unarchive Process**: Reverse of archive
1. Query from `results_archive`
2. Copy to `results`
3. Delete from `results_archive`
4. Update `t_event.archived = 0`

**Transaction Management**:
```java
@Transactional
public ArchiveResponse archiveEvent(String eventId) {
    // All operations in single transaction
    // Rollback on any error
}
```

---

## 🔐 Security & Authentication

### Session-Based Authentication

**Flow**:
1. User logs in → Server generates session ID
2. Session data stored in `t_org_user` table:
   - `session_id` (UUID)
   - `session_expiry_time` (DATETIME, now + 2 hours)
3. Client stores session info in sessionStorage
4. Every protected request includes session headers
5. Server validates session on each request

**Session Headers** (Client → Server):
- `sessionId`: UUID string
- `sessionExpiryTime`: ISO timestamp
- `username`: Username
- `orgId`: Organization UUID

### SessionValidationInterceptor

**Purpose**: Validate session before processing protected endpoints  
**Implementation**: `HandlerInterceptor` interface  

**Logic**:
```java
@Override
public boolean preHandle(HttpServletRequest request, 
                         HttpServletResponse response, 
                         Object handler) throws Exception {
    
    String path = request.getRequestURI();
    
    // Whitelist public endpoints
    if (path.startsWith("/login") || 
        path.startsWith("/public/") ||
        path.startsWith("/swagger-ui") ||
        path.startsWith("/api-docs")) {
        return true;
    }
    
    // Extract session headers
    String sessionId = request.getHeader("sessionId");
    String username = request.getHeader("username");
    String sessionExpiryTime = request.getHeader("sessionExpiryTime");
    
    // Validate headers present
    if (sessionId == null || username == null || sessionExpiryTime == null) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
    
    // Query user from database
    TOrgUser user = userRepository.findByUsername(username);
    if (user == null || !user.getSessionId().equals(sessionId)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
    
    // Check expiry (server-side)
    Date expiry = user.getSessionExpiryTime();
    if (expiry == null || expiry.before(new Date())) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }
    
    // Session valid
    return true;
}
```

**Registration**:
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Autowired
    private SessionValidationInterceptor sessionValidationInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionValidationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/public/**", 
                                     "/swagger-ui/**", "/api-docs/**");
    }
}
```

### CORS Configuration

**Purpose**: Allow frontend (different origin) to call backend  
**Configuration**:
```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:3000",  // Dev frontend
                "https://www.mypacetracker.com",  // Prod frontend
                "https://mypacetracker.com"
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .exposedHeaders("OrgId", "UserName", "SessionId", 
                            "SessionExpiryTime", "Role") // Allow reading custom headers
            .allowCredentials(true)
            .maxAge(3600);
}
```

**Exposed Headers**: Client can read these headers from response

---

## 🏁 Race Modes

### 1. NET Mode
**Timing**: Individual chip time (start to finish)  
**Primary Metric**: `Timenet = Time100 - Time1`  
**Use Case**: Waves start, mass participation events  

**Database Fields**:
- `Time1`: Start time (when chip crosses start line)
- `Time100`: Finish time (when chip crosses finish line)
- `Timenet`: Net time (calculated)
- `Timeofcl`: Official time (backup)

**Ranking**: By `Timenet` (ascending)

### 2. OFFICIAL Mode
**Timing**: Gun time (uniform start)  
**Primary Metric**: `Timeofcl = Time100 - Timegun`  
**Use Case**: Competitive races, single start  

**Database Fields**:
- `Timegun`: Gun time (from `t_event_cat` table)
- `Time100`: Finish time
- `Timeofcl`: Official time (calculated)
- `Timenet`: Net time (also tracked)

**Ranking**: By `Timeofcl` (ascending)

**Display**: Both net and official times shown

### 3. LAP Mode
**Timing**: Multiple finish line crossings  
**Primary Metric**: `Lap` count + `bestlaptime`  
**Use Case**: Circuit races, timed events (e.g., 60-minute race)  

**Database Fields**:
- `Lap`: Number of laps completed (increments on each finish line cross)
- `bestlaptime`: Best single lap time
- `Time100`: Last finish time (cumulative)
- `Timenet`: Total race time

**Ranking**: 
1. Primary: `Lap` count (descending)
2. Secondary: `bestlaptime` (ascending)

**Special Handling**:
- Backend increments `Lap` on each finish
- Frontend displays LAP leaderboard with different columns
- Different stored procedure for LAP reporting

**Configuration** (RaceSetup):
- `isLap = 1` in `t_event_cat`
- `racemode = 'LAP'`
- Additional fields: `halflapDistance`, `fulllapDistance`, `numberOfLaps`

---

## 🗄️ Archiving System

### Purpose
- Preserve completed event data long-term
- Reduce active table size for performance
- Maintain ability to query old results

### Architecture

**Two Mirror Tables**:
- `results` - Active events
- `results_archive` - Archived events

**Identical Structure**: 100+ columns, exact match

### Archive Process

1. **Initiate Archive** (Admin action):
   ```
   POST /org/event/archive
   Body: { eventId: "uuid" }
   ```

2. **Backend Logic** (`EventArchiveService.archiveEvent()`):
   - Check event exists and not already archived
   - Query all results: `SELECT * FROM results WHERE EventId = ?`
   - For each result:
     - Copy to `results_archive` (field-by-field)
     - Delete from `results`
   - Update `t_event`: `archived = 1`
   - Return count of archived results

3. **Result**:
   - All results moved to archive table
   - Event marked as archived
   - Frontend shows in "Archived Events" tab

### Unarchive Process

1. **Initiate Unarchive**:
   ```
   POST /org/event/unarchive
   Body: { eventId: "uuid" }
   ```

2. **Backend Logic**:
   - Reverse of archive process
   - Copy from `results_archive` to `results`
   - Update `t_event`: `archived = 0`

### Transparent Access

**Key Feature**: All result queries automatically detect archived status

**Implementation**:
```java
public List<TResults> getResultsByEventAndCat(String eventId, String category) {
    TEvent event = eventRepository.findById(eventId).orElseThrow();
    
    if (isArchived(event)) {
        // Query from archive table
        List<TResultsArchive> archived = 
            archiveRepository.findByEventIdAndCat(eventId, category);
        // Convert to TResults format for consistent return type
        return convertArchiveToResults(archived);
    } else {
        // Query from active table
        return resultsRepository.findByEventIdAndCat(eventId, category);
    }
}
```

**Benefits**:
- Frontend doesn't need to know about archiving
- Same API endpoints work for both active and archived
- Seamless user experience

### Data Integrity

**100% Field Copy**:
```java
archive.setPid(result.getPid());
archive.setEventId(result.getEventId());
// ... 100+ fields individually copied
// Ensures no data loss
```

**Transaction Safety**:
```java
@Transactional
public ArchiveResponse archiveEvent(String eventId) {
    // All operations in single transaction
    // If any step fails, entire process rolls back
    // Guarantees data integrity
}
```

---

## ⚙️ Setup & Configuration

### Prerequisites
- JDK 17
- Maven 3.x
- MySQL 8.x
- IDE (IntelliJ IDEA recommended)

### Database Setup

1. **Create Database**:
   ```sql
   CREATE DATABASE mptbasedev CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **Import Schema** (if available):
   ```bash
   mysql -u root -p mptbasedev < dump.sql
   ```

3. **Create Archive Table**:
   ```bash
   mysql -u root -p mptbasedev < create_results_archive.sql
   ```

4. **Install Stored Procedures**:
   ```bash
   mysql -u root -p mptbasedev < P_LEADERBOARD_REPORT.sql
   mysql -u root -p mptbasedev < P_ASSIGN_RANK1CAT.sql
   ```

### Application Configuration

**File**: `src/main/resources/application.properties`

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/mptbasedev
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

# Server
server.port=8080

# File Upload
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Timeouts (15 minutes)
server.connection-timeout=900000
spring.mvc.async.request-timeout=900000

# Compression
server.compression.enabled=true
server.compression.mime-types=text/html,text/xml,text/plain,text/css,application/json
server.compression.min-response-size=1024

# Swagger/OpenAPI
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enabled=true
springdoc.api-docs.enabled=true
```

**Environment-Specific**:
- `application-dev.properties` - Development settings
- `application-prod.properties` - Production settings

**Activate Profile**:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Lombok Configuration

**File**: `lombok.config` (project root)
```
lombok.addLombokGeneratedAnnotation = true
lombok.anyConstructor.addConstructorProperties = true
```

**Purpose**: Reduce boilerplate code with annotations
- `@Data` - Getters, setters, toString, equals, hashCode
- `@Entity` - JPA entity
- `@Service`, `@Repository`, `@RestController` - Spring components

---

## 🏃 Building & Running

### Development Mode

```bash
# Navigate to project
cd C:\MyPaceTracker\MSMyPaceTracker\ms-reporting-app

# Clean previous builds
mvn clean

# Compile
mvn compile

# Run application
mvn spring-boot:run
```

**Application starts on**: `http://localhost:8080`

**Check Health**:
```bash
curl http://localhost:8080/api-docs
```

**Access Swagger UI**:
```
http://localhost:8080/swagger-ui.html
```

### Production Build

```bash
# Build JAR file
mvn clean package

# Output: target/reporting-app-1.0.0.jar
```

**Run JAR**:
```bash
java -jar target/reporting-app-1.0.0.jar
```

**With Specific Profile**:
```bash
java -jar target/reporting-app-1.0.0.jar --spring.profiles.active=prod
```

### Docker Deployment (Optional)

**Dockerfile**:
```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/reporting-app-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

**Build Image**:
```bash
docker build -t mypacetracker-backend .
```

**Run Container**:
```bash
docker run -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/mptbasedev \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=password \
  mypacetracker-backend
```

---

## 🧪 Testing

### Test Framework
- **JUnit 5**: Test runner
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing support

### Existing Tests
- `LoginControllerTest.java` - Controller layer tests
- `LoginServiceTest.java` - Service layer tests
- `SessionValidationInterceptorTest.java` - Interceptor tests

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=LoginServiceTest

# Run with coverage
mvn clean test jacoco:report
```

**Coverage Report**: `target/site/jacoco/index.html`

### Test Examples

**Service Test**:
```java
@Test
public void testSuccessfulLogin() {
    // Arrange
    TOrgUser user = new TOrgUser();
    user.setUsername("admin");
    user.setPassword("password");
    user.setOrgId("org-uuid");
    user.setRole("admin");
    
    when(userRepository.findByUsername("admin")).thenReturn(user);
    
    // Act
    LoginResponse response = loginService.authenticate("admin", "password");
    
    // Assert
    assertNotNull(response);
    assertNotNull(response.getSessionId());
    assertEquals("admin", response.getUsername());
    assertEquals("admin", response.getRole());
    
    // Verify session saved
    verify(userRepository, times(1)).save(any(TOrgUser.class));
}
```

**Controller Test**:
```java
@Test
public void testLoginEndpoint() throws Exception {
    LoginResponse response = new LoginResponse();
    response.setSessionId("session-uuid");
    response.setUsername("admin");
    response.setOrgId("org-uuid");
    response.setRole("admin");
    
    when(loginService.authenticate("admin", "password")).thenReturn(response);
    
    mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"admin\",\"password\":\"password\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(header().exists("SessionId"));
}
```

---

## ⚠️ Error Handling

### Standard Error Responses

**Format**:
```json
{
  "error": "Error message",
  "status": 400,
  "timestamp": "2026-02-15T12:00:00"
}
```

### Common HTTP Status Codes
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Invalid input
- `401 Unauthorized` - Authentication failure
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

### Exception Handling

**Controller-Level**:
```java
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    try {
        LoginResponse response = loginService.authenticate(
            request.getUsername(), 
            request.getPassword()
        );
        return ResponseEntity.ok(response);
    } catch (InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(Map.of("error", "Invalid username or password"));
    } catch (Exception e) {
        logger.error("Login error", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Internal server error"));
    }
}
```

**TODO**: Implement `@ControllerAdvice` for global exception handling

---

## 🚀 Performance & Optimization

### Database Indexing

**Critical Indexes**:
```sql
CREATE INDEX idx_event_id ON results(EventId);
CREATE INDEX idx_bib ON results(Bib);
CREATE INDEX idx_category ON results(EventId, Cat);
CREATE INDEX idx_status ON results(status);
CREATE INDEX idx_rank ON results(EventId, Rank1tot);
CREATE INDEX idx_archived ON t_event(archived);
```

### Connection Pooling

**HikariCP** (default in Spring Boot):
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### Caching (Future Enhancement)

**Spring Cache**:
```java
@Cacheable(value = "categories", key = "#eventId")
public List<TEventCat> getCategoriesByEventId(String eventId) {
    return categoryRepository.findByEventId(eventId);
}
```

### Query Optimization

**Use Stored Procedures** for complex queries:
- `P_LEADERBOARD_REPORT` - Optimized leaderboard generation
- `P_ASSIGN_RANK1CAT` - Batch rank calculation

**Spring Data Projections**:
```java
public interface ParticipantSummary {
    String getBib();
    String getFirstName();
    String getLastName();
    Integer getRankCat();
}

@Query("SELECT p.bib as bib, p.firstName as firstName, ... FROM TResults p WHERE p.eventId = :eventId")
List<ParticipantSummary> findSummaryByEventId(@Param("eventId") String eventId);
```

---

## 📚 API Documentation

### Swagger UI

**Access**: http://localhost:8080/swagger-ui.html (when running)

**Features**:
- Interactive API explorer
- Try out endpoints directly
- View request/response schemas
- Authentication testing

**Annotations** (for documentation):
```java
@RestController
@RequestMapping("/race")
@Tag(name = "Race Configuration", description = "Endpoints for managing race categories")
public class RaceController {
    
    @PostMapping("/categories")
    @Operation(summary = "Get all categories for an event")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categories retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<List<RaceCategoryResponse>> getCategories(
        @RequestBody @Parameter(description = "Event ID request") EventRequest request
    ) {
        // ...
    }
}
```

---

## 🔮 Future Enhancements

1. **Password Hashing**: Use BCrypt instead of plain text
2. **JWT Tokens**: Replace session-based auth with JWT
3. **Spring Security**: Implement comprehensive security framework
4. **Caching**: Add Redis for session and result caching
5. **Message Queue**: Use RabbitMQ/Kafka for async operations
6. **Monitoring**: Add Actuator endpoints for health checks
7. **Logging**: Centralized logging with ELK stack
8. **Rate Limiting**: Prevent API abuse
9. **API Versioning**: Support multiple API versions
10. **GraphQL**: Alternative to REST for flexible queries

---

**Every Second Counts** ⏱️
