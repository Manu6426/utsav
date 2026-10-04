# Utsav — Production Blueprint

**Status:** static demo (single-file vanilla-JS + Tailwind CDN SPA, hash routes: Home, Browse, Vendor profile, Compare, Team builder, Vendor onboarding, My bookings, Occasions; 21 seed vendors, 8 categories, 14 occasions, 9 cities; all state in localStorage; one network call — an ipapi.co geo lookup; bookings are request-only "UTS-XXXXXX" references with no payment; rule-based keyword concierge, not an LLM).

**Goal:** evolve it into a real, revenue-capable marketplace with user/vendor accounts, real bookings, and real checkout — with a **Java / Spring Boot backend**, so the repo doubles as senior Java full-stack portfolio work.

**Non-goal:** this is a plan, not a build. Nothing below has been implemented.

---

## 1. Architecture

### Target shape

```
┌─────────────┐      HTTPS       ┌──────────────────────────────┐
│   Frontend  │ ──────────────▶ │  Spring Boot REST API        │
│ (static SPA │   /api/* JSON   │  Spring Security + JWT       │
│  served CDN │                 │  Spring Data JPA / Hibernate │
└─────────────┘                 └──────────────┬───────────────┘
                                               │ JDBC
                                      ┌────────▼────────┐
                                      │   PostgreSQL    │
                                      └─────────────────┘
```

- **Frontend (Phase 0–2):** keep the current SPA exactly as-is, served as static files (no rewrite required). It talks to `GET/POST /api/...` instead of localStorage. This keeps velocity high while the backend is built.
- **Frontend (later, Phase 3+):** migrate to React (Vite) when the SPA's vanilla-JS complexity becomes a drag — justified briefly: component reuse across web + Capacitor/React Native, real routing, type safety with TypeScript, and a larger hiring signal. Not a launch blocker.
- **Backend:** Spring Boot 3.x, Spring Data JPA/Hibernate, PostgreSQL 16. Layered: `controller → service → repository`, DTOs at the boundary (never expose JPA entities), MapStruct or manual mappers, Bean Validation on requests, Flyway for migrations, Springdoc OpenAPI for docs.

### Domain model (JPA entities)

| Entity | Key fields | Notes |
|---|---|---|
| `User` | id, name, email (unique), phone, passwordHash, role (`CUSTOMER`/`VENDOR`/`ADMIN`), status | Spring Security principal |
| `Vendor` | id, owner (→User), businessName, category (enum: 8 current), city (enum), bio, priceTier, rating, reviewCount, coverImageUrl, status (`PENDING`/`APPROVED`/`SUSPENDED`) | approval flow gates visibility |
| `Service` | id, vendor (→Vendor), name, description, basePrice, unit (`PER_EVENT`/`PER_HOUR`/`PER_PERSON`), durationMin, active | replaces today's per-vendor flat offering |
| `PortfolioItem` | id, vendor (→Vendor), imageUrl, caption, sortOrder | today's images/ become object storage |
| `Booking` | id, customer (→User), vendor (→Vendor), service (→Service), eventDate, slot, occasion, guestCount, reference (`UTS-XXXXXX`), status (`REQUESTED`/`CONFIRMED`/`CANCELLED`/`COMPLETED`), totalAmount | the paid unit of work |
| `Review` | id, booking (→Booking, unique), vendor, author (→User), rating 1–5, text, createdAt | one review per completed booking |
| `TeamBundle` | id, customer, name, occasion, eventDate, status; join `BundleItem(booking)` | today's "Team builder" persisted server-side |
| `Payment` | id, booking (→Booking), provider (`RAZORPAY`/`STRIPE`), providerOrderId, providerPaymentId, amount, currency, status (`CREATED`/`AUTHORIZED`/`CAPTURED`/`FAILED`/`REFUNDED`), idempotencyKey | auditable money trail |

Seed data: the current 21 vendors / 8 categories / 14 occasions / 9 cities become a Flyway migration (`V1__seed.sql`) so every environment boots with the demo dataset.

