import { Admin, EditGuesser, ListGuesser, Resource } from 'react-admin';
import polyglotI18nProvider from 'ra-i18n-polyglot';
import translation from './i18n/translation';
import authProvider from './providers/authProvider';
import dataProvider from './providers/dataProvider';
import Dashboard from './Dashboard';

function App() {
  const i18nProvider = polyglotI18nProvider(locale => translation[locale], 'fr');
  return (
    <Admin dashboard={Dashboard} authProvider={authProvider} i18nProvider={i18nProvider} dataProvider={dataProvider}>
      <Resource name="user" list={ListGuesser} edit={EditGuesser}/>
    </Admin>
  );
}

export default App;
