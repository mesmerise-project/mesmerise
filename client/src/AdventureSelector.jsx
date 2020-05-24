import React, { useEffect, useState } from "react";
import './css/AdventureSelector.css';

function AdventureSelector(props) {
  const onSelect = props.onSelect;
  const [adventures, setAdventures] = useState([])
  useEffect(() => {
    props.api.listAdventures().then(setAdventures);
  }, [props.api])
  return (
    <div className="adventureSelector">
      <h2>Choose Your Adventure</h2>
      <ul className="adventureList">
        {adventures.map((a, i) =>
          <li className="adventureButton" key={i} onClick={
            () => onSelect(adventures[i])
          }>{a}</li>
        )}
      </ul>
    </div>
  );
}

export default AdventureSelector;