### Key REST endpoints

```
Auth
  POST /api/auth/register            {name, email, phone, password} → 201 + tokens
  POST /api/auth/login               {email, password} → access + refresh JWT
  POST /api/auth/refresh             {refreshToken} → new access token
  POST /api/auth/logout              (revoke refresh token)

Vendors (public read; vendor/admin write)
  GET  /api/vendors?category=&city=&q=&page=&size=
  GET  /api/vendors/{id}             (profile + services + portfolio + reviews)
  POST /api/vendors                  (role=VENDOR: create business profile → PENDING)
  PATCH /api/vendors/{id}            (owner or ADMIN)
  POST /api/admin/vendors/{id}/approve   (ADMIN only)

Bookings (authenticated)
  GET  /api/bookings                 (own bookings; vendor sees theirs)
  POST /api/bookings                 {serviceId, eventDate, slot, occasion, ...} → reference UTS-XXXXXX, status=REQUESTED
  POST /api/bookings/{id}/cancel

Reviews
  POST /api/bookings/{id}/reviews    (only after COMPLETED, one per booking)

Team bundles
  GET/POST /api/bundles, POST /api/bundles/{id}/items

Payments
  POST /api/payments/orders          {bookingId} → provider order (Razorpay order / Stripe PaymentIntent)
  POST /api/payments/webhook/razorpay
  POST /api/payments/webhook/stripe
  POST /api/payments/{id}/refund     (VENDOR/ADMIN)
```

Security: Spring Security filter chain, stateless JWT access tokens (15 min) + rotating refresh tokens (7–30 days, stored hashed server-side for revocation), role-based `@PreAuthorize` on endpoints, BCrypt passwords, rate-limiting on auth endpoints.

---

## 2. Billing & payments

### Providers
- **India:** Razorpay (UPI, cards, netbanking, wallets) — the primary market implied by the vendor dataset.
- **US:** Stripe (cards; Apple/Google Pay via Payment Element).
- **PCI scope:** use **provider-hosted checkout only** — Razorpay Checkout.js / Stripe Checkout or Payment Element. Card data never touches Utsav servers → SAQ-A level scope, no card storage, no PCI audit burden.

### Booking payment flow
1. Customer creates booking → `REQUESTED`, slot **soft-reserved** (15-minute hold, `reservedUntil` timestamp).
2. `POST /api/payments/orders` → backend creates a Razorpay order *or* Stripe PaymentIntent (amount from `Service.basePrice` + fees; **never trust the client-sent amount**), stores `Payment` row with `CREATED` status + idempotency key.
3. Frontend completes payment in provider-hosted UI.
4. Provider fires **webhook** → backend verifies signature (Razorpay HMAC-SHA256 / Stripe signature header), marks payment `CAPTURED`, booking → `CONFIRMED`, releases/extends hold, sends confirmation SMS/email.
5. If the hold expires unpaid → scheduled job cancels the `REQUESTED` booking and frees the slot.

### Webhook handling (do it right the first time)
- Idempotent consumers: `providerPaymentId` unique constraint; duplicate deliveries are no-ops.
- Verify signatures **before** any state change; reject-and-log on mismatch.
- Persist raw webhook payloads (`webhook_event` table) for replay/debugging.
- Return 200 fast; do heavy work (notifications, analytics) async via `@Async` or a queue.

### Refunds
- `POST /api/payments/{id}/refund` (vendor or admin) → provider refund API → payment `REFUNDED`, booking `CANCELLED`.
- Policy decision needed: cancellation windows (e.g., full refund >72h, 50% 24–72h, none <24h) — encode as a `RefundPolicy` config, not code branches.

---

## 3. Auth (replacing localStorage/demo state)

