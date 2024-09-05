package telran.java53.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.binding.QuerydslPredicate;

import telran.java53.person.model.Person;

@QuerydslPredicate
public interface PersonRepository extends JpaRepository<Person, Integer>{
	Stream<Person> findByAddressCityIgnoreCase(String city);
	
	Stream<Person> findByNameIgnoreCase(String name);
	
	Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to);
}
