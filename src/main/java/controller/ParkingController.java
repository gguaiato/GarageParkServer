package controller;

import com.garagepark.bd.ParkingSpotRepository;
import com.garagepark.bd.PersonRepository;
import com.garagepark.bd.model.ParkingSpot;
import com.garagepark.bd.model.Person;

import java.util.List;

public class ParkingController {

    private final PersonRepository personRepository = PersonRepository.instance();
    private final ParkingSpotRepository parkingSpotRepository = ParkingSpotRepository.instance();
    private static ParkingController instance;

    private ParkingController() {}

    public static ParkingController instance() {
        if (instance == null) {
            instance = new ParkingController();
        }
        return instance;
    }

    public boolean includeParkingSPot(String name) {
        ParkingSpot parkingSpot = new ParkingSpot(name);
        return parkingSpotRepository.insert(parkingSpot);
    }

    public List<ParkingSpot> listParkingSpots(int limit) {
        return parkingSpotRepository.list(limit);
    }

    public ParkingSpot getParkingSpot(String name) {
        return parkingSpotRepository.findByName(name);
    }

    public boolean makeReservation(String parkingSpotName, String nusp) {
        Person person = personRepository.findByNusp(nusp);
        ParkingSpot parkingSpot = parkingSpotRepository.findByName(parkingSpotName);
        if (person != null && parkingSpot != null) {
            person.setParkingSpotReserved(parkingSpot);
            parkingSpot.setOwner(person);
            personRepository.update(person);
            return true;
        }
        return false;
    }

    public boolean clearReservation(String parkingSpotName) {
        ParkingSpot parkingSpot = parkingSpotRepository.findByName(parkingSpotName);
        if (parkingSpot != null) {
            Person person = parkingSpot.getOwner();
            if (person != null) {
                person.setParkingSpotReserved(null);
                parkingSpot.setOwner(null);
                personRepository.update(person);
            }
            return true;
        }
        return false;
    }
}
