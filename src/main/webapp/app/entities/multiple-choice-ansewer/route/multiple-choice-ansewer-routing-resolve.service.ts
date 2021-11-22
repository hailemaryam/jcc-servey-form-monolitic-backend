import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMultipleChoiceAnsewer, MultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';
import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';

@Injectable({ providedIn: 'root' })
export class MultipleChoiceAnsewerRoutingResolveService implements Resolve<IMultipleChoiceAnsewer> {
  constructor(protected service: MultipleChoiceAnsewerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMultipleChoiceAnsewer> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((multipleChoiceAnsewer: HttpResponse<MultipleChoiceAnsewer>) => {
          if (multipleChoiceAnsewer.body) {
            return of(multipleChoiceAnsewer.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new MultipleChoiceAnsewer());
  }
}
