import { Admin, ListGuesser, Resource } from 'react-admin';
import jsonServerProvider from 'ra-data-json-server';

function App() {
  const dataProvider = jsonServerProvider('https://jsonplaceholder.typicode.com');
  return (
    <Admin dataProvider={dataProvider}>
      <Resource name="users" list={ListGuesser}/>
    </Admin>
  );
}

export default App;
