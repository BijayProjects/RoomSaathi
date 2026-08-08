'use client';

import React, { useState } from 'react';
import PropertyCard, { Property } from '@/components/PropertyCard';
import BookingModal from '@/components/BookingModal';
import { Search, MapPin, Filter, SlidersHorizontal } from 'lucide-react';

const mockAllProperties: Property[] = [
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
    amenities: ['WiFi 100Mbps', 'Power Backup', 'AC']
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
    amenities: ['Balcony View', 'Full Kitchen', 'Parking']
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
    amenities: ['Private Garden', 'Jacuzzi', 'Chef']
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
    amenities: ['Ergonomic Chair', 'Fiber Net', '24/7 Access']
  },
  {
    id: 'prop_105',
    title: 'Cozy Budget Room near Thamel',
    city: 'Kathmandu',
    category: 'ROOM',
    price: 30,
    rating: 4.6,
    image: 'https://images.unsplash.com/photo-1590490360182-c33d57733427?auto=format&fit=crop&w=600&q=80',
    seller: 'Bikash Tamang',
    instantBooking: true,
    amenities: ['Hot Shower', 'WiFi', 'Rooftop Terrace']
  },
  {
    id: 'prop_106',
    title: 'Lakeside Penthouse Suite',
    city: 'Pokhara',
    category: 'APARTMENT',
    price: 110,
    rating: 4.95,
    image: 'https://images.unsplash.com/photo-1512917774080-9991f1c4c750?auto=format&fit=crop&w=600&q=80',
    seller: 'Pooja Karki',
    instantBooking: true,
    amenities: ['Lake View', 'Elevator', 'Private Gym']
  }
];

export default function ExplorePage() {
  const [searchTerm, setSearchTerm] = useState('');
  const [cityFilter, setCityFilter] = useState('ALL');
  const [categoryFilter, setCategoryFilter] = useState('ALL');
  const [selectedProperty, setSelectedProperty] = useState<Property | null>(null);

  const filtered = mockAllProperties.filter((p) => {
    const matchesSearch = p.title.toLowerCase().includes(searchTerm.toLowerCase()) || p.seller.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesCity = cityFilter === 'ALL' || p.city === cityFilter;
    const matchesCategory = categoryFilter === 'ALL' || p.category === categoryFilter;
    return matchesSearch && matchesCity && matchesCategory;
  });

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-10">
      <div className="mb-8">
        <h1 className="text-3xl font-extrabold text-slate-900">Explore Stays & Rentals</h1>
        <p className="text-sm text-slate-500 mt-1">Browse verified rooms, apartments, and co-living spaces across Nepal</p>
      </div>

      {/* Filter Controls Bar */}
      <div className="bg-white p-4 rounded-2xl border border-slate-200 shadow-sm mb-8 space-y-3 md:space-y-0 md:flex md:items-center md:gap-4">
        {/* Search Input */}
        <div className="relative flex-grow">
          <Search className="w-4 h-4 absolute left-3.5 top-3 text-slate-400" />
          <input
            type="text"
            placeholder="Search property title, amenity, or host..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            className="w-full pl-10 pr-4 py-2 text-sm border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500"
          />
        </div>

        {/* City Filter */}
        <div className="flex items-center gap-2">
          <MapPin className="w-4 h-4 text-brand-500 shrink-0" />
          <select
            value={cityFilter}
            onChange={(e) => setCityFilter(e.target.value)}
            className="bg-slate-50 border border-slate-200 text-slate-800 text-xs font-bold rounded-xl px-3 py-2 focus:outline-none focus:ring-2 focus:ring-brand-500 cursor-pointer"
          >
            <option value="ALL">All Cities</option>
            <option value="Kathmandu">Kathmandu</option>
            <option value="Pokhara">Pokhara</option>
            <option value="Lalitpur">Lalitpur</option>
          </select>
        </div>

        {/* Category Filter */}
        <div className="flex items-center gap-2">
          <Filter className="w-4 h-4 text-brand-500 shrink-0" />
          <select
            value={categoryFilter}
            onChange={(e) => setCategoryFilter(e.target.value)}
            className="bg-slate-50 border border-slate-200 text-slate-800 text-xs font-bold rounded-xl px-3 py-2 focus:outline-none focus:ring-2 focus:ring-brand-500 cursor-pointer"
          >
            <option value="ALL">All Categories</option>
            <option value="ROOM">Private Rooms</option>
            <option value="APARTMENT">Apartments</option>
            <option value="VILLA">Villas</option>
            <option value="COWORKING">Coworking Desks</option>
          </select>
        </div>
      </div>

      {/* Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
        {filtered.map((p) => (
          <PropertyCard key={p.id} property={p} onBook={(prop) => setSelectedProperty(prop)} />
        ))}
      </div>

      {filtered.length === 0 && (
        <div className="text-center py-16 bg-white rounded-2xl border border-slate-200">
          <h3 className="text-base font-bold text-slate-800">No matching properties found</h3>
          <p className="text-xs text-slate-500 mt-1">Try relaxing your search terms or clearing city filters.</p>
        </div>
      )}

      {/* Booking Modal */}
      <BookingModal property={selectedProperty} onClose={() => setSelectedProperty(null)} />
    </div>
  );
}
