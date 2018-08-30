# Java2DGameEngine
-------------------------------------------------------
# Before wising up
-------------------------------------------------------
A tank game written from using java's vast library.
Done so with the purpose to learn graphics rendering, game logic, sound management, and network management.
The game is meant to be open source, so anyone can chime in if a certain feature is wished for.

The game is built with maven. To build the game yourself run
cd to/java/tank/game
mvn install package
Which will create a shapshot in the target folder.
Run the .jar file and the game will run.

And to run it type
java -jar ./target/JavaTankGame1.0-SNAPSHOT.jar
-----------------------
# DISCONTINUED
-----------------------
After many hours of frustrations this approach ended up being ditched. 

# So, what happened?
First I learned the limitation of Java's 2D graphics library. 
I started out tiling a map myself, using the Map Editor Tiled (www.mapeditor.org), then interpreting their file format .tmx or .json into Java, so the images could be rendered. Which worked fine at first. I decided to do some refactoring to separate logical units from graphical processing units, somewhat an architecture resembling MVC (Model View Controller). After doing this, Java started acting up with slow tile rendering, resulting in an unbearable game (thank God I don't have epilepsy). 

Now I didn't start developing knowing I needed some sort of game loop in place, it just sort of happened along the way. If the game loop was structured properly I could perhaps have salvaged my mistakes. With this said, I chose not to continue down this road, after reading much online about the hazards of using Java's Graphics library for game development. I continued forward using another simple 2D library with the name of Slick2D. 
I quickly learned that Slick2D was outdated, even though much of the library worked fine, since it was built on top of OpenGL3.3+, there were some issues that were unresolvable for a newbie mind such as my own.
The first problems started when I was trying to render Tiled map into the game, which seemed to work at times if I'd hit the right compilation (I think?), other times it simply didn't work for no obvious reason. I scattered around the documentation and the Google machine to find some answers to this debacle, to find out the Slick2D itself stopped being in development in 2013, the creator himself had jumped over to LibGDX instead. This was a stab in the heart, even though this was the case, I continued my effort to completing the game using this library. I ended up writing my own .tmx parser to accomodate for outdated code, all this did was stop all rendering in the engine, with no error codes, or way of debugging. This was the final straw. I'd had enough. It has to be said, I started out with the intentions of wanting to learn of to code rendering logic and game logic (physics included), since I was already using a graphics library to render my sprites, why even bother with using subpar libraries. I wasn't writing any graphics renderer of my own anyways. This led my to switch to one of the more popular Game Engines, Unity. Now I could focus on the actual game, instead of being bogged down by annoying buggy libraries.
I therefore refer to this library [LIBRARY] for future references to this project.


# What did I learn from this experience?
Read up on already working practices when it comes to abstract thinking. I had no clue of game loops before starting coding this game, I do now. A bit too late. In the future I'd love to write my own little game using OpenGL/Vulkan/Directx directly amd C++ as my programming language, setting up a proper game loop, with a state machine. 

I learned how to use Maven, which was interesting.
I got to apply basic maths to rendering positions and game logic, which was fun.
I learned a ton about Game Engines them self, and a little bit about 3D rendering, and the difference between 2D and 3D.
I learned not to expect to switch library at some point through the project. Read up on the project you wanna do, plan the technologies you wanna be using as well as what your game is about precisely.
