package com.licenta.web.rest;

import com.licenta.domain.Review;
import com.licenta.repository.ReviewRepository;
import com.licenta.service.ReviewService;
import com.licenta.service.dto.ReviewDTO;
import com.licenta.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.licenta.domain.Review}.
 */
@RestController
@RequestMapping("/api")
public class ReviewResource {

    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    private static final String ENTITY_NAME = "review";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReviewService reviewService;

    private final ReviewRepository reviewRepository;

    public ReviewResource(ReviewService reviewService, ReviewRepository reviewRepository) {
        this.reviewService = reviewService;
        this.reviewRepository = reviewRepository;
    }

    /**
     * {@code POST  /reviews} : Create a new review.
     *
     * @param reviewDTO the reviewDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reviewDTO, or with status {@code 400 (Bad Request)} if the review has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reviews")
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) throws URISyntaxException {
        log.debug("REST request to save Review : {}", reviewDTO);
        if (reviewDTO.getIdReview() != null) {
            throw new BadRequestAlertException("A new review cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReviewDTO result = reviewService.save(reviewDTO);
        return ResponseEntity
            .created(new URI("/api/reviews/" + result.getIdReview()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getIdReview().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reviews/:idReview} : Updates an existing review.
     *
     * @param idReview the id of the reviewDTO to save.
     * @param reviewDTO the reviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewDTO,
     * or with status {@code 400 (Bad Request)} if the reviewDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reviews/{idReview}")
    public ResponseEntity<ReviewDTO> updateReview(
        @PathVariable(value = "idReview", required = false) final Long idReview,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Review : {}, {}", idReview, reviewDTO);
        if (reviewDTO.getIdReview() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idReview, reviewDTO.getIdReview())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewRepository.existsById(idReview)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReviewDTO result = reviewService.save(reviewDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reviewDTO.getIdReview().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reviews/:idReview} : Partial updates given fields of an existing review, field will ignore if it is null
     *
     * @param idReview the id of the reviewDTO to save.
     * @param reviewDTO the reviewDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reviewDTO,
     * or with status {@code 400 (Bad Request)} if the reviewDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reviewDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reviewDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reviews/{idReview}", consumes = "application/merge-patch+json")
    public ResponseEntity<ReviewDTO> partialUpdateReview(
        @PathVariable(value = "idReview", required = false) final Long idReview,
        @RequestBody ReviewDTO reviewDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Review partially : {}, {}", idReview, reviewDTO);
        if (reviewDTO.getIdReview() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(idReview, reviewDTO.getIdReview())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reviewRepository.existsById(idReview)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReviewDTO> result = reviewService.partialUpdate(reviewDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reviewDTO.getIdReview().toString())
        );
    }

    /**
     * {@code GET  /reviews} : get all the reviews.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reviews in body.
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviews(Pageable pageable) {
        log.debug("REST request to get a page of Reviews");
        Page<ReviewDTO> page = reviewService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reviews/:id} : get the "id" review.
     *
     * @param id the id of the reviewDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reviewDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reviews/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        log.debug("REST request to get Review : {}", id);
        Optional<ReviewDTO> reviewDTO = reviewService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reviewDTO);
    }

    /**
     * {@code DELETE  /reviews/:id} : delete the "id" review.
     *
     * @param id the id of the reviewDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.debug("REST request to delete Review : {}", id);
        reviewService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/last/reviews")
    public ResponseEntity<List<Review>> getLastReviews(Pageable pageable) {
        Page<Review> page = reviewService.findLastReviews(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
