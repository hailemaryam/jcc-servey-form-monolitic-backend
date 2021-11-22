import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormProgresssComponent } from './list/form-progresss.component';
import { FormProgresssDetailComponent } from './detail/form-progresss-detail.component';
import { FormProgresssUpdateComponent } from './update/form-progresss-update.component';
import { FormProgresssDeleteDialogComponent } from './delete/form-progresss-delete-dialog.component';
import { FormProgresssRoutingModule } from './route/form-progresss-routing.module';

@NgModule({
  imports: [SharedModule, FormProgresssRoutingModule],
  declarations: [FormProgresssComponent, FormProgresssDetailComponent, FormProgresssUpdateComponent, FormProgresssDeleteDialogComponent],
  entryComponents: [FormProgresssDeleteDialogComponent],
})
export class FormProgresssModule {}
