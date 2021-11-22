import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TypeOfOrganizationComponent } from '../list/type-of-organization.component';
import { TypeOfOrganizationDetailComponent } from '../detail/type-of-organization-detail.component';
import { TypeOfOrganizationUpdateComponent } from '../update/type-of-organization-update.component';
import { TypeOfOrganizationRoutingResolveService } from './type-of-organization-routing-resolve.service';

const typeOfOrganizationRoute: Routes = [
  {
    path: '',
    component: TypeOfOrganizationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TypeOfOrganizationDetailComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TypeOfOrganizationUpdateComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TypeOfOrganizationUpdateComponent,
    resolve: {
      typeOfOrganization: TypeOfOrganizationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(typeOfOrganizationRoute)],
  exports: [RouterModule],
})
export class TypeOfOrganizationRoutingModule {}