- **Spring Security + JWT**, replacing today's anonymous localStorage state entirely.
- Roles: `CUSTOMER`, `VENDOR`, `ADMIN`.
- Registration: email + phone (OTP via SMS provider — MSG91/Twilio — for Indian numbers), BCrypt-hashed passwords.
- **Vendor onboarding approval flow** (replaces the local form at `#/onboard`): vendor submits business profile + documents → status `PENDING` → admin reviews in an admin dashboard → `APPROVED` (vendor goes live) or rejected with reason. Audit-log every transition.
- Session hygiene: access token 15 min, refresh token rotation with reuse detection, logout revokes refresh token server-side, password reset via time-limited emailed token.
- Migrate "My bookings" and "Team builder" to server-side ownership: on first login, offer a one-time import of the user's localStorage bookings keyed by phone number match (with explicit consent), then delete local state.

---

## 4. Hosting options (with trade-offs & rough costs)

| Option | Frontend | Backend + DB | Rough cost | Trade-offs |
|---|---|---|---|---|
| **Render (recommended start)** | Static site — free | Web service ~$7/mo + Postgres ~$7/mo (or $6 managed) | **~$13–15/mo** | Zero-DevOps, deploys from GitHub, free TLS; sleeps on free tier (use paid for prod); limited regions |
| **Railway** | Static — free tier | ~$5–10/mo usage-based + Postgres | **~$10–20/mo** | Great DX, generous free tier; costs creep with traffic |
| **Fly.io** | Static | 1 shared VM ~$2–5/mo + Postgres ~$5–7/mo | **~$7–12/mo** | Cheapest real option; more config; great global edge story later |
| **AWS (later)** | S3 + CloudFront | ECS/Fargate or EC2 + RDS Postgres | **~$40–80/mo** | Full control, real prod story for interviews; overkill before revenue; you manage everything |

Recommendation: **frontend on Vercel/Netlify (free, CDN, instant previews); backend + Postgres on Render paid tier (~$14/mo)** until monthly revenue justifies AWS. Keep everything behind environment variables from day one so the move is a config change, not a rewrite. Dockerize the backend (`Dockerfile` + `docker-compose.yml` with Postgres) so it runs identically everywhere.

---

## 5. Phone app path

### Step 1 — PWA first (fastest, days not weeks)
Checklist to add to the current SPA:
- [ ] `manifest.json` (name, icons 192/512, theme colors, `display: standalone`)
- [ ] Service worker (Workbox): app-shell caching, stale-while-revalidate for `/api/vendors*` reads, offline fallback page
- [ ] Installability: served over HTTPS, maskable icons, `beforeinstallprompt` handler with a custom "Install app" nudge
- [ ] Push notifications: Web Push (VAPID) wired to booking confirmations/reminders — backend stores subscriptions
- [ ] iOS caveats documented: no push on iOS < 16.4, "Add to Home Screen" flow instead of install prompt

### Step 2 — Capacitor wrapper (weeks)
- Wrap the *same* web app with Capacitor: `npx cap init`, native shell for iOS/Android, App Store / Play Store presence.
- Add only the native plugins the PWA can't do well: camera (portfolio uploads), native share sheet, deep links (`utsav://booking/{id}`), biometric login for the JWT refresh flow.
- One codebase, two stores — the right 80/20.

### Step 3 — Native (later phase, only if traction demands it)
- **React Native** if the frontend migrated to React (shared components/logic); **Flutter** if starting fresh or the team prefers Dart.
- Honest note: native only earns its cost with heavy camera/maps/background-sync needs or app-store featuring ambitions. Defer until revenue or funding says otherwise.

---

## 6. Repo restructuring checklist

Target layout (reads as senior Java full-stack work):

