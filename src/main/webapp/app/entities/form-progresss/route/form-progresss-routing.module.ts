import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormProgresssComponent } from '../list/form-progresss.component';
import { FormProgresssDetailComponent } from '../detail/form-progresss-detail.component';
import { FormProgresssUpdateComponent } from '../update/form-progresss-update.component';
import { FormProgresssRoutingResolveService } from './form-progresss-routing-resolve.service';

const formProgresssRoute: Routes = [
  {
    path: '',
    component: FormProgresssComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormProgresssDetailComponent,
    resolve: {
      formProgresss: FormProgresssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormProgresssUpdateComponent,
    resolve: {
      formProgresss: FormProgresssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormProgresssUpdateComponent,
    resolve: {
      formProgresss: FormProgresssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formProgresssRoute)],
  exports: [RouterModule],
})
export class FormProgresssRoutingModule {}
