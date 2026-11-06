# BÁO CÁO THỰC HÀNH: LAB 08
**MÔN HỌC:** THIẾT KẾ PHẦN MỀM (SOFTWARE DESIGN)  
**Chủ đề:** Tiến trình kiến trúc Command Pattern, Undo/Redo & Law of Demeter (Series bài tập 6.15 – 6.18)

---

### Thông tin sinh viên
* **Họ và tên:** Văn Thanh Trà
* **Mã số sinh viên (MSSV):** 4651050286
* **Giáo trình:** *Introduction to Software Design with Java* (Martin P. Robillard - Chapter 6)
* **Môi trường thực hành:** Eclipse IDE (Temurin-17 / JDK-26)

---

## I. YÊU CẦU VÀ BỐI CẢNH CHUỖI BÀI TẬP (6.15 – 6.18)

Chuỗi bài tập 6.15 đến 6.18 đại diện cho một tiến trình tiến hóa kiến trúc phần mềm kinh điển xoay quanh mẫu thiết kế **Command Pattern** trên nền tảng lớp `Program` (quản lý lịch chiếu các suất diễn `Show` cho các ngày `Day` trong tuần):

1. **Nền tảng (Ex 6.13 - 6.14):** Lịch trình được tổ chức qua `EnumMap<Day, Show>`. Ngày không có suất diễn được chuẩn hóa bằng mẫu **Null Object** (`NullShow`). Các thao tác sửa đổi được chuyển thành các đối tượng `Command` thông qua Factory Methods.
2. **Yêu cầu Lab 08:** Xây dựng cơ chế **Hoàn tác (Undo)** và **Làm lại (Redo)** hoàn chỉnh, phân tách tầng quản lý lịch sử lệnh độc lập, bảo vệ nguyên lý Demeter (**Law of Demeter**), và tái tích hợp vào Domain Model mà không biến `Program` thành một **God Class**.

---

## II. BÁO CÁO PHƯƠNG PHÁP GIẢI QUYẾT TỪNG BÀI TẬP

### 1. Exercise 6.15: Lệnh có khả năng hoàn tác (Undoable Command)
* **Mục tiêu:** Mở rộng thiết kế Command để mọi hành động biến đổi lịch trình đều có thể hoàn tác (`undo`).
* **Phương pháp giải quyết:**
  * **Mở rộng giao diện:** Định nghĩa `interface UndoableCommand extends Command` bổ sung phương thức `void undo()`.
  * **Kỹ thuật chụp trạng thái (State Capture):** Mỗi command (viết bằng Anonymous Inner Class) tự lưu snapshot dữ liệu cần thiết trước khi thực thi:
    * **Add (`createAddCommand`):** Lưu lại show hiện tại (`previousShow`). Khi `undo()`, nếu vị trí đó trước đó có show thì phục hồi lại, nếu không thì trả về ngày trống.
    * **Remove (`createRemoveCommand`):** Lưu vết show sắp xóa: `show = aShows.get(pDay)`. Khi `undo()`, thêm lại chính show đó: `add(show, pDay)`.
    * **Clear (`createClearCommand`):** Sao chép toàn bộ ánh xạ trước khi xóa: `previousShows = new EnumMap<>(aShows)`. Khi `undo()`, phục hồi nguyên trạng bằng `aShows.putAll(previousShows)`.
  * **Bảo toàn tính đóng gói:** Toàn bộ phương thức sửa đổi trạng thái gốc (`add`, `remove`, `clear`) của `Program` được chuyển thành `private`. Client bắt buộc phải tương tác thông qua Command.

