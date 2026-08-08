'use client';

import React from 'react';
import Link from 'next/link';
import { Smartphone, ShieldCheck, Heart, Mail, Globe } from 'lucide-react';

export default function Footer() {
  return (
    <footer className="bg-slate-900 text-slate-300 pt-12 pb-8 border-t border-slate-800 mt-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-4 gap-8 mb-12">
        <div>
          <div className="flex items-center gap-3 mb-4">
            <div className="w-9 h-9 bg-brand-500 text-white rounded-xl flex items-center justify-center font-extrabold text-lg">
              R
            </div>
            <span className="font-extrabold text-xl text-white">RoomSaathi</span>
          </div>
          <p className="text-sm text-slate-400 mb-4 leading-relaxed">
            Your cozy saathi for verified room rentals, flatmates, co-living villas, and host management.
          </p>
          <div className="flex items-center gap-2 text-xs text-brand-400 font-semibold bg-slate-800/80 px-3 py-1.5 rounded-lg w-fit">
            <ShieldCheck className="w-4 h-4" /> 100% KYC Verified Properties
          </div>
        </div>

        <div>
          <h4 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Quick Navigation</h4>
          <ul className="space-y-2.5 text-sm">
            <li><Link href="/" className="hover:text-brand-400 transition-colors">Home Platform</Link></li>
            <li><Link href="/explore" className="hover:text-brand-400 transition-colors">Explore Stays</Link></li>
            <li><Link href="/roommates" className="hover:text-brand-400 transition-colors">Roommate Finder</Link></li>
            <li><Link href="/host" className="hover:text-brand-400 transition-colors">Become a Host</Link></li>
          </ul>
        </div>

        <div>
          <h4 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Mobile & API</h4>
          <ul className="space-y-2.5 text-sm">
            <li>
              <Link href="/download" className="hover:text-brand-400 transition-colors flex items-center gap-1.5">
                <Smartphone className="w-4 h-4 text-brand-500" /> RoomSaathi Android APK v1.1.0
              </Link>
            </li>
            <li>
              <a href="/api/v1/app/latest-version" target="_blank" rel="noreferrer" className="hover:text-brand-400 transition-colors flex items-center gap-1.5">
                <Globe className="w-4 h-4 text-emerald-400" /> Version API Endpoint
              </a>
            </li>
            <li><span className="text-xs text-slate-500">QR Check-in Ticket Generator Enabled</span></li>
          </ul>
        </div>

        <div>
          <h4 className="text-white font-bold text-sm uppercase tracking-wider mb-4">Support & Contact</h4>
          <p className="text-sm text-slate-400 mb-2 flex items-center gap-2">
            <Mail className="w-4 h-4 text-brand-500" /> support@roomsaathi.app
          </p>
          <p className="text-xs text-slate-500 leading-relaxed">
            Operating across Kathmandu, Pokhara, Lalitpur, and major cities across Nepal.
          </p>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-6 border-t border-slate-800 flex flex-col sm:flex-row items-center justify-between text-xs text-slate-500">
        <p>© 2026 RoomSaathi Inc. All rights reserved.</p>
        <p className="flex items-center gap-1 mt-2 sm:mt-0">
          Crafted with <Heart className="w-3.5 h-3.5 text-red-500 fill-red-500" /> for comfortable stays.
        </p>
      </div>
    </footer>
  );
}
