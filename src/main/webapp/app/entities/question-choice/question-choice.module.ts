import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { QuestionChoiceComponent } from './list/question-choice.component';
import { QuestionChoiceDetailComponent } from './detail/question-choice-detail.component';
import { QuestionChoiceUpdateComponent } from './update/question-choice-update.component';
import { QuestionChoiceDeleteDialogComponent } from './delete/question-choice-delete-dialog.component';
import { QuestionChoiceRoutingModule } from './route/question-choice-routing.module';

@NgModule({
  imports: [SharedModule, QuestionChoiceRoutingModule],
  declarations: [
    QuestionChoiceComponent,
    QuestionChoiceDetailComponent,
    QuestionChoiceUpdateComponent,
    QuestionChoiceDeleteDialogComponent,
  ],
  entryComponents: [QuestionChoiceDeleteDialogComponent],
})
export class QuestionChoiceModule {}
