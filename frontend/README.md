# Web Store Frontend

Frontend hiện đại cho ứng dụng Web Store với React, TypeScript, và WebSocket integration.

## Công nghệ sử dụng

- **React 18** - UI Framework
- **TypeScript** - Type safety
- **Vite** - Build tool nhanh
- **Tailwind CSS** - Styling hiện đại
- **React Router** - Client-side routing
- **TanStack Query (React Query)** - Data fetching & caching
- **Zustand** - State management
- **Axios** - HTTP client
- **SockJS & Stomp.js** - WebSocket cho live chat
- **Lucide React** - Icon library

## Tính năng

- ✅ Authentication (Đăng nhập/Đăng ký)
- ✅ Quản lý sản phẩm (Danh sách, Chi tiết, Tìm kiếm, Lọc)
- ✅ Giỏ hàng và Thanh toán
- ✅ Quản lý địa chỉ
- ✅ Đơn hàng
- ✅ Đánh giá sản phẩm
- ✅ Live Chat với WebSocket
- ✅ UI/UX hiện đại, responsive

## Cài đặt

```bash
cd frontend
npm install
```

## Chạy ứng dụng

```bash
npm run dev
```

Ứng dụng sẽ chạy tại `http://localhost:3000`

## Build cho production

```bash
npm run build
```

## Cấu trúc dự án

```
frontend/
├── src/
│   ├── components/      # React components tái sử dụng
│   ├── pages/           # Các trang chính
│   ├── services/        # API services
│   ├── store/           # Zustand stores
│   ├── types/           # TypeScript types
│   ├── utils/           # Utility functions
│   └── styles/          # Global styles
├── public/              # Static files
└── package.json
```

## Kết nối Backend

Frontend mặc định kết nối tới backend tại `http://localhost:8080/api/auth`

### Chạy FE + BE trên 2 máy cùng mạng LAN

- **BE**: chạy trên máy A, mở port `8080`, và cấu hình Spring Boot `server.address: 0.0.0.0`
- **FE**: chạy trên máy A hoặc máy B (máy khác trong LAN)

Để FE trỏ đúng sang IP của BE, set biến môi trường khi chạy Vite (khuyến nghị):

- **Windows PowerShell**:

```bash
$env:VITE_BACKEND_URL="http://<IP_MAY_BE>:8080"
npm run dev
```

- **macOS/Linux**:

```bash
VITE_BACKEND_URL="http://<IP_MAY_BE>:8080" npm run dev
```

Ví dụ:

```bash
$env:VITE_BACKEND_URL="http://172.31.98.187:8080"
npm run dev
```

Ghi chú:
- Nếu bạn **đã từng set `VITE_API_URL` = `http://localhost:8080/api/auth`** thì FE sẽ ưu tiên biến này và sẽ bị lỗi khi truy cập từ máy khác. Hãy xoá/đổi `VITE_API_URL` hoặc set lại theo IP.

## WebSocket Chat

Live chat sử dụng STOMP over WebSocket:
- Endpoint: `/ws/chat`
- Message destination: `/app/support-chat/message`
- Topic: `/topic/conversation.{conversationId}`

## Tài khoản mặc định

Tạo tài khoản mới thông qua trang đăng ký hoặc sử dụng API backend.

## Scripts

- `npm run dev` - Chạy development server
- `npm run build` - Build cho production
- `npm run preview` - Preview production build
- `npm run lint` - Chạy ESLint

## Ghi chú

- Đảm bảo backend đang chạy trước khi start frontend
- CORS đã được cấu hình trong `vite.config.ts`
- JWT token được lưu trong localStorage
