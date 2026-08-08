# RoomSaathi Web Platform — Direct Vercel Deployment Guide

This directory (`/web`) contains the complete, standalone production website for **RoomSaathi**. It includes modern UI, responsive room explorer, PWA support, APK download section, and REST API routing configured for Vercel.

---

## 🚀 How to Deploy to Vercel in 2 Minutes

### Option 1: Vercel CLI
1. Open your terminal in this directory:
   ```bash
   cd web
   ```
2. Run:
   ```bash
   npx vercel
   ```
3. Follow the quick prompt to log into your Vercel account. Done!

---

### Option 2: Push to GitHub & Connect to Vercel
1. Upload this `/web` folder or repository to GitHub.
2. Go to [https://vercel.com/new](https://vercel.com/new)
3. Import your GitHub repository.
4. Set **Root Directory** to `web`.
5. Click **Deploy**. Vercel will instantly host your website with automatic HTTPS!

---

## 📁 Included Website Files

- `index.html` — Main RoomSaathi landing page, room explorer, APK download section, and FAQ.
- `styles.css` — Modern design tokens, RoomSaathi brand colors (`#008080`), responsive layouts.
- `app.js` — Property filtering, REST API integration, and booking workflows.
- `vercel.json` — Vercel routing configuration and REST API headers.
- `api/latest-version.json` — REST endpoint for RoomSaathi Android APK update checks (`/api/v1/app/latest-version`).
- `manifest.json` & `sw.js` — PWA support for adding RoomSaathi to Home Screen.
