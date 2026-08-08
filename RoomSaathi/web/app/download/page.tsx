'use client';

import React from 'react';
import { Smartphone, Download, CheckCircle2, QrCode, ShieldCheck, ArrowRight, RefreshCw } from 'lucide-react';

export default function DownloadPage() {
  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div className="text-center space-y-4 mb-10">
        <div className="w-16 h-16 bg-brand-500 text-white rounded-3xl flex items-center justify-center font-extrabold text-3xl shadow-xl mx-auto">
          R
        </div>
        <h1 className="text-3xl sm:text-4xl font-extrabold text-slate-900">
          Download RoomSaathi Android App
        </h1>
        <p className="text-sm text-slate-600 max-w-xl mx-auto">
          Get official native mobile experience with offline digital QR passes, real-time host chat notifications, and instant stay bookings.
        </p>
      </div>

      {/* Main Release Card */}
      <div className="bg-white rounded-3xl border border-slate-200 p-6 sm:p-8 shadow-xl space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center justify-between border-b border-slate-100 pb-6 gap-4">
          <div>
            <span className="bg-emerald-100 text-emerald-800 text-xs font-extrabold px-3 py-1 rounded-full uppercase tracking-wider">
              Stable Build v1.1.0
            </span>
            <h2 className="text-xl font-extrabold text-slate-900 mt-2">RoomSaathi Official Release APK</h2>
            <p className="text-xs text-slate-500">Published August 2026 • Verified Malware-Free</p>
          </div>

          <a
            href="https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk"
            className="bg-brand-500 hover:bg-brand-600 text-white font-extrabold text-sm px-6 py-3.5 rounded-xl shadow-lg hover:shadow-brand-500/25 transition-all flex items-center justify-center gap-2 shrink-0"
          >
            <Download className="w-4 h-4" />
            <span>Download APK (28 MB)</span>
          </a>
        </div>

        {/* Specs Table */}
        <div className="grid grid-cols-2 sm:grid-cols-4 gap-4 text-xs bg-slate-50 p-4 rounded-2xl border border-slate-100">
          <div>
            <span className="text-slate-500">Version</span>
            <p className="font-extrabold text-slate-900 text-sm mt-0.5">1.1.0</p>
          </div>
          <div>
            <span className="text-slate-500">File Size</span>
            <p className="font-extrabold text-slate-900 text-sm mt-0.5">28 MB</p>
          </div>
          <div>
            <span className="text-slate-500">Min Android</span>
            <p className="font-extrabold text-slate-900 text-sm mt-0.5">7.0 (Nougat)</p>
          </div>
          <div>
            <span className="text-slate-500">Architecture</span>
            <p className="font-extrabold text-slate-900 text-sm mt-0.5">Universal APK</p>
          </div>
        </div>

        {/* Release Notes */}
        <div>
          <h3 className="font-bold text-sm text-slate-900 mb-2">What&apos;s New in v1.1.0:</h3>
          <ul className="space-y-2 text-xs text-slate-600">
            <li className="flex items-start gap-2">
              <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0 mt-0.5" />
              <span><strong>Web Synchronization:</strong> Seamless account & booking sync between web platform and Android mobile app.</span>
            </li>
            <li className="flex items-start gap-2">
              <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0 mt-0.5" />
              <span><strong>Encrypted QR Check-in:</strong> Instant check-in ticket pass for instant property host validation.</span>
            </li>
            <li className="flex items-start gap-2">
              <CheckCircle2 className="w-4 h-4 text-emerald-600 shrink-0 mt-0.5" />
              <span><strong>Roommate Matchmaker:</strong> Post flatmate requests and match based on lifestyle preferences.</span>
            </li>
          </ul>
        </div>

        {/* How to Install Guide */}
        <div className="border-t border-slate-100 pt-6">
          <h3 className="font-bold text-sm text-slate-900 mb-3">How to Install APK on Android:</h3>
          <ol className="list-decimal list-inside space-y-1.5 text-xs text-slate-600 leading-relaxed">
            <li>Click <strong>Download APK</strong> button above to save the file to your phone.</li>
            <li>Open your phone&apos;s <strong>Downloads</strong> folder and tap `roomsaathi-v1.1.0.apk`.</li>
            <li>If prompted, allow <em>"Install from Unknown Sources"</em> in your browser settings.</li>
            <li>Tap <strong>Install</strong> and open RoomSaathi!</li>
          </ol>
        </div>
      </div>
    </div>
  );
}
