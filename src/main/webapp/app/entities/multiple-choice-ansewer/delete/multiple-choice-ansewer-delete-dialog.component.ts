import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';
import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';

@Component({
  templateUrl: './multiple-choice-ansewer-delete-dialog.component.html',
})
export class MultipleChoiceAnsewerDeleteDialogComponent {
  multipleChoiceAnsewer?: IMultipleChoiceAnsewer;

  constructor(protected multipleChoiceAnsewerService: MultipleChoiceAnsewerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.multipleChoiceAnsewerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
