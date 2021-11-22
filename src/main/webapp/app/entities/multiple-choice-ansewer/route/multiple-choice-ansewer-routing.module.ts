import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MultipleChoiceAnsewerComponent } from '../list/multiple-choice-ansewer.component';
import { MultipleChoiceAnsewerDetailComponent } from '../detail/multiple-choice-ansewer-detail.component';
import { MultipleChoiceAnsewerUpdateComponent } from '../update/multiple-choice-ansewer-update.component';
import { MultipleChoiceAnsewerRoutingResolveService } from './multiple-choice-ansewer-routing-resolve.service';

const multipleChoiceAnsewerRoute: Routes = [
  {
    path: '',
    component: MultipleChoiceAnsewerComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MultipleChoiceAnsewerDetailComponent,
    resolve: {
      multipleChoiceAnsewer: MultipleChoiceAnsewerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MultipleChoiceAnsewerUpdateComponent,
    resolve: {
      multipleChoiceAnsewer: MultipleChoiceAnsewerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MultipleChoiceAnsewerUpdateComponent,
    resolve: {
      multipleChoiceAnsewer: MultipleChoiceAnsewerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(multipleChoiceAnsewerRoute)],
  exports: [RouterModule],
})
export class MultipleChoiceAnsewerRoutingModule {}
