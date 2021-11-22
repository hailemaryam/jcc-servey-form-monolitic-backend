import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormProgresss, getFormProgresssIdentifier } from '../form-progresss.model';

export type EntityResponseType = HttpResponse<IFormProgresss>;
export type EntityArrayResponseType = HttpResponse<IFormProgresss[]>;

@Injectable({ providedIn: 'root' })
export class FormProgresssService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/form-progressses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formProgresss: IFormProgresss): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formProgresss);
    return this.http
      .post<IFormProgresss>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formProgresss: IFormProgresss): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formProgresss);
    return this.http
      .put<IFormProgresss>(`${this.resourceUrl}/${getFormProgresssIdentifier(formProgresss) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(formProgresss: IFormProgresss): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formProgresss);
    return this.http
      .patch<IFormProgresss>(`${this.resourceUrl}/${getFormProgresssIdentifier(formProgresss) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFormProgresss>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormProgresss[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormProgresssToCollectionIfMissing(
    formProgresssCollection: IFormProgresss[],
    ...formProgresssesToCheck: (IFormProgresss | null | undefined)[]
  ): IFormProgresss[] {
    const formProgressses: IFormProgresss[] = formProgresssesToCheck.filter(isPresent);
    if (formProgressses.length > 0) {
      const formProgresssCollectionIdentifiers = formProgresssCollection.map(
        formProgresssItem => getFormProgresssIdentifier(formProgresssItem)!
      );
      const formProgresssesToAdd = formProgressses.filter(formProgresssItem => {
        const formProgresssIdentifier = getFormProgresssIdentifier(formProgresssItem);
        if (formProgresssIdentifier == null || formProgresssCollectionIdentifiers.includes(formProgresssIdentifier)) {
          return false;
        }
        formProgresssCollectionIdentifiers.push(formProgresssIdentifier);
        return true;
      });
      return [...formProgresssesToAdd, ...formProgresssCollection];
    }
    return formProgresssCollection;
  }

  protected convertDateFromClient(formProgresss: IFormProgresss): IFormProgresss {
    return Object.assign({}, formProgresss, {
      startedOn: formProgresss.startedOn?.isValid() ? formProgresss.startedOn.toJSON() : undefined,
      submitedOn: formProgresss.submitedOn?.isValid() ? formProgresss.submitedOn.toJSON() : undefined,
      sentedOn: formProgresss.sentedOn?.isValid() ? formProgresss.sentedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startedOn = res.body.startedOn ? dayjs(res.body.startedOn) : undefined;
      res.body.submitedOn = res.body.submitedOn ? dayjs(res.body.submitedOn) : undefined;
      res.body.sentedOn = res.body.sentedOn ? dayjs(res.body.sentedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((formProgresss: IFormProgresss) => {
        formProgresss.startedOn = formProgresss.startedOn ? dayjs(formProgresss.startedOn) : undefined;
        formProgresss.submitedOn = formProgresss.submitedOn ? dayjs(formProgresss.submitedOn) : undefined;
        formProgresss.sentedOn = formProgresss.sentedOn ? dayjs(formProgresss.sentedOn) : undefined;
      });
    }
    return res;
  }
}
