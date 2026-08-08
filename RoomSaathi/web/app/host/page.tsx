'use client';

import React, { useState } from 'react';
import { Building2, Plus, CheckCircle2, ShieldCheck, DollarSign, MapPin } from 'lucide-react';

export default function HostPage() {
  const [title, setTitle] = useState('');
  const [city, setCity] = useState('Kathmandu');
  const [category, setCategory] = useState('ROOM');
  const [price, setPrice] = useState('40');
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) return;
    setSubmitted(true);
  };

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="mb-8">
        <div className="inline-flex items-center gap-1.5 bg-brand-50 text-brand-700 text-xs font-bold px-3 py-1 rounded-full border border-brand-200 mb-2">
          <Building2 className="w-3.5 h-3.5 text-brand-500" /> Host & Seller Portal
        </div>
        <h1 className="text-3xl font-extrabold text-slate-900">List Your Property or Room</h1>
        <p className="text-sm text-slate-500 mt-1">Earn income by hosting travelers, students, and young professionals on RoomSaathi</p>
      </div>

      {!submitted ? (
        <form onSubmit={handleSubmit} className="bg-white rounded-3xl border border-slate-200 p-6 sm:p-8 shadow-xl space-y-5">
          <h2 className="font-extrabold text-lg text-slate-900 border-b border-slate-100 pb-3">Property Listing Details</h2>

          <div>
            <label className="block text-xs font-bold text-slate-700 mb-1">Listing Title</label>
            <input
              type="text"
              required
              placeholder="e.g., Sunlit Private Room in Jhamsikhel"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-2.5 text-sm border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500"
            />
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">City Location</label>
              <select
                value={city}
                onChange={(e) => setCity(e.target.value)}
                className="w-full px-3 py-2.5 text-xs font-bold border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500 bg-slate-50"
              >
                <option value="Kathmandu">Kathmandu</option>
                <option value="Pokhara">Pokhara</option>
                <option value="Lalitpur">Lalitpur</option>
                <option value="Bhaktapur">Bhaktapur</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">Category</label>
              <select
                value={category}
                onChange={(e) => setCategory(e.target.value)}
                className="w-full px-3 py-2.5 text-xs font-bold border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500 bg-slate-50"
              >
                <option value="ROOM">Private Room</option>
                <option value="APARTMENT">Apartment</option>
                <option value="VILLA">Villa / Co-Living</option>
                <option value="COWORKING">Coworking Space</option>
              </select>
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 mb-1">Nightly Price ($)</label>
              <input
                type="number"
                required
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                className="w-full px-4 py-2.5 text-sm border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500"
              />
            </div>
          </div>

          <button
            type="submit"
            className="w-full bg-brand-500 hover:bg-brand-600 text-white font-extrabold text-sm py-3.5 rounded-xl shadow-lg transition-all flex items-center justify-center gap-2"
          >
            <Plus className="w-4 h-4" />
            <span>Publish Property Listing</span>
          </button>
        </form>
      ) : (
        <div className="bg-white rounded-3xl border border-slate-200 p-8 text-center space-y-4 shadow-xl">
          <div className="w-14 h-14 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
            <CheckCircle2 className="w-8 h-8" />
          </div>
          <h2 className="text-xl font-extrabold text-slate-900">Listing Submitted for Verification!</h2>
          <p className="text-xs text-slate-600 max-w-md mx-auto">
            Your property <strong>"{title}"</strong> (${price}/night in {city}) is now under rapid review by RoomSaathi KYC safety team.
          </p>
          <button
            onClick={() => setSubmitted(false)}
            className="bg-slate-100 hover:bg-slate-200 text-slate-800 font-bold text-xs px-6 py-2.5 rounded-xl transition-colors"
          >
            List Another Property
          </button>
        </div>
      )}
    </div>
  );
}
