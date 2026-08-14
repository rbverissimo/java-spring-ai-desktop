import { useState, useRef, useEffect } from 'react'
import './App.css'
import ChatSidebar from './components/layout/ChatSidebar'
import ChatHeader from './components/layout/ChatHeader'
import ChatWindow from './components/layout/ChatWindow'
import { api } from './api/client'


function App() {
  const [input, setInput] = useState('')
  const [messages, setMessages] = useState([])
  const [isLoading, setIsLoading] = useState(false)
  const messagesEndRef = useRef(null)

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" })
  }

  useEffect(() => {
    scrollToBottom()
  }, [messages])

  const handleSend = async () => {
    if (!input.trim()) return

    const userMessage = { role: 'user', content: input }
    setMessages((prev) => [...prev, userMessage])
    setInput('')
    setIsLoading(true)

    try {

      const chatData = {
          message: userMessage.content,
          conversationId: 'default-thread'
      }

      const data = await api.post('/api/chat/', chatData);

      const aiMessage = { role: 'assistant', content: data.response }
      setMessages((prev) => [...prev, aiMessage])
    } catch (error) {
      console.error('Error:', error)
      const errorMessage = { role: 'error', content: 'Sorry, something went wrong. Please check your backend connection.' }
      setMessages((prev) => [...prev, errorMessage])
    } finally {
      setIsLoading(false)
    }
  }

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault()
      handleSend()
    }
  }

  return (
    <div className="flex h-screen bg-[#242424] text-gray-200">

      <ChatSidebar />
      
      <main className="flex-1 flex flex-col relative">

        <ChatHeader />

        <ChatWindow messages={messages} 
          isLoading={isLoading} 
          messagesEndRef={messagesEndRef}
        />
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
