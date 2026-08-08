'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import PropertyCard, { Property } from '@/components/PropertyCard';
import BookingModal from '@/components/BookingModal';
import RoommateCard, { Roommate } from '@/components/RoommateCard';
import AiConciergeModal from '@/components/AiConciergeModal';
import { Smartphone, Sparkles, ShieldCheck, Search, Users, Building2, Bot, ArrowRight, Star, QrCode } from 'lucide-react';

const mockProperties: Property[] = [
  {
    id: 'prop_101',
    title: 'Skyline Luxury Studio & Private Room',
    city: 'Kathmandu',
    category: 'ROOM',
    price: 45,
    rating: 4.9,
    image: 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=600&q=80',
    seller: 'Ananda Shrestha',
    instantBooking: true,
    amenities: ['WiFi 100Mbps', 'Power Backup', 'AC', 'Shared Kitchen']
  },
  {
    id: 'prop_102',
    title: 'Garden View 2BHK Serviced Apartment',
    city: 'Pokhara',
    category: 'APARTMENT',
    price: 85,
    rating: 4.8,
    image: 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=600&q=80',
    seller: 'Sujata Gurung',
    instantBooking: true,
    amenities: ['Balcony View', 'Full Kitchen', 'Washing Machine', 'Parking']
  },
  {
    id: 'prop_103',
    title: 'Himalayan Breeze Villa & Co-Living',
    city: 'Lalitpur',
    category: 'VILLA',
    price: 130,
    rating: 5.0,
    image: 'https://images.unsplash.com/photo-1613490493576-7fde63acd811?auto=format&fit=crop&w=600&q=80',
    seller: 'Rohan Joshi',
    instantBooking: false,
    amenities: ['Private Garden', 'Jacuzzi', 'Dedicated Workspace', 'Chef']
  },
  {
    id: 'prop_104',
    title: 'Central Tech Hub Coworking Desk & Room',
    city: 'Kathmandu',
    category: 'COWORKING',
    price: 25,
    rating: 4.7,
    image: 'https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&w=600&q=80',
    seller: 'Saugat Malla',
    instantBooking: true,
    amenities: ['Ergonomic Chair', 'Fiber Net', 'Free Coffee', '24/7 Access']
  }
];

const mockRoommates: Roommate[] = [
  {
    id: 'rm_1',
    name: 'Prashant Adhikari',
    age: 24,
    gender: 'Male',
    profession: 'Software Engineer',
    city: 'Kathmandu',
    budget: 250,
    habits: ['Non-Smoker', 'Early Riser', 'Clean & Quiet', 'Techie'],
    bio: 'Looking for a flatmate in Jhamsikhel or Sanepa. Quiet during weekdays.',
    avatar: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=200&q=80',
    compatibility: 96
  },
  {
    id: 'rm_2',
    name: 'Aayusha Sharma',
    age: 23,
    gender: 'Female',
    profession: 'Graphic Designer',
    city: 'Lalitpur',
    budget: 220,
    habits: ['Pet Friendly', 'Weekend Cook', 'Yoga', 'Non-Smoker'],
    bio: 'Searching for a cozy 2BHK roommate around Baneshwor.',
    avatar: 'https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=200&q=80',
    compatibility: 92
  }
];

