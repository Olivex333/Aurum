// Wspolna obsluga logowania w panelu administratora.
// Token JWT trzymany w sessionStorage (znika po zamknieciu karty).
(function () {
  const TOKEN_KEY = 'aurumAdminToken';

  function getToken() {
    try { return sessionStorage.getItem(TOKEN_KEY); } catch (e) { return null; }
  }

  function goToLogin() {
    try { sessionStorage.removeItem(TOKEN_KEY); } catch (e) { /* brak dostepu do storage */ }
    const back = encodeURIComponent(location.pathname);
    location.href = '/admin-login.html?next=' + back;
  }

  // fetch z naglowkiem Authorization; przy 401/403 wraca do logowania
  window.adminFetch = async function (url, options = {}) {
    const token = getToken();
    if (!token) { goToLogin(); throw new Error('Brak tokena'); }
    const headers = Object.assign({}, options.headers, { Authorization: 'Bearer ' + token });
    const resp = await fetch(url, Object.assign({}, options, { headers }));
    if (resp.status === 401 || resp.status === 403) { goToLogin(); throw new Error('Brak autoryzacji'); }
    return resp;
  };

  window.adminLogout = goToLogin;

  if (!getToken() && !location.pathname.endsWith('admin-login.html')) goToLogin();
})();
