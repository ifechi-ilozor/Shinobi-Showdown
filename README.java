package Indy;

/** Things to note:
 * 
 * Instead of having separate Naruto & Sasuke classes, I have one Shinobi
 * class that shows up as Naruto or Sasuke depending on the arguments passed in the 
 * constructor. I initially had separate classes, but all of the code was essentially the same
 * except for the name. Having one Shinobi class made the multiplayer feature much easier to
 * implement and also allowed me to delete repeated code.
 * 
 * The searching and running is similar to Pacman's in the way that the computer is looking
 * for the closest or farthest point from the target. But unlike Pacman, my algorithm doesn't queue
 * up possible locations. Instead there is a helper method that returns an arrayList of possiblities. 
 * For searching, the character controlled by the computer cannot move in the opposite direction
 * but for running the character can because if suddenly the other character attacks, I wanted
 * the computer character to be able to run back the way it came. 
 * 
 * For overall computer control, the actions are controlled by if statements and queue of actions
 * in a timeline. I decided it made no sense for a the computer character to attack if it was nowhere
 * near the other character, so the timeline that runs makes the computer character find the other
 * until it gets within a certain range, and when it's in that range, the computer will dequeue an action.
 * However, the computer character doesn't always execute the dequeued action. If, for example, the 
 * computer character is able to super attack and is in range, the computer character
 * will super attack and not use the dequeued action. 
 * */
 