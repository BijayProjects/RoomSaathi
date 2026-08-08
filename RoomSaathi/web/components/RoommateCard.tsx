'use client';

import React from 'react';
import { UserCheck, MapPin, Sparkles, DollarSign, Clock, MessageSquare } from 'lucide-react';

export interface Roommate {
  id: string;
  name: string;
  age: number;
  gender: string;
  profession: string;
  city: string;
  budget: number;
  habits: string[];
  bio: string;
  avatar: string;
  compatibility: number;
}

export default function RoommateCard({ roommate }: { roommate: Roommate }) {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 p-5 shadow-sm hover:shadow-md transition-all flex flex-col justify-between">
      <div>
        <div className="flex items-center justify-between mb-3">
          <div className="flex items-center gap-3">
            <img src={roommate.avatar} alt={roommate.name} className="w-12 h-12 rounded-full object-cover border-2 border-brand-500" />
            <div>
              <h4 className="font-bold text-slate-900 text-sm">{roommate.name}, {roommate.age}</h4>
              <p className="text-xs text-slate-500">{roommate.profession} • 📍 {roommate.city}</p>
            </div>
          </div>
          <span className="bg-emerald-50 text-emerald-700 font-extrabold text-xs px-2.5 py-1 rounded-full border border-emerald-200 flex items-center gap-1">
            <Sparkles className="w-3 h-3 text-emerald-600" /> {roommate.compatibility}% Match
          </span>
        </div>

        <p className="text-xs text-slate-600 italic mb-3 bg-slate-50 p-2.5 rounded-xl border border-slate-100">
          "{roommate.bio}"
        </p>

        <div className="flex items-center justify-between text-xs text-slate-600 mb-3">
          <span className="font-semibold">Budget: <strong className="text-brand-500">${roommate.budget}/mo</strong></span>
          <span className="text-slate-500">{roommate.gender}</span>
        </div>

        <div className="flex flex-wrap gap-1.5 mb-4">
          {roommate.habits.map((habit, idx) => (
            <span key={idx} className="bg-brand-50 text-brand-700 text-[10px] font-bold px-2 py-0.5 rounded-md border border-brand-100">
              {habit}
            </span>
          ))}
        </div>
      </div>

      <button
        onClick={() => alert(`Connecting with ${roommate.name} via RoomSaathi Chat...`)}
        className="w-full bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs py-2.5 rounded-xl transition-colors flex items-center justify-center gap-1.5"
      >
        <MessageSquare className="w-3.5 h-3.5" />
        <span>Chat with {roommate.name.split(' ')[0]}</span>
      </button>
    </div>
  );
}
