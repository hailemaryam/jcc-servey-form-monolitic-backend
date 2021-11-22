import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { QuestionChoiceComponent } from '../list/question-choice.component';
import { QuestionChoiceDetailComponent } from '../detail/question-choice-detail.component';
import { QuestionChoiceUpdateComponent } from '../update/question-choice-update.component';
import { QuestionChoiceRoutingResolveService } from './question-choice-routing-resolve.service';

const questionChoiceRoute: Routes = [
  {
    path: '',
    component: QuestionChoiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: QuestionChoiceDetailComponent,
    resolve: {
      questionChoice: QuestionChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: QuestionChoiceUpdateComponent,
    resolve: {
      questionChoice: QuestionChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: QuestionChoiceUpdateComponent,
    resolve: {
      questionChoice: QuestionChoiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(questionChoiceRoute)],
  exports: [RouterModule],
})
export class QuestionChoiceRoutingModule {}
