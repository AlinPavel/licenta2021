package com.licenta.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Catalog.class);
        Catalog catalog1 = new Catalog();
        catalog1.setIdCatalog(1L);
        Catalog catalog2 = new Catalog();
        catalog2.setIdCatalog(catalog1.getIdCatalog());
        assertThat(catalog1).isEqualTo(catalog2);
        catalog2.setIdCatalog(2L);
        assertThat(catalog1).isNotEqualTo(catalog2);
        catalog1.setIdCatalog(null);
        assertThat(catalog1).isNotEqualTo(catalog2);
    }
}
