import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IForm, getFormIdentifier } from '../form.model';

export type EntityResponseType = HttpResponse<IForm>;
export type EntityArrayResponseType = HttpResponse<IForm[]>;

@Injectable({ providedIn: 'root' })
export class FormService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/forms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(form: IForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(form);
    return this.http
      .post<IForm>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(form: IForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(form);
    return this.http
      .put<IForm>(`${this.resourceUrl}/${getFormIdentifier(form) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(form: IForm): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(form);
    return this.http
      .patch<IForm>(`${this.resourceUrl}/${getFormIdentifier(form) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IForm>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IForm[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormToCollectionIfMissing(formCollection: IForm[], ...formsToCheck: (IForm | null | undefined)[]): IForm[] {
    const forms: IForm[] = formsToCheck.filter(isPresent);
    if (forms.length > 0) {
      const formCollectionIdentifiers = formCollection.map(formItem => getFormIdentifier(formItem)!);
      const formsToAdd = forms.filter(formItem => {
        const formIdentifier = getFormIdentifier(formItem);
        if (formIdentifier == null || formCollectionIdentifiers.includes(formIdentifier)) {
          return false;
        }
        formCollectionIdentifiers.push(formIdentifier);
        return true;
      });
      return [...formsToAdd, ...formCollection];
    }
    return formCollection;
  }

  protected convertDateFromClient(form: IForm): IForm {
    return Object.assign({}, form, {
      createdOn: form.createdOn?.isValid() ? form.createdOn.toJSON() : undefined,
      updatedOn: form.updatedOn?.isValid() ? form.updatedOn.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdOn = res.body.createdOn ? dayjs(res.body.createdOn) : undefined;
      res.body.updatedOn = res.body.updatedOn ? dayjs(res.body.updatedOn) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((form: IForm) => {
        form.createdOn = form.createdOn ? dayjs(form.createdOn) : undefined;
        form.updatedOn = form.updatedOn ? dayjs(form.updatedOn) : undefined;
      });
    }
    return res;
  }
}
