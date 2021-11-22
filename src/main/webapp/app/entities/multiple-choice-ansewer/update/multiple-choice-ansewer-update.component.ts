import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMultipleChoiceAnsewer, MultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';
import { MultipleChoiceAnsewerService } from '../service/multiple-choice-ansewer.service';
import { IAnswer } from 'app/entities/answer/answer.model';
import { AnswerService } from 'app/entities/answer/service/answer.service';

@Component({
  selector: 'jhi-multiple-choice-ansewer-update',
  templateUrl: './multiple-choice-ansewer-update.component.html',
})
export class MultipleChoiceAnsewerUpdateComponent implements OnInit {
  isSaving = false;

  answersSharedCollection: IAnswer[] = [];

  editForm = this.fb.group({
    id: [],
    choice: [],
    answer: [],
  });

  constructor(
    protected multipleChoiceAnsewerService: MultipleChoiceAnsewerService,
    protected answerService: AnswerService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ multipleChoiceAnsewer }) => {
      this.updateForm(multipleChoiceAnsewer);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const multipleChoiceAnsewer = this.createFromForm();
    if (multipleChoiceAnsewer.id !== undefined) {
      this.subscribeToSaveResponse(this.multipleChoiceAnsewerService.update(multipleChoiceAnsewer));
    } else {
      this.subscribeToSaveResponse(this.multipleChoiceAnsewerService.create(multipleChoiceAnsewer));
    }
  }

  trackAnswerById(index: number, item: IAnswer): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMultipleChoiceAnsewer>>): void {
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

  protected updateForm(multipleChoiceAnsewer: IMultipleChoiceAnsewer): void {
    this.editForm.patchValue({
      id: multipleChoiceAnsewer.id,
      choice: multipleChoiceAnsewer.choice,
      answer: multipleChoiceAnsewer.answer,
    });

    this.answersSharedCollection = this.answerService.addAnswerToCollectionIfMissing(
      this.answersSharedCollection,
      multipleChoiceAnsewer.answer
    );
  }

  protected loadRelationshipsOptions(): void {
    this.answerService
      .query()
      .pipe(map((res: HttpResponse<IAnswer[]>) => res.body ?? []))
      .pipe(map((answers: IAnswer[]) => this.answerService.addAnswerToCollectionIfMissing(answers, this.editForm.get('answer')!.value)))
      .subscribe((answers: IAnswer[]) => (this.answersSharedCollection = answers));
  }

  protected createFromForm(): IMultipleChoiceAnsewer {
    return {
      ...new MultipleChoiceAnsewer(),
      id: this.editForm.get(['id'])!.value,
      choice: this.editForm.get(['choice'])!.value,
      answer: this.editForm.get(['answer'])!.value,
    };
  }
}
