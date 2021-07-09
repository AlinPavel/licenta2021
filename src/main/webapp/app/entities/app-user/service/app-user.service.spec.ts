import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAppUser, AppUser } from '../app-user.model';

import { AppUserService } from './app-user.service';

describe('Service Tests', () => {
  describe('AppUser Service', () => {
    let service: AppUserService;
    let httpMock: HttpTestingController;
    let elemDefault: IAppUser;
    let expectedResult: IAppUser | IAppUser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AppUserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        idAppUser: 0,
        firstName: 'AAAAAAA',
        lastName: 'AAAAAAA',
        cNP: 0,
        grupa: 0,
        an: 0,
        disciplina: 'AAAAAAA',
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

      it('should create a AppUser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AppUser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AppUser', () => {
        const returnedFromService = Object.assign(
          {
            idAppUser: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            cNP: 1,
            grupa: 1,
            an: 1,
            disciplina: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AppUser', () => {
        const patchObject = Object.assign(
          {
            lastName: 'BBBBBB',
            grupa: 1,
            disciplina: 'BBBBBB',
          },
          new AppUser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AppUser', () => {
        const returnedFromService = Object.assign(
          {
            idAppUser: 1,
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            cNP: 1,
            grupa: 1,
            an: 1,
            disciplina: 'BBBBBB',
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

      it('should delete a AppUser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAppUserToCollectionIfMissing', () => {
        it('should add a AppUser to an empty array', () => {
          const appUser: IAppUser = { idAppUser: 123 };
          expectedResult = service.addAppUserToCollectionIfMissing([], appUser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appUser);
        });

        it('should not add a AppUser to an array that contains it', () => {
          const appUser: IAppUser = { idAppUser: 123 };
          const appUserCollection: IAppUser[] = [
            {
              ...appUser,
            },
            { idAppUser: 456 },
          ];
          expectedResult = service.addAppUserToCollectionIfMissing(appUserCollection, appUser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AppUser to an array that doesn't contain it", () => {
          const appUser: IAppUser = { idAppUser: 123 };
          const appUserCollection: IAppUser[] = [{ idAppUser: 456 }];
          expectedResult = service.addAppUserToCollectionIfMissing(appUserCollection, appUser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appUser);
        });

        it('should add only unique AppUser to an array', () => {
          const appUserArray: IAppUser[] = [{ idAppUser: 123 }, { idAppUser: 456 }, { idAppUser: 96343 }];
          const appUserCollection: IAppUser[] = [{ idAppUser: 123 }];
          expectedResult = service.addAppUserToCollectionIfMissing(appUserCollection, ...appUserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const appUser: IAppUser = { idAppUser: 123 };
          const appUser2: IAppUser = { idAppUser: 456 };
          expectedResult = service.addAppUserToCollectionIfMissing([], appUser, appUser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(appUser);
          expect(expectedResult).toContain(appUser2);
        });

        it('should accept null and undefined values', () => {
          const appUser: IAppUser = { idAppUser: 123 };
          expectedResult = service.addAppUserToCollectionIfMissing([], null, appUser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(appUser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
