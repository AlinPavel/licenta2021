import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISport, Sport } from '../sport.model';

import { SportService } from './sport.service';

describe('Service Tests', () => {
  describe('Sport Service', () => {
    let service: SportService;
    let httpMock: HttpTestingController;
    let elemDefault: ISport;
    let expectedResult: ISport | ISport[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SportService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        idSport: 0,
        nume: 'AAAAAAA',
        locatie: 'AAAAAAA',
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

      it('should create a Sport', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Sport()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Sport', () => {
        const returnedFromService = Object.assign(
          {
            idSport: 1,
            nume: 'BBBBBB',
            locatie: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Sport', () => {
        const patchObject = Object.assign(
          {
            nume: 'BBBBBB',
          },
          new Sport()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Sport', () => {
        const returnedFromService = Object.assign(
          {
            idSport: 1,
            nume: 'BBBBBB',
            locatie: 'BBBBBB',
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

      it('should delete a Sport', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSportToCollectionIfMissing', () => {
        it('should add a Sport to an empty array', () => {
          const sport: ISport = { idSport: 123 };
          expectedResult = service.addSportToCollectionIfMissing([], sport);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sport);
        });

        it('should not add a Sport to an array that contains it', () => {
          const sport: ISport = { idSport: 123 };
          const sportCollection: ISport[] = [
            {
              ...sport,
            },
            { idSport: 456 },
          ];
          expectedResult = service.addSportToCollectionIfMissing(sportCollection, sport);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Sport to an array that doesn't contain it", () => {
          const sport: ISport = { idSport: 123 };
          const sportCollection: ISport[] = [{ idSport: 456 }];
          expectedResult = service.addSportToCollectionIfMissing(sportCollection, sport);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sport);
        });

        it('should add only unique Sport to an array', () => {
          const sportArray: ISport[] = [{ idSport: 123 }, { idSport: 456 }, { idSport: 84879 }];
          const sportCollection: ISport[] = [{ idSport: 123 }];
          expectedResult = service.addSportToCollectionIfMissing(sportCollection, ...sportArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sport: ISport = { idSport: 123 };
          const sport2: ISport = { idSport: 456 };
          expectedResult = service.addSportToCollectionIfMissing([], sport, sport2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sport);
          expect(expectedResult).toContain(sport2);
        });

        it('should accept null and undefined values', () => {
          const sport: ISport = { idSport: 123 };
          expectedResult = service.addSportToCollectionIfMissing([], null, sport, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sport);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
