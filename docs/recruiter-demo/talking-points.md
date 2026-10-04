# Utsav — Recruiter Talking Points

## 2-minute pitch script

> "Utsav is a celebration vendor marketplace I designed and built — think of it as a one-stop shop for booking decorators, photographers, caterers, DJs and makeup artists for Indian, American, and multicultural celebrations.
>
> The idea came from watching how painful event planning is: you're bouncing between Instagram pages, referrals and spreadsheets, with no way to compare vendors side by side or keep the whole plan inside one budget.
>
> So I built a single-page web app with four core flows. First, an AI-style concierge — you type something like 'wedding photographer in Boston under three thousand,' and a rule-based matching engine pulls out the occasion, city and budget, then scores vendors by rating, verification and budget fit. It's deterministic, runs entirely in the browser, no LLM cost.
>
> Second, a Budget Freeze feature: you set a total budget once and every browse, filter and bundle stays inside it — it persists across sessions.
>
> Third, a team builder that bundles up to three vendors into one event team with a live combined price. And fourth, a booking flow that issues a confirmation reference number.
>
> Today it's a front-end demo — a single-file vanilla-JS app with Tailwind, hash routing, 21 seeded vendors across 8 categories, 9 cities, and all state in localStorage. It's live and fully interactive.
>
> The production plan — and this is where my Java background comes in — is a Spring Boot REST API backed by PostgreSQL: real vendor accounts, JWT auth, bookings persisted server-side, and Stripe payments. I deliberately built the front end's data model so it maps one-to-one onto JPA entities, so the backend is a migration of proven logic, not a rewrite."

---

## Likely recruiter questions & suggested answers

**1. "How does the concierge work — is it actually AI?"**
Honest answer: "It's a rule-based matching engine, not an LLM. A keyword parser extracts occasion, city, budget, currency and guest count from plain-language input, then scores vendors on rating, verification status, city match and budget fit. I call it an AI concierge in the UI because that's the user-facing concept, but I'd never claim it's machine learning — it's deterministic, instant, and costs nothing to run. In production I'd consider an LLM layer for query understanding, with the scorer as a fallback."

**2. "Where's the backend?"**
"There isn't one yet — that's the honest state. It's a front-end SPA demo: all state lives in localStorage, and the only network call is an optional geo lookup. The production backend is the planned next phase: a Spring Boot REST API with PostgreSQL, which is exactly the stack I'm targeting professionally. The front-end data model was designed so vendors, bookings and teams map directly to JPA entities."

**3. "Why build the front end first instead of starting with the backend?"**
"To validate the product before committing to a schema. The interesting problems in Utsav are interaction problems — does the concierge matching feel smart, does Budget Freeze actually help planning, does the team builder make sense. I wanted real user interactions with those flows first, so the backend I build next serves proven logic instead of guesses."

**4. "How would you add payments?"**
"Stripe. At booking time I'd collect a deposit rather than the current 'No payment due now' placeholder. On the backend, a booking state machine — pending → confirmed → completed — driven by Stripe webhooks, with idempotency keys so retries never double-charge. The UTS reference number the app already issues would become the idempotency anchor."

**5. "How would you handle scale?"**
"Today there's nothing to scale — it's a static file. For production: the API stateless behind a load balancer, PostgreSQL with indexes on city/category/price, Redis caching for hot browse queries, and a CDN for vendor images. The read-heavy browse path and write-heavy booking path scale independently. And I'd add rate limiting on the concierge endpoint so a scraper can't hammer the matcher."

**6. "What about double-booking — two people booking the same vendor for the same slot?"**
"Right now it can't happen because there's no shared state — but it's the first thing the backend has to solve. I'd use optimistic locking on the slot record: version the availability row, and if two transactions collide, one retries with a friendly 'that slot was just taken' message. Date/slot availability becomes a first-class entity, not a form field."

**7. "How do vendors get on the platform? Who verifies them?"**
"Today there's a vendor onboarding form that submits a listing for review — in the demo it's stored locally. In production, that's a moderation queue: vendors create accounts, submit listings with portfolio images, and an admin approves them before they go live. The 'verified' badge you see in the demo would be backed by real identity and business checks."

**8. "Why vanilla JS instead of React?"**
"Pragmatism for a single-file demo — zero build step, deploys as one static file to GitHub Pages, and the whole app is readable in one sitting. The trade-off is real: no component reuse, manual DOM updates. For the production build I'd use React for the front end — it's in my core stack alongside Spring Boot — and the current app's view structure already mirrors how I'd componentize it."

**9. "What's the hardest technical problem you solved?"**
"The concierge matcher, honestly. Making 'birthday decorator in Dallas under $800 for 50 guests' reliably parse into structured filters — handling currency symbols, city aliases, occasion synonyms — with plain regex and keyword tables, and then scoring vendors so the ranking feels intelligent without any ML. Deterministic, testable, and it degrades gracefully when the input is vague."

**10. "What would you do differently?"**
"Two things. First, I'd have sketched the API contract — even as OpenAPI YAML — before building the UI, so the front end calls a documented interface from day one instead of me retrofitting it. Second, I'd separate the seed data from the app logic earlier; right now vendors ship inside the single file, which is fine for a demo but the first thing to split when the backend lands."

---

## One-line closers (pick one)

- "It's a working product demo today and a designed system for tomorrow — I'd love to build the backend the right way on your team."
- "The demo proves I can ship; the roadmap proves I can design. The Spring Boot phase is next either way."
- "Happy to walk through the code — the matching engine is the part I'm proudest of."
