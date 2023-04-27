import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 * Collison - a class to deal with collison
 * 
 * @author Luka Lasic
 * @since 20-4-2023
 */
public class Collison {
   private boolean collided;
   private Point2D changePoint;
   private boolean moveDown;
   private boolean moveUp;
   private boolean moveLeft;
   private boolean moveRight;
   private TrackMovement tm = new TrackMovement();

   /**
    * @param collided
    */
   public void setCollided(boolean collided) {
      this.collided = collided;
   }

   /**
    * @param changePoint
    */
   public void setChangePoint(Point2D changePoint) {
      this.changePoint = changePoint;
   }

   /**
    * @return boolean
    */
   public boolean isCollided() {
      return collided;
   }

   /**
    * @return Point2D
    */
   public Point2D getChangePoint() {
      return changePoint;
   }

   /**
    * A empty deafult constructor
    */
   public Collison() {

   }

   /**
    * A simple collsion construcotr to be used as an intial start
    * 
    * @param collided
    * @param changePoint
    */
   public Collison(boolean collided, Point2D changePoint) {
      this.collided = collided;
      this.changePoint = changePoint;
   }

   /**
    * a method to check the collsion in order to make the player slide and not go
    * out of bounds
    * 
    * @param mv
    * @param mask
    * @param playerX
    * @param playerY
    * @param moveBackgroundX
    * @param moveBackgroundY
    * @return Collison
    */
   public Collison checkingCollison(MovableBackground mv, Image mask, int playerX, int playerY,
         int moveBackgroundX, int moveBackgroundY) {
      // System.out.println("Players X Cord in Collsion " + playerX);
      // System.out.println("PLayers Y Cord in Collsion " + playerY);

      // getting the players movement
      moveDown = tm.isMoveDown();
      moveUp = tm.isMoveUp();
      moveLeft = tm.isMoveLeft();
      moveRight = tm.isMoveRight();

      // making a pixel reader adjusted to get the correct x and y values
      Color collisonColor = mask.getPixelReader().getColor(playerX, playerY);
      this.collided = false;

      // checking for collsion based on the center of the image
      // if the mask if red then the player needs to be adjust
      if (collisonColor.getRed() > 0.90) {
         collided = true;
         // trying to implement stop and slide

         // if the player is moving down create slding affect if nesscary
         if (moveDown) {
            boolean slideLeft = false;
            boolean slideRight = false;
            PixelReader reader = mask.getPixelReader();

            // a group of check to see if their is a need to slide the player

            if (mask.getPixelReader().getColor(playerX + 25, playerY).getRed() > 0.90) {
               // System.out.println("Color of right: " + reader.getColor(playerX + 25,
               // playerY).getRed());
               slideLeft = true;
               // System.out.println("Slide Left: True");
            }

            if (mask.getPixelReader().getColor(playerX - 25, playerY).getRed() > 0.90) {
               // System.out.println("Color of left: " + reader.getColor(playerX - 25,
               // playerY).getRed());
               slideRight = true;
               // System.out.println("Slide Right: True);
            }

            // if both are true then the playeris hitting a flat wall and only needs to have
            // it posistion inveresed
            if (slideLeft && slideRight) {
               // System.out.println("Both Slide: True");
               return new Collison(collided, new Point2D(0, -10));// moveablebackground.getSpeed());

               // slding the player to the right
            } else if (slideRight) {

               // System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX, playerY + 10);

               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int y = playerY + 5; y > playerY - 5; y--) {
                  for (int x = playerX; x < playerX + 10; x++) {
                     // System.out.println("X Coord: " +x+ " Y coord:"+y);
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y - 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x + 1, y - 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }
               /*
                * int y = (int)goal.getY();
                * int x = (int)goal.getX();
                * while(escape){
                * 
                * if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y +
                * 1).getGreen() > 0.6) {
                * 
                * // getting the point where ther is a red pixel right next to a green one
                * point2 = new Point2D(x+1, y+1);
                * escape = false;
                * }
                * x++;
                * 
                * }
                */

               Point2D wall = point2;

               // deafult case in case math dosent work
               if (wall == null) {
                  // System.out.println("STOPP");
                  return new Collison(collided, new Point2D(0, 10));
               }
               // wall.add(point2);
               // collisionPoint.subtract(goal);
               // double length =
               // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

               /*
                * We are just making the player move in the driection of the slope of the wall
                */

               Point2D collisonToWall = wall.subtract(collisionPoint);

               // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
               // ;

               // int slideX = (int)length;//(int)(moveBackgroundX + length);
               // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

               // returning the vector of change that is need to adjust the x and y
               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));

            } else if (slideLeft) {
               // System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX, playerY + 10);
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int y = playerY + 5; y > playerY - 5; y--) {
                  for (int x = playerX; x > playerX - 10; x--) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     System.out.println("R: " + reader.getColor(x, y).getRed());
                     System.out.println("G: " + reader.getColor(x - 1, y - 1).getGreen());
                     if (reader.getColor(x, y).getRed() > 0.8 && reader.getColor(x - 1, y - 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x - 1, y - 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }
               /*
                * int y = (int)goal.getY();
                * int x = (int)goal.getX();
                * while(escape){
                * 
                * if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y +
                * 1).getGreen() > 0.6) {
                * 
                * // getting the point where ther is a red pixel right next to a green one
                * point2 = new Point2D(x+1, y+1);
                * escape = false;
                * }
                * x++;
                * 
                * }
                */

               Point2D wall = point2;
               if (wall == null) {
                  System.out.println("STOPP");
                  return new Collison(collided, new Point2D(0, 10));
               }
               // wall.add(point2);
               // collisionPoint.subtract(goal);
               // double length =
               // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

               /*
                * We are just making the player move in the driection of the slope of the wall
                */

               Point2D collisonToWall = wall.subtract(collisionPoint);

               // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
               // ;

               double length = 0;
               // int slideX = (int)length;//(int)(moveBackgroundX + length);
               // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));

            } else {
               return new Collison(collided, new Point2D(0, 10));
            }

         } else if (moveUp) {

            boolean slideLeft = false;
            boolean slideRight = false;
            PixelReader reader = mask.getPixelReader();
            // determing that if the player is mving up if the image should slide left or
            // right
            if (mask.getPixelReader().getColor(playerX + 25, playerY).getRed() > 0.90) {
               System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
               slideLeft = true;
               // System.out.println("Slide Left: True");
            }
            if (mask.getPixelReader().getColor(playerX - 25, playerY).getRed() > 0.90) {
               System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
               slideRight = true;
               // System.out.println("Slide Right: True\n\n\n\n\n\n");
            }

            if (slideLeft && slideRight) {
               System.out.println("Both Slide: True\n\n\n\n\n\n");
               return new Collison(collided, new Point2D(0, 10));// moveablebackground.getSpeed());

            } else if (slideLeft) {

               System.out.println("Slide Left IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX, playerY - 1);

               // getting vectir beacsue by subtracting to oiits is the same as getting the
               // slope in dx and dy
               Point2D vectorToGoal = collisionPoint.subtract(goal);
               System.out.println("Vector to Goal dx:" + vectorToGoal.getX() + " dy: " + vectorToGoal.getY());
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int x = playerX; x > playerX - 10; x--) {
                  for (int y = playerY - 1; y < playerY + 1; y++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x - 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x - 1, y + 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }

               Point2D wall = point2;
               if (wall == null) {
                  return new Collison(collided, new Point2D(0, 10));
               }
               Point2D vectorToWall = wall.subtract(collisionPoint);
               Point2D collisonToWall = wall.subtract(collisionPoint);

               /*
                * System.out.println("Vector to Wall dx:" + vectorToWall.getX() + " dy: " +
                * vectorToWall.getY() )
                * // wall.add(point2);
                * // collisionPoint.subtract(goal);
                * double length = vectorToGoal.dotProduct(vectorToWall);
                * int newX = (int)(goal.getX() + length * wall.getX());
                * int newY = (int)(goal.getY() + length * wall.getY());
                * System.out.println("Scaller value changing to Wall dx:" + length);
                * System.out.println("Scaller change point to Wall x:" + newX + " y: " + newY
                * );
                * 
                * Point2D newLocation = new Point2D(length, length);
                * System.out.println("Vector to Wall dx:" + newLocation.getX() + " dy: " +
                * newLocation.getY() );
                * 
                * // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);
                * Point2D collisonToWall = wall.subtract(collisionPoint);
                * System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " +
                * collisonToWall.getY() );
                * 
                * // double projection =
                * collisonToWall.dotProduct(goal.subtract(collisionPoint))
                * // ;
                * Point2D vectorToGoalGPT = goal.subtract(collisionPoint);
                * Point2D vectorToWallGPT = wall.subtract(collisionPoint);
                * double dotProduct = vectorToGoal.dotProduct(vectorToWall);
                * double lengthGPT = dotProduct / vectorToWall.magnitude();
                * Point2D newLocationGPT =
                * collisionPoint.add(vectorToWall.normalize().multiply(length));
                * System.out.println("New to Wall dx:" + newLocationGPT.getX() + " dy: " +
                * newLocationGPT.getY() );
                * Point2D vecotrMove = newLocationGPT.subtract(collisionPoint);
                * System.out.println("Move vector dx:" + vecotrMove.getX() + " dy: " +
                * vecotrMove.getY() );
                * 
                * 
                * Point2D newLocationVector = vectorToWall.normalize().multiply(length);
                * 
                * // Calculate the new location by subtracting the vector from the collision
                * point
                * Point2D newLocation2 = collisionPoint.subtract(newLocationVector);
                * System.out.println("New to Wall dx:" + newLocation2.getX() + " dy: " +
                * newLocation2.getY());
                * 
                * // Calculate the adjustments needed to move the player to the new location
                * int adjustX = (int)(newLocation2.getX() - playerX);
                * int adjustY = (int)(newLocation2.getY() - playerY);
                * System.out.println("AdjustX based off dot: " + adjustX);
                * System.out.println("AdjustY based off dot: " + adjustY);
                * // int slideX = (int)length;//(int)(moveBackgroundX + length);
                * // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]
                */

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
               // return vecotrMove;

            } else if (slideRight) {

               System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX, playerY - 10);
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int x = playerX; x < playerX + 10; x++) {
                  for (int y = playerY - 5; y < playerY + 5; y++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x + 1, y + 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }
               /*
                * int y = (int)goal.getY();
                * int x = (int)goal.getX();
                * while(escape){
                * 
                * if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y +
                * 1).getGreen() > 0.6) {
                * 
                * // getting the point where ther is a red pixel right next to a green one
                * point2 = new Point2D(x+1, y+1);
                * escape = false;
                * }
                * x++;
                * 
                * }
                */

               Point2D wall = point2;
               if (wall == null) {
                  System.out.println("STOPP");
                  return new Collison(collided, new Point2D(0, 10));
               }
               // wall.add(point2);
               // collisionPoint.subtract(goal);
               // double length =
               // collisionPoint.distance(point2);//collisionPoint.dotProduct(wall);

               /*
                * We are just making the player move in the driection of the slope of the wall
                */

               Point2D collisonToWall = wall.subtract(collisionPoint);

               // double projection = collisonToWall.dotProduct(goal.subtract(collisionPoint))
               // ;

               double length = 0;
               // int slideX = (int)length;//(int)(moveBackgroundX + length);
               // int slideY = (int)-length;//(int)((moveBackgroundY+10) - length);]

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));

            }

         } else if (moveLeft) {

            boolean slideUp = false;
            boolean slideDown = false;
            PixelReader reader = mask.getPixelReader();
            // determing that if the player is mving up if the image should slide left or
            // right
            if (mask.getPixelReader().getColor(playerX, playerY + 25).getRed() > 0.90) {
               System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
               slideUp = true;
               // System.out.println("Slide Left: True");
            }
            if (mask.getPixelReader().getColor(playerX, playerY - 25).getRed() > 0.90) {
               System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
               slideDown = true;
               // System.out.println("Slide Right: True\n\n\n\n\n\n");
            }

            if (slideUp && slideDown) {
               System.out.println("Both Slide: True\n\n\n\n\n\n");
               return new Collison(collided, new Point2D(10, 0));// moveablebackground.getSpeed());

            } else if (slideUp) {

               System.out.println("Slide UP IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX - 10, playerY);

               // getting vectir beacsue by subtracting to oiits is the same as getting the
               // slope in dx and dy
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int x = playerX + 5; x > playerX - 5; x--) {
                  for (int y = playerY - 5; y < playerY + 5; y++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     System.out.println("R: " + reader.getColor(x, y).getRed());
                     System.out.println("G: " + reader.getColor(x + 1, y - 1).getGreen());
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 2, y - 1).getGreen() > 0.5) {
                        // try {
                        System.out.println("THE GOD IS DIED");
                        // Thread.sleep(1000);

                        // } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        // e.printStackTrace();
                        // }
                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x + 2, y - 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }

               Point2D wall = point2;
               if (wall == null) {
                  return new Collison(collided, new Point2D(10, 0));
               }
               Point2D vectorToWall = wall.subtract(collisionPoint);
               Point2D collisonToWall = wall.subtract(collisionPoint);
               System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " + collisonToWall.getY());

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));

            } else if (slideDown) {

               System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX - 10, playerY);
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int x = playerX; x < playerX + 10; x++) {
                  for (int y = playerY - 5; y < playerY + 5; y++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x + 1, y + 1).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x + 1, y + 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }

               Point2D wall = point2;
               if (wall == null) {
                  System.out.println("STOPP");
                  return new Collison(collided, new Point2D(0, 10));
               }

               /*
                * We are just making the player move in the driection of the slope of the wall
                */

               Point2D collisonToWall = wall.subtract(collisionPoint);

               double length = 0;

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
            }
         } else if (moveRight) {
            boolean slideUp = false;
            boolean slideDown = false;
            PixelReader reader = mask.getPixelReader();
            // determing that if the player is mving up if the image should slide left or
            // right
            if (mask.getPixelReader().getColor(playerX, playerY - 25).getRed() > 0.90) {
               System.out.println("Color of right: " + reader.getColor(playerX + 25, playerY).getRed());
               slideDown = true;
               // System.out.println("Slide Left: True");
            }
            if (mask.getPixelReader().getColor(playerX, playerY + 25).getRed() > 0.90) {
               System.out.println("Color of left: " + reader.getColor(playerX - 25, playerY).getRed());
               slideUp = true;
               // System.out.println("Slide Right: True\n\n\n\n\n\n");
            }

            if (slideUp && slideDown) {
               System.out.println("Both Slide: True\n\n\n\n\n\n");
               return new Collison(collided, new Point2D(-10, 0));// moveablebackground.getSpeed());

            } else if (slideDown) {

               System.out.println("Slide UP IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX + 10, playerY);

               // getting vectir beacsue by subtracting to oiits is the same as getting the
               // slope in dx and dy
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int x = playerX + 1; x < playerX + 10; x++) {
                  for (int y = playerY - 5; y < playerY + 10; y++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     System.out.println("R: " + reader.getColor(x, y).getRed());
                     System.out.println("G: " + reader.getColor(x - 2, y).getGreen());
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x - 2, y).getGreen() > 0.6) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x - 2, y);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }

               Point2D wall = point2;
               if (wall == null) {
                  return new Collison(collided, new Point2D(-10, 0));
               }
               Point2D vectorToWall = wall.subtract(collisionPoint);
               Point2D collisonToWall = wall.subtract(collisionPoint);
               System.out.println("collisonToWall dx:" + collisonToWall.getX() + " dy: " + collisonToWall.getY());

               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));

            } else if (slideUp) {

               System.out.println("Slide RIGHT IS ACTIVE Slide: True\n\n\n\n\n\n");
               boolean escape = false;
               // trying to do slide
               // getting the colliosn point based on players current posistion
               Point2D collisionPoint = new Point2D(playerX, playerY);
               Point2D point2 = null;
               // where the player is going to be
               Point2D goal = new Point2D(playerX - 10, playerY);
               // a loop that creates a search box for pixels and detecting from the players
               // posistion
               // in order to find a pixel that is green and has a red pixel next to it
               // in order to make the player slide
               for (int y = playerY - 10; y < playerY + 5; y++) {
                  for (int x = playerX; x < playerX + 10; x++) {
                     System.out.println("X Coord: " + x + " Y coord:" + y);
                     System.out.println("R: " + reader.getColor(x, y).getRed());
                     System.out.println("G: " + reader.getColor(x - 1, y - 1).getGreen());
                     if (reader.getColor(x, y).getRed() > 0.9 && reader.getColor(x - 1, y - 1).getGreen() > 0.5) {

                        // getting the point where ther is a red pixel right next to a green one
                        point2 = new Point2D(x - 1, y - 1);
                        escape = true;
                        break;
                     }

                  }
                  if (escape)
                     break;
               }

               Point2D wall = point2;
               if (wall == null) {
                  System.out.println("STOPP");
                  return new Collison(collided, new Point2D(-10, 0));
               }

               /*
                * We are just making the player move in the driection of the slope of the wall
                */

               Point2D collisonToWall = wall.subtract(collisionPoint);

               double length = 0;
               // returning the vector of change that is need to adjust the x and y

               return new Collison(collided, new Point2D(collisonToWall.getX(), collisonToWall.getY()));
            }

         }

      } else {
         // System.out.println("No collison");

      }
      // returning the case when the and making now adjustments
      return new Collison(collided, new Point2D(0, 0));

   }
}
