import { defineConfig, loadEnv } from 'vite'
import react from '@vitejs/plugin-react'
import path from 'path'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '')
  // Nếu có VITE_BACKEND_URL, dùng nó. Nếu không, mặc định localhost:8080
  // Lưu ý: Nếu backend chạy trên máy khác, cần set VITE_BACKEND_URL=http://<IP-backend>:8080
  const backendUrl = env.VITE_BACKEND_URL || 'http://localhost:8080'
  
  console.log(`[Vite] Proxy target: ${backendUrl}`)

  return {
  plugins: [react()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
  define: {
    'global': 'globalThis',
  },
  optimizeDeps: {
    include: ['sockjs-client'],
  },
  server: {
    host: '0.0.0.0', 
    port: 3000,
    hmr: {
      host: 'localhost',
      port: 3000,
    },
    proxy: {
      '/api': {
        target: backendUrl,
        changeOrigin: true,
        rewrite: (path) => path, 
      },
      '/ws': {
        target: backendUrl,
        ws: true,
        changeOrigin: true,
        rewrite: (path) => path,
      },
    },
  },
  }
})
