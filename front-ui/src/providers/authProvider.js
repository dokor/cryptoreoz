import { LOGIN_PAGE } from '../routes';
import dayjs from 'dayjs';

const LOCALSTORAGE_AUTH_NAME = 'auth';
const LOCALSTORAGE_PERMISSIONS_NAME = 'permissions';

const parseJwt = (token) => {
  var base64Url = token.split('.')[1];
  var base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  var jsonPayload = decodeURIComponent(atob(base64).split('').map(function (c) {
    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
  }).join(''));

  return JSON.parse(jsonPayload);
};

const updateToken = (currentToken) => {
  const request = new Request('/api/admin/session', {
    method: 'PUT',
    body: currentToken,
    headers: new Headers({ 'Content-Type': 'text/plain' }),
  });

  return fetch(request)
    .then(response => response.json())
    .then(authInfosResponse => {
      const authInfos = parseJwt(authInfosResponse.webSessionToken);
      const userInfos = JSON.parse(localStorage.getItem(LOCALSTORAGE_AUTH_NAME));
      userInfos.token = authInfosResponse.webSessionToken;
      userInfos.exp = authInfos.exp;
      localStorage.setItem(LOCALSTORAGE_AUTH_NAME, JSON.stringify(userInfos));
      console.log('token updated')
    });
};

const expirationTokenInMinutes = (exp) => {
  const expDate = dayjs(exp * 1000);
  const now = dayjs();
  return expDate.diff(now, 'minutes');
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
        localStorage.setItem(LOCALSTORAGE_AUTH_NAME, JSON.stringify({
          exp: userInfos.exp,
          token: authInfos.webSessionToken,
          id: userInfos.idUser,
          fullName: userInfos.fullName,
          avatar: '',
        }));
        localStorage.setItem(LOCALSTORAGE_PERMISSIONS_NAME, userInfos.permissions);
      })
      .catch(() => {
        throw new Error('Identification incorrecte')
      });
  },
  checkError: (error) => {
    const status = error.status;
    if (status === 401 || status === 403) {
      localStorage.removeItem(LOCALSTORAGE_AUTH_NAME);
      localStorage.removeItem(LOCALSTORAGE_PERMISSIONS_NAME);
      return Promise.reject({ redirectTo: '/credentials-required' });
    }
    // other error code (404, 500, etc): no need to log out
    return Promise.resolve();
  },
  checkAuth: () => {
    try {
      const { token, exp } = JSON.parse(localStorage.getItem(LOCALSTORAGE_AUTH_NAME));
      const expirationInMinutes = expirationTokenInMinutes(exp);
      // token expiré
      if (expirationInMinutes < 0) {
        return Promise.reject();
      }
      // token bientot expiré donc on le regenere
      else if (expirationInMinutes < 3) {
        updateToken(token);
        return Promise.resolve();
      }
      // token encore valide pour un moment
      else {
        return Promise.resolve();
      }
    } catch (error) {
      return Promise.reject(error);
    }
  },
  logout: () => {
    localStorage.removeItem(LOCALSTORAGE_AUTH_NAME);
    localStorage.removeItem(LOCALSTORAGE_PERMISSIONS_NAME);
    return Promise.resolve(LOGIN_PAGE);
  },
  getIdentity: () => {
    try {
      const { id, fullName, avatar } = JSON.parse(localStorage.getItem(LOCALSTORAGE_AUTH_NAME));
      return Promise.resolve({ id, fullName, avatar });
    } catch (error) {
      return Promise.reject(error);
    }
  },
  getPermissions: () => {
    const role = localStorage.getItem(LOCALSTORAGE_PERMISSIONS_NAME);
    return role ? Promise.resolve(role) : Promise.reject();
  }
}
