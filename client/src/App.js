import React, { useState } from 'react';
import './css/App.css';
import Adventure from './Adventure.jsx';
import AdventureSelector from './AdventureSelector.jsx';
import AdventureClient from './AdventureClient.js';
import './css/fontawesome.css';

function App() {
  const api = new AdventureClient("/api");
  const [adventure, setAdventure] = useState(null);
  return adventure
    ? <Adventure api={api} name={adventure} />
    : <AdventureSelector api={api} onSelect={setAdventure} />
    ;
}

export default App;
