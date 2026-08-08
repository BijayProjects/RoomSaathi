'use client';

import React, { useState } from 'react';
import { Property } from './PropertyCard';
import { X, CheckCircle2, QrCode, ShieldCheck, Sparkles, Calendar, User } from 'lucide-react';

interface BookingModalProps {
  property: Property | null;
  onClose: () => void;
}

export default function BookingModal({ property, onClose }: BookingModalProps) {
  const [confirmed, setConfirmed] = useState(false);
  const [nights, setNights] = useState(2);
  const [guestName, setGuestName] = useState('Subhash Dev');

  if (!property) return null;

  const total = property.price * nights + 12; // $12 service & security fee

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-slate-900/60 backdrop-blur-sm p-4 animate-in fade-in duration-200">
      <div className="bg-white w-full max-w-md rounded-2xl shadow-2xl overflow-hidden border border-slate-200">
        {/* Modal Header */}
        <div className="bg-brand-500 text-white p-4 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Sparkles className="w-5 h-5 text-amber-200" />
            <h3 className="font-extrabold text-base">RoomSaathi Booking & Pass</h3>
          </div>
          <button onClick={onClose} className="p-1 rounded-full hover:bg-white/20 transition-colors">
            <X className="w-5 h-5" />
          </button>
        </div>

        {/* Modal Body */}
        {!confirmed ? (
          <div className="p-5 space-y-4">
            <div className="flex gap-3 items-center bg-brand-50 p-3 rounded-xl border border-brand-200">
              <img src={property.image} alt={property.title} className="w-16 h-16 rounded-lg object-cover" />
              <div>
                <span className="text-[10px] font-bold uppercase text-brand-600 bg-white px-2 py-0.5 rounded-full border border-brand-200">
                  {property.category}
                </span>
                <h4 className="font-bold text-slate-900 text-sm line-clamp-1 mt-1">{property.title}</h4>
                <p className="text-xs text-slate-500">📍 {property.city} • Host: {property.seller}</p>
              </div>
            </div>

            <div className="space-y-3 pt-2">
              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Guest Full Name</label>
                <div className="relative">
                  <User className="w-4 h-4 absolute left-3 top-2.5 text-slate-400" />
                  <input
                    type="text"
                    value={guestName}
                    onChange={(e) => setGuestName(e.target.value)}
                    className="w-full pl-9 pr-3 py-2 text-sm border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500"
                  />
                </div>
              </div>

              <div>
                <label className="block text-xs font-bold text-slate-700 mb-1">Stay Duration (Nights)</label>
                <div className="flex items-center gap-3">
                  <input
                    type="range"
                    min="1"
                    max="14"
                    value={nights}
                    onChange={(e) => setNights(Number(e.target.value))}
                    className="w-full accent-brand-500"
                  />
                  <span className="font-extrabold text-brand-600 text-sm min-w-[60px] text-right">
                    {nights} {nights === 1 ? 'Night' : 'Nights'}
                  </span>
                </div>
              </div>

              {/* Price Breakdown */}
              <div className="bg-slate-50 p-3 rounded-xl space-y-1.5 text-xs">
                <div className="flex justify-between text-slate-600">
                  <span>${property.price} x {nights} nights</span>
                  <span>${property.price * nights}</span>
                </div>
                <div className="flex justify-between text-slate-600">
                  <span>RoomSaathi Safety & QR Pass Fee</span>
                  <span>$12</span>
                </div>
                <div className="border-t border-slate-200 pt-1.5 flex justify-between font-extrabold text-sm text-slate-900">
                  <span>Total Amount</span>
                  <span className="text-brand-500">${total}</span>
                </div>
              </div>
            </div>

            {/* Confirm CTA */}
            <button
              onClick={() => setConfirmed(true)}
              className="w-full bg-brand-500 hover:bg-brand-600 text-white font-bold py-3 rounded-xl shadow-lg transition-all flex items-center justify-center gap-2 text-sm"
            >
              <ShieldCheck className="w-4 h-4" />
              <span>Confirm & Generate Digital Ticket</span>
            </button>
          </div>
        ) : (
          /* Confirmation & Digital Ticket */
          <div className="p-6 text-center space-y-4">
            <div className="w-12 h-12 bg-emerald-100 text-emerald-600 rounded-full flex items-center justify-center mx-auto">
              <CheckCircle2 className="w-7 h-7" />
            </div>

            <div>
              <h4 className="font-extrabold text-slate-900 text-lg">Booking Confirmed!</h4>
              <p className="text-xs text-slate-500 mt-1">Ticket #RS-{Math.floor(100000 + Math.random() * 900000)} • Synchronized with Mobile App</p>
            </div>

            {/* QR Pass Box */}
            <div className="bg-slate-900 text-white p-4 rounded-2xl space-y-3">
              <div className="flex justify-between items-center text-xs text-slate-400 border-b border-slate-800 pb-2">
                <span>Guest: <strong>{guestName}</strong></span>
                <span>Nights: <strong>{nights}</strong></span>
              </div>

              <div className="bg-white p-3 rounded-xl inline-block shadow-inner">
                <QrCode className="w-28 h-28 text-slate-900 mx-auto" />
              </div>

              <p className="text-[11px] text-brand-400 font-bold">
                Show this digital pass at property check-in or scan in the RoomSaathi Android App
              </p>
            </div>

            <button
              onClick={onClose}
              className="w-full bg-slate-100 hover:bg-slate-200 text-slate-800 font-bold py-2.5 rounded-xl text-sm transition-colors"
            >
              Close Window
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
