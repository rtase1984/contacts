package com.company.contacts.controllers;

import com.company.contacts.entities.Contact;
import com.company.contacts.services.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        Mockito.reset(contactService);
    }

    @Test
    public void testGetAllContacts() throws Exception {
        // Given
        Contact contact1 = new Contact(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789", "123 Main St", "Engineer", "https://linkedin.com/johndoe");
        Contact contact2 = new Contact(2L, "Alice", "Smith", LocalDate.of(1985, 5, 20), "alice.smith@example.com", "987654321", "456 Oak Ave", "Manager", "https://linkedin.com/alicesmith");
        when(contactService.getAllContacts()).thenReturn(Arrays.asList(contact1, contact2));

        // When / Then
        mockMvc.perform(get("/api/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Alice"));
    }

    @Test
    public void testGetContactById() throws Exception {
        // Given
        Contact contact = new Contact(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "john.doe@example.com", "123456789", "123 Main St", "Engineer", "https://linkedin.com/johndoe");
        when(contactService.getContactById(1L)).thenReturn(Optional.of(contact));

        // When / Then
        mockMvc.perform(get("/api/contacts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testSaveContact() throws Exception {
        // Given
        Contact contact = new Contact(null, "Alice", "Smith", LocalDate.of(1985, 5, 20), "alice.smith@example.com", "987654321", "456 Oak Ave", "Manager", "https://linkedin.com/alicesmith");
        Contact savedContact = new Contact(1L, "Alice", "Smith", LocalDate.of(1985, 5, 20), "alice.smith@example.com", "987654321", "456 Oak Ave", "Manager", "https://linkedin.com/alicesmith");
        when(contactService.saveContact(any(Contact.class))).thenReturn(savedContact);

        // When / Then
        mockMvc.perform(post("/api/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contact)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Alice"));
    }

    @Test
    public void testDeleteContact() throws Exception {
        // Given
        Long contactId = 1L;
        doNothing().when(contactService).deleteContact(contactId);

        // When / Then
        mockMvc.perform(delete("/api/contacts/{id}", contactId))
                .andExpect(status().isNoContent());
    }
}
