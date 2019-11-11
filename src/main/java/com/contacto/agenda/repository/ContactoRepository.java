package com.contacto.agenda.repository;
import com.contacto.agenda.domain.Contacto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Contacto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {

}
