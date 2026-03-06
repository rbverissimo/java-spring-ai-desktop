import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  server: {
    host: true,
    port: 5173,
    strictPort: true,
    hmr: false,
    watch: {
      ignored: ['**/*']
    }
  },
  build: {
    outDir: '../main/resources/static',
    emptyOutDir: true
  }
})
