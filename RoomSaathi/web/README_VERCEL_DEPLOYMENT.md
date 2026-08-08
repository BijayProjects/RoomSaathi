# RoomSaathi Next.js Web Platform — Vercel Deployment Guide

This directory (`/web`) contains the full, production-ready **Next.js 14 App Router** website for **RoomSaathi**. It is styled with Tailwind CSS, matching the RoomSaathi orange brand theme (`#FF6F00`), and includes:

- 🏠 **Home Page**: Hero banner, verified stay explorer, flatmate matchmaker preview, and APK release card.
- 🔍 **Explore Stays (`/explore`)**: Interactive search, city filtering (Kathmandu, Pokhara, Lalitpur), category filters (Rooms, Apartments, Villas, Coworking).
- 👥 **Roommate Finder (`/roommates`)**: Flatmate compatibility profiles, budget/lifestyle preferences, and chat affordances.
- 📱 **APK Download (`/download`)**: APK release v1.1.0 details, installation instructions, and release notes.
- 🏢 **Host & Seller Portal (`/host`)**: Property listing creation form.
- 🤖 **Gemini AI Concierge**: Interactive AI stay assistant widget.
- 🎟️ **Digital Booking & QR Pass**: Interactive booking checkout and digital QR ticket pass generator.
- ⚡ **REST API (`/api/v1/app/latest-version`)**: Next.js API route serving live version JSON for Android mobile app updates.

---

## 🚀 How to Deploy to Vercel in 2 Minutes

### Option 1: Deploy with Vercel CLI
1. Open your terminal in the `/web` folder:
   ```bash
   cd web
   ```
2. Run Vercel deploy:
   ```bash
   npx vercel
   ```
3. Follow the CLI prompt to connect your Vercel account. Done!

---

### Option 2: Connect GitHub Repository to Vercel
1. Push your repository to GitHub.
2. Go to [https://vercel.com/new](https://vercel.com/new).
3. Import your repository.
4. Set **Root Directory** to `web`.
5. Framework Preset will auto-detect as **Next.js**.
6. Click **Deploy**. Vercel will build and host your website with automatic HTTPS and global edge CDN!

---

## 🛠️ Project Structure
```
/web
  ├── app/
  │   ├── layout.tsx              # Root Layout & Theme
  │   ├── page.tsx                # Home Page with Hero & Explorer
  │   ├── explore/page.tsx        # Stay Search & Filter Page
  │   ├── roommates/page.tsx      # Roommate Matchmaker Page
  │   ├── download/page.tsx       # APK Download & Release Notes
  │   ├── host/page.tsx           # Host/Seller Listing Form
  │   └── api/v1/app/latest-version/route.ts # REST API Endpoint
  ├── components/
  │   ├── Navbar.tsx              # Header & Role Selector
  │   ├── Footer.tsx              # Footer Navigation
  │   ├── PropertyCard.tsx        # Stay Listing Card
  │   ├── BookingModal.tsx        # Checkout & QR Pass Modal
  │   ├── RoommateCard.tsx        # Flatmate Card
  │   └── AiConciergeModal.tsx    # Gemini AI Chat Assistant
  ├── package.json
  ├── next.config.js
  ├── tailwind.config.js
  ├── tsconfig.json
  └── vercel.json
```
