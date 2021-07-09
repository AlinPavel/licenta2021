jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { SportService } from '../service/sport.service';
import { ISport, Sport } from '../sport.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { SportUpdateComponent } from './sport-update.component';

describe('Component Tests', () => {
  describe('Sport Management Update Component', () => {
    let comp: SportUpdateComponent;
    let fixture: ComponentFixture<SportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let sportService: SportService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(SportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      sportService = TestBed.inject(SportService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const sport: ISport = { idSport: 456 };
        const appUser: IAppUser = { idAppUser: 7396 };
        sport.appUser = appUser;

        const appUserCollection: IAppUser[] = [{ idAppUser: 889 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [appUser];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ sport });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const sport: ISport = { idSport: 456 };
        const appUser: IAppUser = { idAppUser: 93209 };
        sport.appUser = appUser;

        activatedRoute.data = of({ sport });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(sport));
        expect(comp.appUsersSharedCollection).toContain(appUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sport = { idSport: 123 };
        spyOn(sportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sport }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(sportService.update).toHaveBeenCalledWith(sport);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sport = new Sport();
        spyOn(sportService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: sport }));
        saveSubject.complete();

        // THEN
        expect(sportService.create).toHaveBeenCalledWith(sport);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const sport = { idSport: 123 };
        spyOn(sportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ sport });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(sportService.update).toHaveBeenCalledWith(sport);
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
