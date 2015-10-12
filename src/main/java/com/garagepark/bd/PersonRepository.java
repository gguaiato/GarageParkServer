package com.garagepark.bd;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import com.garagepark.bd.model.Person;

public class PersonRepository extends Repository<Person> {

	private static Logger logger = Logger.getLogger(PersonRepository.class);
	private static final long serialVersionUID = 926118319362559453L;
	private static PersonRepository instance;

	private PersonRepository() {
		super();
	}

	public static PersonRepository instance() {
		if (instance == null)
			instance = new PersonRepository();
		return instance;
	}

	public Person findByNusp(String nusp) {
		return findUniqueByCriteria(Restrictions.eq("nusp", nusp));
	}

	public boolean insert(Person person) {
		Person existingPerson = findByNusp(person.getNusp());
		if (existingPerson == null) {
			logger.info("Inserindo usuario na base de dados com nusp = "
					+ person.getNusp());
			save(person);
			return true;
		}
		return false;
	}

	public boolean update(Person person) {
        if (person != null) {
            logger.info("Atualizando usuario na base de dados com nusp = "
                    + person.getNusp());
            save(person);
            return true;
        }
        return false;
    }

	public boolean delete(String nusp) {
		Person existingPerson = findByNusp(nusp);
		if (existingPerson != null) {
			logger.info("Deletando usuario na base de dados com nusp = "
					+ nusp);
			delete(existingPerson);
			return true;
		}
		return false;
	}

	public List<Person> list(int limitQtd) {
		return super.list(limitQtd);
	}
}
