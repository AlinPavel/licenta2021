import { IAppUser } from 'app/entities/app-user/app-user.model';

export interface ICatalog {
  idCatalog?: number;
  nota?: number | null;
  appUser?: IAppUser | null;
}

export class Catalog implements ICatalog {
  constructor(public idCatalog?: number, public nota?: number | null, public appUser?: IAppUser | null) {}
}

export function getCatalogIdentifier(catalog: ICatalog): number | undefined {
  return catalog.idCatalog;
}
