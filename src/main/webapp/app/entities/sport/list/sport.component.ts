import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISport } from '../sport.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { SportService } from '../service/sport.service';
import { SportDeleteDialogComponent } from '../delete/sport-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-sport',
  templateUrl: './sport.component.html',
})
export class SportComponent implements OnInit {
  sports: ISport[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected sportService: SportService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.sports = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'idSport';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.sportService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ISport[]>) => {
          this.isLoading = false;
          this.paginateSports(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.sports = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackIdSport(index: number, item: ISport): number {
    return item.idSport!;
  }

  delete(sport: ISport): void {
    const modalRef = this.modalService.open(SportDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sport = sport;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'idSport') {
      result.push('idSport');
    }
    return result;
  }

  protected paginateSports(data: ISport[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.sports.push(d);
      }
    }
  }
}
