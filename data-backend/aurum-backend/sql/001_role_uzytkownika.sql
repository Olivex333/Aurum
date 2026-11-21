-- Rola uzytkownika: 'user' (domyslnie) albo 'admin'.
-- Endpointy panelu (/api/users, dodawanie i usuwanie produktow) wymagaja roli admin.
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'user';

ALTER TABLE users
  DROP CONSTRAINT IF EXISTS users_role_check;
ALTER TABLE users
  ADD CONSTRAINT users_role_check CHECK (role IN ('user', 'admin'));

-- Nadanie roli admin wybranemu kontu (zarejestruj je najpierw w aplikacji):
-- UPDATE users SET role = 'admin' WHERE email = 'twoj@email.pl';
