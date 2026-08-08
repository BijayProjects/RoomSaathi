// RoomSaathi Web Platform Engine
const state = {
  currentRole: 'BUYER',
  isLoggedIn: true,
  userProfile: {
    name: 'Subhash Dev',
    email: 'buyer@roomsaathi.com',
    role: 'BUYER'
  },
  apkRelease: {
    appName: 'RoomSaathi',
    version: '1.1.0',
    fileSize: '28 MB',
    releaseDate: 'August 2026',
    downloadUrl: 'https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk',
    releaseNotes: 'Enhanced Web sync, real-time booking push updates, and PWA integration.'
  },
  selectedCategory: 'ALL',
  properties: [
    {
      id: 'prop_101',
      title: 'Skyline Luxury Studio & Private Room',
      city: 'Kathmandu',
      category: 'ROOM',
      price: 45,
      rating: 4.9,
      image: 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267?auto=format&fit=crop&w=600&q=80',
      seller: 'Ananda Shrestha',
      instantBooking: true
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
      instantBooking: true
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
      instantBooking: false
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
      instantBooking: true
    }
  ]
};

document.addEventListener('DOMContentLoaded', () => {
  renderApkSection();
  renderProperties();
  setupEventListeners();
  registerServiceWorker();
});

function registerServiceWorker() {
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./sw.js').catch(err => console.log('SW registration skipped', err));
  }
}

function renderApkSection() {
  const versionEl = document.getElementById('apkVersion');
  const sizeEl = document.getElementById('apkSize');
  const dateEl = document.getElementById('apkDate');
  const linkEl = document.getElementById('apkDownloadBtn');

  if (versionEl) versionEl.innerText = `v${state.apkRelease.version}`;
  if (sizeEl) sizeEl.innerText = state.apkRelease.fileSize;
  if (dateEl) dateEl.innerText = state.apkRelease.releaseDate;
  if (linkEl) linkEl.href = state.apkRelease.downloadUrl;
}

function renderProperties() {
  const container = document.getElementById('roomGridContainer');
  if (!container) return;

  const filtered = state.properties.filter(p => {
    return state.selectedCategory === 'ALL' || p.category === state.selectedCategory;
  });

  if (filtered.length === 0) {
    container.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 40px; color: #64748B;">
      <h3>No rooms found in this category</h3>
      <p>Try switching categories or clearing search filters.</p>
    </div>`;
    return;
  }

  container.innerHTML = filtered.map(p => `
    <div class="room-card">
      <img src="${p.image}" class="room-thumb" alt="${p.title}" />
      <div class="room-body">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 6px;">
          <span style="font-size: 11px; background: #E0F2F1; color: #008080; padding: 2px 8px; border-radius: 12px; font-weight:700;">
            ${p.category}
          </span>
          <span style="font-size: 12px; font-weight:700; color: #F59E0B;">★ ${p.rating}</span>
        </div>
        <h3 class="room-title">${p.title}</h3>
        <p class="room-location">📍 ${p.city} • Verified Host ${p.seller}</p>
        <div class="room-footer">
          <div class="room-price">$${p.price} <span>/ night</span></div>
          <button class="btn btn-primary" onclick="openBookingModal('${p.id}')">Book Room</button>
        </div>
      </div>
    </div>
  `).join('');
}

function setupEventListeners() {
  const pills = document.querySelectorAll('.category-pill');
  pills.forEach(pill => {
    pill.addEventListener('click', (e) => {
      pills.forEach(p => p.classList.remove('active'));
      e.target.classList.add('active');
      state.selectedCategory = e.target.getAttribute('data-cat') || 'ALL';
      renderProperties();
    });
  });
}

function openBookingModal(propId) {
  const prop = state.properties.find(p => p.id === propId);
  if (!prop) return;
  alert(`RoomSaathi Web Checkout:\n\nProperty: ${prop.title}\nPrice: $${prop.price}/night\n\nRedirecting to secure payment checkout & QR generation...`);
}

function switchRole(role) {
  state.currentRole = role;
  state.userProfile.role = role;
  alert(`Switched to ${role} Mode on RoomSaathi Web Platform.`);
}
