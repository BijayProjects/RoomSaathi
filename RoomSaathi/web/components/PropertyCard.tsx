'use client';

import React from 'react';
import Image from 'next/image';
import { Star, MapPin, CheckCircle2, Shield, Calendar } from 'lucide-react';

export interface Property {
  id: string;
  title: string;
  city: string;
  category: 'ROOM' | 'APARTMENT' | 'VILLA' | 'COWORKING';
  price: number;
  rating: number;
  image: string;
  seller: string;
  instantBooking: boolean;
  amenities: string[];
}

interface PropertyCardProps {
  property: Property;
  onBook: (property: Property) => void;
}

export default function PropertyCard({ property, onBook }: PropertyCardProps) {
  return (
    <div className="bg-white rounded-2xl border border-slate-200 overflow-hidden shadow-sm hover:shadow-xl transition-all duration-300 group flex flex-col justify-between">
      <div>
        {/* Image Thumbnail */}
        <div className="relative h-48 w-full overflow-hidden bg-slate-100">
          <img
            src={property.image}
            alt={property.title}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
          />
          <div className="absolute top-3 left-3 bg-brand-500 text-white text-[11px] font-extrabold px-2.5 py-1 rounded-full shadow-md uppercase tracking-wider">
            {property.category}
          </div>
          <div className="absolute top-3 right-3 bg-white/90 backdrop-blur-md text-amber-600 text-xs font-bold px-2.5 py-1 rounded-full shadow flex items-center gap-1">
            <Star className="w-3.5 h-3.5 fill-amber-500 text-amber-500" />
            {property.rating}
          </div>
        </div>

        {/* Card Content */}
        <div className="p-4">
          <div className="flex items-center gap-1 text-slate-500 text-xs font-semibold mb-1">
            <MapPin className="w-3.5 h-3.5 text-brand-500" />
            <span>{property.city}</span>
            <span className="mx-1">•</span>
            <span className="text-emerald-600 font-bold flex items-center gap-0.5">
              <CheckCircle2 className="w-3 h-3" /> Verified Host
            </span>
          </div>

          <h3 className="font-bold text-base text-slate-900 line-clamp-1 group-hover:text-brand-500 transition-colors">
            {property.title}
          </h3>

          <p className="text-xs text-slate-500 mt-1 line-clamp-1">
            Hosted by <span className="font-semibold text-slate-700">{property.seller}</span>
          </p>

          {/* Amenities Chips */}
          <div className="flex flex-wrap gap-1.5 mt-3">
            {property.amenities.map((amenity, idx) => (
              <span key={idx} className="bg-slate-100 text-slate-600 text-[10px] font-medium px-2 py-0.5 rounded-md">
                {amenity}
              </span>
            ))}
          </div>
        </div>
      </div>

      {/* Card Footer */}
      <div className="p-4 pt-0 mt-2 border-t border-slate-100 flex items-center justify-between">
        <div>
          <span className="text-xl font-extrabold text-brand-500">${property.price}</span>
          <span className="text-xs text-slate-500 font-medium"> / night</span>
        </div>

        <button
          onClick={() => onBook(property)}
          className="bg-brand-500 hover:bg-brand-600 text-white font-bold text-xs px-4 py-2.5 rounded-xl transition-all shadow-md hover:shadow-brand-500/25 flex items-center gap-1.5"
        >
          <Calendar className="w-3.5 h-3.5" />
          <span>Book Now</span>
        </button>
      </div>
    </div>
  );
}
