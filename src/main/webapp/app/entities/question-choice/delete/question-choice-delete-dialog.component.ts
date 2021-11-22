import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IQuestionChoice } from '../question-choice.model';
import { QuestionChoiceService } from '../service/question-choice.service';

@Component({
  templateUrl: './question-choice-delete-dialog.component.html',
})
export class QuestionChoiceDeleteDialogComponent {
  questionChoice?: IQuestionChoice;

  constructor(protected questionChoiceService: QuestionChoiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.questionChoiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
