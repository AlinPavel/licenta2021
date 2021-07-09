import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface IReview {
  idReview?: number;
  nota?: number | null;
  comentariu?: string | null;
  appUser?: IAppUser | null;
}

export class Review implements IReview {
  constructor(public idReview?: number, public nota?: number | null, public comentariu?: string | null, public appUser?: IAppUser | null) {}
}

export function getReviewIdentifier(review: IReview): number | undefined {
  return review.idReview;
}
