import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICatalog, Catalog } from '../catalog.model';

import { CatalogService } from './catalog.service';

describe('Service Tests', () => {
  describe('Catalog Service', () => {
    let service: CatalogService;
    let httpMock: HttpTestingController;
    let elemDefault: ICatalog;
    let expectedResult: ICatalog | ICatalog[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CatalogService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        idCatalog: 0,
        nota: 0,
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

      it('should create a Catalog', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Catalog()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Catalog', () => {
        const returnedFromService = Object.assign(
          {
            idCatalog: 1,
            nota: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Catalog', () => {
        const patchObject = Object.assign(
          {
            nota: 1,
          },
          new Catalog()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Catalog', () => {
        const returnedFromService = Object.assign(
          {
            idCatalog: 1,
            nota: 1,
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

      it('should delete a Catalog', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCatalogToCollectionIfMissing', () => {
        it('should add a Catalog to an empty array', () => {
          const catalog: ICatalog = { idCatalog: 123 };
          expectedResult = service.addCatalogToCollectionIfMissing([], catalog);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalog);
        });

        it('should not add a Catalog to an array that contains it', () => {
          const catalog: ICatalog = { idCatalog: 123 };
          const catalogCollection: ICatalog[] = [
            {
              ...catalog,
            },
            { idCatalog: 456 },
          ];
          expectedResult = service.addCatalogToCollectionIfMissing(catalogCollection, catalog);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Catalog to an array that doesn't contain it", () => {
          const catalog: ICatalog = { idCatalog: 123 };
          const catalogCollection: ICatalog[] = [{ idCatalog: 456 }];
          expectedResult = service.addCatalogToCollectionIfMissing(catalogCollection, catalog);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalog);
        });

        it('should add only unique Catalog to an array', () => {
          const catalogArray: ICatalog[] = [{ idCatalog: 123 }, { idCatalog: 456 }, { idCatalog: 49668 }];
          const catalogCollection: ICatalog[] = [{ idCatalog: 123 }];
          expectedResult = service.addCatalogToCollectionIfMissing(catalogCollection, ...catalogArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const catalog: ICatalog = { idCatalog: 123 };
          const catalog2: ICatalog = { idCatalog: 456 };
          expectedResult = service.addCatalogToCollectionIfMissing([], catalog, catalog2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(catalog);
          expect(expectedResult).toContain(catalog2);
        });

        it('should accept null and undefined values', () => {
          const catalog: ICatalog = { idCatalog: 123 };
          expectedResult = service.addCatalogToCollectionIfMissing([], null, catalog, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(catalog);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
