import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationService } from '../service/type-of-organization.service';

@Component({
  templateUrl: './type-of-organization-delete-dialog.component.html',
})
export class TypeOfOrganizationDeleteDialogComponent {
  typeOfOrganization?: ITypeOfOrganization;

  constructor(protected typeOfOrganizationService: TypeOfOrganizationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.typeOfOrganizationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
