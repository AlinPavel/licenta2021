import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISport } from '../sport.model';
import { SportService } from '../service/sport.service';

@Component({
  templateUrl: './sport-delete-dialog.component.html',
})
export class SportDeleteDialogComponent {
  sport?: ISport;

  constructor(protected sportService: SportService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sportService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
