# Utsav — Celebration Vendor Marketplace

Utsav is a marketplace for booking celebration vendors — photographers, decorators,
caterers, makeup artists, DJs, and more — across Indian cities. Customers browse
vendors by category, city, and budget, check portfolios and reviews, and book a
date/time slot. Vendors can onboard themselves and collect ratings.

This repo is the **production rebuild** of what started as a single-file demo
(`frontend/index.html`, still shipped here as the working demo UI). Phase 1
turns it into a real Java full-stack project: a Spring Boot REST API backed by
a relational database, with the demo frontend talking to it.

> **Status:** Phase 1 — core marketplace API. Payments, authentication, and the
> mobile app are explicitly later phases (see Roadmap).

## Architecture

```
                    ┌─────────────────────────────┐
                    │  frontend/index.html        │
                    │  vanilla JS + Tailwind CDN  │
                    │  (demo UI, :8000)           │
                    └──────────────┬──────────────┘
                                   │  REST / JSON
                                   │  CORS-enabled
                    ┌──────────────▼──────────────┐
                    │  backend  (Spring Boot 3)   │
                    │  Java 17, Maven  (:8080)    │
                    │                             │
                    │  controller → service →     │
                    │  repository → entity (JPA)  │
                    └──────────────┬──────────────┘
                                   │  JDBC
                    ┌──────────────▼──────────────┐
                    │  H2 (dev, in-memory)        │
                    │  PostgreSQL (prod)          │
                    └─────────────────────────────┘
```

The backend is layered the way Spring apps are usually built, so every piece has
one job:

- `controller` — thin HTTP adapters; translate requests/responses, no business logic.
- `service` — business rules (price resolution, booking reference generation,
  rating recomputation). This is what the unit tests cover.
- `repository` — Spring Data JPA interfaces; queries derived from method names
  plus one explicit `@Query` for vendor search filters.
- `model` — JPA entities matching the demo's seed data shape.
- `dto` — request objects with Bean Validation (`@NotBlank`, `@FutureOrPresent`…).
- `exception` — `ResourceNotFoundException` → 404 plus a global handler that
  returns a consistent JSON error body (including validation failures).
- `config` — CORS (origins from `CORS_ORIGINS`) and a `CommandLineRunner` seeder
  that loads the same 21 vendors / 8 categories / 14 occasions as the demo on
  first run, so the API serves real data immediately.

## Tech stack

| Layer    | Choice |
|----------|--------|
| Backend  | Java 17, Spring Boot 3.2.5, Spring Data JPA (Hibernate) |
| Database | H2 in-memory for dev, PostgreSQL for prod (Spring profiles) |
| Build    | Maven (wrapper included — no local Maven install needed) |
| Frontend | Single `index.html`, vanilla JS, Tailwind via CDN |
| Tests    | JUnit 5 + Mockito (service layer) |

## How to run

**Backend** (from `backend/`, needs JDK 17+ — the `mvnw` wrapper fetches Maven itself):

```bash
cd backend
./mvnw spring-boot:run          # dev profile, H2, seeds data on startup
```

The API is then at `http://localhost:8080`. The H2 console (dev only) is at
`http://localhost:8080/h2-console` (JDBC URL `jdbc:h2:mem:utsavdb`, user `sa`,
empty password).

**Frontend** (any static server; it calls the API at `localhost:8080`):

```bash
cd frontend
python3 -m http.server 8000     # open http://localhost:8000
```

**Production profile** — point at a real PostgreSQL:

```bash
APP_PROFILE=prod DB_URL=jdbc:postgresql://host:5432/utsav \
  DB_USER=... DB_PASS=... CORS_ORIGINS=https://yourdomain.com \
  ./mvnw spring-boot:run
```

**Tests:**

```bash
./mvnw test
```

## API overview

All endpoints are under `/api` and speak JSON.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/vendors?category=&city=&maxBudget=` | Search vendors with optional filters |
| GET | `/api/vendors/{id}` | Vendor detail (portfolio, services, reviews) |
| POST | `/api/vendors` | Onboard a vendor (starts unverified) |
| GET | `/api/categories` | All 8 categories |
| GET | `/api/occasions` | All 14 occasions |
| POST | `/api/bookings` | Create a booking (price resolved server-side from the vendor's services; returns a `UTS-XXXXXX` reference) |
| GET | `/api/bookings` | List bookings |
| GET | `/api/bookings/{reference}` | Booking by reference |
| DELETE | `/api/bookings/{reference}` | Cancel a booking |
| GET | `/api/vendors/{id}/reviews` | Reviews for a vendor |
| POST | `/api/vendors/{id}/reviews` | Add a review (rating 1–5; vendor average recomputed) |

Errors come back as JSON, e.g. `{"status":404,"error":"Not Found","message":"Vendor not found: xyz"}`,
and validation failures as `{"status":400,"error":"Bad Request","message":"date: Booking date cannot be in the past"}`.

### Quick smoke test

```bash
curl localhost:8080/api/categories | head -c 200
curl "localhost:8080/api/vendors?city=Bengaluru&maxBudget=50000" | head -c 200
curl -X POST localhost:8080/api/bookings -H 'Content-Type: application/json' -d \
  '{"vendorId":"lenscraft-studios","serviceName":"Full-day wedding","date":"2026-12-25",
    "timeSlot":"10:00-14:00","customerName":"Aarav Sharma","phone":"+919876543210"}'
```

## Domain model

- **Vendor** — natural string id (matches the demo's slugs, e.g. `lenscraft-studios`),
  name, category, city, rating, review count, verified/newcomer flags, starting
  price + currency, languages, service offerings, portfolio items.
- **ServiceOffering** — bookable service with its own price (bookings resolve the
  price server-side so clients can't tamper with it).
- **Booking** — vendor, date, time slot, customer details, generated `UTS-XXXXXX`
  reference, status (`CONFIRMED`/`CANCELLED`).
- **Review** — vendor, 1–5 rating, text, author; each new review recomputes the
  vendor's average.
- **Category / Occasion** — catalog tables for browsing and filtering.

## Roadmap

- **Phase 2** — authentication (Spring Security + JWT), vendor login, booking
  ownership.
- **Phase 3** — payments (Razorpay/Stripe), booking confirmation flow.
- **Phase 4** — mobile app consuming the same API; Flyway migrations replacing
  `ddl-auto=update`; vendor availability calendar.

## Repo layout

```
utsav/
├── frontend/      # demo UI: index.html + images/ (as originally built)
├── backend/       # Spring Boot API (this phase's main work)
│   ├── pom.xml / mvnw
│   └── src/main/java/com/utsav/{controller,service,repository,model,dto,config,exception}
├── docs/          # planning notes (app comparison, production plan)
└── README.md
```
