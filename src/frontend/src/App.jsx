import { useState, useRef, useEffect } from 'react'
import './App.css'
import ChatSidebar from './components/layout/ChatSidebar'
import ChatHeader from './components/layout/ChatHeader'

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
      const response = await fetch(`/api/chat?message=${encodeURIComponent(input)}`)
      if (!response.ok) {
        throw new Error('Failed to fetch response from AI')
      }
      const data = await response.text()
      const aiMessage = { role: 'assistant', content: data }
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

      <header className="p-4 border-b border-gray-700 text-center">
        <h1 className="m-0 text-2xl font-bold text-[#646cff]">Coltran</h1>
      </header>
      
      <main className="flex-1 flex flex-col relative">

        <ChatHeader />

        <div className="w-full max-w-3xl flex flex-col gap-6">
          {messages.length === 0 && (
            <div className="text-center text-gray-500 mt-16 text-xl">
              How can I help you today?
            </div>
          )}
          {messages.map((msg, index) => (
            <div key={index} className={`flex w-full ${msg.role === 'user' ? 'justify-end' : msg.role === 'assistant' ? 'justify-start' : 'justify-center'}`}>
              <div className={`max-w-[85%] md:max-w-[75%] px-5 py-3 rounded-2xl leading-relaxed break-words whitespace-pre-wrap ${
                msg.role === 'user' 
                  ? 'bg-[#646cff] text-white rounded-br-none' 
                  : msg.role === 'assistant'
                  ? 'bg-[#333] text-[#e0e0e0] rounded-bl-none'
                  : 'bg-red-900/20 text-red-400 border border-red-500 text-sm'
              }`}>
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
      </main>

      <footer className="p-4 md:p-8 border-t border-gray-700 bg-[#242424] flex justify-center">
        <div className="w-full max-w-3xl flex gap-4 bg-[#333] px-4 py-2 rounded-3xl items-end shadow-lg">
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
    </div>
  )
}

export default App
