package com.licenta.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogDTO.class);
        CatalogDTO catalogDTO1 = new CatalogDTO();
        catalogDTO1.setIdCatalog(1L);
        CatalogDTO catalogDTO2 = new CatalogDTO();
        assertThat(catalogDTO1).isNotEqualTo(catalogDTO2);
        catalogDTO2.setIdCatalog(catalogDTO1.getIdCatalog());
        assertThat(catalogDTO1).isEqualTo(catalogDTO2);
        catalogDTO2.setIdCatalog(2L);
        assertThat(catalogDTO1).isNotEqualTo(catalogDTO2);
        catalogDTO1.setIdCatalog(null);
        assertThat(catalogDTO1).isNotEqualTo(catalogDTO2);
    }
}
