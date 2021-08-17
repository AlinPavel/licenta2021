import { IUser } from 'app/entities/user/user.model';
import { ICatalog } from 'app/entities/catalog/catalog.model';
import { ISport } from 'app/entities/sport/sport.model';
import { IReview } from 'app/entities/review/review.model';

export interface IAppUser {
  idAppUser?: number;
  firstName?: string | null;
  lastName?: string | null;
  cNP?: number | null;
  grupa?: number | null;
  an?: number | null;
  disciplina?: string | null;
  user?: IUser | null;
  idCatalogs?: ICatalog[] | null;
  idSports?: ISport[] | null;
  idReviews?: IReview[] | null;
  email?: string;
}

export class AppUser implements IAppUser {
  constructor(
    public idAppUser?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public cNP?: number | null,
    public grupa?: number | null,
    public an?: number | null,
    public disciplina?: string | null,
    public user?: IUser | null,
    public idCatalogs?: ICatalog[] | null,
    public idSports?: ISport[] | null,
    public idReviews?: IReview[] | null,
    public email?: string
  ) {}
}

export function getAppUserIdentifier(appUser: IAppUser): number | undefined {
  return appUser.idAppUser;
}
