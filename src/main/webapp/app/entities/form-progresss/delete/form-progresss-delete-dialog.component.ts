import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormProgresss } from '../form-progresss.model';
import { FormProgresssService } from '../service/form-progresss.service';

@Component({
  templateUrl: './form-progresss-delete-dialog.component.html',
})
export class FormProgresssDeleteDialogComponent {
  formProgresss?: IFormProgresss;

  constructor(protected formProgresssService: FormProgresssService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formProgresssService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
