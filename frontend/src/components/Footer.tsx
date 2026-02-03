export default function Footer() {
  return (
    <footer className="bg-gray-900 text-white mt-auto">
      <div className="container mx-auto px-4 py-8">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          <div>
            <h3 className="text-lg font-semibold mb-4">Về chúng tôi</h3>
            <p className="text-gray-400">
              Web Store - Nền tảng mua sắm trực tuyến hiện đại với đầy đủ tính năng và hỗ trợ
              khách hàng 24/7 - Nhân Viên Làng Chài yêu khách như con.
            </p>
          </div>
          <div>
            <h3 className="text-lg font-semibold mb-4">Liên kết</h3>
            <ul className="space-y-2 text-gray-400">
              <li>
                <a href="/products" className="hover:text-white transition-colors">
                  Sản phẩm
                </a>
              </li>
              <li>
                <a href="/about" className="hover:text-white transition-colors">
                  Giới thiệu
                </a>
              </li>
              <li>
                <a href="/contact" className="hover:text-white transition-colors">
                  Liên hệ
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-lg font-semibold mb-4">Hỗ trợ</h3>
            <ul className="space-y-2 text-gray-400">
              <li>
                <a href="/faq" className="hover:text-white transition-colors">
                  Câu hỏi thường gặp
                </a>
              </li>
              <li>
                <a href="/shipping" className="hover:text-white transition-colors">
                  Vận chuyển
                </a>
              </li>
              <li>
                <a href="/returns" className="hover:text-white transition-colors">
                  Đổi trả
                </a>
              </li>
            </ul>
          </div>
          <div>
            <h3 className="text-lg font-semibold mb-4">Theo dõi</h3>
            <p className="text-gray-400 mb-4">Kết nối với chúng tôi trên mạng xã hội</p>
            <div className="flex space-x-4">
              <a href="https://www.facebook.com/nguyen.hoang.nam.838688?locale=vi_VN" className="text-gray-400 hover:text-white transition-colors">
                Facebook
              </a>
              <a href="https://www.instagram.com/ngx.hgnam/" className="text-gray-400 hover:text-white transition-colors">
                Instagram
              </a>
              <a href="https://tuftedinteriors.co.uk/" className="text-gray-400 hover:text-white transition-colors">
                Twitter
              </a>
            </div>
          </div>
        </div>
        <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
          <p>&copy; 2025 Web Store. Tất cả quyền được bảo lưu.</p>
        </div>
      </div>
    </footer>
  );
}
