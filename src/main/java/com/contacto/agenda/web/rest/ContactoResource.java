package com.contacto.agenda.web.rest;

import com.contacto.agenda.domain.Contacto;
import com.contacto.agenda.repository.ContactoRepository;
import com.contacto.agenda.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; 
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.contacto.agenda.domain.Contacto}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ContactoResource {

    private final Logger log = LoggerFactory.getLogger(ContactoResource.class);

    private static final String ENTITY_NAME = "contacto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContactoRepository contactoRepository;

    public ContactoResource(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    /**
     * {@code POST  /contactos} : Create a new contacto.
     *
     * @param contacto the contacto to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contacto, or with status {@code 400 (Bad Request)} if the contacto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contactos")
    public ResponseEntity<Contacto> createContacto(@Valid @RequestBody Contacto contacto) throws URISyntaxException {
        log.debug("REST request to save Contacto : {}", contacto);
        if (contacto.getId() != null) {
            throw new BadRequestAlertException("A new contacto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Contacto result = contactoRepository.save(contacto);
        return ResponseEntity.created(new URI("/api/contactos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contactos} : Updates an existing contacto.
     *
     * @param contacto the contacto to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contacto,
     * or with status {@code 400 (Bad Request)} if the contacto is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contacto couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contactos")
    public ResponseEntity<Contacto> updateContacto(@Valid @RequestBody Contacto contacto) throws URISyntaxException {
        log.debug("REST request to update Contacto : {}", contacto);
        if (contacto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Contacto result = contactoRepository.save(contacto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contacto.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contactos} : get all the contactos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactos in body.
     */
    @GetMapping("/contactos")
    public ResponseEntity<List<Contacto>> getAllContactos(Pageable pageable) {
        log.debug("REST request to get a page of Contactos");
        Page<Contacto> page = contactoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contactos/:id} : get the "id" contacto.
     *
     * @param id the id of the contacto to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contacto, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contactos/{id}")
    public ResponseEntity<Contacto> getContacto(@PathVariable Long id) {
        log.debug("REST request to get Contacto : {}", id);
        Optional<Contacto> contacto = contactoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contacto);
    }

    /**
     * {@code DELETE  /contactos/:id} : delete the "id" contacto.
     *
     * @param id the id of the contacto to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contactos/{id}")
    public ResponseEntity<Void> deleteContacto(@PathVariable Long id) {
        log.debug("REST request to delete Contacto : {}", id);
        contactoRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
