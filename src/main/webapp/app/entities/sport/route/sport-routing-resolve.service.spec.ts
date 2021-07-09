jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISport, Sport } from '../sport.model';
import { SportService } from '../service/sport.service';

import { SportRoutingResolveService } from './sport-routing-resolve.service';

describe('Service Tests', () => {
  describe('Sport routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SportRoutingResolveService;
    let service: SportService;
    let resultSport: ISport | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SportRoutingResolveService);
      service = TestBed.inject(SportService);
      resultSport = undefined;
    });

    describe('resolve', () => {
      it('should return ISport returned by find', () => {
        // GIVEN
        service.find = jest.fn(idSport => of(new HttpResponse({ body: { idSport } })));
        mockActivatedRouteSnapshot.params = { idSport: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSport).toEqual({ idSport: 123 });
      });

      it('should return new ISport if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSport = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSport).toEqual(new Sport());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { idSport: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSport = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSport).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
