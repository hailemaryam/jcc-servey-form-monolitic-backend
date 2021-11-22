import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITypeOfOrganization } from '../type-of-organization.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-type-of-organization-detail',
  templateUrl: './type-of-organization-detail.component.html',
})
export class TypeOfOrganizationDetailComponent implements OnInit {
  typeOfOrganization: ITypeOfOrganization | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfOrganization }) => {
      this.typeOfOrganization = typeOfOrganization;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
