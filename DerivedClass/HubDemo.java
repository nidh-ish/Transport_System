package demo19059;

import java.util.*;
import base.*;

class HubDemo extends Hub {

	public HubDemo(Location loc) {
		super(loc);
		trucks = new ArrayList<Truck> ();
	}

	@Override
	public boolean add(Truck truck) {                         // This method adds a truck to the queue of the hub                            
		if(this.trucks.size() < this.getCapacity()) {         // Checks if the queue is full             
            this.trucks.add(truck);                           // if not then it adds the truck
		    return true;                                  
        }
        return false;                                         // returns false if the queue is full
	}

	@Override
	public void remove(Truck truck) {                         // removes the truck from the queue
		this.trucks.remove(truck);
	}

	@Override
    public synchronized Highway getNextHighway(Hub last, Hub dest) {         // This function gives the highway joining the current hub and hub that leads to the destination/final hub
                                                                             // Here we pass the last hub from which the truck came and the destination hub of the truck   
        for(Highway hwy : this.getHighways()) {                              // we iterate over all the highways connected with the current hub (getHighways method provides them)       
                                                                             // we get a highway as hwy
            if(hwy.getEnd().getLoc().getX() == dest.getLoc().getX() && hwy.getEnd().getLoc().getY() == dest.getLoc().getY()) {  // If the coordinates ending hub/destination hub of hwy is equal to the coordinates of the final hub 
                return hwy;                                                                                                     // Then we return this highway as this leads us directly to the destnation hub
            }

        }
        // If we do not get any highway directly leading to us to the destination, then we will use randomized routing to select one highway that eventually leads us to the final hub
        int x = (int) (Math.random() * this.getHighways().size());       // We select a random integer in the range of the number of highways we have for this particular hub   
        Highway hwynxt = this.getHighways().get(x);                      // Now using the random integer, we select the highway corresponding to that index in the list

        if(hwynxt.getEnd().getLoc().getX() != last.getLoc().getX() && hwynxt.getEnd().getLoc().getX() != last.getLoc().getY()) {   // This condotion checks if this highway does not leads us back to the last hub
            return hwynxt;                                                                                                         // If the conditon is satisfied, then we return the highway
        }

        else {                                                          // If the above conditon fails, that is, we got the same path which we came from  
            int y = (int) (Math.random() * this.getHighways().size());  // then, we chose another random value and return the highway corresponding to that index - 1
            if(y == 0) {                                                // If the value is zero then we take the index as zero 
                return this.getHighways().get(0);                      
            }
            else {
                return this.getHighways().get(y - 1);                   // else we take the index as value - 1
            }
        }     
    }   

	@Override
    protected synchronized void processQ(int deltaT) {                                 // This method is called at each time step
        int i = 0;
        while(trucks.size() > 0) {                                                     // While there are trucks in the hub 

            Truck truck = trucks.get(i);                                               // We get a truck
            Hub nexthub = Network.getNearestHub(truck.getDest());                      // nexthub contains the hub of the truck nearest to its destination location (destination hub)
            Highway nexthigh = this.getNextHighway(this, nexthub);                     // nexthigh contins the highway joining the current hub to the hub that leads towards the nexthub
                
            if(this != nexthub) {                                                      // If the current hub is not equal to the nexthub (destination hub) 

                if(nexthigh == null) {                                                 // If there is no highway possible 
                    trucks.remove(truck);                                              // We remove the truck from the queue
                    continue;                              
                }

                if(nexthigh.hasCapacity()) {                                           // If the next highway has capacity, that is it can add more trucks
                    nexthigh.add(truck);                                               // Then we add the truck to the next highway
                    truck.enter(nexthigh);                                             // we call the enter function on this truck
                    trucks.remove(truck);                                              // We remove the truck from the queue
                }
            }

            else {                                                                    // If the current hub is equal to the nexthub(destination hub) then we set the location of the truck as the destination of the truck 
                truck.setLoc(truck.getDest());
            }
            i = (int) (Math.random() * trucks.size());                                // Since we are removing trucks from the queue, so we use counter that gives us random value according to the current size of the queue
        }
    }
    
	private ArrayList<Truck> trucks;
}
