import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISport } from '../sport.model';

@Component({
  selector: 'jhi-sport-detail',
  templateUrl: './sport-detail.component.html',
})
export class SportDetailComponent implements OnInit {
  sport: ISport | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sport }) => {
      this.sport = sport;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
