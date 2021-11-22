import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IQuestionChoice, QuestionChoice } from '../question-choice.model';
import { QuestionChoiceService } from '../service/question-choice.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';

@Component({
  selector: 'jhi-question-choice-update',
  templateUrl: './question-choice-update.component.html',
})
export class QuestionChoiceUpdateComponent implements OnInit {
  isSaving = false;

  questionsSharedCollection: IQuestion[] = [];

  editForm = this.fb.group({
    id: [],
    option: [],
    question: [],
  });

  constructor(
    protected questionChoiceService: QuestionChoiceService,
    protected questionService: QuestionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionChoice }) => {
      this.updateForm(questionChoice);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const questionChoice = this.createFromForm();
    if (questionChoice.id !== undefined) {
      this.subscribeToSaveResponse(this.questionChoiceService.update(questionChoice));
    } else {
      this.subscribeToSaveResponse(this.questionChoiceService.create(questionChoice));
    }
  }

  trackQuestionById(index: number, item: IQuestion): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQuestionChoice>>): void {
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

  protected updateForm(questionChoice: IQuestionChoice): void {
    this.editForm.patchValue({
      id: questionChoice.id,
      option: questionChoice.option,
      question: questionChoice.question,
    });

    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing(
      this.questionsSharedCollection,
      questionChoice.question
    );
  }

  protected loadRelationshipsOptions(): void {
    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing(questions, this.editForm.get('question')!.value)
        )
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));
  }

  protected createFromForm(): IQuestionChoice {
    return {
      ...new QuestionChoice(),
      id: this.editForm.get(['id'])!.value,
      option: this.editForm.get(['option'])!.value,
      question: this.editForm.get(['question'])!.value,
    };
  }
}
