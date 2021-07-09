package com.licenta.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.licenta.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Review.class);
        Review review1 = new Review();
        review1.setIdReview(1L);
        Review review2 = new Review();
        review2.setIdReview(review1.getIdReview());
        assertThat(review1).isEqualTo(review2);
        review2.setIdReview(2L);
        assertThat(review1).isNotEqualTo(review2);
        review1.setIdReview(null);
        assertThat(review1).isNotEqualTo(review2);
    }
}
