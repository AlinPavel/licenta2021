import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SportComponent } from '../list/sport.component';
import { SportDetailComponent } from '../detail/sport-detail.component';
import { SportUpdateComponent } from '../update/sport-update.component';
import { SportRoutingResolveService } from './sport-routing-resolve.service';

const sportRoute: Routes = [
  {
    path: '',
    component: SportComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idSport/view',
    component: SportDetailComponent,
    resolve: {
      sport: SportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SportUpdateComponent,
    resolve: {
      sport: SportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':idSport/edit',
    component: SportUpdateComponent,
    resolve: {
      sport: SportRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sportRoute)],
  exports: [RouterModule],
})
export class SportRoutingModule {}
