jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CatalogService } from '../service/catalog.service';
import { ICatalog, Catalog } from '../catalog.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { CatalogUpdateComponent } from './catalog-update.component';

describe('Component Tests', () => {
  describe('Catalog Management Update Component', () => {
    let comp: CatalogUpdateComponent;
    let fixture: ComponentFixture<CatalogUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let catalogService: CatalogService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CatalogUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      catalogService = TestBed.inject(CatalogService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const catalog: ICatalog = { idCatalog: 456 };
        const appUser: IAppUser = { idAppUser: 98651 };
        catalog.appUser = appUser;

        const appUserCollection: IAppUser[] = [{ idAppUser: 33883 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [appUser];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ catalog });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const catalog: ICatalog = { idCatalog: 456 };
        const appUser: IAppUser = { idAppUser: 77469 };
        catalog.appUser = appUser;

        activatedRoute.data = of({ catalog });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(catalog));
        expect(comp.appUsersSharedCollection).toContain(appUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const catalog = { idCatalog: 123 };
        spyOn(catalogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalog }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(catalogService.update).toHaveBeenCalledWith(catalog);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const catalog = new Catalog();
        spyOn(catalogService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: catalog }));
        saveSubject.complete();

        // THEN
        expect(catalogService.create).toHaveBeenCalledWith(catalog);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const catalog = { idCatalog: 123 };
        spyOn(catalogService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ catalog });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(catalogService.update).toHaveBeenCalledWith(catalog);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackAppUserByIdAppUser', () => {
        it('Should return tracked AppUser primary key', () => {
          const entity = { idAppUser: 123 };
          const trackResult = comp.trackAppUserByIdAppUser(0, entity);
          expect(trackResult).toEqual(entity.idAppUser);
        });
      });
    });
  });
});
