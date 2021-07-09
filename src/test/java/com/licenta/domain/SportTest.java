package com.licenta.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sport.class);
        Sport sport1 = new Sport();
        sport1.setIdSport(1L);
        Sport sport2 = new Sport();
        sport2.setIdSport(sport1.getIdSport());
        assertThat(sport1).isEqualTo(sport2);
        sport2.setIdSport(2L);
        assertThat(sport1).isNotEqualTo(sport2);
        sport1.setIdSport(null);
        assertThat(sport1).isNotEqualTo(sport2);
    }
}
