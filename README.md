# DompetKu.
DompetKu merupakan aplikasi desktop yang dikembangkan untuk mendukung proses pengelolaan keuangan pribadi secara sistematis, aman, dan berbasis data. Aplikasi ini menawarkan berbagai fitur yang dirancang untuk membantu pengguna dalam mencatat aktivitas finansial sehari-hari dan menghasilkan laporan yang dapat digunakan untuk analisis serta pengambilan keputusan yang lebih tepat dalam pengelolaan keuangan.

## Fitur Utama

### Akumulasi Pengeluaran dan Pemasukan
Pengguna dapat melihat pemasukan, pengeluaran, dan saldo saat ini dari pengguna.

### Visualisasi Pengeluaran berdasarkan Kategori
Tiap pengeluaran yang terjadi, dihitung dan divisualisasikan melalui PieChart berdasarkan kategori yang ada.

### Histori Pemasukan dan Pengeluaran
Pemasukan dan Pengeluaran pengguna dimasukkan dalam bentuk tabel agar dapat di-track secara langsung oleh pengguna.

### Laporan Bulanan
Seluruh pemasukan dan pengeluaran setiap bulannya dapat dikeluarkan dalam bentuk teks dan akumulasi saldo di bulan yang dipilih.

## Instalasi dan Dependencies

### Instalasi
#### Clone Repository
```
git clone https://github.com/LarrYdaSnaiL/UAS-PBO_DompetKu_CDM.git
```

#### Buka IntelliJ IDE
Pastikan sudah terpasang beberapa hal ini di perangkat anda:
* [Java JDK 24](https://www.google.com/search?q=https://www.oracle.com/java/technologies/downloads/%23jdk24&authuser=1)
* [Maven](https://maven.apache.org/download.cgi)
* [PostgreSQL](https://www.postgresql.org/download/)
* [Extension `pgcrypto` pada PostgreSQL](https://www.postgresql.org/docs/current/pgcrypto.html)

#### Jalankan Aplikasi
Menggunakan Maven:
```
mvn javafx:run
```
atau Jalankan file: `Main.java`

### Dependencies
#### Dependencies Aplikasi
* javafx-controls: Digunakan untuk menyediakan semua komponen antarmuka pengguna (UI) standar dalam JavaFX, seperti tombol, label, dan tabel.
* javafx-fxml: Digunakan untuk memungkinkan pemisahan antara desain antarmuka (dalam format XML) dengan logika aplikasi (dalam kode Java).
* junit-jupiter-api: Digunakan untuk menyediakan anotasi dan fungsi yang diperlukan untuk menulis unit test menggunakan framework JUnit 5.
* junit-jupiter-engine: Digunakan sebagai mesin yang menemukan dan menjalankan test yang telah ditulis menggunakan JUnit Jupiter API.
* postgresql: Digunakan sebagai driver JDBC (Java Database Connectivity) yang memungkinkan aplikasi Java untuk terhubung dan berinteraksi dengan database PostgreSQL.

#### Dependencies Database
* pgcrypto (PostgreSQL Extension): Ini bukan library Java, melainkan sebuah modul tambahan untuk database PostgreSQL.
    * Fungsi: Digunakan untuk menyediakan berbagai fungsi kriptografi langsung di dalam database. Dalam proyek ini, pgcrypto secara spesifik dipakai untuk melakukan hashing dan verifikasi password secara aman.
    * Implementasi: Dependensi ini diaktifkan melalui perintah `CREATE EXTENSION IF NOT EXISTS pgcrypto;` di dalam file `database.sql`. Fungsi `crypt()` dan `gen_salt()` dari ekstensi ini kemudian dipanggil dari kode Java (LoginController.java) saat registrasi dan login.

## [Link Youtube Demo dan 3 Pilar PBO](https://youtu.be/OppfgqG1cVk)