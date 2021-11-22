import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormProgresss, FormProgresss } from '../form-progresss.model';
import { FormProgresssService } from '../service/form-progresss.service';

@Injectable({ providedIn: 'root' })
export class FormProgresssRoutingResolveService implements Resolve<IFormProgresss> {
  constructor(protected service: FormProgresssService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormProgresss> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formProgresss: HttpResponse<FormProgresss>) => {
          if (formProgresss.body) {
            return of(formProgresss.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormProgresss());
  }
}
