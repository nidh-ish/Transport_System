package demo19059;

import base.*;
import java.util.*;

class NetworkDemo extends Network {
    
    public NetworkDemo() {
        this.highways = new ArrayList<Highway> ();
        this.hubs = new ArrayList<Hub> ();
        this.trucks = new ArrayList<Truck> ();
    }

    @Override 
    public synchronized void add(Hub hub) {                          // We add a hub to the list
        this.hubs.add(hub);
    }

    @Override
    public synchronized void add(Highway hwy) {                       // We add a highway to the list
        this.highways.add(hwy);
    }

    @Override
    public synchronized void add(Truck truck) {                       // We add a truck to the list 
        this.trucks.add(truck);
    }

    @Override
    public synchronized void start() {                                // Start the simulation
        for(int i = 0; i < hubs.size(); i++) {
            hubs.get(i).start();
        }
        for(int j = 0; j < trucks.size(); j++) {
            trucks.get(j).start();
        }
    }

    @Override
    public synchronized void redisplay(Display disp) {                // called by derived classes hubs, highways, trucks passing in display
        for(int i = 0; i < hubs.size(); i++) {
            hubs.get(i).draw(disp);
        }
        for(int j = 0; j < highways.size(); j++) {
            highways.get(j).draw(disp);
        }
        for(int k = 0; k < trucks.size(); k++) {
            trucks.get(k).draw(disp);
        }        
    }

    @Override
    protected synchronized Hub findNearestHubForLoc(Location loc) {   // this method finds the nearest hub for a particular location
        ArrayList<Integer> nearest = new ArrayList<Integer> ();           
        for(Hub hub : hubs) {
            nearest.add(loc.distSqrd(hub.getLoc()));                  // We maintain a list that contain squared distance of every hub from the given location
        }    
        int minindex = 0;                                             // We use this variable to store the index in the list with the minimum value 
        int minvalue = nearest.get(0);                                // Initially we assume the minimum value to the first item in the array list
        for(int i = 1; i < nearest.size(); i++) {                     // Now we iterate over all the elements in the list (size of the list is equal to the number of hubs we have)
            if(minvalue > nearest.get(i)) {                           // If we find a value that is less than the minimum value we assumed, then we replace the minimum value with it  
                minvalue = nearest.get(i);              
                minindex = i;                                         // We also store the index of the minimum value in the list
            }
        }
        return hubs.get(minindex);                                    // Now this index in the hubs list gives us the minimum distant hub from the given location               
    }

    private ArrayList<Highway> highways;
    private ArrayList<Hub> hubs;
    private ArrayList<Truck> trucks;
}
