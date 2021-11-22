import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMultipleChoiceAnsewer, getMultipleChoiceAnsewerIdentifier } from '../multiple-choice-ansewer.model';

export type EntityResponseType = HttpResponse<IMultipleChoiceAnsewer>;
export type EntityArrayResponseType = HttpResponse<IMultipleChoiceAnsewer[]>;

@Injectable({ providedIn: 'root' })
export class MultipleChoiceAnsewerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/multiple-choice-ansewers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(multipleChoiceAnsewer: IMultipleChoiceAnsewer): Observable<EntityResponseType> {
    return this.http.post<IMultipleChoiceAnsewer>(this.resourceUrl, multipleChoiceAnsewer, { observe: 'response' });
  }

  update(multipleChoiceAnsewer: IMultipleChoiceAnsewer): Observable<EntityResponseType> {
    return this.http.put<IMultipleChoiceAnsewer>(
      `${this.resourceUrl}/${getMultipleChoiceAnsewerIdentifier(multipleChoiceAnsewer) as number}`,
      multipleChoiceAnsewer,
      { observe: 'response' }
    );
  }

  partialUpdate(multipleChoiceAnsewer: IMultipleChoiceAnsewer): Observable<EntityResponseType> {
    return this.http.patch<IMultipleChoiceAnsewer>(
      `${this.resourceUrl}/${getMultipleChoiceAnsewerIdentifier(multipleChoiceAnsewer) as number}`,
      multipleChoiceAnsewer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMultipleChoiceAnsewer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMultipleChoiceAnsewer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMultipleChoiceAnsewerToCollectionIfMissing(
    multipleChoiceAnsewerCollection: IMultipleChoiceAnsewer[],
    ...multipleChoiceAnsewersToCheck: (IMultipleChoiceAnsewer | null | undefined)[]
  ): IMultipleChoiceAnsewer[] {
    const multipleChoiceAnsewers: IMultipleChoiceAnsewer[] = multipleChoiceAnsewersToCheck.filter(isPresent);
    if (multipleChoiceAnsewers.length > 0) {
      const multipleChoiceAnsewerCollectionIdentifiers = multipleChoiceAnsewerCollection.map(
        multipleChoiceAnsewerItem => getMultipleChoiceAnsewerIdentifier(multipleChoiceAnsewerItem)!
      );
      const multipleChoiceAnsewersToAdd = multipleChoiceAnsewers.filter(multipleChoiceAnsewerItem => {
        const multipleChoiceAnsewerIdentifier = getMultipleChoiceAnsewerIdentifier(multipleChoiceAnsewerItem);
        if (
          multipleChoiceAnsewerIdentifier == null ||
          multipleChoiceAnsewerCollectionIdentifiers.includes(multipleChoiceAnsewerIdentifier)
        ) {
          return false;
        }
        multipleChoiceAnsewerCollectionIdentifiers.push(multipleChoiceAnsewerIdentifier);
        return true;
      });
      return [...multipleChoiceAnsewersToAdd, ...multipleChoiceAnsewerCollection];
    }
    return multipleChoiceAnsewerCollection;
  }
}
