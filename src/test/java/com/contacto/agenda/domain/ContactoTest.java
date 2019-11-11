package com.contacto.agenda.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.contacto.agenda.web.rest.TestUtil;

public class ContactoTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Contacto.class);
        Contacto contacto1 = new Contacto();
        contacto1.setId(1L);
        Contacto contacto2 = new Contacto();
        contacto2.setId(contacto1.getId());
        assertThat(contacto1).isEqualTo(contacto2);
        contacto2.setId(2L);
        assertThat(contacto1).isNotEqualTo(contacto2);
        contacto1.setId(null);
        assertThat(contacto1).isNotEqualTo(contacto2);
    }
}
