import { Admin, EditGuesser, ListGuesser, Resource } from 'react-admin';
import polyglotI18nProvider from 'ra-i18n-polyglot';
import translation from './i18n/translation';
import authProvider from './providers/authProvider';
import dataProvider from './providers/dataProvider';
import Dashboard from './Dashboard';

// let socket = null;
// try {
//   let host = document.location.host;
//   let pathname = document.location.pathname;
//   console.log('host', host);
//   console.log('pathname', pathname);
//   socket = new WebSocket('ws://localhost:8084/socket/blabla');
// } catch (exception) {
//   console.log(exception);
// }

function App() {
  const i18nProvider = polyglotI18nProvider(locale => translation[locale], 'fr');
  return (
    <Admin dashboard={Dashboard} authProvider={authProvider} i18nProvider={i18nProvider} dataProvider={dataProvider}>
      <Resource name="user" list={ListGuesser} edit={EditGuesser}/>
    </Admin>
  );
}

export default App;
