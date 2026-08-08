'use client';

import React, { useState } from 'react';
import RoommateCard, { Roommate } from '@/components/RoommateCard';
import { Users, Sparkles, Filter, Plus } from 'lucide-react';

const mockAllRoommates: Roommate[] = [
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
  },
  {
    id: 'rm_3',
    name: 'Suman Thapa',
    age: 26,
    gender: 'Male',
    profession: 'Civil Engineer',
    city: 'Pokhara',
    budget: 200,
    habits: ['Non-Smoker', 'Fitness Enthusiast', 'Quiet Hours'],
    bio: 'Moving to Lakeside Pokhara. Looking for a responsible co-tenant.',
    avatar: 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=200&q=80',
    compatibility: 89
  }
];

export default function RoommatesPage() {
  const [city, setCity] = useState('ALL');

  const filtered = mockAllRoommates.filter(rm => city === 'ALL' || rm.city === city);

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="flex flex-col md:flex-row md:items-center justify-between mb-8 gap-4">
        <div>
          <div className="inline-flex items-center gap-1.5 bg-brand-50 text-brand-700 text-xs font-bold px-3 py-1 rounded-full border border-brand-200 mb-2">
            <Sparkles className="w-3.5 h-3.5 text-brand-500" /> RoomSaathi Matchmaker
          </div>
          <h1 className="text-3xl font-extrabold text-slate-900">Roommate & Flatmate Finder</h1>
          <p className="text-sm text-slate-500 mt-1">Connect with verified individuals searching for co-living spaces in Nepal</p>
        </div>

        <button
          onClick={() => alert("Post Flatmate Request flow initiated...")}
          className="bg-brand-500 hover:bg-brand-600 text-white font-bold text-xs px-4 py-3 rounded-xl shadow-md transition-all flex items-center gap-2 w-fit"
        >
          <Plus className="w-4 h-4" />
          <span>Post Your Flatmate Profile</span>
        </button>
      </div>

      {/* City Filter */}
      <div className="flex items-center gap-2 mb-8 bg-white p-3 rounded-2xl border border-slate-200 w-fit">
        <Filter className="w-4 h-4 text-brand-500" />
        <span className="text-xs font-bold text-slate-700">City Filter:</span>
        <select
          value={city}
          onChange={(e) => setCity(e.target.value)}
          className="bg-slate-50 border border-slate-200 text-xs font-bold rounded-lg px-2.5 py-1 focus:outline-none focus:ring-2 focus:ring-brand-500"
        >
          <option value="ALL">All Cities</option>
          <option value="Kathmandu">Kathmandu</option>
          <option value="Lalitpur">Lalitpur</option>
          <option value="Pokhara">Pokhara</option>
        </select>
      </div>

      {/* Grid */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {filtered.map(rm => (
          <RoommateCard key={rm.id} roommate={rm} />
        ))}
      </div>
    </div>
  );
}
