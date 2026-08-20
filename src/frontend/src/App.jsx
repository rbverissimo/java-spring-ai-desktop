import { useState, useRef, useEffect } from 'react'
import './App.css'
import ChatSidebar from './components/layout/ChatSidebar'
import ChatHeader from './components/layout/ChatHeader'
import ChatWindow from './components/layout/ChatWindow'
import { useChatThread } from './hooks/useChatThread'

function App() {
  const [input, setInput] = useState('')
  const [activeConversationId, setActiveConversationId] = useState('default-thread')
  const [conversationsVersion, setConversationsVersion] = useState(0)
  const [answeredNewChats, setAnsweredNewChats] = useState(new Set())
  const messagesEndRef = useRef(null)

  const { messages, isLoading, isFetchingHistory, sendMessage } = useChatThread(activeConversationId)
  const prevIsLoadingRef = useRef(isLoading)

  useEffect(() => {
    if (prevIsLoadingRef.current && !isLoading) {
      // isLoading transitioned from true to false, meaning the agent finished answering
      // Only trigger if this active conversation hasn't been answered/fetched yet as a new chat
      if (activeConversationId.startsWith('chat_') && !answeredNewChats.has(activeConversationId)) {
        setAnsweredNewChats(prev => new Set(prev).add(activeConversationId))
        setConversationsVersion(v => v + 1)
      }
    }
    prevIsLoadingRef.current = isLoading
  }, [isLoading, activeConversationId, answeredNewChats])

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const handleSend = async () => {
    if (!input.trim() || isLoading) return
    const currentInput = input
    setInput('')
    await sendMessage(currentInput)
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  const handleNewChat = () => {
    const newId = 'chat_' + Date.now();
    setActiveConversationId(newId);
  }

  return (
    <div className="flex h-screen bg-[#242424] text-gray-200">

      <ChatSidebar 
        activeConversationId={activeConversationId}
        onSelectConversation={setActiveConversationId}
        onNewChat={handleNewChat}
        conversationsVersion={conversationsVersion}
      />
      
      <main className="flex-1 flex flex-col relative">

        <ChatHeader />

        {isFetchingHistory ? (
          <div className="flex-1 flex items-center justify-center text-gray-400">
            Loading conversation history...
          </div>
        ) : (
          <ChatWindow 
            messages={messages} 
            isLoading={isLoading} 
            messagesEndRef={messagesEndRef}
          />
        )}

        <footer className="p-4 md:p-8 border-t border-gray-700 bg-[#242424] flex justify-center">
          <div className="w-full max-w-1xl flex gap-4 bg-[#333] px-4 py-2 rounded-3xl items-end shadow-lg">
            <textarea
              className="flex-1 bg-transparent border-none text-white py-2 font-inherit text-base resize-none max-h-[200px] outline-none"
              value={input}
              onChange={(e) => setInput(e.target.value)}
              onKeyDown={handleKeyPress}
              placeholder="Type your message..."
              rows="1"
            />
            <button 
              className="bg-none border-none text-[#646cff] font-bold cursor-pointer px-4 py-2 transition-colors duration-200 hover:text-[#747bff] disabled:text-gray-600 disabled:cursor-not-allowed"
              onClick={handleSend} 
              disabled={isLoading || !input.trim()}
            >
              Send
            </button>
          </div>
        </footer>
      </main>
    </div>
  )
}

export default App
