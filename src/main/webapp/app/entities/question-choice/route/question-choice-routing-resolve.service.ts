import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IQuestionChoice, QuestionChoice } from '../question-choice.model';
import { QuestionChoiceService } from '../service/question-choice.service';

@Injectable({ providedIn: 'root' })
export class QuestionChoiceRoutingResolveService implements Resolve<IQuestionChoice> {
  constructor(protected service: QuestionChoiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IQuestionChoice> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((questionChoice: HttpResponse<QuestionChoice>) => {
          if (questionChoice.body) {
            return of(questionChoice.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new QuestionChoice());
  }
}