```
utsav/
├── backend/
│   └── src/main/java/com/utsav/
│       ├── config/          (Security, CORS, Jackson, OpenAPI)
│       ├── controller/      (Auth, Vendor, Booking, Payment, Admin…)
│       ├── service/         (business logic; interfaces + impl)
│       ├── repository/      (Spring Data JPA interfaces)
│       ├── domain/          (JPA entities)
│       ├── dto/             (request/response records)
│       ├── security/        (JWT filter, UserDetailsService)
│       ├── webhook/         (Razorpay/Stripe signature verification)
│       └── exception/       (GlobalExceptionHandler → RFC 7807 problem+json)
│   └── src/main/resources/db/migration/   (Flyway: V1__seed.sql from today's seed vendors)
│   └── src/test/java/...    (JUnit 5 + Mockito unit tests; Testcontainers Postgres integration tests)
├── frontend/                (current SPA moved out of root; later: Vite React app)
├── docs/                    (this file, ADRs, API guide)
├── docker-compose.yml       (api + postgres, one command local boot)
├── .env.example             (DB_URL, JWT_SECRET, RAZORPAY_KEY_*, STRIPE_* — never commit real secrets)
├── .github/workflows/ci.yml (build + test + Docker image on every push)
└── README.md                (badges: build, coverage; what/why, quickstart, architecture diagram, API docs link)
```

Checklist:
- [ ] Move `index.html` + `images/` into `frontend/`; add `backend/` Spring Boot skeleton (Spring Initializr: web, security, data-jpa, validation, flyway, postgres, testcontainers).
- [ ] README: badges (CI, license), 60-second `docker compose up` quickstart, architecture diagram, link to Swagger UI, honest "demo → production" roadmap pointer to this doc.
- [ ] API docs: Springdoc OpenAPI — every controller annotated; Swagger UI served at `/swagger-ui.html` in dev/staging.
- [ ] Env config: 12-factor via `application.yml` + env vars; `.env.example` documents every variable; CI fails if a required var is undocumented.
- [ ] Tests: unit (services with Mockito, target ≥70% on `service/`), integration (`@DataJpaTest` + Testcontainers Postgres for repositories; `@SpringBootTest` for the booking→payment→webhook happy path). CI runs the full suite on PRs.
- [ ] Seed migration: convert today's 21 vendors/8 categories/14 occasions/9 cities into `V1__seed.sql` (Flyway) — deterministic demo data in every environment.
- [ ] Branch protection on `main`: require CI green + 1 review.

---

## 7. Phased plan & effort

| Phase | Scope | Rough effort* |
|---|---|---|
| **Phase 0 — Restructure + docs (1–2 weeks)** | Repo layout above, README, `.env.example`, `docker-compose.yml`, CI skeleton, Flyway seed migration from current vendors, OpenAPI stub | 1–2 weeks solo |
| **Phase 1 — Core API + Postgres + auth (4–6 weeks)** | Spring Boot backend, domain model, vendor/booking/review/bundle CRUD, Spring Security + JWT + roles, vendor approval flow, frontend switched from localStorage to `/api/*` | 4–6 weeks solo (the big lift) |
| **Phase 2 — Payments + real booking (3–4 weeks)** | Razorpay + Stripe integration, payment intent/order flow, webhook handlers (idempotent, signature-verified), hold-expiry job, refunds, confirmation notifications | 3–4 weeks solo |
| **Phase 3 — PWA + hosting hardening (2–3 weeks)** | Manifest + service worker + Web Push, frontend on Vercel, backend + Postgres on Render, staging/prod envs, backups, basic observability (Sentry + Actuator/Prometheus) | 2–3 weeks solo |
| **Phase 4 — Native app (later)** | Capacitor wrapper → stores; React Native/Flutter only on traction | 4–8 weeks when justified |

\*Solo part-time estimates for an experienced Java dev; halve with full-time focus.

**Sequencing logic:** Phase 0 makes the repo interview-ready immediately (structure signals seniority before any feature lands). Phase 1 is the point of no return — real data, real users. Phase 2 is where money moves, so it gets the most testing rigor (webhook replay tests, refund policy tests). Phase 3 is polish + ops. Phase 4 waits for users to demand it.

**Biggest risks, stated plainly:** (1) payment webhooks are the #1 source of "money taken, booking lost" bugs — invest in idempotency + payload logging; (2) vendor supply is harder than software — the approval flow means nothing without vendors to approve; (3) scope creep on the frontend rewrite — ship the SPA against the real API first, React later.
