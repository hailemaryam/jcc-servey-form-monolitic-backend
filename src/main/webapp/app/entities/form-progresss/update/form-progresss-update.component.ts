import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IFormProgresss, FormProgresss } from '../form-progresss.model';
import { FormProgresssService } from '../service/form-progresss.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IForm } from 'app/entities/form/form.model';
import { FormService } from 'app/entities/form/service/form.service';

@Component({
  selector: 'jhi-form-progresss-update',
  templateUrl: './form-progresss-update.component.html',
})
export class FormProgresssUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  formsSharedCollection: IForm[] = [];

  editForm = this.fb.group({
    id: [],
    submited: [],
    startedOn: [],
    submitedOn: [],
    sentedOn: [],
    user: [],
    form: [],
  });

  constructor(
    protected formProgresssService: FormProgresssService,
    protected userService: UserService,
    protected formService: FormService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formProgresss }) => {
      if (formProgresss.id === undefined) {
        const today = dayjs().startOf('day');
        formProgresss.startedOn = today;
        formProgresss.submitedOn = today;
        formProgresss.sentedOn = today;
      }

      this.updateForm(formProgresss);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formProgresss = this.createFromForm();
    if (formProgresss.id !== undefined) {
      this.subscribeToSaveResponse(this.formProgresssService.update(formProgresss));
    } else {
      this.subscribeToSaveResponse(this.formProgresssService.create(formProgresss));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackFormById(index: number, item: IForm): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormProgresss>>): void {
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

  protected updateForm(formProgresss: IFormProgresss): void {
    this.editForm.patchValue({
      id: formProgresss.id,
      submited: formProgresss.submited,
      startedOn: formProgresss.startedOn ? formProgresss.startedOn.format(DATE_TIME_FORMAT) : null,
      submitedOn: formProgresss.submitedOn ? formProgresss.submitedOn.format(DATE_TIME_FORMAT) : null,
      sentedOn: formProgresss.sentedOn ? formProgresss.sentedOn.format(DATE_TIME_FORMAT) : null,
      user: formProgresss.user,
      form: formProgresss.form,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, formProgresss.user);
    this.formsSharedCollection = this.formService.addFormToCollectionIfMissing(this.formsSharedCollection, formProgresss.form);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.formService
      .query()
      .pipe(map((res: HttpResponse<IForm[]>) => res.body ?? []))
      .pipe(map((forms: IForm[]) => this.formService.addFormToCollectionIfMissing(forms, this.editForm.get('form')!.value)))
      .subscribe((forms: IForm[]) => (this.formsSharedCollection = forms));
  }

  protected createFromForm(): IFormProgresss {
    return {
      ...new FormProgresss(),
      id: this.editForm.get(['id'])!.value,
      submited: this.editForm.get(['submited'])!.value,
      startedOn: this.editForm.get(['startedOn'])!.value ? dayjs(this.editForm.get(['startedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      submitedOn: this.editForm.get(['submitedOn'])!.value ? dayjs(this.editForm.get(['submitedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      sentedOn: this.editForm.get(['sentedOn'])!.value ? dayjs(this.editForm.get(['sentedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      user: this.editForm.get(['user'])!.value,
      form: this.editForm.get(['form'])!.value,
    };
  }
}
