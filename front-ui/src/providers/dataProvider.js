import { fetchUtils } from 'react-admin';
import simpleRestProvider from 'ra-data-simple-rest';

const httpClient = (url, options = {}) => {
  if (!options.headers) {
    options.headers = new Headers({ Accept: 'application/json' });
  }

  const authInfosString = localStorage.getItem('auth');
  if (authInfosString) {
    const { token } = JSON.parse(authInfosString);
    options.headers.set('Authorization', `Bearer ${token}`);
  }

  return fetchUtils.fetchJson(url, options);
};

export default simpleRestProvider('/api', httpClient);
