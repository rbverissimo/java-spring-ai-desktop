import React  from "react";
import { User } from 'lucide-react';


const ChatHeader = () => {
    return (
        <header className="p-4 border-b border-gray-700 flex items-center justify-between">
          <h2 className="text-lg font-medium text-[#646cff]">Coltran AI Assistant</h2>
          <div className="w-8 h-8 rounded-full bg-indigo-600 flex items-center justify-center">
            <User size={18} />
          </div>
        </header>
    );
};

export default ChatHeader;