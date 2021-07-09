import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface ISport {
  idSport?: number;
  nume?: string | null;
  locatie?: string | null;
  appUser?: IAppUser | null;
}

export class Sport implements ISport {
  constructor(public idSport?: number, public nume?: string | null, public locatie?: string | null, public appUser?: IAppUser | null) {}
}

export function getSportIdentifier(sport: ISport): number | undefined {
  return sport.idSport;
}
