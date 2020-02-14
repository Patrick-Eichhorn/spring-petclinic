package org.springframework.samples.petclinic.system;

import com.devskiller.jfairy.Bootstrap;
import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
public class DataBasePopulator {

	private final int NUM_OWNERS_TO_ADD = 100;

	private final int PETS_PER_OWNER = 3;

	@Autowired
	private OwnerRepository ownerRepo;

	@Autowired
	private PetRepository petRepo;

	@PostConstruct
	@Transactional
	void addMoreOwnersAndPets() {

		Random numberGen = new Random(42);

		List<PetType> petTypes = petRepo.findPetTypes();

		Fairy ownerGen = Bootstrap.builder().withLocale(Locale.ENGLISH) // english owners
																		// ...
				.withRandomSeed(42).build();
		Fairy petGen = Bootstrap.builder().withLocale(Locale.FRENCH) // with french pets
				.withRandomSeed(42).build();

		for (int i = 0; i < NUM_OWNERS_TO_ADD; i++) {
			Person p = ownerGen.person();
			Owner newOwner = new Owner();
			newOwner.setAddress(p.getAddress().getAddressLine1());
			newOwner.setCity(p.getAddress().getCity());
			newOwner.setTelephone("1234567890");
			newOwner.setFirstName(p.getFirstName());
			newOwner.setLastName(p.getLastName());

			double petCount = numberGen.nextDouble() * PETS_PER_OWNER;
			for (int j = 0; j <= petCount; j++) {
				Person pe = petGen.person();
				Pet pet = new Pet();
				pet.setBirthDate(pe.getDateOfBirth());
				pet.setName(pe.getFirstName());
				pet.setType(petTypes.get((int) (numberGen.nextDouble() * petTypes.size())));
				newOwner.addPet(pet);
			}

			ownerRepo.save(newOwner);
		}
	}

}
