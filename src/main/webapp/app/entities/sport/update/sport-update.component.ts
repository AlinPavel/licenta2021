import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISport, Sport } from '../sport.model';
import { SportService } from '../service/sport.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';

@Component({
  selector: 'jhi-sport-update',
  templateUrl: './sport-update.component.html',
})
export class SportUpdateComponent implements OnInit {
  isSaving = false;

  appUsersSharedCollection: IAppUser[] = [];

  editForm = this.fb.group({
    idSport: [],
    nume: [],
    locatie: [],
    appUser: [],
  });

  constructor(
    protected sportService: SportService,
    protected appUserService: AppUserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sport }) => {
      this.updateForm(sport);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sport = this.createFromForm();
    if (sport.idSport !== undefined) {
      this.subscribeToSaveResponse(this.sportService.update(sport));
    } else {
      this.subscribeToSaveResponse(this.sportService.create(sport));
    }
  }

  trackAppUserByIdAppUser(index: number, item: IAppUser): number {
    return item.idAppUser!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISport>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(sport: ISport): void {
    this.editForm.patchValue({
      idSport: sport.idSport,
      nume: sport.nume,
      locatie: sport.locatie,
      appUser: sport.appUser,
    });

    this.appUsersSharedCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersSharedCollection, sport.appUser);
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query()
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('appUser')!.value))
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersSharedCollection = appUsers));
  }

  protected createFromForm(): ISport {
    return {
      ...new Sport(),
      idSport: this.editForm.get(['idSport'])!.value,
      nume: this.editForm.get(['nume'])!.value,
      locatie: this.editForm.get(['locatie'])!.value,
      appUser: this.editForm.get(['appUser'])!.value,
    };
  }
}
