# VitalsAI

**VitalsAI** is an event-driven personal health analytics system that tracks blood pressure, medication intake, and exercise. It integrates with LLMs (Large Language Models) to discover personalized trends and insights over time.

## ✨ Features

- RESTful API for logging vitals, medication, and activity
- Event-driven architecture using Kafka
- LLM integration for trend summarization and health recommendations
- Modular Python- and Go-based consumers for insight generation
- Angular frontend (optional) for tracking and visualization

## 🏗️ Architecture Overview

```text
┌────────────────────────────┐
│        Angular UI          │
│  (Vitals, Meds, Insights)  │
└────────────┬───────────────┘
             │ REST
┌────────────▼───────────────┐
│  Spring Boot REST API      │◄──────┐
│  - CRUD for Vitals, Meds   │       │
│  - Expose summaries        │       │
│  - Publishes Kafka Events  │       │
└────────────┬───────────────┘       │
             │ Kafka Event Bus       │
┌────────────▼────────────────┐      │
│       Event Processor       │      │
│  - Consumes events          │      │
│  - Correlates time-series   │      │
│  - Builds prompts           │      │
│  - Calls OpenAI API         │      │
│  - Stores insights          │──────┘
└─────────────────────────────┘
```

```text
                             +---------------------+
                             | Angular Frontend    |
                             +---------------------+
                                       │
                     +------------------------------------+
                     | Spring Boot REST API (Vitals/Meds) |
                     +------------------------------------+
                            │                   │
                   Kafka Event Bus       REST: GET /history
                            │                   │
            +---------------------------+       │
            | Go-based producers        |       │
            +---------------------------+       │
                            │                   ▼
            +-------------------------------+   +----------------------------+
            | Python-based LLM consumer     |   | Optional: Python LLM API   |
            | - Time correlation + prompts  |   | for on-demand AI summaries |
            | - OpenAI/GPT/Claude integration|   +----------------------------+
            +-------------------------------+

```

## 📦 Components

### 🔷 Spring Boot REST API (Java)

- Endpoints for:
  - `POST /vitals`
  - `POST /meds`
  - `POST /activity`
  - `GET /insights`
- Publishes events to Kafka topics:
  - `vitals-recorded`
  - `meds-taken`
  - `activity-logged`

### 🔶 Python-based Kafka Consumer + LLM

- Listens on above topics
- Buffers recent events (7–30 days)
- Correlates and generates prompts
- Calls OpenAI GPT-4o (or Claude)
- Sends results back to:
  - `insight-generated` topic
  - Spring Boot API via REST (optional)

### 🟢 Angular Frontend (Optional)

- Forms to enter data
- Dashboard to view history and insights
- Optional charting with ngx-charts or Chart.js

## ⚙️ Tech Stack

| Layer          | Technology         |
| -------------- | ------------------ |
| Frontend       | Angular            |
| REST API       | Spring Boot (Java) |
| Eventing       | Kafka              |
| Processing     | Go                 |
| AI             | Python / OpenAI / LangChain |
| Storage        | PostgreSQL         |
| Infrastructure | Docker, Kubernetes |

## 🔮 Future Enhancements

- Add vector store (Qdrant) for long-term similarity queries
- Use Prometheus & Grafana for observability
- Multi-user support & OAuth2 login
- Alerting or recommendation engine

## 🧪 Getting Started (Planned)

- `docker-compose` for Kafka, DB, API, consumer
- `scripts/dev-seed.sh` to simulate vitals + meds
- Frontend at `localhost:4200`, API at `localhost:8080`

---

> This project combines real-time analytics, AI, and modern cloud-native architecture. Built for exploration, insight, and extensibility.

---
 
## ETL (Extract/Transform/Load) Pipeline:
### Extraction Phase:

- **Device synchronization**: Pull data from wearables, smart scales, blood pressure monitors via APIs or direct connections
- **Manual entry processing**: Handle user-input data with validation and standardization
- **Temporal alignment**: Ensure all data points have consistent timestamp formats and timezone handling
- **Data completeness checks**: Identify missing readings or gaps in continuous monitoring

### Transformation Phase:

- **Unit standardization**: Convert between metric/imperial, normalize measurement units across devices
- **Outlier detection**: Flag physiologically impossible readings (BP over 300, heart rate under 30, etc.)
- **Smoothing and filtering**: Apply moving averages to noisy sensor data while preserving meaningful spikes
- **Feature engineering**: Calculate derived metrics like BP variability, sleep efficiency, exercise intensity zones
- **Temporal aggregation**: Create hourly, daily, weekly summaries from raw measurements
- **Contextual enrichment**: Add metadata like day of week, season, work vs. weekend categorization

### Loading Phase:

- **Duplicate handling**: Prevent reprocessing of already-stored readings
- **Backfill operations**: Handle historical data imports and retroactive corrections
- **Real-time streaming**: Process live data feeds with minimal latency
- **Data validation**: Final quality checks before storage commitment

