'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { Home, Search, Smartphone, Users, PlusCircle, UserCheck, Sparkles, Building2 } from 'lucide-react';

export default function Navbar() {
  const [role, setRole] = useState<'BUYER' | 'SELLER' | 'ADMIN'>('BUYER');

  return (
    <header className="sticky top-0 z-50 glass-nav border-b border-slate-200">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
        {/* Brand Logo */}
        <Link href="/" className="flex items-center gap-3 group">
          <div className="w-10 h-10 bg-brand-500 text-white rounded-xl flex items-center justify-center font-extrabold text-xl shadow-md group-hover:scale-105 transition-transform">
            R
          </div>
          <div>
            <span className="font-extrabold text-xl tracking-tight text-slate-900 group-hover:text-brand-500 transition-colors">
              RoomSaathi
            </span>
            <span className="block text-[10px] text-brand-600 font-bold uppercase tracking-wider">
              Web & App Platform
            </span>
          </div>
        </Link>

        {/* Desktop Navigation Links */}
        <nav className="hidden md:flex items-center gap-6">
          <Link href="/" className="text-sm font-semibold text-slate-700 hover:text-brand-500 flex items-center gap-1.5 transition-colors">
            <Home className="w-4 h-4" /> Home
          </Link>
          <Link href="/explore" className="text-sm font-semibold text-slate-700 hover:text-brand-500 flex items-center gap-1.5 transition-colors">
            <Search className="w-4 h-4" /> Explore Stays
          </Link>
          <Link href="/roommates" className="text-sm font-semibold text-slate-700 hover:text-brand-500 flex items-center gap-1.5 transition-colors">
            <Users className="w-4 h-4" /> Roommate Finder
          </Link>
          <Link href="/host" className="text-sm font-semibold text-slate-700 hover:text-brand-500 flex items-center gap-1.5 transition-colors">
            <Building2 className="w-4 h-4" /> Host / Seller
          </Link>
          <Link href="/download" className="text-sm font-semibold text-slate-700 hover:text-brand-500 flex items-center gap-1.5 transition-colors">
            <Smartphone className="w-4 h-4 text-brand-500" /> Android APK
          </Link>
        </nav>

        {/* Role Selector & Actions */}
        <div className="flex items-center gap-3">
          <div className="hidden sm:flex items-center bg-brand-50 text-brand-700 text-xs font-bold px-3 py-1.5 rounded-full border border-brand-200">
            <UserCheck className="w-3.5 h-3.5 mr-1" />
            Role: 
            <select 
              value={role} 
              onChange={(e) => setRole(e.target.value as any)}
              className="bg-transparent font-extrabold text-brand-600 focus:outline-none cursor-pointer ml-1"
            >
              <option value="BUYER">BUYER</option>
              <option value="SELLER">SELLER (Host)</option>
              <option value="ADMIN">ADMIN</option>
            </select>
          </div>

          <Link 
            href="/download"
            className="bg-brand-500 hover:bg-brand-600 text-white font-bold text-xs sm:text-sm px-4 py-2.5 rounded-xl shadow-md hover:shadow-lg transition-all flex items-center gap-2"
          >
            <Smartphone className="w-4 h-4" />
            <span>Download APK</span>
          </Link>
        </div>
      </div>
    </header>
  );
}
