package telran.java53.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslPredicate;


import telran.java53.person.model.Person;

@QuerydslPredicate
public interface PersonRepository extends JpaRepository<Person, Integer>{
	Stream<Person> findByAddressCityIgnoreCase(String city);
	
	Stream<Person> findByNameIgnoreCase(String name);
	
	Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);
	
	@Query(value = "SELECT COUNT(p.id), p.city FROM java53.persons p GROUP BY p.city;",
			nativeQuery = true)
	Stream<Object[]> findPopulationInEachCity();
	//Couldnt find a way to parse data to CityPopulationDto, so used Object[]
	//where the first element is Population Number and the second is City Name.
}
