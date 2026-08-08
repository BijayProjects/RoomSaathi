'use client';

import React, { useState } from 'react';
import { Bot, Send, Sparkles, X, User } from 'lucide-react';

interface Message {
  sender: 'ai' | 'user';
  text: string;
}

export default function AiConciergeModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const [messages, setMessages] = useState<Message[]>([
    {
      sender: 'ai',
      text: 'Namaste! I am your RoomSaathi Gemini AI Concierge. Ask me anything about stays in Kathmandu, budget room estimates, flatmate safety guidelines, or neighborhood tips!'
    }
  ]);
  const [input, setInput] = useState('');

  if (!isOpen) return null;

  const handleSend = () => {
    if (!input.trim()) return;

    const userText = input;
    setInput('');
    setMessages(prev => [...prev, { sender: 'user', text: userText }]);

    // Smart AI response simulation
    setTimeout(() => {
      let reply = "I can help you locate verified properties in Kathmandu, Lalitpur, or Pokhara!";
      const lower = userText.toLowerCase();

      if (lower.includes('kathmandu') || lower.includes('room')) {
        reply = "For Kathmandu, verified private rooms start around $35–$45/night near Thamel or Jhamsikhel with high-speed WiFi and generator backup.";
      } else if (lower.includes('roommate') || lower.includes('flat')) {
        reply = "RoomSaathi offers flatmate matching based on budget, non-smoker preferences, quiet hours, and co-working habits!";
      } else if (lower.includes('price') || lower.includes('cost')) {
        reply = "Our rooms range from $25/night for co-working desks up to $130/night for luxury serviced villas in Pokhara.";
      }

      setMessages(prev => [...prev, { sender: 'ai', text: reply }]);
    }, 600);
  };

  return (
    <div className="fixed bottom-6 right-6 z-50 w-full max-w-sm bg-white rounded-2xl shadow-2xl border border-slate-200 overflow-hidden animate-in slide-in-from-bottom-5">
      {/* Header */}
      <div className="bg-brand-500 text-white p-3.5 flex items-center justify-between">
        <div className="flex items-center gap-2">
          <Bot className="w-5 h-5 text-amber-200" />
          <span className="font-extrabold text-sm">RoomSaathi AI Concierge</span>
        </div>
        <button onClick={onClose} className="p-1 hover:bg-white/20 rounded-full transition-colors">
          <X className="w-4 h-4" />
        </button>
      </div>

      {/* Messages */}
      <div className="p-3 h-72 overflow-y-auto space-y-2.5 bg-slate-50 text-xs">
        {messages.map((m, idx) => (
          <div key={idx} className={`flex gap-2 ${m.sender === 'user' ? 'justify-end' : 'justify-start'}`}>
            {m.sender === 'ai' && (
              <div className="w-6 h-6 bg-brand-500 text-white rounded-full flex items-center justify-center font-bold text-[10px] shrink-0">
                AI
              </div>
            )}
            <div className={`p-2.5 rounded-xl max-w-[80%] leading-relaxed ${
              m.sender === 'user' 
                ? 'bg-brand-500 text-white rounded-br-none' 
                : 'bg-white text-slate-800 border border-slate-200 shadow-sm rounded-bl-none'
            }`}>
              {m.text}
            </div>
          </div>
        ))}
      </div>

      {/* Input */}
      <div className="p-2 border-t border-slate-200 bg-white flex gap-2">
        <input
          type="text"
          placeholder="Ask AI Concierge..."
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={(e) => e.key === 'Enter' && handleSend()}
          className="w-full px-3 py-2 text-xs border border-slate-200 rounded-xl focus:outline-none focus:ring-2 focus:ring-brand-500"
        />
        <button
          onClick={handleSend}
          className="bg-brand-500 hover:bg-brand-600 text-white p-2 rounded-xl transition-colors"
        >
          <Send className="w-4 h-4" />
        </button>
      </div>
    </div>
  );
}
