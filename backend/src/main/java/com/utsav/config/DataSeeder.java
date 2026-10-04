package com.utsav.config;

import com.utsav.model.Category;
import com.utsav.model.Credential;
import com.utsav.model.Occasion;
import com.utsav.model.PortfolioItem;
import com.utsav.model.Review;
import com.utsav.model.ServiceOffering;
import com.utsav.model.Vendor;
import com.utsav.repository.CategoryRepository;
import com.utsav.repository.OccasionRepository;
import com.utsav.repository.ReviewRepository;
import com.utsav.repository.VendorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds the database with the same catalog the frontend demo ships with:
 * 8 categories, 14 occasions and 21 vendors, generated from frontend/index.html
 * so the API and the demo never drift apart. Runs once, only on an empty database.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categories;
    private final OccasionRepository occasions;
    private final VendorRepository vendors;
    private final ReviewRepository reviews;

    public DataSeeder(CategoryRepository categories, OccasionRepository occasions,
                      VendorRepository vendors, ReviewRepository reviews) {
        this.categories = categories;
        this.occasions = occasions;
        this.vendors = vendors;
        this.reviews = reviews;
    }

    @Override
    public void run(String... args) {
        if (vendors.count() > 0) {
            return; // already seeded
        }
        seedCategories();
        seedOccasions();
        seedVendors();
    }

    private void seedCategories() {
        categories.save(new Category("decorators", "Decorators", "images/cat-decorator.jpg", "Mandaps, stages & party decor"));
        categories.save(new Category("makeup", "Makeup Artists", "images/cat-makeup.jpg", "Bridal & party glam"));
        categories.save(new Category("mehendi", "Mehendi Artists", "images/cat-mehendi.jpg", "Bridal henna & party designs"));
        categories.save(new Category("photographers", "Photographers", "images/cat-photographer.jpg", "Candid & cinematic stories"));
        categories.save(new Category("choreographers", "Choreographers", "images/cat-choreographer.jpg", "Sangeet dance trainers"));
        categories.save(new Category("hosts", "Event Hosts & MCs", "images/cat-host.jpg", "Anchors who own the room"));
        categories.save(new Category("djs", "DJs", "images/cat-dj.jpg", "Dance floors that never empty"));
        categories.save(new Category("caterers", "Caterers", "images/cat-caterer.jpg", "Feasts for every guest list"));
    }

    private void seedOccasions() {
        occasions.save(new Occasion("wedding", "Wedding", "images/hero.jpg", "The big day, done right — every vendor you need, one shortlist."));
        occasions.save(new Occasion("sangeet", "Sangeet", "images/port-sangeet.jpg", "Dance rehearsals, dhol energy, and a night nobody forgets."));
        occasions.save(new Occasion("mehendi", "Mehendi", "images/cat-mehendi.jpg", "Morning henna rituals with artists who paint stories."));
        occasions.save(new Occasion("birthday", "Birthday", "images/cat-birthday.jpg", "First birthdays to fiftieths — decor, cake tables & fun."));
        occasions.save(new Occasion("bachelor", "Bachelor / Bachelorette", "images/cat-dj.jpg", "One last wild night — DJs, venues, and zero regrets."));
        occasions.save(new Occasion("babyshower", "Baby Shower", "images/port-table.jpg", "Seemantham or sprinkle — soft pastels, sweet setups."));
        occasions.save(new Occasion("bridalshower", "Bridal Shower", "images/port-bridal.jpg", "Brunch, glam, and the bride's favorite people."));
        occasions.save(new Occasion("anniversary", "Anniversary", "images/port-prewedding.jpg", "Silver, pearl, golden — celebrate the years in style."));
        occasions.save(new Occasion("graduation", "Graduation", "images/cat-photographer.jpg", "Caps off — portraits and parties for the grad."));
        occasions.save(new Occasion("housewarming", "Housewarming", "images/cat-decorator.jpg", "Griha pravesham or new-home party — bless it beautifully."));
        occasions.save(new Occasion("halloween", "Halloween Party", "images/port-sangeet.jpg", "Haunted setups, spooky tables, killer playlists."));
        occasions.save(new Occasion("christmas", "Christmas & New Year", "images/cat-decorator.jpg", "Trees, lights, and midnight countdowns."));
        occasions.save(new Occasion("corporate", "Corporate Event", "images/cat-host.jpg", "Offsites and galas with hosts who keep it sharp."));
        occasions.save(new Occasion("prewedding", "Pre-wedding Shoot", "images/port-prewedding.jpg", "Golden-hour portraits before the chaos begins."));
    }

    private void seedVendors() {
        seedVendor("meera", "Meera's Marigold Events", "decorators", "Houston", "USA", "$",
                4.9, 132, 1200, true, false,
                "Mandaps that smell like home.", "Meera has dressed 300+ mandaps across Texas over 9 years. Her team grows its own marigolds — yes, really — and builds stages that photograph like a dream. Famous for calm, on-time setups even when the baraat runs two hours late.",
                List.of("English", "Hindi", "Telugu"),
                List.of("wedding", "sangeet", "mehendi", "housewarming", "prewedding"),
                List.of(new String[]{"Full mandap decor", "1200", "event"}, new String[]{"Sangeet stage", "900", "event"}, new String[]{"Mehendi morning setup", "650", "event"}, new String[]{"Floral entryway", "300", "setup"}, new String[]{"Photobooth corner", "450", "event"}),
                List.<String[]>of(
                new String[]{"images/hero.jpg", "Grand floral mandap", "A 400-flower mandap built for a Houston wedding — marigolds grown by Meera's own team."},
                new String[]{"images/cat-decorator.jpg", "Stage draping", "Layered drapes and florals dressed for a sangeet stage."},
                new String[]{"images/port-mandap.jpg", "Mandap canopy detail", "Close-up of the marigold canopy work."},
                new String[]{"images/port-table.jpg", "Reception table styling", "Guest tables dressed for a 150-guest reception."},
                new String[]{"images/port-sangeet.jpg", "Sangeet stage", "Full-stage florals for a family sangeet night."}),
                List.<String[]>of(new String[]{"Specialty", "Home-grown marigold farm - 300+ mandaps dressed"}, new String[]{"Course", "Floral Design Intensive, 2019"}),
                List.of(new String[]{"Ananya R.", "5", "Sep 2026", "The mandap made my mother cry. Meera's team set up 400 flowers while we were still at the temple. Flawless."}, new String[]{"Divya S.", "5", "Aug 2026", "On time, on budget, and the stage looked straight out of a movie. Worth every dollar."}, new String[]{"Kavya M.", "4", "Jun 2026", "Gorgeous work. One backdrop panel arrived late but they fixed it before guests walked in."}, new String[]{"Rohit P.", "5", "May 2026", "They handled my sister's sangeet stage overnight. Magicians, honestly."}),
                List.of("lens", "priya"));

        seedVendor("priya", "Glam by Priya", "makeup", "New York", "USA", "$",
                4.8, 214, 350, true, false,
                "Red lips approved by 200+ brides.", "Priya is a certified pro artist who specializes in South Asian bridal glam — the kind that lasts through pheras, a hundred hugs, and happy tears. Trial sessions at her Queens studio.",
                List.of("English", "Hindi", "Tamil"),
                List.of("wedding", "sangeet", "prewedding", "bridalshower", "anniversary"),
                List.of(new String[]{"Bridal makeup + hair", "350", "person"}, new String[]{"Party makeup", "120", "person"}, new String[]{"Makeup trial", "80", "session"}, new String[]{"Groom styling", "90", "person"}),
                List.<String[]>of(
                new String[]{"images/cat-makeup.jpg", "Bridal trial look", "Soft-glam trial at the Queens studio."},
                new String[]{"images/port-bridal.jpg", "Wedding-day glam", "The finished bridal look, built to last 14 hours."},
                new String[]{"images/port-mehendi-detail.jpg", "Detail finishing", "Precision detail work for a party look."}),
                List.<String[]>of(new String[]{"Certification", "Lakme Salon Certified Makeup Artistry, 2021"}, new String[]{"Course", "Airbrush Techniques Masterclass, 2023"}, new String[]{"Products used", "MAC, Huda Beauty & Charlotte Tilbury"}),
                List.of(new String[]{"Sneha K.", "5", "Sep 2026", "My makeup survived 14 hours, three outfit changes, and my crying. Priya is a miracle worker."}, new String[]{"Lakshmi V.", "5", "Jul 2026", "She understood 'soft but make it bridal' perfectly. Trial was so thorough."}, new String[]{"Anitha D.", "4", "May 2026", "Beautiful work, slightly rushed during the big-morning rush — book the trial."}, new String[]{"Farah N.", "5", "Apr 2026", "Did my whole bridal party of six. Everyone looked like themselves, just luminous."}),
                List.of("aisha"));

        seedVendor("aisha", "Henna by Aisha", "mehendi", "Bay Area", "USA", "$",
                5.0, 6, 80, true, true,
                "Bridal henna, party henna, tiny tattoos.", "Aisha learned henna from her grandmother in Hyderabad and turned a dorm-room side hustle into the Bay Area's most-booked newcomer. Intricate bridal work in under four hours, organic cones only.",
                List.of("English", "Hindi", "Urdu"),
                List.of("mehendi", "wedding", "sangeet", "bridalshower", "birthday"),
                List.of(new String[]{"Bridal henna (both hands)", "220", "person"}, new String[]{"Party henna", "80", "person"}, new String[]{"Kids' henna motifs", "40", "person"}),
                List.<String[]>of(
                new String[]{"images/cat-mehendi.jpg", "Bridal henna", "Full bridal henna, organic cones only."},
                new String[]{"images/port-mehendi-detail.jpg", "Henna detail", "Close-up of Aisha's fine-line bridal work."}),
                List.<String[]>of(new String[]{"Certification", "Organic Henna Safety & Design Course, 2022"}, new String[]{"Products used", "100% organic, chemical-free henna cones"}),
                List.of(new String[]{"Zara H.", "5", "Sep 2026", "The stain was DARK for a week. My bridal henna got more compliments than my lehenga."}, new String[]{"Priya N.", "5", "Aug 2026", "Booked her for my sister's mehendi morning — fast, sweet, insanely detailed."}, new String[]{"Meera J.", "5", "Jul 2026", "Newcomer prices, veteran skills. Get her before she raises rates!"}),
                List.of("priya"));

        seedVendor("lens", "Lens & Light Studio", "photographers", "Dallas", "USA", "$",
                4.9, 98, 1500, true, false,
                "Candid first, posed never.", "Husband-wife duo Arjun and Sara shoot 40+ Indian weddings a year. Two shooters, a same-day teaser reel, and a gallery your grandchildren will fight over.",
                List.of("English", "Hindi"),
                List.of("wedding", "prewedding", "sangeet", "anniversary", "birthday"),
                List.of(new String[]{"Full-day wedding (2 shooters)", "1500", "event"}, new String[]{"Pre-wedding shoot", "600", "session"}, new String[]{"Sangeet coverage", "800", "event"}, new String[]{"Teaser reel", "300", "add-on"}),
                List.<String[]>of(
                new String[]{"images/cat-photographer.jpg", "Wedding day coverage", "Candid coverage by Arjun and Sara."},
                new String[]{"images/port-prewedding.jpg", "Pre-wedding shoot", "Golden-hour portraits before the big day."},
                new String[]{"images/port-bridal.jpg", "Bridal portraits", "Quiet bridal portraits between ceremonies."}),
                List.<String[]>of(new String[]{"Equipment", "Sony A7IV x 2 - 35mm & 85mm f/1.4 GM"}, new String[]{"Certification", "FAA Part 107 Drone License"}, new String[]{"Course", "Fearless Photographers Workshop, 2022"}),
                List.of(new String[]{"Vikram T.", "5", "Sep 2026", "They caught my dad's expression when he saw my bride. I cried watching the teaser."}, new String[]{"Nisha R.", "5", "Aug 2026", "Invisible during the ceremony, everywhere that mattered. 900 stunning photos."}, new String[]{"Aditi L.", "4", "Jun 2026", "Gorgeous gallery. Delivery took 5 weeks, but worth the wait."}),
                List.of("meera"));

        seedVendor("natya", "Natya Beats", "choreographers", "Boston", "USA", "$",
                4.9, 8, 60, true, true,
                "Your cousins WILL nail the hook step.", "Classically trained in Bharatanatyam, fluent in Bollywood. Natya Beats has taught 200+ family members their sangeet routines — including one very reluctant chacha who now won't sit down.",
                List.of("English", "Hindi", "Tamil"),
                List.of("sangeet", "wedding", "birthday", "anniversary"),
                List.of(new String[]{"Sangeet choreography (4 sessions)", "240", "package"}, new String[]{"Couple's first dance", "180", "package"}, new String[]{"Kids' group number", "150", "package"}, new String[]{"Hourly rehearsal", "60", "hour"}),
                List.<String[]>of(
                new String[]{"images/cat-choreographer.jpg", "Sangeet rehearsal", "Teaching a 14-person family medley."},
                new String[]{"images/port-sangeet.jpg", "Stage rehearsal", "Final run-through before sangeet night."}),
                List.<String[]>of(),
                List.of(new String[]{"Deepa K.", "5", "Sep 2026", "She taught 14 of us — ages 8 to 68 — a 4-minute medley in 4 sessions. Standing ovation."}, new String[]{"Sanjay M.", "5", "Aug 2026", "Patient, funny, and our sangeet video looks professional."}, new String[]{"Ritu S.", "4", "Jul 2026", "Great with kids. Wish we'd booked more sessions!"}),
                List.of("arjun"));

        seedVendor("arjun", "MC Arjun Rao", "hosts", "New York", "USA", "$",
                4.8, 76, 400, true, false,
                "Bilingual banter, zero awkward silences.", "Arjun has hosted 150+ sangeets and receptions across the tri-state. English-Hindi-Telugu on the mic, games the elders actually enjoy, and timelines that stay on track.",
                List.of("English", "Hindi", "Telugu"),
                List.of("sangeet", "wedding", "corporate", "birthday", "anniversary"),
                List.of(new String[]{"Sangeet hosting (4 hrs)", "400", "event"}, new String[]{"Wedding reception MC", "550", "event"}, new String[]{"Corporate anchor", "600", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-host.jpg", "Sangeet hosting", "On the mic at a 150-guest sangeet."},
                new String[]{"images/port-sangeet.jpg", "Reception stage", "Hosting a wedding reception."}),
                List.<String[]>of(),
                List.of(new String[]{"Kiran B.", "5", "Sep 2026", "He had both families laughing within ten minutes. The dumb charades round was legendary."}, new String[]{"Shreya P.", "5", "Jul 2026", "Professional, punctual, and kept our 6-hour sangeet moving perfectly."}, new String[]{"Amit J.", "4", "May 2026", "Great energy. Book early — he's in demand."}),
                List.of("natya"));

        seedVendor("everafter", "EverAfter Decor", "decorators", "Bay Area", "USA", "$",
                5.0, 5, 600, true, true,
                "Pinterest boards, but real.", "Two best friends, one storage unit full of drapes, and five flawless events. EverAfter does modern Indian decor — think pampas grass meets marigold, at newcomer prices.",
                List.of("English", "Hindi"),
                List.of("wedding", "birthday", "bridalshower", "babyshower", "anniversary"),
                List.of(new String[]{"Full venue decor", "600", "event"}, new String[]{"Balloon + floral arch", "250", "setup"}, new String[]{"Table styling (10 tables)", "350", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-decorator.jpg", "Backyard wedding", "A full backyard transformed for a wedding."},
                new String[]{"images/port-table.jpg", "Table styling", "Styled tables for 60 guests."},
                new String[]{"images/cat-birthday.jpg", "Birthday setup", "A 40th birthday install."}),
                List.<String[]>of(),
                List.of(new String[]{"Neha G.", "5", "Sep 2026", "Our backyard wedding looked like a venue. They worked with our tiny budget so kindly."}, new String[]{"Sara F.", "5", "Aug 2026", "The arch was all over Instagram. Setup and teardown were silent and fast."}),
                List.of());

        seedVendor("glowcraft", "GlowCraft Artistry", "makeup", "Houston", "USA", "$",
                4.9, 7, 180, true, true,
                "Soft glam that survives happy tears.", "Nisha worked backstage at fashion week before moving to Houston. Airbrush bridal looks at prices that don't need a family meeting — and lashes that stay put.",
                List.of("English", "Hindi", "Gujarati"),
                List.of("wedding", "bridalshower", "prewedding", "birthday", "graduation"),
                List.of(new String[]{"Bridal airbrush", "180", "person"}, new String[]{"Engagement look", "120", "person"}, new String[]{"Bridesmaid (per person)", "75", "person"}),
                List.<String[]>of(
                new String[]{"images/cat-makeup.jpg", "Airbrush bridal", "Airbrush bridal look, humidity-proofed."},
                new String[]{"images/port-bridal.jpg", "Engagement look", "Soft-glam engagement makeup."}),
                List.<String[]>of(new String[]{"Certification", "TEMPTU Airbrush Certified"}, new String[]{"Products used", "Kryolan & Dior Backstage kit"}),
                List.of(new String[]{"Pooja V.", "5", "Sep 2026", "Airbrush lasted 12 hours in Houston humidity. Enough said."}, new String[]{"Rina D.", "5", "Jul 2026", "So gentle and precise. My engagement photos are flawless."}),
                List.of());

        seedVendor("shubh", "Shubh Decor Studio", "decorators", "Hyderabad", "India", "₹",
                4.8, 187, 25000, true, false,
                "Big-fat-wedding decor, honest prices.", "Twelve years, 500+ weddings across Telangana and Andhra Pradesh. Shubh Decor owns its flowers, trussing, and lighting — which is why their quotes never have surprises.",
                List.of("Telugu", "Hindi", "English"),
                List.of("wedding", "sangeet", "mehendi", "housewarming"),
                List.of(new String[]{"Wedding mandap + stage", "25000", "event"}, new String[]{"Sangeet stage", "15000", "event"}, new String[]{"Home mehendi setup", "8000", "event"}, new String[]{"Car decoration", "2500", "setup"}),
                List.<String[]>of(
                new String[]{"images/hero.jpg", "Grand mandap", "A 300-guest wedding mandap in Hyderabad."},
                new String[]{"images/port-mandap.jpg", "Mandap detail", "Fresh seasonal blooms, close up."},
                new String[]{"images/cat-decorator.jpg", "Stage + florals", "Wedding stage with full floral dressing."}),
                List.<String[]>of(new String[]{"Experience", "12 years - 500+ weddings across Telangana & AP"}, new String[]{"Equipment", "In-house trussing & intelligent lighting rig"}),
                List.of(new String[]{"Sravani K.", "5", "Sep 2026", "They decorated our whole wedding in one night. My in-laws are still talking about the entrance."}, new String[]{"Harish G.", "5", "Aug 2026", "Transparent billing, no last-minute extras. Rare in this industry."}, new String[]{"Lalitha M.", "4", "Jun 2026", "Beautiful mandap. Flower freshness could be better in summer — ask for seasonal blooms."}),
                List.of("karthik"));

        seedVendor("kaya", "Kaya Kalp Makeup", "makeup", "Chennai", "India", "₹",
                4.9, 143, 8000, true, false,
                "Muhurtham-proof makeup.", "Lakshmi's HD bridal looks stay put through six-hour muhurthams and Chennai humidity. Trials at her Adyar studio include a full skin-prep consultation.",
                List.of("Tamil", "English", "Hindi"),
                List.of("wedding", "prewedding", "bridalshower"),
                List.of(new String[]{"Bridal HD makeup", "8000", "person"}, new String[]{"Reception look", "6000", "person"}, new String[]{"Trial session", "1500", "session"}),
                List.<String[]>of(
                new String[]{"images/cat-makeup.jpg", "HD bridal look", "Muhurtham-proof HD bridal makeup."},
                new String[]{"images/port-bridal.jpg", "Muhurtham glam", "The finished look, zero retouching needed."}),
                List.<String[]>of(new String[]{"Certification", "HD & Airbrush Diploma, Fat Mu Pro Academy"}, new String[]{"Products used", "MAC Pro & Kryolan Supracolor"}),
                List.of(new String[]{"Divya R.", "5", "Sep 2026", "Sweat-proof through a June muhurtham. My photos need zero retouching."}, new String[]{"Janani S.", "5", "Jul 2026", "The trial alone was worth it — she fixed my skincare routine too."}),
                List.of("ritu"));

        seedVendor("ritu", "Mehendi Magic by Ritu", "mehendi", "Bangalore", "India", "₹",
                5.0, 9, 2100, true, true,
                "Full bridal in 4 hours flat.", "Ritu does three weddings a weekend in peak season and still takes party bookings. Organic henna, deep-stain guarantee, and designs from minimal chic to full bridal.",
                List.of("Hindi", "English", "Kannada"),
                List.of("mehendi", "wedding", "sangeet"),
                List.of(new String[]{"Bridal (hands + feet)", "2100", "person"}, new String[]{"Party henna", "500", "person"}, new String[]{"Engagement motif", "800", "person"}),
                List.<String[]>of(
                new String[]{"images/cat-mehendi.jpg", "Bridal henna set", "Hands and feet, done in under four hours."},
                new String[]{"images/port-mehendi-detail.jpg", "Party henna", "Minimal-chic motifs for a sangeet morning."}),
                List.<String[]>of(),
                List.of(new String[]{"Shalini P.", "5", "Sep 2026", "Darkest stain I've ever had. She finished both hands in 3.5 hours."}, new String[]{"Nandini K.", "5", "Aug 2026", "So fast without rushing the detail. Highly recommended."}),
                List.of("kaya"));

        seedVendor("karthik", "Frames by Karthik", "photographers", "Chennai", "India", "₹",
                4.9, 11, 15000, true, true,
                "Temple-town storytelling.", "Karthik shot his first wedding on a borrowed camera; eleven 5-star reviews later, he's Chennai's worst-kept secret. Drone coverage included in every package.",
                List.of("Tamil", "English"),
                List.of("wedding", "prewedding", "housewarming"),
                List.of(new String[]{"Wedding day (1 shooter + drone)", "15000", "event"}, new String[]{"Pre-wedding (2 locations)", "9000", "session"}, new String[]{"Family portraits", "5000", "session"}),
                List.<String[]>of(
                new String[]{"images/cat-photographer.jpg", "Wedding + drone", "Full-day coverage with drone."},
                new String[]{"images/port-prewedding.jpg", "Pre-wedding shoot", "Two locations, golden light."},
                new String[]{"images/port-bridal.jpg", "Bridal portraits", "Temple-town bridal portraits."}),
                List.<String[]>of(new String[]{"Equipment", "Canon R6 - RF 28-70mm f/2"}, new String[]{"Certification", "DGCA Drone Permit"}),
                List.of(new String[]{"Arun V.", "5", "Sep 2026", "The drone shot of our temple wedding is framed in our living room now."}, new String[]{"Meenakshi D.", "5", "Jul 2026", "Unobtrusive, creative, and half the price of the big studios."}),
                List.of("shubh"));

        seedVendor("dhoom", "Dhoom Dance Crew", "choreographers", "Hyderabad", "India", "₹",
                4.8, 89, 5000, true, false,
                "Sangeet choreography in 6 sessions.", "Four dancers, one dhol player on call, and sangeet routines that uncles can actually learn. Dhoom has choreographed 200+ family performances across two states.",
                List.of("Telugu", "Hindi", "English"),
                List.of("sangeet", "wedding", "birthday"),
                List.of(new String[]{"Family sangeet (6 sessions)", "5000", "package"}, new String[]{"Bride/groom solo", "3000", "package"}, new String[]{"Flash mob", "8000", "package"}),
                List.<String[]>of(
                new String[]{"images/cat-choreographer.jpg", "Family sangeet", "Six sessions to stage-ready."},
                new String[]{"images/port-sangeet.jpg", "Stage rehearsal", "Dress rehearsal with the dhol player."}),
                List.<String[]>of(),
                List.of(new String[]{"Sandeep R.", "5", "Sep 2026", "My 60-year-old father did a hook step. Enough said."}, new String[]{"Geetha N.", "5", "Aug 2026", "Six sessions, zero stress, one unforgettable sangeet."}),
                List.of("divya"));

        seedVendor("divya", "Anchor Divya", "hosts", "Bangalore", "India", "₹",
                5.0, 4, 7000, true, true,
                "Games your athai will actually play.", "Ex-RJ Divya brings radio energy to sangeets and birthdays. Kannada, Hindi, English, Tamil — she'll get your shyest cousin on stage and keep the aunties laughing.",
                List.of("Kannada", "Hindi", "English", "Tamil"),
                List.of("sangeet", "wedding", "birthday", "corporate"),
                List.of(new String[]{"Sangeet anchoring", "7000", "event"}, new String[]{"Birthday hosting", "5000", "event"}, new String[]{"Corporate emcee", "10000", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-host.jpg", "Sangeet anchoring", "Hosting a 200-guest sangeet."}),
                List.<String[]>of(),
                List.<String[]>of(new String[]{"Rakshita S.", "5", "Sep 2026", "She handled a 300-guest sangeet like a pro. The couple's game round was hilarious."}),
                List.of("dhoom"));

        seedVendor("petals", "Petal & Pine Events", "decorators", "Chennai", "India", "₹",
                4.7, 12, 9000, true, true,
                "Birthdays, half-saree ceremonies, and small joys.", "Specialists in intimate celebrations — half-saree functions, seemanthams, first birthdays. Thoughtful, elegant decor that doesn't need a wedding budget.",
                List.of("Tamil", "English"),
                List.of("birthday", "babyshower", "housewarming", "anniversary"),
                List.of(new String[]{"Birthday setup", "9000", "event"}, new String[]{"Half-saree ceremony", "12000", "event"}, new String[]{"Seemantham decor", "10000", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-birthday.jpg", "Birthday setup", "A first-birthday celebration install."},
                new String[]{"images/port-table.jpg", "Seemantham table", "Styled table for an intimate seemantham."}),
                List.<String[]>of(),
                List.of(new String[]{"Kavitha J.", "5", "Aug 2026", "My daughter's half-saree function looked straight out of a magazine."}, new String[]{"Revathi A.", "4", "Jul 2026", "Lovely work, very warm team. Slightly delayed setup — plan buffer time."}),
                List.of());

        seedVendor("marcus", "DJ Marcus Cole", "djs", "Boston", "USA", "$",
                4.8, 87, 500, true, false,
                "From Bollywood to Bad Bunny, one dance floor.", "Marcus DJs 60+ South Asian weddings a year. Bhangra-to-Bad-Bunny transitions, dhol player on request, and a dance floor that empties only when the lights come on.",
                List.of("English"),
                List.of("wedding", "sangeet", "birthday", "halloween", "christmas", "corporate", "bachelor", "graduation"),
                List.of(new String[]{"Wedding DJ (5 hrs)", "500", "event"}, new String[]{"Sangeet DJ", "350", "event"}, new String[]{"Dhol player add-on", "250", "add-on"}, new String[]{"Halloween party set", "400", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-dj.jpg", "Wedding DJ set", "Five hours, bhangra to Bad Bunny."},
                new String[]{"images/port-sangeet.jpg", "Sangeet night", "Keeping the dance floor full till midnight."}),
                List.<String[]>of(),
                List.of(new String[]{"Jessica T.", "5", "Oct 2026", "He read our crowd perfectly — my Indian side and his American side never left the floor."}, new String[]{"Aarav M.", "5", "Sep 2026", "The bhangra-to-hip-hop transitions were unreal. Best sangeet ever."}),
                List.of("confetti"));

        seedVendor("confetti", "Confetti & Co.", "decorators", "Dallas", "USA", "$",
                4.9, 6, 250, true, true,
                "Balloon arches that break the internet.", "Started in a garage in 2024, now Dallas's go-to for birthdays and baby showers. Organic balloon garlands, neon signs, and genuine same-week availability.",
                List.of("English", "Spanish"),
                List.of("birthday", "babyshower", "bridalshower", "halloween", "graduation", "anniversary"),
                List.of(new String[]{"Balloon garland", "250", "setup"}, new String[]{"Full birthday setup", "600", "event"}, new String[]{"Neon sign rental", "120", "rental"}, new String[]{"Halloween porch", "300", "setup"}),
                List.<String[]>of(
                new String[]{"images/cat-birthday.jpg", "Birthday install", "Full birthday setup in 40 minutes."},
                new String[]{"images/port-table.jpg", "Balloon garland", "Organic balloon garland backdrop."}),
                List.<String[]>of(),
                List.of(new String[]{"Emily R.", "5", "Sep 2026", "The garland was the backdrop of every photo. Setup took 40 minutes flat."}, new String[]{"Sofia L.", "5", "Aug 2026", "Affordable, adorable, and so easy to work with."}),
                List.of("marcus"));

        seedVendor("hudson", "Hudson & Harvest", "caterers", "New York", "USA", "$",
                4.7, 112, 900, true, false,
                "Farm-to-table, chaat to charcuterie.", "A catering company that actually gets Indian weddings — live chaat counters next to a carving station. Serving 50 to 500 guests across the tri-state area.",
                List.of("English", "Hindi"),
                List.of("wedding", "corporate", "birthday", "housewarming", "christmas", "anniversary"),
                List.of(new String[]{"Wedding buffet (per 100 guests)", "900", "event"}, new String[]{"Live chaat counter", "400", "add-on"}, new String[]{"Corporate lunch", "750", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-caterer.jpg", "Wedding buffet", "Buffet service for 100 guests."},
                new String[]{"images/port-table.jpg", "Gala dinner", "Plated dinner at a Diwali gala."}),
                List.<String[]>of(),
                List.of(new String[]{"Rajesh K.", "4", "Sep 2026", "The chaat counter had a longer line than the bar. Food was excellent, service slightly slow at peak."}, new String[]{"Michelle D.", "5", "Aug 2026", "They catered our 200-person Diwali gala flawlessly."}),
                List.of());

        seedVendor("wicked", "Wicked Whimsy Studios", "decorators", "Bay Area", "USA", "$",
                5.0, 5, 350, true, true,
                "Halloween haunted houses, Christmas wonderlands.", "Prop stylists obsessed with October and December. Haunted house builds, spooky tablescapes, and Christmas installs that belong in movies — at newcomer prices.",
                List.of("English"),
                List.of("halloween", "christmas", "birthday", "corporate"),
                List.of(new String[]{"Halloween home haunt", "350", "event"}, new String[]{"Christmas install", "500", "event"}, new String[]{"Spooky tablescape", "200", "setup"}),
                List.<String[]>of(
                new String[]{"images/cat-birthday.jpg", "Porch haunt", "The house every kid talked about."},
                new String[]{"images/port-sangeet.jpg", "Stage build", "Spooky stage with floating candles."}),
                List.<String[]>of(),
                List.of(new String[]{"Hannah W.", "5", "Oct 2026", "Our house was THE house on the street. Kids are still talking about it."}, new String[]{"Chris B.", "5", "Sep 2026", "Insanely creative. The floating candles! The fog! Worth double."}),
                List.of());

        seedVendor("zahra", "Zahra Luxe Events", "decorators", "Dubai", "UAE", "AED",
                4.9, 143, 4500, true, false,
                "Arabian-night luxury meets desi grandeur.", "Dubai's go-to team for big-fat weddings — Indian, Arab, and fusion. Ten years across Palm Jumeirah ballrooms and desert venues. Emirates ID verified, trade-licensed, and famous for setups that photograph like film sets.",
                List.of("English", "Hindi", "Arabic", "Urdu"),
                List.of("wedding", "sangeet", "mehendi", "corporate", "anniversary"),
                List.of(new String[]{"Luxury wedding stage + mandap", "4500", "event"}, new String[]{"Sangeet night production", "3200", "event"}, new String[]{"Mehendi morning setup", "1800", "event"}, new String[]{"Corporate gala decor", "2500", "event"}),
                List.<String[]>of(
                new String[]{"images/hero.jpg", "Luxury wedding stage", "A palace-like Nikah stage in Dubai."},
                new String[]{"images/port-mandap.jpg", "Mandap detail", "Close-up of the floral mandap work."},
                new String[]{"images/port-sangeet.jpg", "Sangeet production", "LED walls and dhol entry."}),
                List.<String[]>of(),
                List.of(new String[]{"Fatima A.", "5", "Sep 2026", "Our Nikah stage looked like a palace. Guests from three countries asked who did the decor."}, new String[]{"Rohan M.", "5", "Aug 2026", "Sangeet production with LED walls and dhol entry — flawless."}, new String[]{"Sara K.", "4", "Jul 2026", "Premium pricing, premium result. Book early, they fill fast."}),
                List.of("dunes"));

        seedVendor("dunes", "Dunes & Vows Films", "photographers", "Dubai", "UAE", "AED",
                5.0, 9, 3500, true, true,
                "Desert golden hour is our studio.", "A young duo shooting weddings across the dunes and the Marina. Drone + candid, same-day edits, and prices that undercut the big studios while the portfolio grows. Emirates ID verified.",
                List.of("English", "Hindi", "Malayalam"),
                List.of("wedding", "prewedding", "sangeet", "anniversary"),
                List.of(new String[]{"Full wedding day (photo+drone)", "3500", "event"}, new String[]{"Pre-wedding desert shoot", "1500", "session"}, new String[]{"Sangeet candid coverage", "1200", "event"}),
                List.<String[]>of(
                new String[]{"images/cat-photographer.jpg", "Desert pre-wedding", "Golden-hour portraits in the dunes."},
                new String[]{"images/port-prewedding.jpg", "Vows at golden hour", "Drone still from a desert ceremony."},
                new String[]{"images/port-bridal.jpg", "Bridal portraits", "Quiet portraits between ceremonies."}),
                List.<String[]>of(new String[]{"Equipment", "Sony A7SIII - DJI Mavic 3 Pro"}, new String[]{"Course", "Candid Wedding Storytelling, 2024"}),
                List.of(new String[]{"Anjali P.", "5", "Sep 2026", "Our desert pre-wedding shoot looks straight out of a movie. They knew exactly where the light would be."}, new String[]{"Omar S.", "5", "Aug 2026", "Drone shots over the dunes during our vows — unreal. Fastest-growing team in Dubai for a reason."}),
                List.of("zahra"));

    }

    private void seedVendor(String id, String name, String categoryId, String city, String country,
                            String currency, double rating, int reviewCount, int startingPrice,
                            boolean verified, boolean newcomer,
                            String tagline, String bio,
                            List<String> languages, List<String> occasionIds,
                            List<String[]> services, List<String[]> portfolio, List<String[]> credentials,
                            List<String[]> seedReviews, List<String> collaboratorIds) {
        Category category = categories.findById(categoryId)
                .orElseThrow(() -> new IllegalStateException("Unknown category: " + categoryId));
        Vendor vendor = new Vendor();
        vendor.setId(id);
        vendor.setName(name);
        vendor.setCategory(category);
        vendor.setCity(city);
        vendor.setCountry(country);
        vendor.setCurrency(currency);
        vendor.setRating(rating);
        vendor.setReviewCount(reviewCount);
        vendor.setStartingPrice(startingPrice);
        vendor.setVerified(verified);
        vendor.setNewcomer(newcomer);
        vendor.setTagline(tagline);
        vendor.setBio(bio);
        vendor.setLanguages(languages);
        vendor.setOccasionIds(occasionIds);
        vendor.setCollaboratorIds(collaboratorIds);
        vendor.setServices(services.stream()
                .map(s -> new ServiceOffering(s[0], Integer.parseInt(s[1]), s[2]))
                .toList());
        vendor.setPortfolio(portfolio.stream().map(p -> new PortfolioItem(p[0], p[1], p[2])).toList());
        vendor.setCredentials(credentials.stream().map(c -> new Credential(c[0], c[1])).toList());
        vendors.save(vendor);
        for (String[] r : seedReviews) {
            Review review = new Review();
            review.setVendor(vendor);
            review.setAuthor(r[0]);
            review.setRating(Integer.parseInt(r[1]));
            review.setDateLabel(r[2]);
            review.setText(r[3]);
            reviews.save(review);
        }
    }
}
