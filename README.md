# DesignBook - Chapter 6 Exercises (15 to 18)

Dự án Java triển khai trọn vẹn các bài tập từ 15 đến 18 thuộc **Chapter 6: Design Patterns** trong sách *Introduction to Software Design with Java* của Martin P. Robillard.

## Cấu trúc thư mục

```
DesignBook-Chapter6/
├── .project                     # Cấu hình dự án Eclipse
├── .classpath                   # Classpath Eclipse (JavaSE)
├── .settings/                   # Compiler settings (Java 17+)
├── bin/                         # Bytecode đã biên dịch
└── src/
    └── chapter6/
        ├── AllExercisesDemo.java  # Chạy thử toàn bộ các bài 15-18 cùng lúc
        ├── common/              # Các lớp miền cơ sở dùng chung
        │   ├── Day.java
        │   ├── Show.java
        │   ├── NullShow.java
        │   ├── Movie.java
        │   ├── Concert.java
        │   ├── Command.java
        │   └── UndoableCommand.java
        ├── exercise15/          # Bài 15: Undoable Command
        │   ├── Program.java
        │   └── Exercise15.java
        ├── exercise16/          # Bài 16: CommandProcessor & Law of Demeter
        │   ├── CommandProcessor.java
        │   └── Exercise16.java
        ├── exercise17/          # Bài 17: CommandProcessor hỗ trợ Redo
        │   ├── CommandProcessor.java
        │   └── Exercise17.java
        └── exercise18/          # Bài 18: Đóng gói vào Program & Tránh God Class
            ├── CommandProcessor.java (Interface)
            ├── BasicCommandProcessor.java
            ├── Program.java
            └── Exercise18.java
```
