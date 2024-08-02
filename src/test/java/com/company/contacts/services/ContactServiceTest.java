package com.company.contacts.services;

import com.company.contacts.entities.Contact;
import com.company.contacts.repositories.ContactRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllContacts() {
        // Given
        Contact contact1 = new Contact(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789", "123 Main St", "Engineer", "https://linkedin.com/johndoe");
        Contact contact2 = new Contact(2L, "Alice", "Smith", LocalDate.of(1985, 5, 20), "alice.smith@example.com", "987654321", "456 Oak Ave", "Manager", "https://linkedin.com/alicesmith");
        when(contactRepository.findAll()).thenReturn(Arrays.asList(contact1, contact2));


        // When
        List<Contact> contacts = contactService.getAllContacts();

        // Then
        assertThat(contacts).hasSize(2);
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    public void testGetContactById() {
        // Given
        Contact contact = new Contact(1L, "John", "Doe", LocalDate.of(1990, 1,1), "john.doe@example.com", "123456789", "123 Main St", "Engineer", "https://linkedin.com/johndoe");
        when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

        // When
        Optional<Contact> foundContact = contactService.getContactById(1L);

        // Then
        assertThat(foundContact).isPresent();
        assertThat(foundContact.get().getFirstName()).isEqualTo("John");
    }

    @Test
    public void testSaveContact() {
        // Given
        Contact contact = new Contact(1L, "Alice", "Smith", LocalDate.of(1985, 5, 20),  "alice.smith@example.com", "987654321", "456 Oak Ave", "Manager", "https://linkedin.com/alicesmith");
        when(contactRepository.save(contact)).thenReturn(contact);

        // When
        Contact savedContact = contactService.saveContact(contact);

        // Then
        assertThat(savedContact.getId()).isEqualTo(1L);
        verify(contactRepository, times(1)).save(contact);
    }

    @Test
    public void testDeleteContact() {
        // Given
        Long contactId = 1L;
        doNothing().when(contactRepository).deleteById(contactId);

        // When
        contactService.deleteContact(contactId);

        // Then
        verify(contactRepository, times(1)).deleteById(contactId);
    }
}
