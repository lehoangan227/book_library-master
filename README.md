Project gồm:
- UserController: tạo người dùng, thay đổi thông tin người dùng, xóa người dùng, xem chi tiết người dùng, lấy ra danh sách người dùng (phân trang + sort nhiều điều kiện + tìm kiếm)
- RoleController: tạo mới role, cập nhật role (tạo mới và cập nhật cho phép thêm/loại bỏ user, permission cho role), xem chi tiết role, xóa role, lấy ra danh sách role (phân trang + sort nhiều điều kiện + tìm kiếm)
- PermissionController: thêm, sửa, xóa, xem chi tiết, xem danh sách(phân trang + sort nhiều điều kiện + tìm kiếm), lấy các permission từ user
- BookController: thêm, sửa(thêm và sửa cho phép thêm/loại bỏ category cho sách), xóa, xem chi tiết, xem danh sách(phân trang + sort nhiều điều kiện + tìm kiếm)
- CategoryController: thêm, sửa, xóa, xem chi tiết, xem danh sách(phân trang + sort nhiều điều kiện + tìm kiếm),lấy tất cả sách bởi category
- BorrowController: thêm, sửa, xóa, cập nhật status, xem chi tiết, xem danh sách(phân trang + sort nhiều điều kiện + tìm kiếm)
- PostController: thêm, sửa, xóa, xem chi tiết, xem danh sách(phân trang + sort nhiều điều kiện + tìm kiếm)
- CommentController: thêm, sửa, xóa, xem chi tiết, xem danh sách trong post(phân trang + sort nhiều điều kiện + tìm kiếm)
- LikeController: like, unlike, lấy tổng số like của 1 post, lấy ra các post đã like, lấy ra các user đã like post
- DashBoardController: lấy top 5 bài viết nhiều like nhất, thống kê số lượng sách bởi thể loại
- AuthenticationController: đăng nhập, đăng xuất, xác thực token, refresh token

Project có:
- Phân quyền chức năng theo file role với PreAuthorize, phân quyền dữ liệu (ở các api: thay đổi thông tin người dùng, xóa người dùng, xem chi tiết người dùng, xem chi tiết các đơn mượn sách, xem danh sách các đơn mượn sách, 
sửa post, xóa post, sửa cmt, xóa cmt, unlike)
- Validate dữ liệu (ở 1 số requestDTO)
- Đa ngôn ngữ (tiếng anh và tiếng Việt)
- Xử lý exception, lấy message theo đa ngôn ngữ
- Lập lịch (update status của các đơn mượn sách thành quá hạn nếu ngày hiện tại > ngày hết hạn, tự động xóa các refresh token lưu trong DB nếu hết hạn)
