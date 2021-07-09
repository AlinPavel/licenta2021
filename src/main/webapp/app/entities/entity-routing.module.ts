import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-user',
        data: { pageTitle: 'AppUsers' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'catalog',
        data: { pageTitle: 'Catalogs' },
        loadChildren: () => import('./catalog/catalog.module').then(m => m.CatalogModule),
      },
      {
        path: 'sport',
        data: { pageTitle: 'Sports' },
        loadChildren: () => import('./sport/sport.module').then(m => m.SportModule),
      },
      {
        path: 'review',
        data: { pageTitle: 'Reviews' },
        loadChildren: () => import('./review/review.module').then(m => m.ReviewModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
