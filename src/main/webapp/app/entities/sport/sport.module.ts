import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { SportComponent } from './list/sport.component';
import { SportDetailComponent } from './detail/sport-detail.component';
import { SportUpdateComponent } from './update/sport-update.component';
import { SportDeleteDialogComponent } from './delete/sport-delete-dialog.component';
import { SportRoutingModule } from './route/sport-routing.module';

@NgModule({
  imports: [SharedModule, SportRoutingModule],
  declarations: [SportComponent, SportDetailComponent, SportUpdateComponent, SportDeleteDialogComponent],
  entryComponents: [SportDeleteDialogComponent],
})
export class SportModule {}
