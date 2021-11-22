import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TypeOfOrganizationComponent } from './list/type-of-organization.component';
import { TypeOfOrganizationDetailComponent } from './detail/type-of-organization-detail.component';
import { TypeOfOrganizationUpdateComponent } from './update/type-of-organization-update.component';
import { TypeOfOrganizationDeleteDialogComponent } from './delete/type-of-organization-delete-dialog.component';
import { TypeOfOrganizationRoutingModule } from './route/type-of-organization-routing.module';

@NgModule({
  imports: [SharedModule, TypeOfOrganizationRoutingModule],
  declarations: [
    TypeOfOrganizationComponent,
    TypeOfOrganizationDetailComponent,
    TypeOfOrganizationUpdateComponent,
    TypeOfOrganizationDeleteDialogComponent,
  ],
  entryComponents: [TypeOfOrganizationDeleteDialogComponent],
})
export class TypeOfOrganizationModule {}
