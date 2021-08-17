import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppUser, getAppUserIdentifier } from '../app-user.model';
import { map } from 'rxjs/operators';

export type EntityResponseType = HttpResponse<IAppUser>;
export type EntityAppUserType = HttpResponse<number>;
export type EntityArrayResponseType = HttpResponse<IAppUser[]>;

@Injectable({ providedIn: 'root' })
export class AppUserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/app-users');

  public urlForCurrentAppUser = this.applicationConfigService.getEndpointFor('api/custom/current/appuser');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  findIdAppUser(): Observable<number> {
    return this.http
      .get<number>(`${this.urlForCurrentAppUser}`, { observe: 'response' })
      .pipe(map((res: EntityAppUserType) => this.convertAppUserIdFromServer(res)));
  }

  create(appUser: IAppUser): Observable<EntityResponseType> {
    return this.http.post<IAppUser>(this.resourceUrl, appUser, { observe: 'response' });
  }

  update(appUser: IAppUser): Observable<EntityResponseType> {
    return this.http.put<IAppUser>(`${this.resourceUrl}/${getAppUserIdentifier(appUser) as number}`, appUser, { observe: 'response' });
  }

  partialUpdate(appUser: IAppUser): Observable<EntityResponseType> {
    return this.http.patch<IAppUser>(`${this.resourceUrl}/${getAppUserIdentifier(appUser) as number}`, appUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAppUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAppUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppUserToCollectionIfMissing(appUserCollection: IAppUser[], ...appUsersToCheck: (IAppUser | null | undefined)[]): IAppUser[] {
    const appUsers: IAppUser[] = appUsersToCheck.filter(isPresent);
    if (appUsers.length > 0) {
      const appUserCollectionIdentifiers = appUserCollection.map(appUserItem => getAppUserIdentifier(appUserItem)!);
      const appUsersToAdd = appUsers.filter(appUserItem => {
        const appUserIdentifier = getAppUserIdentifier(appUserItem);
        if (appUserIdentifier == null || appUserCollectionIdentifiers.includes(appUserIdentifier)) {
          return false;
        }
        appUserCollectionIdentifiers.push(appUserIdentifier);
        return true;
      });
      return [...appUsersToAdd, ...appUserCollection];
    }
    return appUserCollection;
  }

  protected convertAppUserIdFromServer(res: EntityAppUserType): number {
    return res.body!.valueOf();
  }
}