### 2. Exercise 6.16: CommandProcessor & Tuân thủ Law of Demeter
* **Mục tiêu:** Tách biệt trách nhiệm thực thi và lưu trữ lịch sử lệnh khỏi Client; đảm bảo cấu trúc thiết kế tuân thủ nghiêm ngặt **Law of Demeter**.
* **Phương pháp giải quyết:**
  * **Phân tách trách nhiệm (SRP):** Tạo lớp trung gian `CommandProcessor` đóng vai trò Invoker. Lớp này nắm giữ danh sách lịch sử lệnh đã thực thi: `List<UndoableCommand> aCommands = new ArrayList<>()`.
  * **Cung cấp API mức cao:**
    * `consume(UndoableCommand pCommand)`: Thực thi lệnh (`pCommand.execute()`) và lưu vào ngăn xếp.
    * `undoLast()`: Rút phần tử trên cùng ra khỏi ngăn xếp và gọi `command.undo()`.
  * **Tuân thủ Law of Demeter (LoD):** Client không được phép gọi chuỗi lồng nhau kiểu `processor.getHistory().getLast().undo()`. Toàn bộ cấu trúc ngăn xếp và thuật toán hoàn tác nằm trọn vẹn trong `CommandProcessor`. Client chỉ tương tác thông qua `consume()` và `undoLast()`.

### 3. Exercise 6.17: Nâng cấp CommandProcessor hỗ trợ Redo (Two-Stack Model)
* **Mục tiêu:** Mở rộng bộ xử lý lệnh để hỗ trợ thực thi lại (`redo`) các hành động vừa bị Undo.
* **Phương pháp giải quyết:**
  * **Kiến trúc Two-Stack Model:** `CommandProcessor` duy trì đồng thời 2 ngăn xếp riêng biệt:
    * `aExecutedCommands`: Lưu trữ các lệnh đã và đang có hiệu lực.
    * `aUndoneCommands`: Lưu trữ các lệnh vừa bị Undo và sẵn sàng để Redo.
  * **Chu trình luân chuyển lệnh khép kín:**
    * Khi `undoLast()`: Rút lệnh từ đỉnh `aExecutedCommands` $ightarrow$ gọi `command.undo()` $ightarrow$ đẩy sang đỉnh `aUndoneCommands`.
    * Khi `redoLast()`: Rút lệnh từ đỉnh `aUndoneCommands` $ightarrow$ gọi `command.execute()` $ightarrow$ đẩy ngược lại đỉnh `aExecutedCommands`.
    * **Tính nhất quán lịch sử:** Khi client kích hoạt một lệnh mới qua `consume()`, toàn bộ ngăn xếp `aUndoneCommands` bắt buộc phải bị xóa trắng (`clear()`) để tránh xung đột lịch sử rẽ nhánh.

### 4. Exercise 6.18: Đóng gói vào Program & Giải pháp tránh "God Class"
* **Mục tiêu:** Đóng gói `CommandProcessor` vào trong `Program` để Client có thể gọi trực tiếp `program.add()`, `program.undoLast()` mà không làm `Program` biến thành God Class.
* **Phương pháp giải quyết:**
  * **Vấn đề God Class:** Nếu đưa trực tiếp các danh sách ngăn xếp lệnh và thuật toán Undo/Redo vào bên trong `Program`, `Program` sẽ vi phạm nghiêm trọng nguyên lý Đơn trách nhiệm (**SRP**).
  * **Giải pháp kiến trúc:**
    1. **Tách Interface (Decoupling):** Trích xuất `interface CommandProcessor` định nghĩa hợp đồng độc lập (`consume`, `undoLast`, `redoLast`), hiện thực bằng `BasicCommandProcessor`.
    2. **Tiêm phụ thuộc (Dependency Injection):** `Program` nhận `CommandProcessor` thông qua Constructor: `public Program(CommandProcessor pProcessor)`. Thiết kế này tuân thủ nguyên lý **DIP** và **OCP**, cho phép thay thế chiến lược xử lý lệnh mà không cần sửa `Program`.
    3. **Mẫu thiết kế Facade & Ủy quyền (Delegation):** Chuyển các hàm tạo command thành `private`. Các phương thức public của `Program` (`add`, `remove`, `clear`, `undoLast`, `redoLast`) đóng vai trò Facade: tự đóng gói command nội bộ và ủy quyền (*delegate*) việc thực thi cho `CommandProcessor`.

