package telran.java53.person.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class Address implements Serializable{
	private static final long serialVersionUID = 8612267464487811786L;

	String city;
	String street;
	Integer building;
}
