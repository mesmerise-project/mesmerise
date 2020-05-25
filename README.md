Mesmerise
=========
An immersion-enhancing tool for storytelling, table-top role playing games,
improv theater, and other social activities.

What does it do?
----------------
Mesmerise is a remote-controlled image viewer and music player.
It organizes images and music tracks into _scenes_, which you can then
switch between using a web-based control panel.

Change between scenes as your story progresses or as your party travels around
the world, to heighten the immersion of listeners, players and participants.

Mesmerise is designed to run on a computer connected to a large
screen and sound device, with the game master or story teller controlling
the action remotely, using a computer, tablet, or smartphone.

Getting started
---------------

1. Download and unpack
   [the latest version](https://github.com/mesmerise-project/mesmerise/releases/latest)
   of Mesmerise.
2. Create an [adventure](#adventures) and drop it in the `adventures`
   subdirectory of the Mesmerise directory.
3. On the computer you want to display your adventures, run Mesmerise by
   double-clicking `mesmerise.jar` file in the Mesmerise directory.
4. On the device you want to control your adventures from, open a web browser
   and navigate to the address displayed on your viewer computer.

<span id="adventures"></span>
Creating adventures
-------------------
An adventure is simply a directory with the following contents:
* A directory `images` which contains all images for your adventure.
* A directory `music` which contains all music for your adventure.
  Music files must be in MP3 format.
* A file `scenes.ini`, which defines all *scenes* used by your adventure.

A scene is simply an assignment of a name to a background and a music track.
These assignments are written in `scenes.ini` file, and has the following form:

```ini
[Scene name]
background = image file name
music = music file name
```

The background file referred to by a scene needs to be placed in the `images`
directory, and the music file needs to be placed in the `music` directory.
All components of a scene are optional, so you can create silent scenes,
or scenes without a background.

Any number of scenes can be defined in the `scenes.ini` file.
For instance, to create an adventure with two scenes,
one named "Creepy Forest" and one named "Rural Village",
each with a background and a soundtrack, you would write the following
`scenes.ini`:

```ini
[Creepy Forest]
background = forest.jpg
music = generic_background_music.mp3

[Rural Village]
background = village.jpg
music = generic_background_music.mp3
```

Note how both scenes make use of the same background music.
This illustrates how scenes may share assets with each other.

Development
-----------
Mesmerise is build using Kotlin and React.
To build your own copy, you will need **make**, **Gradle** and **NPM**
installed on your system.

Once you do, check out the source code and run `make dist` to build your own
release package.

License
-------
All code and assets are Copyright © 2020 by the Mesmerise project contributors.
The application is made available to the public under the
[Affero GPL license version 3.0](https://www.gnu.org/licenses/agpl-3.0.en.html).