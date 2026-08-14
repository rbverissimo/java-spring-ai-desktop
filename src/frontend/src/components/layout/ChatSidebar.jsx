import React, { useState, useEffect } from 'react';
import { Plus } from 'lucide-react';
import { api } from '../../api/client';

const ChatSidebar = () => {
    const [recentChats, setRecentChats] = useState([]);

    const fetchConversations = async () => {
        try {
            const data = await api.get('/api/chat/conversations');
            setRecentChats(data);
        } catch (error) {
            console.error('Error fetching conversations:', error);
        }
    };

    useEffect(() => {
        fetchConversations();
    }, []);

    return (
        <aside className="w-64 bg-gray-950 p-4 flex flex-col border-r border-gray-800">

            <button className="flex items-center gap-2 border border-gray-700 rounded-lg p-3 hover:bg-gray-800 transition colors mb-4">
                <Plus size={18} />
                <span>New Chat</span>
            </button>

            <div className="flex-1 overflow-y-auto">
                <p className="text-xs font-semibold text-gray-500 uppercase tracking-wide mb-3">Recent</p>
                <div className="space-y-2">
                    {recentChats.map((chat, index) => {
                        return (
                            <div key={chat.conversationId} className="flex item-center gap-3 p-2 rounded-md hover:bg-gray-800 cursor-pointer text-sm text-gray-300">
                                <span className="truncate">{chat.title}</span>
                            </div>
                        );
                    })}
                </div>
            </div>
        </aside>
    );
};

export default ChatSidebar;