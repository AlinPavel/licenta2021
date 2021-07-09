package com.licenta.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReviewDTO.class);
        ReviewDTO reviewDTO1 = new ReviewDTO();
        reviewDTO1.setIdReview(1L);
        ReviewDTO reviewDTO2 = new ReviewDTO();
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO2.setIdReview(reviewDTO1.getIdReview());
        assertThat(reviewDTO1).isEqualTo(reviewDTO2);
        reviewDTO2.setIdReview(2L);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
        reviewDTO1.setIdReview(null);
        assertThat(reviewDTO1).isNotEqualTo(reviewDTO2);
    }
}
