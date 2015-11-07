package com.garagepark.bd;

import com.garagepark.bd.model.ParkingSpot;
import com.garagepark.bd.model.Person;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class ParkingSpotRepository extends Repository<ParkingSpot> {

	private static Logger logger = Logger.getLogger(ParkingSpotRepository.class);
	private static final long serialVersionUID = 926118319362559453L;
	private static ParkingSpotRepository instance;

	private ParkingSpotRepository() {
		super();
	}

	public static ParkingSpotRepository instance() {
		if (instance == null)
			instance = new ParkingSpotRepository();
		return instance;
	}

	public ParkingSpot findByName(String name) {
		return findUniqueByCriteria(Restrictions.eq("name", name));
	}

	public boolean insert(ParkingSpot parkingSpot) {
		ParkingSpot existingParkingSpot = findByName(parkingSpot.getName());
		if (existingParkingSpot == null) {
			logger.info("Inserindo parking spot na base de dados com name = "
					+ parkingSpot.getName());
			save(parkingSpot);
			return true;
		}
		return false;
	}

	public boolean update(ParkingSpot parkingSpot) {
        if (parkingSpot != null) {
            logger.info("Atualizando parking spot na base de dados com name = "
                    + parkingSpot.getName());
            save(parkingSpot);
            return true;
        }
        return false;
    }

	public boolean delete(String name) {
		ParkingSpot existingParkingSpot = findByName(name);
		if (existingParkingSpot != null) {
			logger.info("Deletando parking spot na base de dados com nusp = "
					+ name);
			delete(existingParkingSpot);
			return true;
		}
		return false;
	}

	public List<ParkingSpot> list(int limitQtd) {
		return super.list(limitQtd);
	}
}
