package com.licenta.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SportDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SportDTO.class);
        SportDTO sportDTO1 = new SportDTO();
        sportDTO1.setIdSport(1L);
        SportDTO sportDTO2 = new SportDTO();
        assertThat(sportDTO1).isNotEqualTo(sportDTO2);
        sportDTO2.setIdSport(sportDTO1.getIdSport());
        assertThat(sportDTO1).isEqualTo(sportDTO2);
        sportDTO2.setIdSport(2L);
        assertThat(sportDTO1).isNotEqualTo(sportDTO2);
        sportDTO1.setIdSport(null);
        assertThat(sportDTO1).isNotEqualTo(sportDTO2);
    }
}
