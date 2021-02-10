import { Admin, EditGuesser, ListGuesser, Resource } from 'react-admin';
import simpleRestProvider from 'ra-data-simple-rest';
import polyglotI18nProvider from 'ra-i18n-polyglot';
import translation from './i18n/translation';

function App() {
  const i18nProvider = polyglotI18nProvider(locale => translation[locale], 'fr');
  const dataProvider = simpleRestProvider('/api');
  return (
    <Admin i18nProvider={i18nProvider} dataProvider={dataProvider}>
      <Resource name="user" list={ListGuesser} edit={EditGuesser}/>
    </Admin>
  );
}

export default App;
