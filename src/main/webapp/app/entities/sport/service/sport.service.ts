import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISport, getSportIdentifier } from '../sport.model';

export type EntityResponseType = HttpResponse<ISport>;
export type EntityArrayResponseType = HttpResponse<ISport[]>;

@Injectable({ providedIn: 'root' })
export class SportService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/sports');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sport: ISport): Observable<EntityResponseType> {
    return this.http.post<ISport>(this.resourceUrl, sport, { observe: 'response' });
  }

  update(sport: ISport): Observable<EntityResponseType> {
    return this.http.put<ISport>(`${this.resourceUrl}/${getSportIdentifier(sport) as number}`, sport, { observe: 'response' });
  }

  partialUpdate(sport: ISport): Observable<EntityResponseType> {
    return this.http.patch<ISport>(`${this.resourceUrl}/${getSportIdentifier(sport) as number}`, sport, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSportToCollectionIfMissing(sportCollection: ISport[], ...sportsToCheck: (ISport | null | undefined)[]): ISport[] {
    const sports: ISport[] = sportsToCheck.filter(isPresent);
    if (sports.length > 0) {
      const sportCollectionIdentifiers = sportCollection.map(sportItem => getSportIdentifier(sportItem)!);
      const sportsToAdd = sports.filter(sportItem => {
        const sportIdentifier = getSportIdentifier(sportItem);
        if (sportIdentifier == null || sportCollectionIdentifiers.includes(sportIdentifier)) {
          return false;
        }
        sportCollectionIdentifiers.push(sportIdentifier);
        return true;
      });
      return [...sportsToAdd, ...sportCollection];
    }
    return sportCollection;
  }
}
