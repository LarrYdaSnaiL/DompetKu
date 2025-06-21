-- Ekstensi ini diperlukan untuk fungsi enkripsi password seperti crypt() dan gen_salt().
CREATE
EXTENSION IF NOT EXISTS pgcrypto;

-- Tabel Pengguna (Users)
-- Menyimpan informasi login untuk setiap pengguna.
CREATE TABLE users
(
    user_id       SERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabel Kategori (Categories)
-- Menyimpan kategori pengeluaran yang dibuat oleh pengguna.
CREATE TABLE categories
(
    category_id SERIAL PRIMARY KEY,
    user_id     INT NOT NULL,
    name        VARCHAR(100) NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key ke tabel users
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE
);

-- Tabel Transaksi (Transactions)
-- Menyimpan semua catatan pemasukan dan pengeluaran.
CREATE TABLE transactions
(
    transaction_id   SERIAL PRIMARY KEY,
    user_id          INT NOT NULL,
    category_id      INT,
    description      TEXT,
    amount           NUMERIC(15, 2) NOT NULL,
    type             VARCHAR(7) NOT NULL CHECK (type IN ('income', 'expense')),
    transaction_date DATE NOT NULL,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key ke tabel users
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (user_id)
            ON DELETE CASCADE,

    -- Foreign Key ke tabel categories
    CONSTRAINT fk_category
        FOREIGN KEY (category_id)
            REFERENCES categories (category_id)
            ON DELETE SET NULL
);