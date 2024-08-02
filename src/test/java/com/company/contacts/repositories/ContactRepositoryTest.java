package com.company.contacts.repositories;

import com.company.contacts.entities.Contact;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactRepositoryTest {

    private ContactRepository contactRepository;

    @Test
    public void testFindByFirstNameContaining() {
        // Given
        Contact contact = new Contact();
        contact.setFirstName("John");
        contact.setLastName("Doe");
        contact.setEmail("john.doe@example.com");
        contactRepository.save(contact);

        // When
        List<Contact> foundContacts = contactRepository.findByFirstNameContaining("John");

        // Then
        assertThat(foundContacts).isNotEmpty();
        assertThat(foundContacts.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    public void testSaveContact() {
        // Given
        Contact contact = new Contact();
        contact.setFirstName("Alice");
        contact.setLastName("Smith");
        contact.setEmail("alice.smith@example.com");

        // When
        Contact savedContact = contactRepository.save(contact);

        // Then
        assertThat(savedContact.getId()).isNotNull();
        assertThat(savedContact.getFirstName()).isEqualTo("Alice");
    }
}
