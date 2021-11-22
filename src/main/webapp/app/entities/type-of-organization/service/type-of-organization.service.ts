import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeOfOrganization, getTypeOfOrganizationIdentifier } from '../type-of-organization.model';

export type EntityResponseType = HttpResponse<ITypeOfOrganization>;
export type EntityArrayResponseType = HttpResponse<ITypeOfOrganization[]>;

@Injectable({ providedIn: 'root' })
export class TypeOfOrganizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-of-organizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeOfOrganization: ITypeOfOrganization): Observable<EntityResponseType> {
    return this.http.post<ITypeOfOrganization>(this.resourceUrl, typeOfOrganization, { observe: 'response' });
  }

  update(typeOfOrganization: ITypeOfOrganization): Observable<EntityResponseType> {
    return this.http.put<ITypeOfOrganization>(
      `${this.resourceUrl}/${getTypeOfOrganizationIdentifier(typeOfOrganization) as number}`,
      typeOfOrganization,
      { observe: 'response' }
    );
  }

  partialUpdate(typeOfOrganization: ITypeOfOrganization): Observable<EntityResponseType> {
    return this.http.patch<ITypeOfOrganization>(
      `${this.resourceUrl}/${getTypeOfOrganizationIdentifier(typeOfOrganization) as number}`,
      typeOfOrganization,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeOfOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOfOrganization[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTypeOfOrganizationToCollectionIfMissing(
    typeOfOrganizationCollection: ITypeOfOrganization[],
    ...typeOfOrganizationsToCheck: (ITypeOfOrganization | null | undefined)[]
  ): ITypeOfOrganization[] {
    const typeOfOrganizations: ITypeOfOrganization[] = typeOfOrganizationsToCheck.filter(isPresent);
    if (typeOfOrganizations.length > 0) {
      const typeOfOrganizationCollectionIdentifiers = typeOfOrganizationCollection.map(
        typeOfOrganizationItem => getTypeOfOrganizationIdentifier(typeOfOrganizationItem)!
      );
      const typeOfOrganizationsToAdd = typeOfOrganizations.filter(typeOfOrganizationItem => {
        const typeOfOrganizationIdentifier = getTypeOfOrganizationIdentifier(typeOfOrganizationItem);
        if (typeOfOrganizationIdentifier == null || typeOfOrganizationCollectionIdentifiers.includes(typeOfOrganizationIdentifier)) {
          return false;
        }
        typeOfOrganizationCollectionIdentifiers.push(typeOfOrganizationIdentifier);
        return true;
      });
      return [...typeOfOrganizationsToAdd, ...typeOfOrganizationCollection];
    }
    return typeOfOrganizationCollection;
  }
}