---

## III. BẢNG TỔNG HỢP TIẾN TRÌNH TIẾN HÓA KIẾN TRÚC (EVOLUTION MATRIX)

| Bài tập | Trọng tâm bài toán | Mẫu thiết kế & Nguyên lý | Ưu điểm kiến trúc |
| :--- | :--- | :--- | :--- |
| **Ex 6.15** | Lệnh có thể hoàn tác (Undo) | Command Pattern, State Capture (Snapshot) | Mỗi lệnh tự quản lý khả năng hoàn tác độc lập. |
| **Ex 6.16** | Quản lý lịch sử thực thi & Law of Demeter | Invoker Pattern, Law of Demeter (LoD) | Client không cần tự quản lý stack; cấu trúc ngăn xếp được giấu kín. |
| **Ex 6.17** | Hỗ trợ Redo hai chiều | Two-Stack Architecture (Executed & Undone) | Hỗ trợ đầy đủ luân chuyển Undo/Redo với cơ chế dọn ngăn xếp an toàn. |
| **Ex 6.18** | Đóng gói vào Domain Model mà không tạo God Class | Facade Pattern, Delegation, Dependency Injection (SRP, DIP) | Client có API tự nhiên nhất (`program.add()`); Domain Model và Command Engine tách rời hoàn toàn. |

---

## IV. KẾT QUẢ THỰC NGHIỆM VÀ KIỂM THỬ TRÊN ECLIPSE

Toàn bộ 4 bài tập đã được biên dịch và chạy kiểm thử tự động thành công 100% trên Eclipse IDE:

```text
=== EXERCISE 15 DEMO: Undoable Commands ===
--- 1. Executing Add Movie 1 (Monday) ---
MONDAY     : Inception (2010)
WEDNESDAY  : Interstellar (2014)
--- 4. Executing Clear -> Toàn bộ lịch trở về trống ---
=== UNDOING COMMANDS ===
--- Undo Clear -> Khôi phục lịch phim đầy đủ
--- Undo Remove Monday -> Khôi phục phim Inception
Exercise 15 completed successfully!

=== EXERCISE 16 DEMO: CommandProcessor with Undo ===
processor.consume(addMovie1);
processor.consume(addMovie2);
processor.undoLast(); // Khôi phục trạng thái gần nhất
Can undo further? false
Exercise 16 completed successfully!

=== EXERCISE 17 DEMO: CommandProcessor with Redo ===
processor.undoLast(); // Hoàn tác
processor.redoLast(); // Làm lại thành công
Can redo further? false
Exercise 17 completed successfully!

=== EXERCISE 18 DEMO: Encapsulated Program with Undo/Redo ===
Program program = new Program(new BasicCommandProcessor());
program.add(new Movie("The Dark Knight", 2008, 152), Day.MONDAY);
program.add(new Concert("Live at Wembley", "Queen", 120), Day.FRIDAY);
program.undoLast(); // Gọi trực tiếp trên Program, hoàn tác show Friday
program.redoLast(); // Làm lại show Friday
Exercise 18 completed successfully!
```

---

## V. KẾT LUẬN VÀ BÀI HỌC THIẾT KẾ
1. **Sức mạnh của Command Pattern:** Biến các yêu cầu thành đối tượng (First-class objects) cho phép lưu trữ, trì hoãn thực thi và đảo ngược hành động một cách tinh tế.
2. **Tầm quan trọng của Law of Demeter:** Giúp bảo vệ Client khỏi các thay đổi cấu trúc lưu trữ nội bộ bên trong Invoker.
3. **Giải quyết mâu thuẫn giữa Tiện ích API và Phân tách trách nhiệm:** Sự kết hợp giữa Facade Pattern, Delegation và Dependency Injection giúp cung cấp API tiện lợi mà vẫn giữ hệ thống tuân thủ chặt chẽ SRP và tránh hoàn toàn nguy cơ biến Domain Class thành God Class.
