import { NextResponse } from 'next/server';

export async function GET() {
  return NextResponse.json(
    {
      app_name: 'RoomSaathi',
      current_version: '1.0.0',
      latest_version: '1.1.0',
      update_available: true,
      mandatory: false,
      download_url: 'https://roomsaathi.app/downloads/roomsaathi-v1.1.0.apk',
      file_size: '28 MB',
      release_date: 'August 2026',
      release_notes: 'Official RoomSaathi Next.js Web Platform launch, real-time booking updates, QR passes, and Gemini AI Concierge.',
    },
    {
      headers: {
        'Access-Control-Allow-Origin': '*',
        'Access-Control-Allow-Methods': 'GET, POST, OPTIONS',
      },
    }
  );
}
