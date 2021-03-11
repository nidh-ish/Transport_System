package demo19059;

import java.lang.*;
import base.*;

class TruckDemo extends Truck {

    @Override
    public String getTruckName() {                       // This function returns the name of the truck
        return "Truck19059";
    }

    @Override
    public synchronized void enter(Highway hwy) {       // This function sets the value of the current highway the truck is in
        highwy = hwy;
    }

    @Override
    protected synchronized void update(int deltaT) {         // This function is called in every deltaT time to update the status/position of the truck

        this.currenttime += deltaT;                          // Every time this function is called we increase the currenttime by deltaT

        if(this.currenttime < getStartTime()) {              // If the current time is less than start time, the it does nothing
            this.setStartTime(this.getStartTime() - deltaT); // start time is update to startime - deltaT
			return;
		}

        if (this.getLoc().getX() == this.getSource().getX() && this.getLoc().getY() == this.getSource().getY()) {   // This condition checks if the truck is at the source
            if(this.currenttime >= this.getStartTime()) {                                                           // and currentime is greater than the starttime of the truck
                if (Network.getNearestHub(this.getSource()).add(this)) {                                            // We find the nearest hub from this loction and add truck to the queue of the hub
                    this.setLoc(Network.getNearestHub(this.getSource()).getLoc());                                  // If it is added successfully then we set the location of the truck as the location of that hub
                }
            }
        } 

        else if (reachddest) {                                                                                      // If the truck has reached its destination then we set the location of the truck
            this.setLoc(this.getDest());                                                                            // as its destination location which we get using getDest method
        }          

        else if (highwy != null) {                             // This condition checks if the truck is in the highway
    
            if((((highwy.getMaxSpeed() * deltaT) / 1000.0) * ((highwy.getMaxSpeed() * deltaT) / 1000.0)) >= this.getLoc().distSqrd(highwy.getEnd().getLoc())) {
            // This condition checks if the truck in on a highway and if the distance covered is greater than than the distance between the current location of the truck and the hub that it is going towards
            // Basically if the truck is near the hub/about to reach the hub
                Hub destHub = Network.getNearestHub(this.getDest()); // destHub stores the hub nearest to the destination of the truck (destination hub)
                Location destloc = destHub.getLoc();                 // destloc contains the location of the destination hub
                int destX = destloc.getX();                          // These two varibles contain the x and y coordinates of the destination hub
                int destY = destloc.getY();

                Hub nexthub = highwy.getEnd();                       // nexthub contains the end hub of the highway the truck was travelling in
                Location nextloc = nexthub.getLoc();                 // nextloc conatins the location of this hub 
                int nextX = nextloc.getX();                          // These two variables conatin the x and y coordinates of this hub
                int nextY = nextloc.getY();    

                if (destX == nextX && destY == nextY) {              // we check if the hub the truck is reaching is the destination hub of the truck
                    this.setLoc(destloc);                            // if yes, we set the location of the truck as its destination location
                    reachddest = true;                               // and make the flag as true that indicates that the truck has reached the destination
                }
                   
                if (nexthub.add(this)) {                             // We add this truck to the nexthub
                    highwy.remove(this);                             // If added successfully, then we remove the truck from the highway
                    this.setLoc(nextloc);                            // and set the location of the truck as the location of this hub
                }
    
                else {
                    this.setLoc(nextloc);                            // If none of the above conditon satisfies, then we simply set the location of the truck as the location of the hub that it is going towards(nexthub)
                }
          
            }

            else {                                                     // If the truck is still on highway 

                int x1 = this.getLoc().getX();                         // We get the x and y coordinates of the truck
                int y1 = this.getLoc().getY();
 
                int x2 = highwy.getEnd().getLoc().getX();              // We get the x and y coordinates of the hub this highway is connected to/the hub the truck is going towards
                int y2 = highwy.getEnd().getLoc().getY();
        
                double speed = highwy.getMaxSpeed();                   // We get the speed of the truck on the highway
                double distance = (speed * deltaT) / 1000.0;           // We find the distance travelled 
        
                double hypotenuse = Math.sqrt(this.getLoc().distSqrd(highwy.getEnd().getLoc())); // We calculate hypotenuse, base, height in order to get sin thetha and cos thetha 
                double base = x2 - x1;
                double height = y2 - y1;

                x1 += (int) (distance *  (base / hypotenuse));        // now we add cos of distance travelled to the x coordinate of the truck
                y1 += (int) (distance * (height / hypotenuse));       // and sin of distance travelled to the y coordinate of the truck

                Location update = new Location(x1, y1);               // Now we get the location of the truck and update it using the set location
                this.setLoc(update);     
            }  
        }    

    }

    @Override
    public Hub getLastHub() {               // This method gives us the hub the truck is coming from/starting hub of the highway
        return highwy.getStart();
    }

    private Highway highwy = null;          // This contains the value assigned by the enter method
    private int currenttime = 0;            // This maintains the current time
    private Boolean reachddest = false;     // This checks if the truck has reached the destination
}
