import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IReview, Review } from '../review.model';

import { ReviewService } from './review.service';

describe('Service Tests', () => {
  describe('Review Service', () => {
    let service: ReviewService;
    let httpMock: HttpTestingController;
    let elemDefault: IReview;
    let expectedResult: IReview | IReview[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ReviewService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        idReview: 0,
        nota: 0,
        comentariu: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Review', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Review()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Review', () => {
        const returnedFromService = Object.assign(
          {
            idReview: 1,
            nota: 1,
            comentariu: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Review', () => {
        const patchObject = Object.assign(
          {
            nota: 1,
            comentariu: 'BBBBBB',
          },
          new Review()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Review', () => {
        const returnedFromService = Object.assign(
          {
            idReview: 1,
            nota: 1,
            comentariu: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Review', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addReviewToCollectionIfMissing', () => {
        it('should add a Review to an empty array', () => {
          const review: IReview = { idReview: 123 };
          expectedResult = service.addReviewToCollectionIfMissing([], review);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(review);
        });

        it('should not add a Review to an array that contains it', () => {
          const review: IReview = { idReview: 123 };
          const reviewCollection: IReview[] = [
            {
              ...review,
            },
            { idReview: 456 },
          ];
          expectedResult = service.addReviewToCollectionIfMissing(reviewCollection, review);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Review to an array that doesn't contain it", () => {
          const review: IReview = { idReview: 123 };
          const reviewCollection: IReview[] = [{ idReview: 456 }];
          expectedResult = service.addReviewToCollectionIfMissing(reviewCollection, review);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(review);
        });

        it('should add only unique Review to an array', () => {
          const reviewArray: IReview[] = [{ idReview: 123 }, { idReview: 456 }, { idReview: 42502 }];
          const reviewCollection: IReview[] = [{ idReview: 123 }];
          expectedResult = service.addReviewToCollectionIfMissing(reviewCollection, ...reviewArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const review: IReview = { idReview: 123 };
          const review2: IReview = { idReview: 456 };
          expectedResult = service.addReviewToCollectionIfMissing([], review, review2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(review);
          expect(expectedResult).toContain(review2);
        });

        it('should accept null and undefined values', () => {
          const review: IReview = { idReview: 123 };
          expectedResult = service.addReviewToCollectionIfMissing([], null, review, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(review);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
