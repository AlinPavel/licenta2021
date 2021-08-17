import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReview } from '../review.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ReviewService } from '../service/review.service';
import { ReviewDeleteDialogComponent } from '../delete/review-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-review',
  templateUrl: './review.component.html',
})
export class ReviewComponent implements OnInit {
  reviews: IReview[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected reviewService: ReviewService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.reviews = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'idReview';
    this.ascending = true;
    this.getLast5Review();
  }

  loadAll(): void {
    this.isLoading = true;

    this.reviewService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IReview[]>) => {
          this.isLoading = false;
          this.paginateReviews(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  getLast5Review(): void {
    this.isLoading = true;

    this.reviewService
      .findLastReviews({
        page: this.page,
      })
      .subscribe(
        (res: HttpResponse<IReview[]>) => {
          this.isLoading = false;
          this.paginateReviews(res.body, res.headers);
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  reset(): void {
    this.page = 0;
    this.reviews = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackIdReview(index: number, item: IReview): number {
    return item.idReview!;
  }

  delete(review: IReview): void {
    const modalRef = this.modalService.open(ReviewDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.review = review;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'idReview') {
      result.push('idReview');
    }
    return result;
  }

  protected paginateReviews(data: IReview[] | null, headers: HttpHeaders): void {
    this.links = this.parseLinks.parse(headers.get('link') ?? '');
    if (data) {
      for (const d of data) {
        this.reviews.push(d);
      }
    }
  }
}
