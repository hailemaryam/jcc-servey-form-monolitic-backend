import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITypeOfOrganization, TypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationService } from '../service/type-of-organization.service';

@Injectable({ providedIn: 'root' })
export class TypeOfOrganizationRoutingResolveService implements Resolve<ITypeOfOrganization> {
  constructor(protected service: TypeOfOrganizationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITypeOfOrganization> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((typeOfOrganization: HttpResponse<TypeOfOrganization>) => {
          if (typeOfOrganization.body) {
            return of(typeOfOrganization.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TypeOfOrganization());
  }
}
