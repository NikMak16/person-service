package telran.java53.person.service;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java53.person.dao.PersonRepository;
import telran.java53.person.dto.AddressDto;
import telran.java53.person.dto.ChildDto;
import telran.java53.person.dto.CityPopulationDto;
import telran.java53.person.dto.EmployeeDto;
import telran.java53.person.dto.PersonDto;
import telran.java53.person.dto.exceptions.PersonNotFoundException;
import telran.java53.person.model.Address;
import telran.java53.person.model.Child;
import telran.java53.person.model.Employee;
import telran.java53.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {

	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Transactional
	@Override
	public Boolean addPerson(PersonDto personDto) {
		if (personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(mapToModel(personDto));
		return true;
	}

	

	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		return mapToDto(person);
	}

	

	@Transactional
	@Override
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		personRepository.deleteById(id);
		return mapToDto(person);
	}

	@Transactional
	@Override
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setName(name);
		return mapToDto(person);
	}

	@Transactional
	@Override
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundException::new);
		person.setAddress(modelMapper.map(addressDto, Address.class));
		return mapToDto(person);
	}

	@Transactional(readOnly = true)
	@Override
	public PersonDto[] findPersonsByCity(String city) {
		return personRepository.findByAddressCityIgnoreCase(city).map(p -> mapToDto(p))
				.toArray(PersonDto[]::new);

	}

	@Transactional(readOnly = true)
	@Override
	public PersonDto[] findPersonsByName(String name) {
		return personRepository.findByNameIgnoreCase(name).map(p -> mapToDto(p))
				.toArray(PersonDto[]::new);

	}

	@Transactional(readOnly = true)
	@Override
	public PersonDto[] findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		LocalDate from = LocalDate.now().minusYears(maxAge);
		LocalDate to = LocalDate.now().minusYears(minAge);
		return personRepository.findByBirthDateBetween(from, to).map(p -> mapToDto(p))
				.toArray(PersonDto[]::new);

	}

	@Override
	public Iterable<CityPopulationDto> getCitiesPopulation() {

		return personRepository.getCityPopulation();
	}
	
	
	private PersonDto mapToDto(Person person) {
		if(person instanceof Child) {
			return modelMapper.map(person, ChildDto.class);
		}
		if(person instanceof Employee) {
			return modelMapper.map(person, EmployeeDto.class);
		}
		return modelMapper.map(person, PersonDto.class);
	}
	
	private Person mapToModel(PersonDto personDto) {
		if (personDto instanceof ChildDto) {
			return modelMapper.map(personDto, Child.class);
		}
		if (personDto instanceof EmployeeDto) {
			return modelMapper.map(personDto, Employee.class);
		}
		return modelMapper.map(personDto, Person.class);
	}
	
	@Transactional(readOnly = true)
	@Override
	public PersonDto[] findAllChildren() {
		return personRepository.findAllChildren().
				map(p -> mapToDto(p))
				.toArray(PersonDto[]::new);
	}


	@Transactional(readOnly = true)
	@Override
	public PersonDto[] findEmployeesBySalary(Integer minSalary, Integer maxSalary) {
		return personRepository.findEmployeesBySalaryBetween(minSalary, maxSalary)
				.map(p-> mapToDto(p))
				.toArray(PersonDto[]::new);
	}

	
	@Transactional
	@Override
	public void run(String... args) throws Exception {
		if (personRepository.count() == 0) {
			Person person = new Person(1000, "John", LocalDate.of(1985, 3, 11),
					new Address("Tel Aviv", "Ben Gvirol", 81));

			Child child = new Child(2000, "Mosche", LocalDate.of(2019, 7, 5), new Address("Ashkelon", "Bar Kohva", 21),
					"Shalom");
			Employee employee = new Employee(3000, "Sarah", LocalDate.of(1995, 11, 23),
					new Address("Rehovot", "Herzl", 7), "Motorola", 20_000);

			personRepository.save(person);
			personRepository.save(child);
			personRepository.save(employee);
		}

	}



	
}
