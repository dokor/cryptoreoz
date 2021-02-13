import { LOGIN_PAGE } from '../routes';

const parseJwt = (token) => {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
};

export default {
  login: ({ username, password }) => {
    const request = new Request('/api/admin/session', {
      method: 'POST',
      body: JSON.stringify({ userName: username, password }),
      headers: new Headers({ 'Content-Type': 'application/json' }),
    });
    return fetch(request)
      .then(response => {
        if (response.status < 200 || response.status >= 300) {
          throw new Error(response.statusText);
        }
        return response.json();
      })
      .then(authInfos => {
        const userInfos = parseJwt(authInfos.webSessionToken);
        localStorage.setItem('auth', JSON.stringify({
          token: authInfos.webSessionToken,
          id: userInfos.idUser,
          fullName: userInfos.fullName,
          avatar: '',
        }));
        localStorage.setItem('permissions', userInfos.permissions);
      })
      .catch(() => {
        throw new Error('Identification incorrecte')
      });
  },
  checkError: (error) => {
    const status = error.status;
    if (status === 401 || status === 403) {
      localStorage.removeItem('auth');
      localStorage.removeItem('permissions');
      return Promise.reject({ redirectTo: '/credentials-required' });
    }
    // other error code (404, 500, etc): no need to log out
    return Promise.resolve();
  },
  checkAuth: () => localStorage.getItem('auth')
    ? Promise.resolve()
    : Promise.reject({ redirectTo: '/no-access' }),
  logout: () => {
    localStorage.removeItem('auth');
    localStorage.removeItem('permissions');
    return Promise.resolve(LOGIN_PAGE);
  },
  getIdentity: () => {
    try {
      const { id, fullName, avatar } = JSON.parse(localStorage.getItem('auth'));
      return Promise.resolve({ id, fullName, avatar });
    } catch (error) {
      return Promise.reject(error);
    }
  },
  getPermissions: () => {
    const role = localStorage.getItem('permissions');
    return role ? Promise.resolve(role) : Promise.reject();
  }
}