export default function HomePage() {
  const [selectedCategory, setSelectedCategory] = useState<string>('ALL');
  const [selectedProperty, setSelectedProperty] = useState<Property | null>(null);
  const [isAiOpen, setIsAiOpen] = useState(false);

  const filteredProperties = mockProperties.filter(p => {
    if (selectedCategory === 'ALL') return true;
    return p.category === selectedCategory;
  });

  return (
    <div>
      {/* Hero Banner Section */}
      <section className="relative overflow-hidden bg-gradient-to-br from-brand-50 via-white to-amber-50 border-b border-slate-200 py-16 sm:py-20 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
          
          {/* Hero Left Content */}
          <div className="lg:col-span-7 space-y-6">
            <div className="inline-flex items-center gap-2 bg-brand-100 text-brand-700 text-xs font-extrabold px-3.5 py-1.5 rounded-full border border-brand-200">
              <Sparkles className="w-3.5 h-3.5 text-brand-600" />
              <span>RoomSaathi Web & Android Platform 2026</span>
            </div>

            <h1 className="text-3xl sm:text-5xl font-extrabold text-slate-900 tracking-tight leading-tight">
              Find Your Cozy <span className="text-brand-500">Saathi</span> for Stays, Rooms & Flatmates
            </h1>

            <p className="text-slate-600 text-base sm:text-lg leading-relaxed max-w-2xl">
              Discover certified verified rooms, luxury apartments, and budget stays across Nepal. Search flatmates, book directly on the web, or download the native RoomSaathi Android app.
            </p>

            <div className="flex flex-wrap items-center gap-4 pt-2">
              <Link
                href="/explore"
                className="bg-brand-500 hover:bg-brand-600 text-white font-extrabold text-sm px-6 py-3.5 rounded-xl shadow-lg hover:shadow-brand-500/25 transition-all flex items-center gap-2"
              >
                <Search className="w-4 h-4" />
                <span>Explore Live Rooms</span>
              </Link>

              <Link
                href="/download"
                className="bg-white hover:bg-slate-50 text-slate-800 font-bold text-sm px-6 py-3.5 rounded-xl border border-slate-300 shadow-sm transition-all flex items-center gap-2"
              >
                <Smartphone className="w-4 h-4 text-brand-500" />
                <span>Download APK v1.1.0</span>
              </Link>
            </div>

            {/* Trust Badges */}
            <div className="pt-6 border-t border-slate-200/60 grid grid-cols-3 gap-4 text-xs font-semibold text-slate-600">
              <div className="flex items-center gap-2">
                <ShieldCheck className="w-4 h-4 text-brand-500" />
                <span>100% KYC Hosts</span>
              </div>
              <div className="flex items-center gap-2">
                <QrCode className="w-4 h-4 text-emerald-600" />
                <span>Digital QR Pass</span>
              </div>
              <div className="flex items-center gap-2">
                <Bot className="w-4 h-4 text-purple-600" />
                <span>Gemini AI Concierge</span>
              </div>
            </div>
          </div>

          {/* Hero Right APK Release Widget */}
          <div className="lg:col-span-5">
            <div className="bg-white rounded-2xl border border-slate-200 p-6 shadow-xl relative overflow-hidden">
              <div className="absolute top-0 right-0 bg-emerald-500 text-white text-[10px] font-extrabold px-3 py-1 rounded-bl-xl uppercase tracking-wider">
                Official Release
              </div>

              <div className="flex items-center gap-3 mb-4">
                <div className="w-12 h-12 bg-brand-500 text-white rounded-2xl flex items-center justify-center font-extrabold text-2xl shadow-md">
                  R
                </div>
                <div>
                  <h3 className="font-extrabold text-lg text-slate-900">RoomSaathi Android App</h3>
                  <p className="text-xs text-slate-500">v1.1.0 APK Release • Direct Web Sync</p>
                </div>
              </div>

              <div className="bg-slate-50 p-3.5 rounded-xl border border-slate-100 text-xs space-y-2 mb-4">
                <div className="flex justify-between">
                  <span className="text-slate-500">App File Size:</span>
                  <strong className="text-slate-800">28 MB</strong>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">Requirements:</span>
                  <strong className="text-slate-800">Android 7.0+</strong>
                </div>
                <div className="flex justify-between">
                  <span className="text-slate-500">Release Date:</span>
                  <strong className="text-slate-800">August 2026</strong>
                </div>
              </div>

              <p className="text-xs text-slate-600 mb-5 leading-relaxed bg-brand-50/50 p-2.5 rounded-lg border border-brand-100">
                ✨ <strong>v1.1.0 Feature Highlights:</strong> Instant digital QR check-in passes, real-time booking status, and Gemini AI stay advisor.
              </p>

              <Link
                href="/download"
                className="w-full bg-brand-500 hover:bg-brand-600 text-white font-extrabold text-sm py-3 rounded-xl shadow-md transition-all flex items-center justify-center gap-2"
              >
                <Smartphone className="w-4 h-4" />
                <span>Get RoomSaathi APK</span>
              </Link>
            </div>
          </div>

        </div>
      </section>

      {/* Property Explorer Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="flex flex-col sm:flex-row sm:items-end justify-between mb-8 gap-4">
          <div>
            <h2 className="text-2xl font-extrabold text-slate-900">Explore Verified Stays & Rooms</h2>
            <p className="text-sm text-slate-500 mt-1">Live listings synchronized with RoomSaathi REST API</p>
          </div>

          <div className="flex items-center gap-2 overflow-x-auto pb-2">
            {['ALL', 'ROOM', 'APARTMENT', 'VILLA', 'COWORKING'].map((cat) => (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3.5 py-1.5 rounded-full text-xs font-bold transition-all whitespace-nowrap ${
                  selectedCategory === cat
                    ? 'bg-brand-500 text-white shadow-md'
                    : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-100'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        {/* Grid of Properties */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
          {filteredProperties.map((p) => (
            <PropertyCard key={p.id} property={p} onBook={(prop) => setSelectedProperty(prop)} />
          ))}
        </div>
      </section>

      {/* Roommate Finder Section */}
      <section className="bg-slate-900 text-white py-16 px-4 sm:px-6 lg:px-8">
        <div className="max-w-7xl mx-auto">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between mb-8 gap-4">
            <div>
              <span className="text-xs font-extrabold uppercase text-brand-400 tracking-wider">Roommate Matchmaker</span>
              <h2 className="text-2xl font-extrabold text-white mt-1">Find Compatible RoomSaathis</h2>
              <p className="text-sm text-slate-400">Match based on budget, lifestyle habits, and location preference</p>
            </div>

            <Link href="/roommates" className="text-xs font-bold text-brand-400 hover:text-brand-300 flex items-center gap-1">
              View All Flatmate Listings <ArrowRight className="w-3.5 h-3.5" />
            </Link>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {mockRoommates.map((rm) => (
              <RoommateCard key={rm.id} roommate={rm} />
            ))}
          </div>
        </div>
      </section>

      {/* Floating AI Concierge Button */}
      <button
        onClick={() => setIsAiOpen(true)}
        className="fixed bottom-6 left-6 z-40 bg-brand-500 hover:bg-brand-600 text-white font-extrabold text-xs px-4 py-3 rounded-full shadow-2xl flex items-center gap-2 group transition-all"
      >
        <Bot className="w-5 h-5 text-amber-200 group-hover:scale-110 transition-transform" />
        <span>Ask RoomSaathi AI</span>
      </button>

      {/* Booking Modal */}
      <BookingModal property={selectedProperty} onClose={() => setSelectedProperty(null)} />

      {/* AI Concierge Chat Modal */}
      <AiConciergeModal isOpen={isAiOpen} onClose={() => setIsAiOpen(false)} />
    </div>
  );
}
