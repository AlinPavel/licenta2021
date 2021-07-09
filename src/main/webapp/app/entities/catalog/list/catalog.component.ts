import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICatalog } from '../catalog.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CatalogService } from '../service/catalog.service';
import { CatalogDeleteDialogComponent } from '../delete/catalog-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-catalog',
  templateUrl: './catalog.component.html',
})
export class CatalogComponent implements OnInit {
  catalogs: ICatalog[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected catalogService: CatalogService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.catalogs = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'idCatalog';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.catalogService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ICatalog[]>) => {
          this.isLoading = false;
          this.paginateCatalogs(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.catalogs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackIdCatalog(index: number, item: ICatalog): number {
    return item.idCatalog!;
  }

  delete(catalog: ICatalog): void {
    const modalRef = this.modalService.open(CatalogDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.catalog = catalog;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'idCatalog') {
      result.push('idCatalog');
    }
    return result;
  }

  protected paginateCatalogs(data: ICatalog[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.catalogs.push(d);
      }
    }
  }
}
