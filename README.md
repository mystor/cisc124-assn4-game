Assignment 4 - Nexus Defense
============================

Installation
------------
Run the following bash script on the terminal to compile "Nexus Defense"

	javac Assn4.java EffectManager.java Explosion.java GameFrame.java GamePanel.java MainMenu.java Ship.java SpriteManager.java

The code should now be compiled.  Your folder should contain a `README.md` file, a bunch of `.java` files, a bunch of `.class` files, and an `assets` folder containing a series of `.png` files.

To run the game, run the following command:

	java Assn4.class

Documentation
-------------
Every function in the program is documented using `JavaDoc` style comments.  You can find them by browsing the source files.
This documentation is written in markdown (see `README.md`).

Features
--------
I have added many additional features to the game beyond those required, the following are some of them:

 * Added a Main Menu
 * Added multiple game difficulties (Easy, Medium, Hard and Insane)
 * When game is launched, Main Menu is correctly hidden
 * Confirm dialog is displayed when user attempts to end a game session currently in progress
	* Game is paused while Confirm Dialog is displayed
 * Ships are represented by graphical sprites rather than squares and circles
	* Ships with a blue visor are 'normal' ships, while ships with a red visor are 'fast' ships and move faster
 * When ships collide, rather than simply growing they form links with eachother
	* Links are represented by light pink pulsating lines
	* A ship cannot link with another ship more than once, each ship pair can only form a single link
	* The more links ships have between eachother, the larger the ships are
	* Larger ships are worth more points when destroyed
	* When ships experience 5 or more links, they gain a red crystal graphic and become stationary
	* Stationary ships are indestructable
	* When ships are destroyed, all links with that ship are destroyed.  This can make ships destructable again
 * There are two forms of score in the game, 'life' and 'score'
	* Your life begins at a maximum.  The game is over when it runs out.
	* Life decreases over time due to ships currently on the screen.  The larger the ship, the more damage it does
	* Score is obtained for destroying ships.  It is rewarded based on the number of links which the ship currently has.

Specification Conformance
-------------------------
This game conforms closely to the specifications outlined in the assignment.  The following are the requirements followed by how "Nexus Defense" conforms.

> Instead of moving squares only, your program must display both squares and circles. Your program must choose at random between a square and a circle each time. You could implement this by making Shape an abstract class with subclasses called Square and Circle. Or you could just include a boolean instance variable in the Shape class to tell you whether a shape is a square or a circle. You don't have to change the rules for overlapping shapes; for circles, just consider the "bounding square" of the circle (the smallest square that can be drawn around the circle).

 * I use two different sprites for two different ship types instead of squares and circles.  I have confirmed with the professor that this modification is acceptable
 * I only consider the bouding square around the circular ship sprites for collisions and overlapping shapes.
 * I use an integer variable inside of the ship class to identify the type of shape.  It assumes one of two constant values.

> If the user clicks on a moving shape (or inside the bounding square of a moving circle), the shape should disappear. A click on an unmoving red shape should have no effect. To accomplish this, add a method called inside to your shape class that takes an x and a y coordinate as parameters and returns true if that point is inside the shape (or its bounding square if the shape is a circle). Then add a MouseListener to catch mouse clicks inside the inner panel. For each mouse click, loop through the list of shapes to see if any has been "hit".

 * When you click on a moving ship, it is destroyed and an explosion animation is played. 
 * When the ship has gained 5 links, and becomes unmoving, it will no longer respond to these clicks and is invuneurable.

> Add a "stop" button that will end the program.

 * There is a stop button in the game interface which will prompt the player, and then return them to the Main Menu.
 * On the main menu there is a Quit Game button which will end the program

> Add a "pause" button that will pause the program. When in paused mode, the shapes on the screen should still be visible but should not be moving. Mouse clicks should have no effect in paused mode -- the player should only be able to earn points by destroying moving shapes. In paused mode, the text on the button should change to "resume". When the player clicks the button again, the shapes should start moving again and the text on the button should change back to "pause". (Hint: remember that you can stop a timer and start it again.)

 * There is a pause button in the game window.  It will pause the game.  
 * When paused, no shapes will move, however they will remain visible.
 * Mouse clicks on the game surface will have no effect while the game is paused
 * The button text changes to "resume" when the game is paused
 * The player earns no points when the game is paused

> Make your program keep score:

>> Use a label or un-editable text field to display the score. The display should be updated every time the score changes.

 * A label is used to display both the current life of the Nexus, as well as the player's score


>> The score should start out at zero and the player should earn points for destroying shapes according to their size. The player shouldn't get any points for clicking on a stopped (red) shape. As long as a shape is not stopped, the player should get more points for bigger shapes.

 * The player's score begins at zero.
 * The player gains points by destroying ships
 * The ships give more points for larger ships (more connections)
 * Ships which are stopped cannot be destroyed and give no points

>> A shape at its initial size should be worth 1 point. Each time the shape grows it should be worth an additional point. So, for example, a shape that has had two collisions so far is worth 3 points. This introduces some strategy; you can decide not to destroy a small shape in hopes of getting more points later after it has had a few collisions with other shapes. But the risk is that the shape will have a collision before you can destroy it and then you'll get no points and you'll have an annoying red shape sitting in the way and causing more frequent collisions.

 * This scoring system is implemented for the most part.  Ships are worth `1 + links` points when they are destroyed.
 * Ships which make too many collisions become stationary and cause massive life drain, which is a annoyance to the player.

> Some extra features may involve changing the scoring system, but you may not change the basic ideas that the player gets points for destoying moving shapes and that bigger shapes are worth more.

 * I am following these ideas, the player will get points for destroying moving ships, and bigger ships (more links) are indeed worth more.

> Think of a way to incorporate an editable text field into the game and to use its contents. Here's a simple idea: include a text field into which the player can type their name. (Include a label so that a player will know what the text field is for.) Include the player's name in the score display or somewhere else in the GUI. The displayed name shouldn't change until the the user types "Enter" inside the text field or clicks an "OK" button if you decide to include one next to the field. Make sure you aren't displaying a partially-typed name. If you think of a different way to use a text field, that's fine too; the point is just to practice creating a text field and using its contents.

 * On the main menu there is an editable text field which allows you to enter the name of your nexus.  This name is then used when in game in the window's title, as well as in the score label.

> Layout: You may decide how to lay out the components in the GUI. You must use at least one panel in addition to the panel holding the moving shapes, and you must use at least two different kinds of layout manager in the program.

 * There are many different panels used to lay out all sections of the game, both in the main menu and in the game.
 * The main menu uses a `BorderLayout` as its base, with a `FlowLayout` embedded in the top section to hold the graphic, another `BorderLayout` in the center with the buttons in a `GridLayout` in the `NORTH` position.  The center position of this `BorderLayout` is used to create spacing between the Play buttons and the Quit button.
 * The game uses a `BorderLayout` at its base, with another `BorderLayout` at the top of the screen to hold the buttons and score.  The score is held in a 4-column wide `GridLayout`.

> Colors and Fonts: You must set the foreground color of at least one component to something other than the default. You must also set at least one background color and at least one font to something other than the default. You can do all three of these with one component, or make one change to each of three different components. All that matters is that you practice using each of these changes at least once.

 * Many components have the background color set (for example, the play buttons, the background of the main menu, the stop button)
 * The editable Nexus name TextField has its foreground color set to make it more easily visible.
 * The font of the `Play Game` text is set to be a monospaced font

Credit
------
Concept for game by Margaret Lamb, Professor at Queens University.  CISC 124.

Game code, graphics, and design by Michael Layzell, Student at Queens University.  CISC 124.

