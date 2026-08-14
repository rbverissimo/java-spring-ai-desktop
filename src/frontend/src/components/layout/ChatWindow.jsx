import React from 'react';

const ChatWindow = ({ messages = [], isLoading = false, messagesEndRef }) => {
  return (
    <div className="w-full flex flex-col gap-6 overflow-y-auto py-2">
      {messages.length === 0 && (
        <div className="text-center text-gray-500 mt-16 text-xl">
          How can I help you today?
        </div>
      )}
      {messages.map((msg, index) => (
        <div
          key={index}
          className={`flex w-full ${
            msg.role === 'user'
              ? 'justify-end'
              : msg.role === 'assistant'
              ? 'justify-start'
              : 'justify-center'
          } px-5`}
        >
          <div
            className={`max-w-[85%] md:max-w-[75%] px-5 py-3 rounded-2xl leading-relaxed break-words whitespace-pre-wrap ${
              msg.role === 'user'
                ? 'bg-[#646cff] text-white rounded-br-none'
                : msg.role === 'assistant'
                ? 'bg-[#333] text-[#e0e0e0] rounded-bl-none'
                : 'bg-red-900/20 text-red-400 border border-red-500 text-sm'
            }`}
          >
            {msg.content}
          </div>
        </div>
      ))}
      {isLoading && (
        <div className="flex w-full justify-start italic opacity-70">
          <div className="bg-[#333] text-[#e0e0e0] px-5 py-3 rounded-2xl rounded-bl-none">
            Thinking...
          </div>
        </div>
      )}
      <div ref={messagesEndRef} />
    </div>
  );
};

export default ChatWindow;
