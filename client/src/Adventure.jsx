import React, { useEffect, useState } from 'react';
import './css/Adventure.css';

function renderSceneButton(api, onClick, adventure, scene, selected, active) {
  let className = "sceneButton";
  if(selected) {
    className += " selected";
  }
  if(active) {
    className += " active";
  }
  return (
    <div className={className} key={scene.name} onClick={onClick}>
      <div className="sceneThumbnail">
        <img src={api.sceneThumbnailUrl(adventure, scene.name)} alt={scene.name} />
      </div>
      <p className="sceneCaption">{scene.name}</p>
    </div>
  )
}

function Adventure(props) {
  const [scenes, setScenes] = useState([]);
  const [selected, setSelected] = useState(-1);
  const [active, setActive] = useState(null);
  const [key, setKey] = useState(null);
  const [muted, setMuted] = useState(false);
  const select = step => {
    if(selected + step < 0) {
      setSelected(scenes.length-1);
    } else {
      setSelected((selected + step) % scenes.length);
    }
  };

  useEffect(() => {
    props.api.loadAdventure(props.name).then(a => setScenes(a.scenes));
    const keyHandler = e => setKey(e.keyCode)
    window.addEventListener('keyup', keyHandler);
    return () => {
      window.removeEventListener('keyup', keyHandler)
    };
  }, [props.api, props.name]);

  switch(key) {
    case 37: // left
      select(-1);
      break;
    case 39: // right
      select(1);
      break;
    case 13:
      props.api.setScene(props.name, scenes[selected].name);
      setActive(scenes[selected].name);
      break;
    default:
      // TODO: hotkeys 1-9 for scenes
  }
  if(key) {
    setKey(null);
  }

  return (
    <div className="adventure">
      <h2>{props.name}</h2>
      <div className="sceneControls">
        <div className="sceneList">
          {scenes.map((scene, i) =>
            renderSceneButton(
              props.api,
              () => {
                props.api.setScene(props.name, scene.name);
                setSelected(i);
                setActive(scene.name);
              },
              props.name,
              scene,
              selected === i,
              active === scene.name
            ))
          }
        </div>
        <div className="sceneSidebar">
          <button
            className={`fas fa-volume-${muted ? "mute" : "up"}`}
            onClick={e => {
              props.api.setMuted(!muted);
              setMuted(!muted);
              e.target.blur();
            }}
          />
        </div>
      </div>
    </div>
  );
}

export default Adventure;