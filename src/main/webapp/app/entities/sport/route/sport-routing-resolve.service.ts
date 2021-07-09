import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISport, Sport } from '../sport.model';
import { SportService } from '../service/sport.service';

@Injectable({ providedIn: 'root' })
export class SportRoutingResolveService implements Resolve<ISport> {
  constructor(protected service: SportService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISport> | Observable<never> {
    const id = route.params['idSport'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sport: HttpResponse<Sport>) => {
          if (sport.body) {
            return of(sport.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Sport());
  }
}
