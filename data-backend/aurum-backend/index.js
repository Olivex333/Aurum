/*************************************************************************
 *  index.js (BACKEND)
 *************************************************************************/
const express = require('express');
const cors = require('cors');
const { Pool } = require('pg');
const path = require('path');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');

const SECRET_KEY = 'supersecret';
const app = express();

app.use(cors());
app.use(express.json());

const pool = new Pool({
  user: 'postgres',
  host: 'localhost',
  database: 'aurum',
  password: '123',
  port: 5432,
});

app.use(express.static(path.join(__dirname, 'public')));

app.get('/api/products', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM products ORDER BY id');
    res.json(result.rows);
  } catch (err) {
    console.error('Błąd przy pobieraniu produktów:', err);
    res.status(500).json({ error: 'Błąd serwera' });
  }
});

app.post('/api/products', async (req, res) => {
  try {
    const { name, price, colors, sizes, image_url, description } = req.body;
    if (!name || price == null || !colors) {
      return res
        .status(400)
        .json({ success: false, message: 'Wymagane pola: name, price, colors' });
    }
    const query = `
      INSERT INTO products (name, price, colors, sizes, image_url, description)
      VALUES ($1, $2, $3, $4, $5, $6)
      RETURNING id, name, price, colors, sizes, image_url, description
    `;
    const values = [
      name,
      price,
      colors,
      sizes || null,
      image_url || null,
      description || null,
    ];
    const result = await pool.query(query, values);
    return res.json({
      success: true,
      message: 'Produkt został dodany',
      product: result.rows[0],
    });
  } catch (err) {
    console.error('Błąd przy dodawaniu produktu:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.delete('/api/products', async (req, res) => {
  try {
    const { ids } = req.body;
    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ success: false, message: 'Brak listy produktów do usunięcia' });
    }
    const placeholders = ids.map((_, i) => `$${i + 1}`).join(', ');
    const query = `DELETE FROM products WHERE id IN (${placeholders})`;
    await pool.query(query, ids);
    return res.json({ success: true, message: 'Usunięto wybrane produkty' });
  } catch (err) {
    console.error('Błąd przy usuwaniu produktów:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.get('/products', async (req, res) => {
  try {
    const result = await pool.query('SELECT * FROM products');
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Błąd serwera' });
  }
});

app.get('/api/users', async (req, res) => {
  try {
    const result = await pool.query(`
      SELECT id, email, username, phone, date_of_birth
      FROM users
      ORDER BY id
    `);
    res.json(result.rows);
  } catch (err) {
    console.error(err);
    res.status(500).json({ success: false, message: 'Błąd serwera przy pobieraniu użytkowników' });
  }
});

app.post('/api/users', async (req, res) => {
  try {
    const { email, username, phone, date_of_birth } = req.body;
    if (!email || !username || !phone) {
      return res.status(400).json({ success: false, message: 'Brak wymaganych pól' });
    }
    const insertQuery = `
      INSERT INTO users (email, username, phone, date_of_birth, password)
      VALUES ($1, $2, $3, $4, 'placeholder')
      RETURNING id, email, username, phone, date_of_birth
    `;
    const values = [email, username, phone, date_of_birth || null];
    const result = await pool.query(insertQuery, values);
    return res.json({
      success: true,
      message: 'Dodano użytkownika (bez hasła)',
      user: result.rows[0],
    });
  } catch (err) {
    console.error('Błąd przy tworzeniu użytkownika:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.put('/api/users/:id/password', async (req, res) => {
  try {
    const { id } = req.params;
    const { password } = req.body;
    if (!password) {
      return res.status(400).json({ success: false, message: 'Brak hasła' });
    }
    const hashedPass = await bcrypt.hash(password, 10);
    await pool.query(`UPDATE users SET password = $1 WHERE id = $2`, [hashedPass, id]);
    return res.json({ success: true, message: 'Hasło ustawione pomyślnie' });
  } catch (err) {
    console.error('Błąd przy ustawianiu hasła:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.delete('/api/users', async (req, res) => {
  try {
    const { ids } = req.body;
    if (!Array.isArray(ids) || ids.length === 0) {
      return res.status(400).json({ success: false, message: 'Brak listy do usunięcia' });
    }
    const placeholders = ids.map((_, i) => `$${i + 1}`).join(', ');
    const query = `DELETE FROM users WHERE id IN (${placeholders})`;
    await pool.query(query, ids);
    return res.json({ success: true, message: 'Usunięto wybranych użytkowników' });
  } catch (err) {
    console.error('Błąd przy usuwaniu użytkowników:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.post('/register', async (req, res) => {
  try {
    const { email, username, phone, password, dateOfBirth } = req.body;
    if (!email || !username || !phone || !password || !dateOfBirth) {
      return res.status(400).json({ success: false, message: 'Wszystkie pola są wymagane' });
    }
    const hashedPass = await bcrypt.hash(password, 10);
    await pool.query(
      `INSERT INTO users (email, username, phone, password, date_of_birth)
      VALUES ($1, $2, $3, $4, $5)`,
      [email, username, phone, hashedPass, dateOfBirth]
    );
    return res.json({ success: true, message: 'Zarejestrowano pomyślnie' });
  } catch (err) {
    console.error(err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.post('/login', async (req, res) => {
  try {
    const { loginOrEmail, password } = req.body;
    const userResult = await pool.query(
      'SELECT * FROM users WHERE email = $1 OR username = $1',
      [loginOrEmail]
    );
    if (userResult.rows.length === 0) {
      return res.status(400).json({ success: false, message: 'Użytkownik nie istnieje' });
    }
    const user = userResult.rows[0];
    const match = await bcrypt.compare(password, user.password);
    if (!match) {
      return res.status(401).json({ success: false, message: 'Błędne hasło' });
    }
    const token = jwt.sign({ userId: user.id }, SECRET_KEY, { expiresIn: '3d' });
    return res.json({
      success: true,
      message: 'Zalogowano pomyślnie',
      token,
      user: {
        id: user.id,
        email: user.email,
        username: user.username,
        phone: user.phone
      }
    });
  } catch (err) {
    console.error(err);
    return res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.get('/users-page', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'users.html'));
});
app.get('/products-page', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'products.html'));
});

const PORT = 3000;
app.listen(PORT, () => {
  console.log(`Serwer działa na porcie ${PORT}`);
});

function verifyToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  if (!authHeader) {
    return res.status(401).json({ success: false, message: 'Brak nagłówka Authorization' });
  }
  const token = authHeader.split(' ')[1];
  if (!token) {
    return res.status(401).json({ success: false, message: 'Brak tokena' });
  }
  jwt.verify(token, SECRET_KEY, (err, decoded) => {
    if (err) {
      return res.status(403).json({ success: false, message: 'Niepoprawny token' });
    }
    req.user = decoded;
    next();
  });
}

app.get('/api/cart', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const query = `
      SELECT c.id AS cart_item_id,
             c.quantity,
             c.chosen_color,
             c.chosen_size,
             p.id AS product_id,
             p.name,
             p.price,
             p.image_url,
             p.colors,
             p.sizes,
             p.description
      FROM carts c
      JOIN products p ON c.product_id = p.id
      WHERE c.user_id = $1
      ORDER BY c.id
    `;
    const result = await pool.query(query, [userId]);
    res.json({ success: true, cartItems: result.rows });
  } catch (err) {
    console.error('Błąd przy pobieraniu koszyka:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.post('/api/cart', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const { productId, quantity, chosenColor, chosenSize } = req.body;
    const checkQuery = `
      SELECT id, quantity
      FROM carts
      WHERE user_id = $1
        AND product_id = $2
        AND (chosen_color = $3 OR (chosen_color IS NULL AND $3 IS NULL))
        AND (chosen_size = $4 OR (chosen_size IS NULL AND $4 IS NULL))
    `;
    const checkValues = [userId, productId, chosenColor || null, chosenSize || null];
    const checkResult = await pool.query(checkQuery, checkValues);
    if (checkResult.rows.length > 0) {
      const oldQty = checkResult.rows[0].quantity;
      const newQty = oldQty + (quantity || 1);
      const updateQuery = 'UPDATE carts SET quantity = $1 WHERE id = $2';
      await pool.query(updateQuery, [newQty, checkResult.rows[0].id]);
    } else {
      const insertQuery = `
        INSERT INTO carts (user_id, product_id, quantity, chosen_color, chosen_size)
        VALUES ($1, $2, $3, $4, $5)
      `;
      const insertValues = [userId, productId, quantity || 1, chosenColor || null, chosenSize || null];
      await pool.query(insertQuery, insertValues);
    }
    res.json({ success: true, message: 'Dodano do koszyka' });
  } catch (err) {
    console.error('Błąd przy dodawaniu do koszyka:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.delete('/api/cart/:cartItemId', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const cartItemId = req.params.cartItemId;
    const delQuery = 'DELETE FROM carts WHERE id = $1 AND user_id = $2';
    const result = await pool.query(delQuery, [cartItemId, userId]);
    if (result.rowCount === 0) {
      return res.status(404).json({ success: false, message: 'Nie znaleziono elementu w koszyku' });
    }
    res.json({ success: true, message: 'Usunięto z koszyka' });
  } catch (err) {
    console.error('Błąd przy usuwaniu z koszyka:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.get('/api/wishlist', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const query = `
      SELECT w.id AS wishlist_item_id,
             w.date_added,
             p.id AS product_id,
             p.name,
             p.price,
             p.image_url,
             p.colors,
             p.sizes,
             p.description
      FROM wishlist w
      JOIN products p ON w.product_id = p.id
      WHERE w.user_id = $1
      ORDER BY w.id
    `;
    const result = await pool.query(query, [userId]);
    res.json({
      success: true,
      items: result.rows
    });
  } catch (err) {
    console.error('Błąd przy pobieraniu wishlisty:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.post('/api/wishlist', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const { productId } = req.body;
    const checkQuery = `
      SELECT id FROM wishlist
      WHERE user_id = $1 AND product_id = $2
    `;
    const checkResult = await pool.query(checkQuery, [userId, productId]);
    if (checkResult.rowCount > 0) {
      return res.json({ success: true, message: 'Produkt już jest w wishlist' });
    }
    const insertQuery = `
      INSERT INTO wishlist (user_id, product_id)
      VALUES ($1, $2)
    `;
    await pool.query(insertQuery, [userId, productId]);
    res.json({ success: true, message: 'Dodano do wishlist' });
  } catch (err) {
    console.error('Błąd przy dodawaniu do wishlist:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});

app.delete('/api/wishlist/:wishlistItemId', verifyToken, async (req, res) => {
  try {
    const userId = req.user.userId;
    const wishlistItemId = req.params.wishlistItemId;
    const delQuery = 'DELETE FROM wishlist WHERE id = $1 AND user_id = $2';
    const result = await pool.query(delQuery, [wishlistItemId, userId]);
    if (result.rowCount === 0) {
      return res.status(404).json({ success: false, message: 'Nie znaleziono w wishlist' });
    }
    res.json({ success: true, message: 'Usunięto z wishlist' });
  } catch (err) {
    console.error('Błąd przy usuwaniu z wishlist:', err);
    res.status(500).json({ success: false, message: 'Błąd serwera' });
  }
});
