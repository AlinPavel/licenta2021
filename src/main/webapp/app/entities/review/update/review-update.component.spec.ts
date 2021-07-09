jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReviewService } from '../service/review.service';
import { IReview, Review } from '../review.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

import { ReviewUpdateComponent } from './review-update.component';

describe('Component Tests', () => {
  describe('Review Management Update Component', () => {
    let comp: ReviewUpdateComponent;
    let fixture: ComponentFixture<ReviewUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reviewService: ReviewService;
    let appUserService: AppUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReviewUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReviewUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReviewUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reviewService = TestBed.inject(ReviewService);
      appUserService = TestBed.inject(AppUserService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call AppUser query and add missing value', () => {
        const review: IReview = { idReview: 456 };
        const appUser: IAppUser = { idAppUser: 74889 };
        review.appUser = appUser;

        const appUserCollection: IAppUser[] = [{ idAppUser: 21262 }];
        spyOn(appUserService, 'query').and.returnValue(of(new HttpResponse({ body: appUserCollection })));
        const additionalAppUsers = [appUser];
        const expectedCollection: IAppUser[] = [...additionalAppUsers, ...appUserCollection];
        spyOn(appUserService, 'addAppUserToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ review });
        comp.ngOnInit();

        expect(appUserService.query).toHaveBeenCalled();
        expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, ...additionalAppUsers);
        expect(comp.appUsersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const review: IReview = { idReview: 456 };
        const appUser: IAppUser = { idAppUser: 26661 };
        review.appUser = appUser;

        activatedRoute.data = of({ review });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(review));
        expect(comp.appUsersSharedCollection).toContain(appUser);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const review = { idReview: 123 };
        spyOn(reviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: review }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reviewService.update).toHaveBeenCalledWith(review);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const review = new Review();
        spyOn(reviewService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: review }));
        saveSubject.complete();

        // THEN
        expect(reviewService.create).toHaveBeenCalledWith(review);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const review = { idReview: 123 };
        spyOn(reviewService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ review });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reviewService.update).toHaveBeenCalledWith(review);
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
