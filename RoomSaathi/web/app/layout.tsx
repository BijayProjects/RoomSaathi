import React from 'react';
import './globals.css';
import Navbar from '@/components/Navbar';
import Footer from '@/components/Footer';

export const metadata = {
  title: 'RoomSaathi — Stays, Roommate Finder & Property Rentals',
  description: 'Official website and web platform for RoomSaathi. Find verified rooms, apartments, villas, and flatmates across Nepal or download the Android APK.',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <head>
        <link rel="manifest" href="/manifest.json" />
        <meta name="theme-color" content="#FF6F00" />
      </head>
      <body className="min-h-screen flex flex-col justify-between bg-slate-50 text-slate-900 antialiased">
        <Navbar />
        <main className="flex-grow">{children}</main>
        <Footer />
      </body>
    </html>
  );
}
