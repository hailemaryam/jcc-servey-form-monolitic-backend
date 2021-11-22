import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MultipleChoiceAnsewerComponent } from './list/multiple-choice-ansewer.component';
import { MultipleChoiceAnsewerDetailComponent } from './detail/multiple-choice-ansewer-detail.component';
import { MultipleChoiceAnsewerUpdateComponent } from './update/multiple-choice-ansewer-update.component';
import { MultipleChoiceAnsewerDeleteDialogComponent } from './delete/multiple-choice-ansewer-delete-dialog.component';
import { MultipleChoiceAnsewerRoutingModule } from './route/multiple-choice-ansewer-routing.module';

@NgModule({
  imports: [SharedModule, MultipleChoiceAnsewerRoutingModule],
  declarations: [
    MultipleChoiceAnsewerComponent,
    MultipleChoiceAnsewerDetailComponent,
    MultipleChoiceAnsewerUpdateComponent,
    MultipleChoiceAnsewerDeleteDialogComponent,
  ],
  entryComponents: [MultipleChoiceAnsewerDeleteDialogComponent],
})
export class MultipleChoiceAnsewerModule {}
