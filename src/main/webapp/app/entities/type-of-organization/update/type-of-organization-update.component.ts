import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ITypeOfOrganization, TypeOfOrganization } from '../type-of-organization.model';
import { TypeOfOrganizationService } from '../service/type-of-organization.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-type-of-organization-update',
  templateUrl: './type-of-organization-update.component.html',
})
export class TypeOfOrganizationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected typeOfOrganizationService: TypeOfOrganizationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ typeOfOrganization }) => {
      this.updateForm(typeOfOrganization);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('rmtMonoliticApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const typeOfOrganization = this.createFromForm();
    if (typeOfOrganization.id !== undefined) {
      this.subscribeToSaveResponse(this.typeOfOrganizationService.update(typeOfOrganization));
    } else {
      this.subscribeToSaveResponse(this.typeOfOrganizationService.create(typeOfOrganization));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITypeOfOrganization>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(typeOfOrganization: ITypeOfOrganization): void {
    this.editForm.patchValue({
      id: typeOfOrganization.id,
      name: typeOfOrganization.name,
      description: typeOfOrganization.description,
    });
  }

  protected createFromForm(): ITypeOfOrganization {
    return {
      ...new TypeOfOrganization(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
